package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceTask;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface DeviceTaskDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceTask record);

    int insertSelective(DeviceTask record);

    DeviceTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceTask record);

    int updateByPrimaryKey(DeviceTask record);

    List<T>  findByDeviceId(int page, int size, Long deviceId);

    /**
     * 移除设备对应系统的维保任务
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    int removeByDeviceSystem(@Param("projectId") Long projectId, @Param("deviceId") Long deviceId, @Param("systemId") Long systemId);

    /**
     * 删除设备的维保信息
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}