package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.*;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface FamilyPowerMapper {

    //所有用户实时总功率
    @InfluxQuery("select * from raw_aggr_10s where time >now() -100s and meter_id=? and phase=? order by time desc limit 1")
    List<FamilyPower> getFamilyCurrentPower(String meterID,String phase);

    //当天实时总功率（曲线）
    @InfluxQuery("select * from raw_overall_10s where time > ? and meter_id=? and phase=? order by time asc")
    List<FamilyPower> getFamilyDailyPower(long startTime,String meterID,String phase);

    //单用户累计电量
    @InfluxQuery("select sum(ep) from ep_1h where time > ? and meter_id=? and phase=?")
    List<FamilyEPower> getFamilyAllEPower(long startTime,String meterID,String phase);

    //所有用户区间累计电量
    @InfluxQuery("select sum(ep) from ep_1h where time > ? and time <=? and meter_id=? and phase=?")
    List<FamilyEPower> getFamilyIntervalEPower(long startTime,long endTime,String meterID,String phase);
}
