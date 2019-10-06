package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.TotalEPower;
import com.electric.noinvade.bo.TotalPower;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface AllPowerMapper {

    //所有用户实时总功率
    @InfluxQuery("select * from raw_overall_10s where time >now() -100s order by time desc limit 1")
    List<TotalPower> getAllCurrentPower();

    //当天实时总功率（曲线）
    @InfluxQuery("select * from raw_overall_10s where time > ? order by time desc")
    List<TotalPower> getDayCurrentPower(long startTime);

    //所有用户累计电量
    @InfluxQuery("select sum(ep) from ep_overall_1h where time > ?")
    List<TotalEPower> getAllEPower(long startTime);

    //所有用户区间累计电量
    @InfluxQuery("select sum(ep) from ep_overall_1h where time > ? and time <=?")
    List<TotalEPower> getIntervalEPower(long startTime,long endTime);
}
