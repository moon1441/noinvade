package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.DeviceEPower;
import com.electric.noinvade.bo.DevicePower;
import com.electric.noinvade.bo.FamilyDevicePower;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface DevicePowerMapper {

    //单设备实时功率
    @InfluxQuery("select * from device_aggr_10s where time >now()-10s and type=? and meter_id=? and phase=? order by time desc limit 1")
    List<FamilyDevicePower> getDeviceCurrentFamilyDevicePower(int type, String meterID, String phase);

    //设备10s区间段功率信息，分type
    @InfluxQuery("select * from device_type_10s where time>=? and time<? and type in (?)")
    List<DevicePower> getDevicePower(long start, long end, String types);

    //设备10s区间段汇总功率信息
    @InfluxQuery("select time,sum(p) as p from device_type_10s where time>=? and time<? and type in (?)")
    List<DevicePower> getSumDevicePower(long start, long end, String types);

    //设备按日区间段电量信息
    @InfluxQuery("select * from device_type_ep_1h where time>=? and time<? and type in (?)")
    List<DeviceEPower> getDeviceEPower(long start, long end, String types);

    //所有用户日月累计
    @InfluxQuery("select * from raw_overall_10s where time >now() -100s order by time desc limit 1")
    List<Double> getPower();
}
