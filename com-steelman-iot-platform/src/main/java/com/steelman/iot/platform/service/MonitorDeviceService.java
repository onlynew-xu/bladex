package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.MonitorDevice;

/**
 * @Author lsj
 * @DATE 2021/4/28 0028 14:59
 * @Description:
 */
public interface MonitorDeviceService {
    MonitorDevice getDeviceMonitorHls(Long deviceId);

    void insert(MonitorDevice monitorDevice);

    void deleteDeviceMonitor(Long monitorId, Long deviceId);

    void deleteByMonitorId(Long id);

    void deleteByDeviceId(Long deviceId);
}
