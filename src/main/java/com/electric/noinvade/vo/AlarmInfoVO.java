package com.electric.noinvade.vo;

import lombok.Data;

@Data
public class AlarmInfoVO {
    int alarmType;
    int deviceStatus;
    long time;
    int deviceType;
    String power;

}
