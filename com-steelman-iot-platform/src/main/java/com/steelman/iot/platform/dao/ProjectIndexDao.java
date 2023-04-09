package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.ProjectIndex;

public interface ProjectIndexDao {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectIndex record);

    int insertSelective(ProjectIndex record);

    ProjectIndex selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectIndex record);

    int updateByPrimaryKey(ProjectIndex record);
}