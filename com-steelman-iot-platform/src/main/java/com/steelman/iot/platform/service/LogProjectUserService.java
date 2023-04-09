package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.LogProjectUser;

public interface LogProjectUserService {
    /**
     * 用户访问项目日志
     * @param logProjectUser
     * @return
     */
    Integer saveLogProjectUser(LogProjectUser logProjectUser);
}
