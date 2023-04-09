package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.MonitorDao;
import com.steelman.iot.platform.entity.Monitor;
import com.steelman.iot.platform.service.MonitorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/5/10 0010 15:03
 * @Description:
 */
@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

    @Resource
    private MonitorDao monitorDao;

    @Override
    public Monitor selectBySerialNum(String serialNum) {
        return monitorDao.selectBySerialNum(serialNum);
    }

    @Override
    public void insert(Monitor monitor) {
        monitorDao.insert(monitor);
    }

    @Override
    public Monitor findById(Long id) {

        return monitorDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long id) {
        monitorDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<Monitor> selectByProjectId(Long projectId) {
        return monitorDao.selectByProjectId(projectId);
    }
}
