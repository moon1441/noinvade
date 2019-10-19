package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@Measurement(name = "event")
public class InfluxEvent {

    @Column(name = "time")
    private Instant time;
    @Column(name = "action")
    private Integer action;
    @Column(name = "device")
    private String device;
    @Column(name = "info")
    private String info;
    @Column(name = "meter_id")
    private String meterID;
    @Column(name = "phase")
    private String phase;
    @Column(name = "type")
    private String type;

}
