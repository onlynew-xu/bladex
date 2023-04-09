package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReportDetail {
    private Long consumeTypeId;
    private String consumeTypeName;
    private String ringRatioMeasure;
    private String analogyMeasure;
    private Integer alarmCount;
    private String spikeMeasure;
    private String peakMeasure;
    private String normalMeasure;
    private String valleyMeasure;
    private String totalMeasure;

    ;

}
