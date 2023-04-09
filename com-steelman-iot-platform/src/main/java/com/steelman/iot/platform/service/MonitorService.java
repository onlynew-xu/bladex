package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Monitor;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/5/10 0010 15:03
 * @Description:
 */
public interface MonitorService {
    Monitor selectBySerialNum(String serialNum);

    void insert(Monitor monitor);

    Monitor findById(Long id);

    void delete(Long id);

    /**
     * 查询所有监控设备信息
     * @param projectId
     * @return
     */
    List<Monitor> selectByProjectId(Long projectId);
}
