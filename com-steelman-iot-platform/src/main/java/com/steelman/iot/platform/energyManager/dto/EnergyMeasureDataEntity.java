package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyMeasureDataEntity {
    private Long equipmentId;
    private String equipmentName;
    private Long consumeId;
    private String consumeName;
    private Long energyTypeId;
    private String energyTypeName;
    private String total;
    private String spike;
    private String peak;
    private String normal;
    private String valley;
    private String monthTotal;
    private String monthSpike;
    private String monthPeak;
    private String monthNormal;
    private String monthValley;
    private String daySpike;
    private String dayPeak;
    private String dayNormal;
    private String dayValley;
    private String dayTotal;
    private String yearMonthDay;

}
