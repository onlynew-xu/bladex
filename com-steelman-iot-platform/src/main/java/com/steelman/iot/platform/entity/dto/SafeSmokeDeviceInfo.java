package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafeSmokeDeviceInfo implements Serializable {
    private Long deviceId;
    private String serialNum;
    private String deviceName;
    private Long deviceTypeId;
    private String deviceTypeName;
    private String temperature;
    private String location;
    private String pictureUrl;
    private String videoUrl;
    private Integer status;
    private Integer switchStatus;
    private Integer erasure;
}
