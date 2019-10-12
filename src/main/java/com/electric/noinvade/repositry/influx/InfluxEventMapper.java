package com.electric.noinvade.repositry.influx;

import com.electric.noinvade.bo.InfluxEvent;
import com.electric.noinvade.infra.InfluxQuery;

import java.util.List;

public interface InfluxEventMapper {

    @InfluxQuery("select * from event where time >=?ms and time<?ms")
    List<InfluxEvent> getEventByTime(long start,long end);
}
