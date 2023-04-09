package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureDateData {
    private String total;
    private String spike;
    private String peak;
    private String normal;
    private String valley;
    private String yearMonthDay;
    private String year;
    private String month;
    private String day;
    private String yearMonth;
}
