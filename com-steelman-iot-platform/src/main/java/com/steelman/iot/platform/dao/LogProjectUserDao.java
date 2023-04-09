package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.LogProjectUser;

public interface LogProjectUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(LogProjectUser record);

    int insertSelective(LogProjectUser record);

    LogProjectUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogProjectUser record);

    int updateByPrimaryKey(LogProjectUser record);

    /**
     * 保存用户登录项目的日志
     * @param logProjectUser
     * @return
     */
    Integer saveLogProjectUser(LogProjectUser logProjectUser);
}