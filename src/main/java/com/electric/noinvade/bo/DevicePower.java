package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "device_type_10s")
@Data
//某设备实时功率
public class DevicePower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "p")
    private int power;
    @Column(name = "type",tag = true)
    private String type;

}
