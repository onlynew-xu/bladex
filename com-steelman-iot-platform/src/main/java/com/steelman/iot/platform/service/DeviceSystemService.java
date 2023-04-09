package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceSystem;

import java.util.List;

public interface DeviceSystemService {
    void insertSelective(DeviceSystem record);
    void update(DeviceSystem record);
    void deleteById(Long id);
    void deleteByDeviceId(Long deviceId);

    /**
     * 从特定系统中移除设备
     * @param projectId
     * @param deviceId
     * @param systemId
     */
    void removeDeviceSystem(Long projectId, Long deviceId, Long systemId);

    /**
     * 保存设备系统信息
     * @param deviceSystemList
     * @return
     */
    int insertList(List<DeviceSystem> deviceSystemList);

    /**
     * 获取设备系统
     * @param deviceId
     * @return
     */
    List<DeviceSystem> selectByDeviceId(Long deviceId);

    int updateDeviceNameByDeviceId(Long deviceId, String toString);
}
