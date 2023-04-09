package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerDeviceDto {
    private Long deviceId;
    private String deviceName;
    private Long powerId;
    private String powerName;
    private Long powerDeviceId;
    private String powerDeviceName;
    private Long  loopId;
    private Integer type;
    private String loopName;

}
