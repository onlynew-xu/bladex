package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.AlarmConfig;

import java.util.List;

/**
 * @author tang
 * date 2020-11-23
 */
public interface AlarmConfigService {

    void insertSelective(AlarmConfig record);
    void update(AlarmConfig record);
    void deleteById(Long id);
    AlarmConfig findByid(Long id);

    /**
     * 批量插入报警配置信息
     * @param alarmConfigList
     * @return
     */
   int insertListRecord (List<AlarmConfig> alarmConfigList);

    /**
     * 移除对应系统的报警配置信息
     * @param deviceId
     * @param systemId
     * @return
     */
   int removeByDeviceSystem( Long deviceId, Long systemId);

    /**
     * 移除设备相关的配置信息
     * @param deviceId
     * @return
     */
   int removeByDeviceId(Long deviceId);
}
