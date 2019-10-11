package com.electric.noinvade.bo;

import lombok.Data;

@Data
public class Event {
    String familyID;
    int alarmType;
    int deviceStatus;
    int deviceType;
    int power;
    long time;
}
