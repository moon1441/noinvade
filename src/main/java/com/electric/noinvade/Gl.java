package com.electric.noinvade;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "raw_aggr_10s")
@Data
public class Gl {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id", tag = true)
    private String meterId;
    @Column(name = "pa")
    private String pa;
    @Column(name = "pb")
    private String pb;
    @Column(name = "pc")
    private String pc;

}
