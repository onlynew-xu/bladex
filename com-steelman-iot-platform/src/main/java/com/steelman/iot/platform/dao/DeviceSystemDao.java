package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceSystemDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceSystem record);

    int insertSelective(DeviceSystem record);

    DeviceSystem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceSystem record);

    int updateByPrimaryKey(DeviceSystem record);

    void deleteByDeviceId(Long deviceId);

    void removeDeviceSystem(@Param("projectId") Long projectId, @Param("deviceId") Long deviceId, @Param("systemId") Long systemId);

    int insertList(@Param("deviceSystemList") List<DeviceSystem> deviceSystemList);

    List<DeviceSystem> selectByDeviceId(@Param("deviceId") Long deviceId);

    int updateDeviceNameByDeviceId(@Param("deviceId") Long deviceId, @Param("deviceName") String deviceName);
}