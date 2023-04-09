package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.LogLogin;

import java.util.List;

public interface LogLoginDao {
    int deleteByPrimaryKey(Long id);

    int insert(LogLogin record);

    int insertSelective(LogLogin record);

    LogLogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(LogLogin record);

    int updateByPrimaryKey(LogLogin record);

    /**
     * 保存登录日志
     * @param logLogin
     * @return
     */
    Integer saveLoginLog(LogLogin logLogin);

    List<LogLogin> selectByUserId(Long userId);
}