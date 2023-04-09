package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.LogOperator;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogOperatorDao {
    int deleteByPrimaryKey(Long id);

    int insert(LogOperator record);

    int insertSelective(LogOperator record);

    LogOperator selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogOperator record);

    int updateByPrimaryKey(LogOperator record);

    List<LogOperator> selectByUserId(Long userId);

    List<LogOperator> getLogOperateByProjectId(@Param("projectId") Long projectId);
}