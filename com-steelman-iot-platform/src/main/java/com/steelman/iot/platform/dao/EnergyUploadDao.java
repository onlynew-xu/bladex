package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyUpload;

public interface EnergyUploadDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyUpload record);

    int insertSelective(EnergyUpload record);

    EnergyUpload selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyUpload record);

    int updateByPrimaryKey(EnergyUpload record);
}