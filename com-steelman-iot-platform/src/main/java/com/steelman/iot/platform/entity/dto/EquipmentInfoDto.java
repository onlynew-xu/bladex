package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentInfoDto {
    private Long equipmentId;
    private String equipmentName;
    private String location;
    private String pictureUrl;
    private Long consumeId;
    private String consumeName;
    private Long energyTypeId;
    private String energyTypeName;
    private MeasureData measureData;
}
