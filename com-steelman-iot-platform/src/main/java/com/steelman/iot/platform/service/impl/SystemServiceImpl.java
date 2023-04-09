package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.SystemDao;
import com.steelman.iot.platform.entity.SysSystem;
import com.steelman.iot.platform.service.SystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-12-02
 */
@Service("systemService")
public class SystemServiceImpl extends BaseService implements SystemService {
    @Resource
    private SystemDao systemDao;
    @Override
    public List<SysSystem> getByProjectId(Long projectId) {
        return systemDao.getByProjectId(projectId);
    }
    @Override
    public List<SysSystem> selectByAll() {
        return systemDao.selectByAll();
    }
    @Override
    public List<SysSystem> getByDeviceId(Long deviceId) {
        return systemDao.getByDeviceId(deviceId);
    }
}
