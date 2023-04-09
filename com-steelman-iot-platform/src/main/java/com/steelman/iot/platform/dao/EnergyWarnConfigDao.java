package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyWarnConfig;

public interface EnergyWarnConfigDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyWarnConfig record);

    int insertSelective(EnergyWarnConfig record);

    EnergyWarnConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyWarnConfig record);

    int updateByPrimaryKey(EnergyWarnConfig record);
}