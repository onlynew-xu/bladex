package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.LogLogin;

import java.util.List;

public interface LogLoginService {
    Integer saveLoginLog(LogLogin logLogin);

    List<LogLogin> getLogLogin(Long userId);
}
