package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmInfo;
import org.apache.ibatis.annotations.Param;

public interface AlarmInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmInfo record);

    int insertSelective(AlarmInfo record);

    AlarmInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmInfo record);

    int updateByPrimaryKey(AlarmInfo record);

    /**
     * 移除设备对应系统的报警数据
     * @param projectId
     * @param deviceId
     * @param systemId
     */
    int removeDeviceSystem(@Param("projectId") Long projectId, @Param("deviceId") Long deviceId,@Param("systemId") Long systemId);

    /**
     * 删除设备的预警数据
     * @param deviceId
     * @return
     */
    int removeDeviceId(@Param("deviceId") Long deviceId);
}