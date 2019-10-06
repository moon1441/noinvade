package com.electric.noinvade.vo;

import lombok.Data;

import java.util.Map;

@Data
public class AllPowerInfoVO {
    Double totalPower;
    Double totalDayEPower;
    Double totalMonthEPower;
    Map<Long,Double> powerMap;
    Map<Long,Double> dayEPower;
    Map<Long,Double> monthEPower;
}
