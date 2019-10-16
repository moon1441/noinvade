package com.electric.noinvade.controller;

import com.electric.noinvade.bo.*;
import com.electric.noinvade.repositry.influx.DevicePowerMapper;
import com.electric.noinvade.repositry.influx.FamilyPowerMapper;
import com.electric.noinvade.repositry.mysql.DeviceAuthMapper;
import com.electric.noinvade.repositry.mysql.EventMapper;
import com.electric.noinvade.repositry.mysql.FamilyMapper;
import com.electric.noinvade.util.TimeUtil;
import com.electric.noinvade.vo.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/family")
public class DetailController {

    @Autowired
    private DevicePowerMapper devicePowerMapper;

    @Autowired
    private FamilyPowerMapper familyPowerMapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private DeviceAuthMapper deviceAuthMapper;


    //实时总功率、日月总用电量、
    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public AllPowerInfoVO getAllPowerInfo(@RequestParam("id") String id){
        AllPowerInfoVO allPowerInfoVO = new AllPowerInfoVO();
        Family family = familyMapper.getFamilyByID(id);
        FamilyPower familyCurrentPower = familyPowerMapper.getFamilyCurrentPower(family.getMeterID(), family.getPhase()).get(0);
        FamilyEPower familyDailyEPower = familyPowerMapper.getFamilyAllEPower(family.getPhase(),TimeUtil.getDayZeroTime(0),family.getMeterID()).get(0);
        FamilyEPower familyMonthEPower = familyPowerMapper.getFamilyAllEPower(family.getPhase(),TimeUtil.getMonthFirstDay(),family.getMeterID()).get(0);
        allPowerInfoVO.setTotalPower(familyCurrentPower.getPower());
        allPowerInfoVO.setTotalDayEPower(familyDailyEPower.getPower());
        allPowerInfoVO.setTotalMonthEPower(familyMonthEPower.getPower());


        List<FamilyPower> familyDailyPower = familyPowerMapper.getFamilyDailyPower(family.getPhase(),TimeUtil.getDayZeroTime(0), family.getMeterID());
        familyDailyPower.sort(Comparator.comparing(FamilyPower::getTime));
        Map<Long, Integer> powerMap = familyDailyPower.stream().collect(Collectors.toMap(power -> power.getTime().toEpochMilli(), FamilyPower::getPower));
        allPowerInfoVO.setPowerMap(powerMap);


        Map<Long,Double> dayEPowerMap = Maps.newHashMap();
        for(int i=0;i<TimeUtil.getHour();i++){
            long begin = TimeUtil.getTodayHourTime(i);
            long after = TimeUtil.getTodayHourTime(i+1);
            List<FamilyEPower> familyIntervalEPower = familyPowerMapper.getFamilyIntervalEPower(family.getPhase(),begin, after, family.getMeterID());
            if(!CollectionUtils.isEmpty(familyIntervalEPower)){
                dayEPowerMap.put(after,familyIntervalEPower.get(0).getPower());
            }
        }
        allPowerInfoVO.setDayEPower(dayEPowerMap);
        Map<Long,Double> monthEPowerMap = Maps.newHashMap();
        for(int i=TimeUtil.getDay()-1;i>0;i--){
            long begin = TimeUtil.getDayZeroTime(i);
            long after = TimeUtil.getDayZeroTime(i-1);
            List<FamilyEPower> familyIntervalEPower = familyPowerMapper.getFamilyIntervalEPower(family.getPhase(),begin, after, family.getMeterID());
            if(!CollectionUtils.isEmpty(familyIntervalEPower)) {
                monthEPowerMap.put(begin,familyIntervalEPower.get(0).getPower());
            }
        }
        allPowerInfoVO.setMonthEPower(monthEPowerMap);
        return allPowerInfoVO;
    }

    //设备电量分解
    @RequestMapping(value="/device_power_sum",method = RequestMethod.GET)
    public Map<Integer,List<EPowerInfoVO>> getAllDevicePowerSum(@RequestParam("start") long start,
                                                                @RequestParam("end")long end,
                                                                @RequestParam("types") List<Integer> types,
                                                                @RequestParam("id") String id){
        StringBuilder typeString=new StringBuilder();
        Family family = familyMapper.getFamilyByID(id);
        Map<Integer,List<EPowerInfoVO>> powerInfoVOMap =Maps.newHashMap();
        types.forEach(type -> {
            typeString.append(type);
            typeString.append("|");
            List<EPowerInfoVO> powerInfoVOS = Lists.newArrayList();
            powerInfoVOMap.put(type, powerInfoVOS);
        });
        List<FamilyDeviceEPower> sumFamilyDeviceEPower = familyPowerMapper.getSumFamilyDeviceEPower(family.getPhase(), start, end, family.getMeterID(), typeString.substring(0, typeString.length() - 1));
        setPowerMap(powerInfoVOMap,sumFamilyDeviceEPower);
        return powerInfoVOMap;
    }

    private void setPowerMap(Map<Integer, List<EPowerInfoVO>> powerInfoVOMap, List<FamilyDeviceEPower> deviceSumPowers) {
        for(FamilyDeviceEPower devicePower : deviceSumPowers){
            EPowerInfoVO powerInfoVO= new EPowerInfoVO();
            powerInfoVO.setEPower(devicePower.getPower());
            powerInfoVO.setTime(devicePower.getTime().toEpochMilli());
            if( CollectionUtils.isEmpty(powerInfoVOMap.get(Integer.valueOf(devicePower.getType()))) ){
                List<EPowerInfoVO> powerInfoVOS = Lists.newArrayList();
                powerInfoVOMap.put(Integer.valueOf(devicePower.getType()),powerInfoVOS);
            }
            powerInfoVOMap.get(Integer.valueOf(devicePower.getType())).add(powerInfoVO);
        }
    }

    //设备实时功率图表
    @RequestMapping(value="/device_power_detail",method = RequestMethod.GET)
    public List<PowerInfoVO> getAllDevicePowerDetail(@RequestParam("start") long start,
                                                                  @RequestParam("end")long end,
                                                                  @RequestParam("type") int type,
                                                                  @RequestParam("id") String id){
        List<PowerInfoVO> powerInfoVOS = Lists.newArrayList();
        Family family = familyMapper.getFamilyByID(id);
        List<FamilyDevicePower> familyDevicePower = familyPowerMapper.getFamilyDevicePower(family.getPhase(), start, end, family.getMeterID(), String.valueOf(type));
        familyDevicePower.sort(Comparator.comparing(FamilyDevicePower::getTime));
        familyDevicePower.stream().forEach(power ->{
            PowerInfoVO vo = new PowerInfoVO();
            vo.setPower(power.getPower());
            vo.setTime(power.getTime().toEpochMilli());
            powerInfoVOS.add(vo);
        } );
        return powerInfoVOS;
    }

    //事件列表
    @RequestMapping(value="/device_event_detail",method = RequestMethod.GET)
    public List<AlarmInfoVO> getAllDeviceEventDetail(@RequestParam("pagenum")int pagenum,
                                                     @RequestParam("pagesize")int pagesize,
                                                     @RequestParam("id")String id){
        List<Event> event = eventMapper.getEvent(id, pagesize, pagenum);
        List<AlarmInfoVO> alarmInfoVOS = new ArrayList<>();
        event.stream().forEach(e->{
            AlarmInfoVO vo = new AlarmInfoVO();
            vo.setAlarmType(e.getAlarmType());
            vo.setDeviceStatus(e.getDeviceStatus());
            vo.setDeviceType(e.getDeviceType());
            vo.setPower(e.getPower());
            vo.setTime(e.getTime());
            alarmInfoVOS.add(vo);
        });
        return alarmInfoVOS;
    }

    //设备白名单
    @RequestMapping(value="/device_auth_info",method = RequestMethod.GET)
    //返回授权信息，key是type（1-7），value是状态
    public Map<Integer,Integer> getAllDeviceAuth(@RequestParam("id")String id){
        List<DeviceAuth> deviceAuths = deviceAuthMapper.getAuthByFamilyID(id);
        Map<Integer,Integer> result = Maps.newHashMap();
        for(int i=1;i<=7;i++){
            result.put(i,1);
            for(DeviceAuth deviceAuth: deviceAuths){
                if(deviceAuth.getType() == i){
                    result.put(i,deviceAuth.getStatus());
                }
            }
        }
        return result;
    }

    //设备加白
    @RequestMapping(value="/device_auth",method = RequestMethod.POST)
    @Transactional
    public boolean setAllDeviceAuth(@RequestParam("id")String id,
                                          @RequestParam("type") int type,
                                          @RequestParam("status") int status){
        DeviceAuth deviceAuth = deviceAuthMapper.getAuthSnapshotByTime(id, type, System.currentTimeMillis());
        if(deviceAuth!=null){
            deviceAuth.setStatus(status);
            deviceAuthMapper.update(deviceAuth);
        }else{
            deviceAuth = new DeviceAuth();
            deviceAuth.setStatus(status);
            deviceAuth.setFamilyID(id);
            deviceAuth.setType(type);
            deviceAuthMapper.insert(deviceAuth);
        }
        deviceAuthMapper.insertSnapshot(deviceAuth.getFamilyID(),deviceAuth.getType(),deviceAuth.getStatus(),System.currentTimeMillis());
        return true;
    }
}
