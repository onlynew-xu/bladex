package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.MonitorDevice;
import org.apache.ibatis.annotations.Param;

public interface MonitorDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(MonitorDevice record);

    int insertSelective(MonitorDevice record);

    MonitorDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MonitorDevice record);

    int updateByPrimaryKey(MonitorDevice record);

    MonitorDevice selectByDeviceId(Long deviceId);

    void deleteDeviceMonitor(@Param("monitorId") Long monitorId,@Param("deviceId") Long deviceId);

    void deleteByMonitorId(Long monitorId);

    void deleteByDeviceId(Long deviceId);
}