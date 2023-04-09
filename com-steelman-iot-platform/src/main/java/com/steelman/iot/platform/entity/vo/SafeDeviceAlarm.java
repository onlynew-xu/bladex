package com.steelman.iot.platform.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafeDeviceAlarm implements Serializable {
    private List<DeviceTypeAlarmStatistic> alarmStatisticsData;
    private List<AlarmSimpleInfo> alarmHistory;
}
