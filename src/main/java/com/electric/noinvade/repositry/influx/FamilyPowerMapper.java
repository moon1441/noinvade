package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.*;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface FamilyPowerMapper {

    //所有用户实时总功率
    @InfluxQuery("select time,meter_id,p? as power from raw_aggr_10s where time >now() -100s and meter_id=? order by time desc limit 1")
    List<FamilyPower> getFamilyCurrentPower(String phase,String meterID);

    //当天实时总功率（曲线）
    @InfluxQuery("select time,meter_id,p? as power from raw_overall_10s where time > ?ms and meter_id=? and phase=? order by time asc")
    List<FamilyPower> getFamilyDailyPower(String phase,long startTime,String meterID);

    //单用户累计电量
    @InfluxQuery("select time,meter_id,sum(ep?) as power from ep_1h where time > ?ms and meter_id=?")
    List<FamilyEPower> getFamilyAllEPower(String phase,long startTime,String meterID);

    //所有用户区间累计电量
    @InfluxQuery("select time,meter_id,sum(ep?) as power from ep_1h where time > ?ms and time <=?ms and meter_id=?")
    List<FamilyEPower> getFamilyIntervalEPower(String phase,long startTime,long endTime,String meterID);


    //户内设备按日区间段电量汇总信息
    @InfluxQuery("select time,type,sum(ep?) as power from device_ep_1h where time>=?ms and time<?ms  and meter_id=?  and type in (?) group by type")
    List<FamilyDeviceEPower> getSumFamilyDeviceEPower(String phase,long start, long end,String meterID, String types);


    //户内设备区间段汇总功率信息
    @InfluxQuery("select time,meter_id,p? as power from device_aggr_10s where time>=?ms and time<?ms and meter_id=? and type in (?)")
    List<FamilyDevicePower> getFamilyDevicePower(String phase,long start, long end,String meterID,String types);

}
