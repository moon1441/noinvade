package com.electric.noinvade.bo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "device_num")
@Data
//设备实时负荷
public class DeviceLoad {

    @Column(name = "time")
    private Instant time;
    @Column(name = "num1")
    private int num1;
    @Column(name = "num2")
    private int num2;
    @Column(name = "num3")
    private int num3;
    @Column(name = "num4")
    private int num4;
    @Column(name = "num5")
    private int num5;
    @Column(name = "num6")
    private int num6;
}
