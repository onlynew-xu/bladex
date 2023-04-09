package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.ProjectContact;

public interface ProjectContactDao {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectContact record);

    int insertSelective(ProjectContact record);

    ProjectContact selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProjectContact record);

    int updateByPrimaryKey(ProjectContact record);
}