package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerWaveComponents;

import java.util.List;

public interface PowerWaveComponentsDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerWaveComponents record);

    int insertSelective(PowerWaveComponents record);

    PowerWaveComponents selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerWaveComponents record);

    int updateByPrimaryKey(PowerWaveComponents record);

    List<PowerWaveComponents> selectComponentsByWaveId(Long waveId);
}