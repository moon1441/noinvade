package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "raw_aggr_10s")
@Data
public class DevicePower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id", tag = true)
    private String meterId;
    @Column(name = "pa")
    private Double pa;
    @Column(name = "pb")
    private Double pb;
    @Column(name = "pc")
    private Double pc;

}
