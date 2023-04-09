package com.steelman.iot.platform.energyManager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeMeasure {
    private String name;
    private Object measure;
    private List<EquipmentMeasure> equipmentMeasureList;
}
