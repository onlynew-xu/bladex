package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerGeneratorDevice;

public interface PowerGeneratorDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerGeneratorDevice record);

    int insertSelective(PowerGeneratorDevice record);

    PowerGeneratorDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerGeneratorDevice record);

    int updateByPrimaryKey(PowerGeneratorDevice record);
}