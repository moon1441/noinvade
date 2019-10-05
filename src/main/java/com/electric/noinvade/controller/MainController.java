package com.electric.noinvade.controller;

import com.electric.noinvade.bo.Power;
import com.electric.noinvade.repositry.PowerRepository;
import com.electric.noinvade.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/main")
public class MainController {

    @Autowired
    private PowerRepository powerRepository;

    @RequestMapping(value="/all_family",method = RequestMethod.GET)
    public List<FamilyVO> getFamilies(){
        return null;
    }

    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public List<AllPowerInfoVO> getAllPowerInfo(){
        Power power = powerRepository.getPower();

        return null;
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
