package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Power;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerDao {
    int deleteByPrimaryKey(Long id);

    int insert(Power record);

    int insertSelective(Power record);

    Power selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Power record);

    int updateByPrimaryKey(Power record);

    List<Power> selectByProjectId( Long projectId);

    Power selectByNameAndProjectId(@Param("powerName") String powerName, @Param("projectId") Long projectId);
}