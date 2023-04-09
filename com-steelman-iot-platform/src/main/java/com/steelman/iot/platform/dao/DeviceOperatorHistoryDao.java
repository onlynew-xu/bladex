package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceOperatorHistory;

public interface DeviceOperatorHistoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceOperatorHistory record);

    int insertSelective(DeviceOperatorHistory record);

    DeviceOperatorHistory selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceOperatorHistory record);

    int updateByPrimaryKey(DeviceOperatorHistory record);
}