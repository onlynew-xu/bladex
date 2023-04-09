package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentVariable {
    private Long  id;
    private String name;
    private String brand;
    private Date   productionDate;
    private String ratedPower;
    private Long energyTypeId;
    private String energyTypeName;
    private Long consumeTypeId;
    private String consumeTypeName;
    private Long areaId;
    private String areaName;
    private Long buildingId;
    private String buildingName;
    private Long storeyId;
    private String storeyName;
    private Long roomId;
    private String roomName;
    private Integer locationFlag;
    private String  location;
    private Long   deviceTypeId;
    private String deviceTypeName;
    private Long deviceId;
    private  String deviceName;
    private Long companyId;
    private String companyName;
    private Long projectId;
}
