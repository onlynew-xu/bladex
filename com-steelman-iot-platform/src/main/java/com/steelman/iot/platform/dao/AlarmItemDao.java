package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmItem;

import java.util.List;

public interface AlarmItemDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmItem record);

    int insertSelective(AlarmItem record);

    AlarmItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmItem record);

    int updateByPrimaryKey(AlarmItem record);

    List<AlarmItem> selectAll();
}