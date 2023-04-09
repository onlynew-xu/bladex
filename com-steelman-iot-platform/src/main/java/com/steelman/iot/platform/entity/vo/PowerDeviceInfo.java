package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 16:57
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerDeviceInfo {

    private Long id;
    private Long deviceId;
    private Long deviceTypeId;
    private Integer status;
    private String deviceName;
    private String deviceTypeName;
}
