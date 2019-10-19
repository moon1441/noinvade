package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "device_ep_1h")
@Data
//某户某设备实时功率
public class FamilyDeviceEPower {

    @Column(name = "time")
    private Instant time;
    @Column(name = "meter_id")
    private String meterId;
    @Column(name = "phase")
    private String phase;
    @Column(name = "power")
    private double power;
    @Column(name = "type")
    private String type;

}
