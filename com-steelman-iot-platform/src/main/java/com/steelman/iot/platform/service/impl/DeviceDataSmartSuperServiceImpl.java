package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataSmartSuperDao;
import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.entity.DeviceDataSmartSuper;
import com.steelman.iot.platform.service.DeviceDataSmartSuperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceDataSmartSuperService")
public class DeviceDataSmartSuperServiceImpl extends BaseService implements DeviceDataSmartSuperService {
    @Resource
    private DeviceDataSmartSuperDao deviceDataSmartSuperDao;
    @Override
    public List<DeviceDataSmartSuper> getLastedTenData(Long id) {
        return deviceDataSmartSuperDao.getLastedTenData(id);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataSmartSuperDao.deleteByDeviceId(deviceId);
    }

    @Override
    public DeviceDataSmartSuper getLastData(Long deviceId) {
        return deviceDataSmartSuperDao.getLastData(deviceId);
    }

    @Override
    public List<DeviceDataSmartElec> getLastedTenDataElec(Long deviceId) {
        return deviceDataSmartSuperDao.getLastedTenDataElec(deviceId);
    }
}
