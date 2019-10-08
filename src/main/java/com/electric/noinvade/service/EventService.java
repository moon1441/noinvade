package com.electric.noinvade.service;

import com.electric.noinvade.bo.Event;
import com.electric.noinvade.bo.InfluxEvent;
import com.electric.noinvade.repositry.influx.DevicePowerMapper;
import com.electric.noinvade.repositry.influx.InfluxEventMapper;
import com.electric.noinvade.repositry.mysql.EventMapper;
import com.electric.noinvade.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private DevicePowerMapper devicePowerMapper;

    @Autowired
    private InfluxEventMapper influxEventMapper;

    @Scheduled(fixedRate = 5000)
    public void scheduled(){
        long timestamp = eventMapper.getTimestamp();
        long now = System.currentTimeMillis();
        List<InfluxEvent> events = influxEventMapper.getEventByTime(timestamp * TimeUtil.nano, now * TimeUtil.nano);
        events.stream().forEach(event->{
            Event e = new Event();

        });


    }
}
