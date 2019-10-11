package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "device_aggr_10s")
@Data
//某户某设备实时功率
public class FamilyDevicePower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id")
    private String meterId;
    @Column(name = "phase")
    private String phase;
    @Column(name = "p")
    private int power;
    @Column(name = "type")
    private int type;

}
