package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSimpleInfo {
    private Long equipmentId;
    private String equipmentName;
    private Long typeId;
    private String typeName;
    private Long consumeId;
    private String consumeName;
    private String createTime;
    private Integer status;
}
