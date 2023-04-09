package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnergyMeasureDateData {
    private Long equipmentId;
    private String equipmentName;
    private String location;
    private Long deviceId;
    private Long areaId;
    private Long buildingId;
    private Long storeyId;
    private Long roomId;
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
    private String yearMonthDay;
}
