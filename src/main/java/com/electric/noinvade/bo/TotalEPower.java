package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Data
@Measurement(name = "ep_overall_1h")
public class TotalEPower {
    @Column(name = "time")
    private Instant time;
    @Column(name = "sum", tag = true)
    private Double ePower;
}
