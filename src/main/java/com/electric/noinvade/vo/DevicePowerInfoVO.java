package com.electric.noinvade.vo;

import lombok.Data;

import java.util.List;

@Data
public class DevicePowerInfoVO {

    int type;
    List<PowerInfoVO> powerInfo;
}
