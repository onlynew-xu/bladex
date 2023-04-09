package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.SystemContent;
import org.apache.ibatis.annotations.Param;

public interface SystemContentDao {
    int deleteByPrimaryKey(Long id);

    int insert(SystemContent record);

    int insertSelective(SystemContent record);

    SystemContent selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemContent record);

    int updateByPrimaryKey(SystemContent record);

    SystemContent getByProjectIdAndSystemId(@Param("projectId") Long projectId, @Param("systemId") Long systemId);
}