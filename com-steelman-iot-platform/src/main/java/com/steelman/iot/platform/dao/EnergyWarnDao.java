package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyWarn;

public interface EnergyWarnDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyWarn record);

    int insertSelective(EnergyWarn record);

    EnergyWarn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyWarn record);

    int updateByPrimaryKey(EnergyWarn record);
}