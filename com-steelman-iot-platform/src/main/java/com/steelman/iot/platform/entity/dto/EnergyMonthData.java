package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyMonthData {
    private String year;
    private String month;
    private String yearMonthDay;
    private Long monthTotal;

}
