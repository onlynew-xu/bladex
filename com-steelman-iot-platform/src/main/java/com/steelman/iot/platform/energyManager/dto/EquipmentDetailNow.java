package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDetailNow {
    private Long equipmentId;
    private String equipmentName;
    private Long consumeTypeId;
    private String consumeTypeName;
    private String dayTotal;
    private String monthTotal;
    private String totalTotal;
    private String time;
}
