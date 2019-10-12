package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "ep_1h")
@Data
//某户某设备的实时电量，粒度1h
public class FamilyEPower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id")
    private String meterId;
    @Column(name = "epa")
    private double epa;
    @Column(name = "epb")
    private double epb;
    @Column(name = "epc")
    private double epc;

}
