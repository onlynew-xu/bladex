package com.steelman.iot.platform.entity.dto;

import lombok.Data;

/**
 * @Author lsj
 * @DATE 2021/4/1 0001 16:59
 * @Description:
 */
@Data
public class DeviceDto {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private String deviceTypeName;
}
