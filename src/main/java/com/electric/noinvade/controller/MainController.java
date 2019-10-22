package com.electric.noinvade.controller;

import com.electric.noinvade.bo.*;
import com.electric.noinvade.repositry.influx.AllPowerMapper;
import com.electric.noinvade.repositry.influx.DevicePowerMapper;
import com.electric.noinvade.repositry.mysql.FamilyMapper;
import com.electric.noinvade.repositry.mysql.EventMapper;
import com.electric.noinvade.util.DeviceTypeEnum;
import com.electric.noinvade.util.TimeUtil;
import com.electric.noinvade.vo.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/main")
public class MainController {

    @Autowired
    private AllPowerMapper allPowerMapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private DevicePowerMapper devicePowerMapper;

    @RequestMapping(value="/all_family",method = RequestMethod.GET)
    public List<FamilyVO> getFamilies(){
        List<FamilyVO> familyVOS = Lists.newArrayList();
        List<Family> allFamilies = familyMapper.getAllFamilies();
        List<Event> eventList = eventMapper.getAllAlarmEvent();
        allFamilies.forEach(family -> {
            FamilyVO vo = new FamilyVO();
            vo.setId(family.getId());
            vo.setInfo(family.getDescription());
            if(!CollectionUtils.isEmpty(eventList)) {
                for (Event e : eventList) {
                    if (e.getFamilyID().equals(family.getId())) {
                        vo.setEventType(e.getDeviceStatus());
                        vo.setEventTime(e.getTime());
                        vo.setPower(e.getPower());
                        vo.setAlarm(true);
                    }
                }
            }
            familyVOS.add(vo);
        });
        return familyVOS;
    }

    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public AllPowerInfoVO getAllPowerInfo(){
        AllPowerInfoVO allPowerInfoVO = new AllPowerInfoVO();

        TotalPower totalPower = allPowerMapper.getAllCurrentPower().get(0);
        TotalEPower dayTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getDayZeroTime(0)).get(0);
        TotalEPower monthTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getMonthFirstDay()).get(0);
        allPowerInfoVO.setTotalPower(totalPower.getPower());
        allPowerInfoVO.setTotalDayEPower(dayTotalEPower.getEPower());
        allPowerInfoVO.setTotalMonthEPower(monthTotalEPower.getEPower());

        List<TotalPower> intervalPower = allPowerMapper.getDayCurrentPower(TimeUtil.getDayZeroTime(0));
        intervalPower.sort(Comparator.comparing(TotalPower::getTime));
        Map<Long, Integer> powerMap = intervalPower.stream().collect(Collectors.toMap(power -> power.getTime().toEpochMilli(), TotalPower::getPower));
        allPowerInfoVO.setPowerMap(powerMap);

        Map<Long,Double> dayEPowerMap = Maps.newHashMap();
        for(int i=0;i<TimeUtil.getHour();i++){
            long begin = TimeUtil.getTodayHourTime(i);
            long after = TimeUtil.getTodayHourTime(i+1);
            List<TotalEPower> intervalEPower = allPowerMapper.getIntervalEPower(begin, after);
            if(!CollectionUtils.isEmpty(intervalEPower)){
                dayEPowerMap.put(after,intervalEPower.get(0).getEPower());
            }
        }
        allPowerInfoVO.setDayEPower(dayEPowerMap);
        Map<Long,Double> monthEPowerMap = Maps.newHashMap();
        for(int i=TimeUtil.getDay()-1;i>0;i--){
            long begin = TimeUtil.getDayZeroTime(i);
            long after = TimeUtil.getDayZeroTime(i-1);
            List<TotalEPower> intervalEPower = allPowerMapper.getIntervalEPower(begin, after);
            if(!CollectionUtils.isEmpty(intervalEPower)) {
                monthEPowerMap.put(begin,intervalEPower.get(0).getEPower());
            }
        }
        allPowerInfoVO.setMonthEPower(monthEPowerMap);
        return allPowerInfoVO;
    }

    @RequestMapping(value="/all_device_power_info",method = RequestMethod.GET)
    public Map<Integer,List<PowerInfoVO>> getAllDevicePowerInfo(@RequestParam("start") long start,
                                                         @RequestParam("end")long end,
                                                         @RequestParam("types") List<Integer> types){
        StringBuilder typeString=new StringBuilder();
        Map<Integer,List<PowerInfoVO>> powerInfoVOMap =Maps.newHashMap();
        types.forEach(type -> {
            typeString.append(type);
            typeString.append("|");
            List<PowerInfoVO> powerInfoVOS = Lists.newArrayList();
            powerInfoVOMap.put(type, powerInfoVOS);
        });
        List<DevicePower> devicePowers = devicePowerMapper.getDevicePower(start, end, typeString.substring(0, typeString.length() - 1));


        setPowerMap(powerInfoVOMap, devicePowers);
        List<DevicePower> deviceSumPowers = devicePowerMapper.getSumDevicePower(start, end, typeString.substring(0, typeString.length() - 1));
        List<PowerInfoVO> allPowerInfoVOS = Lists.newArrayList();
        powerInfoVOMap.put(DeviceTypeEnum.ALL.getCode(), allPowerInfoVOS);
        setPowerMap(powerInfoVOMap, deviceSumPowers);
        return powerInfoVOMap;
    }

    private void setPowerMap(Map<Integer, List<PowerInfoVO>> powerInfoVOMap, List<DevicePower> deviceSumPowers) {
        for(DevicePower devicePower : deviceSumPowers){
            PowerInfoVO powerInfoVO= new PowerInfoVO();
            powerInfoVO.setPower(devicePower.getPower());
            powerInfoVO.setTime(devicePower.getTime().toEpochMilli());
            if( CollectionUtils.isEmpty(powerInfoVOMap.get(Integer.valueOf(devicePower.getType()))) ){
                List<PowerInfoVO> powerInfoVOS = Lists.newArrayList();
                powerInfoVOMap.put(Integer.valueOf(devicePower.getType()),powerInfoVOS);
            }
            powerInfoVOMap.get(Integer.valueOf(devicePower.getType())).add(powerInfoVO);
        }
    }

    @RequestMapping(value="/all_device_e_power_info",method = RequestMethod.GET)
    public Map<Integer,List<EPowerInfoVO>> getAllDeviceEPowerInfo(@RequestParam("start") long start,
                                                                 @RequestParam("end")long end,
                                                                 @RequestParam("types") List<Integer> types){
        StringBuilder typeString=new StringBuilder();
        Map<Integer,List<EPowerInfoVO>> powerInfoVOMap =Maps.newHashMap();
        types.forEach(type -> {
            typeString.append(type);
            typeString.append("|");
            List<EPowerInfoVO> powerInfoVOS = Lists.newArrayList();
            powerInfoVOMap.put(type, powerInfoVOS);
        });
        List<DeviceEPower> devicePowers = devicePowerMapper.getDeviceEPower(start, end, typeString.substring(0, typeString.length() - 1));
        for(DeviceEPower deviceEPower : devicePowers){
            EPowerInfoVO powerInfoVO= new EPowerInfoVO();
            powerInfoVO.setEPower(deviceEPower.getPower());
            powerInfoVO.setTime(deviceEPower.getTime().toEpochMilli());
            List<EPowerInfoVO> ePowerInfoVOS = powerInfoVOMap.get(Integer.valueOf(deviceEPower.getType()));
            if (ePowerInfoVOS != null) {
                ePowerInfoVOS.add(powerInfoVO);
            }
        }
        return powerInfoVOMap;
    }
}
