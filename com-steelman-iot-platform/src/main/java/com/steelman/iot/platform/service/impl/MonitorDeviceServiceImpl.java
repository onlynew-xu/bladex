package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.MonitorDeviceDao;
import com.steelman.iot.platform.entity.MonitorDevice;
import com.steelman.iot.platform.service.MonitorDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author lsj
 * @DATE 2021/4/28 0028 15:03
 * @Description:
 */
@Service("monitorDeviceService")
public class MonitorDeviceServiceImpl implements MonitorDeviceService {

    @Resource
    private MonitorDeviceDao monitorDeviceDao;

    @Override
    public MonitorDevice getDeviceMonitorHls(Long deviceId) {
        return monitorDeviceDao.selectByDeviceId(deviceId);
    }

    @Override
    public void insert(MonitorDevice monitorDevice) {
        monitorDeviceDao.insert(monitorDevice);
    }

    @Override
    public void deleteDeviceMonitor(Long monitorId, Long deviceId) {
        monitorDeviceDao.deleteDeviceMonitor(monitorId,deviceId);
    }

    @Override
    public void deleteByMonitorId(Long monitorId) {
        monitorDeviceDao.deleteByMonitorId(monitorId);
    }

    @Override
    public void deleteByDeviceId(Long deviceId) {
        monitorDeviceDao.deleteByDeviceId(deviceId);
    }
}
