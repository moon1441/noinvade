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

    @RequestMapping(value="/device_load",method = RequestMethod.GET)
    public Map<Integer,Integer> getDeviceLoad(){
        List<DeviceLoad> deviceLoad = devicePowerMapper.getDeviceLoad();
        Map<Integer,Integer> result=Maps.newHashMap();

        if(!CollectionUtils.isEmpty(deviceLoad)){
            result.put(1, deviceLoad.get(0).getNum1());
            result.put(2, deviceLoad.get(0).getNum2());
            result.put(3, deviceLoad.get(0).getNum3());
            result.put(4, deviceLoad.get(0).getNum4());
            result.put(5, deviceLoad.get(0).getNum5());
            result.put(6, deviceLoad.get(0).getNum6());
        }
        return result;

    }

    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public AllPowerInfoVO getAllPowerInfo(){
        AllPowerInfoVO allPowerInfoVO = new AllPowerInfoVO();

        List<TotalPower> allCurrentPower = allPowerMapper.getAllCurrentPower();
        List<TotalEPower> dayTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getDayZeroTime(0));
        List<TotalEPower> monthTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getMonthFirstDay());
        allPowerInfoVO.setTotalPower(CollectionUtils.isEmpty(allCurrentPower)?0:allCurrentPower.get(0).getPower());
        allPowerInfoVO.setTotalDayEPower(CollectionUtils.isEmpty(dayTotalEPower)?0:dayTotalEPower.get(0).getEPower());
        allPowerInfoVO.setTotalMonthEPower(CollectionUtils.isEmpty(monthTotalEPower)?0:monthTotalEPower.get(0).getEPower());

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
        List<DevicePower> devicePowersResults = devicePowers.stream().filter(devicePower -> types.contains(Integer.valueOf(devicePower.getType()))).collect(Collectors.toList());
        List<PowerInfoVO> allPowerInfoVOS = Lists.newArrayList();
        powerInfoVOMap.put(DeviceTypeEnum.ALL.getCode(), allPowerInfoVOS);
        for(DevicePower devicePower : devicePowersResults){
            PowerInfoVO powerInfoVO= new PowerInfoVO();
            powerInfoVO.setPower(devicePower.getPower());
            powerInfoVO.setTime(devicePower.getTime().toEpochMilli());
            if( CollectionUtils.isEmpty(powerInfoVOMap.get(Integer.valueOf(devicePower.getType()))) ){
                List<PowerInfoVO> powerInfoVOS = Lists.newArrayList();
                powerInfoVOMap.put(Integer.valueOf(devicePower.getType()),powerInfoVOS);
            }
            powerInfoVOMap.get(Integer.valueOf(devicePower.getType())).add(powerInfoVO);
        }
        return powerInfoVOMap;
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
        List<DeviceEPower> devicePowersResults = devicePowers.stream().filter(devicePower -> types.contains(Integer.valueOf(devicePower.getType()))).collect(Collectors.toList());
        for(DeviceEPower deviceEPower : devicePowersResults){
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
