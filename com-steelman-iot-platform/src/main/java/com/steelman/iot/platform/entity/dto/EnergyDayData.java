package com.steelman.iot.platform.entity.dto;

import lombok.Data;

@Data
public class EnergyDayData {
    private String yearMonthDay;
    private Long dayTotal;
    private Long projectId;
}
