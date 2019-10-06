package com.electric.noinvade.controller;

import com.electric.noinvade.bo.Building;
import com.electric.noinvade.bo.TotalEPower;
import com.electric.noinvade.bo.TotalPower;
import com.electric.noinvade.repositry.influx.AllPowerMapper;
import com.electric.noinvade.repositry.mysql.BuildingMapper;
import com.electric.noinvade.util.TimeUtil;
import com.electric.noinvade.vo.AllPowerInfoVO;
import com.electric.noinvade.vo.DevicePowerInfoVO;
import com.electric.noinvade.vo.FamilyVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
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
    private BuildingMapper buildingMapper;

    @RequestMapping(value="/all_family",method = RequestMethod.GET)
    public List<FamilyVO> getFamilies(){
        List<FamilyVO> familyVOS = Lists.newArrayList();
        List<Building> allBuildings = buildingMapper.getAllBuildings();
        allBuildings.forEach(building -> {
            FamilyVO vo = new FamilyVO();
            vo.setHouseId(building.getId());
            vo.setInfo(building.getDescription());
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
        Map<Long, Double> powerMap = intervalPower.stream().collect(Collectors.toMap(power -> power.getTime().toEpochMilli(), power -> power.getPower()));
        allPowerInfoVO.setPowerMap(powerMap);

        Map<Long,Double> dayEPowerMap = Maps.newHashMap();
        for(int i=0;i<TimeUtil.getHour();i++){
            long begin = TimeUtil.getTodayHourTime(i);
            long after = TimeUtil.getTodayHourTime(i+1);
            List<TotalEPower> intervalEPower = allPowerMapper.getIntervalEPower(begin, after);
            if(!CollectionUtils.isEmpty(intervalEPower)){
                dayEPowerMap.put(after/TimeUtil.nano,intervalEPower.get(0).getEPower());
            }
        }
        allPowerInfoVO.setDayEPower(dayEPowerMap);
        Map<Long,Double> monthEPowerMap = Maps.newHashMap();
        for(int i=TimeUtil.getDay()-1;i>0;i--){
            long begin = TimeUtil.getDayZeroTime(i);
            long after = TimeUtil.getDayZeroTime(i-1);
            List<TotalEPower> intervalEPower = allPowerMapper.getIntervalEPower(begin, after);
            if(!CollectionUtils.isEmpty(intervalEPower)) {
                monthEPowerMap.put(begin/TimeUtil.nano,intervalEPower.get(0).getEPower());
            }
        }
        allPowerInfoVO.setMonthEPower(monthEPowerMap);
        return allPowerInfoVO;
    }

    @RequestMapping(value="/all_device_power_info",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDevicePowerInfo(long start, long end){
        return null;
    }

    @RequestMapping(value="/all_device_e_power_info",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDeviceEPowerInfo(long start, long end){
        return null;
    }
}
