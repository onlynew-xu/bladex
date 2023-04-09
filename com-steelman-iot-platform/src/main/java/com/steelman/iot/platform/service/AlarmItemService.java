package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.AlarmItem;

import java.util.List;

/**
 * @author tang
 * date 2020-11-23
 */
public interface AlarmItemService {

    void insertSelective(AlarmItem record);
    void update(AlarmItem record);
    void deleteById(Long id);
    AlarmItem findByid(Long id);

    /**
     * 获取所有报警项目
     * @return
     */
    List<AlarmItem> selectAll();
}
