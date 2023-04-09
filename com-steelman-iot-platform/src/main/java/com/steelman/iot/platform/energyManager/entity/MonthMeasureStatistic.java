package com.steelman.iot.platform.energyManager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthMeasureStatistic {
    private String year;
    private String month;
    private Integer energyCount;
    private String totalConsume;
    private String yearMonthDay;
    private String yearOverYearConsume;
    private Long projectId;

}
