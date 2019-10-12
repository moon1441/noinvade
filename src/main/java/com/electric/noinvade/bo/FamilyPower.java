package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "raw_aggr_10s")
@Data
//某户某设备的实时电量，粒度1h
public class FamilyPower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id")
    private String meterId;
    @Column(name = "pa")
    private double pa;
    @Column(name = "pb")
    private double pb;
    @Column(name = "pc")
    private double pc;

}
