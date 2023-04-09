package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMaintenanceDetail {
    private Long deviceId;
    private String deviceName;
    private String location;
    private Integer status;
    private Integer erasure;
    private Long projectId;
}
