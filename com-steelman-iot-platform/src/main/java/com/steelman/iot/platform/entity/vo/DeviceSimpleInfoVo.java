package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSimpleInfoVo implements Serializable {
    private Long deviceId;
    private String deviceName;
    private Long deviceTypeId;
    private String deviceTypeName;
    private String createTime;
    private Integer status;
}
