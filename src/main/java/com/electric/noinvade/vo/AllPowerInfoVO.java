package com.electric.noinvade.vo;

import lombok.Data;

import java.util.Map;

@Data
public class AllPowerInfoVO {
    int totalPower;
    double totalDayEPower;
    double totalMonthEPower;
    Map<Long,Integer> powerMap;
    Map<Long,Double> dayEPower;
    Map<Long,Double> monthEPower;
}
