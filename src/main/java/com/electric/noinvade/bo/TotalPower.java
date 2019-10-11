package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@Measurement(name = "raw_overall_10s")
public class TotalPower {
    @Column(name = "time")
    private Instant time;
    @Column(name = "p", tag = true)
    private int power;
}
