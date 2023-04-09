package com.steelman.iot.platform.dao;
import com.steelman.iot.platform.entity.SysSystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemDao {
    /**
     * 获取项目的所有系统
     * @param projectId
     * @return
     */
    List<SysSystem> getByProjectId(@Param("projectId") Long projectId);

    List<SysSystem> selectByAll();

    List<SysSystem> getByDeviceId(Long deviceId); //根据设备id获取系统
}
