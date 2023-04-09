package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceTask;
import com.steelman.iot.platform.entity.Pager;
import org.apache.poi.ss.formula.functions.T;

import java.util.Map;

public interface DeviceTaskService {

    void insertSelective(DeviceTask record);
    void update(DeviceTask record);
    void deleteById(Long id);
    void insert(DeviceTask record);
    DeviceTask findById(Long id);
    Pager<T> findByDeviceId(Map<String, Integer> params, Long deviceId);

    /**
     * 移除设备对应系统的维保任务
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    int removeByDeviceSystem(Long projectId, Long deviceId, Long systemId);

    /**
     * 删除设备的维保信息
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
