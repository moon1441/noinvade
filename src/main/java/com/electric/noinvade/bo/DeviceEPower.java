package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "device_type_ep_1h")
@Data
//某设备实时功率
public class DeviceEPower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "ep")
    private double power;
    @Column(name = "type")
    private String type;

}
