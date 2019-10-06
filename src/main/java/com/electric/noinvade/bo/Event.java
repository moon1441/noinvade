package com.electric.noinvade.bo;

import lombok.Data;

@Data
public class Event {
    String buildingID;
    int alarmType;
    int deviceStatus;
    int deviceType;
    long time;
}
