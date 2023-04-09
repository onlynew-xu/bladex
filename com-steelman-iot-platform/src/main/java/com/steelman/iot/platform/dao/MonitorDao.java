package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Monitor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorDao {
    int deleteByPrimaryKey(Long id);

    int insert(Monitor record);

    int insertSelective(Monitor record);

    Monitor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Monitor record);

    int updateByPrimaryKey(Monitor record);

    Monitor selectBySerialNum(String serialNum);

    /**
     * 查询设备的监控信息
     * @param projectId
     * @return
     */
    List<Monitor> selectByProjectId(@Param("projectId") Long projectId);
}