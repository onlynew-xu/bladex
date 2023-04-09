package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceAlarmDao;
import com.steelman.iot.platform.service.DeviceAlarmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceAlarmService")
public class DeviceAlarmServiceImpl extends BaseService implements DeviceAlarmService {
    @Resource
    private DeviceAlarmDao deviceAlarmDao;

    @Override
    public int removeByDeviceId(Long deviceId) {
        return deviceAlarmDao.removeByDeviceId(deviceId);
    }
}
