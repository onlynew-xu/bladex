package com.steelman.iot.platform.energyManager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDateTotalMeasure {
    private Long id;
    private String name;
    private Long consumeId;
    private String consumeName;
    private String total;
}
