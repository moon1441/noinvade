package com.electric.noinvade.controller;

import com.electric.noinvade.bo.TotalEPower;
import com.electric.noinvade.bo.TotalPower;
import com.electric.noinvade.repositry.influx.DevicePowerMapper;
import com.electric.noinvade.util.TimeUtil;
import com.electric.noinvade.vo.AlarmInfoVO;
import com.electric.noinvade.vo.AllPowerInfoVO;
import com.electric.noinvade.vo.DevicePowerInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/family")
public class DetailController {

    @Autowired
    private DevicePowerMapper devicePowerMapper;

    //实时总功率、日月总用电量
    @RequestMapping(value="/all_power_info",method = RequestMethod.GET)
    public List<AllPowerInfoVO> getAllPowerInfo(@RequestParam("id") String id){
        AllPowerInfoVO allPowerInfoVO = new AllPowerInfoVO();

        TotalPower totalPower = devicePowerMapper.getAllCurrentPower().get(0);
        TotalEPower dayTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getDayZeroTime(0)).get(0);
        TotalEPower monthTotalEPower = allPowerMapper.getAllEPower(TimeUtil.getMonthFirstDay()).get(0);
        allPowerInfoVO.setTotalPower(totalPower.getPower());
        allPowerInfoVO.setTotalDayEPower(dayTotalEPower.getEPower());
        allPowerInfoVO.setTotalMonthEPower(monthTotalEPower.getEPower());
        return null;
    }

    //设备电量分解
    @RequestMapping(value="/device_power_sum",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDevicePowerSum(long start, long end,String houseId){
        return null;
    }

    //设备实时功率图表
    @RequestMapping(value="/device_power_detail",method = RequestMethod.GET)
    public List<DevicePowerInfoVO> getAllDevicePowerDetail(long start, long end,String houseId){
        return null;
    }

    //事件列表
    @RequestMapping(value="/device_event_detail",method = RequestMethod.GET)
    public List<AlarmInfoVO> getAllDeviceEventDetail(int pagenum, int pagesize,String houseId){
        return null;
    }

    //设备白名单
    @RequestMapping(value="/device_auth_info",method = RequestMethod.GET)
    public List<Integer> getAllDeviceAuth(String houseId){
        return null;
    }

    //设备加白
    @RequestMapping(value="/device_auth",method = RequestMethod.POST)
    public List<Integer> getAllDeviceAuth(String houseId,int type){
        return null;
    }
}
