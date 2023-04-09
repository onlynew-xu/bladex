package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlarmConfigDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmConfig record);

    int insertSelective(AlarmConfig record);

    AlarmConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmConfig record);

    int updateByPrimaryKey(AlarmConfig record);

    /**
     * 批量插入数据
     * @param alarmConfigList
     * @return
     */
    int insertListRecord(@Param("alarmConfigList") List<AlarmConfig> alarmConfigList);

    /**
     * 移除设备系统的配置项
     * @param deviceId
     * @param systemId
     * @return
     */
    int removeByDeviceSystem(@Param("deviceId") Long deviceId, @Param("systemId") Long systemId);

    /**
     * 移除设备的预警信息
     * @param deviceId
     * @return
     */
    int removeByDeviceId(@Param("deviceId") Long deviceId);
}