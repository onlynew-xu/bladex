package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataWaveElecDao;
import com.steelman.iot.platform.entity.DeviceDataWaveElec;
import com.steelman.iot.platform.service.DeviceDataWaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceDataWaveService")
public class DeviceDataWaveServiceImpl extends BaseService implements DeviceDataWaveService {
    @Resource
    private DeviceDataWaveElecDao deviceDataWaveElecDao;
    @Override
    public List<DeviceDataWaveElec> getLastedTenData(Long deviceId) {
        return deviceDataWaveElecDao.getLastedTenData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataWaveElecDao.deleteByDeviceId(deviceId);
    }
}
