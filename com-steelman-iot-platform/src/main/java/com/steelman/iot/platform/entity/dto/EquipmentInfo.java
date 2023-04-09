package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentInfo {
    private Long  id;
    private String name;
    private String brand;
    private Date productionDate;
    private String ratedPower;
    private String energyTypeName;
    private String consumeTypeName;
    private String  location;
    private String deviceTypeName;
    private String deviceName;
    private String companyName;
    private Long projectId;
}
