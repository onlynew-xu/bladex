package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmType;

public interface AlarmTypeDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmType record);

    int insertSelective(AlarmType record);

    AlarmType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmType record);

    int updateByPrimaryKey(AlarmType record);
}