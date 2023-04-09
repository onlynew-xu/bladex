package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmLevel;

public interface AlarmLevelDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmLevel record);

    int insertSelective(AlarmLevel record);

    AlarmLevel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmLevel record);

    int updateByPrimaryKey(AlarmLevel record);
}