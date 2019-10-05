package com.electric.noinvade.controller;

import com.electric.noinvade.vo.AlarmInfoVO;
import com.electric.noinvade.vo.AllPowerInfoVO;
import com.electric.noinvade.vo.DevicePowerInfoVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/family")
public class DetailController {

    //实时总功率、日月总用电量、
    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public List<AllPowerInfoVO> getAllPowerInfo(){
        return null;
    }

    //设备电量分解
    @RequestMapping(value="/device_power_sum",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDevicePowerSum(long start, long end,String buildingId,String houseId){
        return null;
    }

    //设备实时功率图表
    @RequestMapping(value="/device_power_detail",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDevicePowerDetail(long start, long end,String buildingId,String houseId){
        return null;
    }

    //事件列表
    @RequestMapping(value="/device_power_detail",method = RequestMethod.GET)
    public List<AlarmInfoVO> getAllDevicePowerDetail(int pagenum, int pagesize,String buildingId,String houseId){
        return null;
    }

    //设备白名单
    @RequestMapping(value="/device_auth_info",method = RequestMethod.GET)
    public List<Integer> getAllDeviceAuth(String buildingId,String houseId){
        return null;
    }

    //设备加白
    @RequestMapping(value="/device_auth",method = RequestMethod.POST)
    public List<Integer> getAllDeviceAuth(String buildingId,String houseId,int type){
        return null;
    }
}
