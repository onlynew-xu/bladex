package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthDetailStatistic {
    private Long equipmentId;
    private String equipmentName;
    private Long consumeTypeId;
    private String consumeTypeName;
    private String year;
    private String month;
    private String monthTotal;
    private Long  monthTotalLong;
    private String yearOverYearMonthTotal;
    private String yearMonthDay;
}
