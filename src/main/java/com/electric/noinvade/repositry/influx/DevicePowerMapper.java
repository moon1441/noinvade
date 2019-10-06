package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface DevicePowerMapper {

    //所有用户实时总功率
    @InfluxQuery("select * from raw_overall_10s where time >now() -100s order by time desc limit 1")
    List<Double> getAllCurrentPower();

    //所有用户日累计
    @InfluxQuery("select * from raw_overall_10s where time >now() -100s order by time desc limit 1")
    List<Double> getAllDayEPower();

    //所有用户日月累计
    @InfluxQuery("select * from raw_overall_10s where time >now() -100s order by time desc limit 1")
    List<Double> getPower();
}
