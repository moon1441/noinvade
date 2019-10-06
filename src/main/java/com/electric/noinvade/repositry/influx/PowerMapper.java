package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.Power;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface PowerMapper {

    @InfluxQuery("select * from raw_aggr_10s where time >now() -100s order by time desc limit 1")
    List<Power> getPower();
}
