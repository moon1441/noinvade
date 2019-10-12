package com.electric.noinvade.service;

import com.electric.noinvade.bo.*;
import com.electric.noinvade.repositry.influx.DevicePowerMapper;
import com.electric.noinvade.repositry.influx.InfluxEventMapper;
import com.electric.noinvade.repositry.mysql.FamilyMapper;
import com.electric.noinvade.repositry.mysql.DeviceAuthMapper;
import com.electric.noinvade.repositry.mysql.EventMapper;
import com.electric.noinvade.util.AlarmTypeEnum;
import com.electric.noinvade.util.DeviceAuthStatusEnum;
import com.electric.noinvade.util.DeviceStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private DevicePowerMapper devicePowerMapper;

    @Autowired
    private InfluxEventMapper influxEventMapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private DeviceAuthMapper deviceAuthMapper;

    @Scheduled(fixedRate = 5000,initialDelay = 10000)
    public void scheduled(){
        long timestamp = eventMapper.getTimestamp();
        long now = System.currentTimeMillis();
        List<InfluxEvent> events = influxEventMapper.getEventByTime(timestamp , now);
        List<Event> resultEvents = events.stream()
                .filter(e-> Integer.valueOf(e.getType()) <=7)
                .map(event -> {
            Family family = familyMapper.getFamilyByMeter(event.getMeterID(), event.getPhase());
            if (family == null) {
                return null;
            }

            Event e = new Event();

            e.setFamilyID(family.getId());
            e.setTime(System.currentTimeMillis());
            e.setDeviceType(Integer.valueOf(event.getType()));
            e.setDeviceStatus(DeviceStatusEnum.getCode(event.getAction()));
            boolean hit=false;
            List<DeviceAuth> deviceAuthList = deviceAuthMapper.getAuthByFamilyID(family.getId(),e.getTime());
            if (CollectionUtils.isEmpty(deviceAuthList)) {
                e.setAlarmType(AlarmTypeEnum.NORMAL.getCode());
                hit=true;
            }
            if(!hit) {
                for (DeviceAuth deviceAuth : deviceAuthList) {
                    if (deviceAuth.getType() == Integer.valueOf(event.getType())
                            && deviceAuth.getStatus() == DeviceAuthStatusEnum.UN_AUTH.getCode()) {
                        e.setAlarmType(AlarmTypeEnum.EXCEPTION.getCode());
                        hit = true;
                    }
                }
                if (!hit) {
                    e.setAlarmType(AlarmTypeEnum.NORMAL.getCode());
                }
            }
            List<FamilyDevicePower> deviceCurrentFamilyDevicePower = devicePowerMapper.getDeviceCurrentFamilyDevicePower(event.getType(), event.getMeterID(), event.getPhase());
            if(!CollectionUtils.isEmpty(deviceCurrentFamilyDevicePower)){
                e.setPower(deviceCurrentFamilyDevicePower.get(0).getPower());
            }
            return e;

        }).filter(obj -> !Objects.isNull(obj)).collect(Collectors.toList());
        eventMapper.updateTimestamp(System.currentTimeMillis());
        if(!CollectionUtils.isEmpty(resultEvents)){
            for(Event e: resultEvents){
                eventMapper.insert(e);
            }
        }

    }
}
