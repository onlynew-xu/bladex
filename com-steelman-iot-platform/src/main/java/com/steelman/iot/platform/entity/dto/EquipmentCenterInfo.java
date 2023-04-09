package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentCenterInfo {

    private Map<String,List<EquipmentSimpleInfo>> energy;

    private Map<String,List<EquipmentSimpleInfo>> consume;

}
