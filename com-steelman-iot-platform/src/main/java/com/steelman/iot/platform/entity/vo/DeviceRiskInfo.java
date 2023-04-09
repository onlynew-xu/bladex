package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRiskInfo implements Serializable {

    private Long deviceId;
    private String deviceName;
    private String location;
    private Long alarmTypeId;
    private String alarmTypeName;
    private Integer status;
}
