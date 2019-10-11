package com.electric.noinvade.vo;

import lombok.Data;

@Data
public class FamilyVO {

    String id;
    String info;
    int eventType; //0没事件，1开机告警，2关机告警
    long eventTime;
    int power;

}
