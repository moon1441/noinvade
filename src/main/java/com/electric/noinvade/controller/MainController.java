package com.electric.noinvade.controller;

import com.electric.noinvade.bo.Building;
import com.electric.noinvade.bo.Power;
import com.electric.noinvade.repositry.influx.PowerMapper;
import com.electric.noinvade.repositry.mysql.BuildingMapper;
import com.electric.noinvade.vo.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/main")
public class MainController {

    @Autowired
    private PowerMapper powerMapper;

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
        Power power = powerMapper.getPower().get(0);
        AllPowerInfoVO allPowerInfoVO = new AllPowerInfoVO();
        allPowerInfoVO.setTotalDayEPower(power.getPa());
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
