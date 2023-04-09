package com.steelman.iot.platform.service;

public interface AlarmInfoService {
    /**
     * 移除设备特定系统下的预计数据
     * @param projectId
     * @param deviceId
     * @param systemId
     */
    void removeDeviceSystem(Long projectId, Long deviceId, Long systemId);

    /**
     * 删除设备的预警信息
     * @param deviceId
     * @return
     */
    int removeByDeviceId(Long deviceId);
}
