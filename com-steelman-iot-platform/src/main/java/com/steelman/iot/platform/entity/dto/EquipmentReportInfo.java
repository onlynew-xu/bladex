package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentReportInfo {
    private Long equipmentId;
    private String equipmentName;
    private String ratedPower;
    private Long projectId;
}
