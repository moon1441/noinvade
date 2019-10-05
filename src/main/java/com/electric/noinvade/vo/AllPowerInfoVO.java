package com.electric.noinvade.vo;

import lombok.Data;

import java.util.Map;

@Data
public class AllPowerInfoVO {
    String totalPower;
    String totalDayEPower;
    String totalMonthEPower;
    Map<Long,String> powerMap;
    Map<Long,String> dayEPower;
    Map<Long,String> monthEPower;
}
