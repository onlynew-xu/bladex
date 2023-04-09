package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.LogLoginDao;
import com.steelman.iot.platform.entity.LogLogin;
import com.steelman.iot.platform.service.LogLoginService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("logLoginService")
public class LogLoginServiceImpl extends BaseService implements LogLoginService {
    @Resource
    private LogLoginDao logLoginDao;
    @Override
    public Integer saveLoginLog(LogLogin logLogin) {
        logLogin.setId(null);
        return logLoginDao.saveLoginLog(logLogin);
    }

    @Override
    public List<LogLogin> getLogLogin(Long userId) {
        return logLoginDao.selectByUserId(userId);
    }
}
