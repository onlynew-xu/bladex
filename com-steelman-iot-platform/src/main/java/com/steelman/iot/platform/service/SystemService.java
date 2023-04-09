package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.SysSystem;

import java.util.List;

public interface SystemService {
    /**
     * 根据项目Id 获取所有系统
     * @param projectId
     * @return
     */
    List<SysSystem> getByProjectId(Long projectId);
    List<SysSystem> selectByAll();
    List<SysSystem> getByDeviceId(Long deviceId); //根据设备id获取系统
}
