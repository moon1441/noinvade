package com.electric.noinvade.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageAlarmInfoVO {
    int totalPage;
    List<AlarmInfoVO> allPowerInfos;
}
