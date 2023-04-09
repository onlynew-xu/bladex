package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasureIntegerDate {
    private Integer total;
    private Integer spike;
    private Integer peak;
    private Integer normal;
    private Integer valley;
    private String yearMonth;
    private String yearMonthDay;
}
