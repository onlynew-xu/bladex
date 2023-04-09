package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerAlarmData {
    private Long powerId;
    private String powerName;
    private Long powerDeviceId;
    private String powerDeviceName;
    private Long loopId;
    private String loopName;
    private String location;
    private Long alarmItemId;
    private String alarmItemName;
    private String createTime;
}
