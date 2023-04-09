package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureDayMonthStatistic {
    //当日总能耗
    private String dayTotal;
    //当日碳总排放量
    private String dayEmissions;
    //昨日总能耗
    private String yesterDayTotal;
    //当月总能耗
    private String monthTotal;
}
