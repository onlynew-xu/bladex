package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.LogProjectUserDao;
import com.steelman.iot.platform.entity.LogProjectUser;
import com.steelman.iot.platform.service.LogProjectUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("logProjectUserService")
public class LogProjectUserServiceImpl extends BaseService implements LogProjectUserService {
    @Resource
    private LogProjectUserDao logProjectUserDao;
    @Override
    public Integer saveLogProjectUser(LogProjectUser logProjectUser) {
        logProjectUser.setId(null);
        return logProjectUserDao.saveLogProjectUser(logProjectUser);
    }
}
