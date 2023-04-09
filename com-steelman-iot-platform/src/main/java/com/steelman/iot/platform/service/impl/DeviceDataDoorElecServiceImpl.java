package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataDoorElecDao;
import com.steelman.iot.platform.entity.DeviceDataDoorElec;
import com.steelman.iot.platform.service.DeviceDataDoorElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceDataDoorElecService")
public class DeviceDataDoorElecServiceImpl extends BaseService implements DeviceDataDoorElecService {
   @Resource
   private DeviceDataDoorElecDao deviceDataDoorElecDao;

    @Override
    public DeviceDataDoorElec selectByLastedData(Long deviceId) {
        return deviceDataDoorElecDao.selectByLastedData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataDoorElecDao.deleteByDeviceId(deviceId);
    }
}
