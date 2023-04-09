package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceOfflineInfo implements Serializable {

    private Long alarmWarnId;
    private Long deviceId;
    private String sn;
    private String deviceName;
    private String location;
    private Date  offlineTime;
    private Integer status;
}
