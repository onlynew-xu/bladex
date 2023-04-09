package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsWaveElecDao;
import com.steelman.iot.platform.entity.DeviceParamsWaveElec;
import com.steelman.iot.platform.service.DeviceParamsWaveElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamsWaveElecService")
public class DeviceParamsWaveElecServiceImpl extends BaseService implements DeviceParamsWaveElecService {
    @Resource
    private DeviceParamsWaveElecDao deviceParamsWaveElecDao;
    @Override
    public int insertRecord(DeviceParamsWaveElec deviceParamsWaveElec) {
        return deviceParamsWaveElecDao.insertSelective(deviceParamsWaveElec);
    }

    @Override
    public void updateVariableParams(DeviceParamsWaveElec deviceParamsWaveElec) {
        deviceParamsWaveElecDao.updateVariableParams(deviceParamsWaveElec);
    }

    @Override
    public DeviceParamsWaveElec findByDeviceId(Long deviceId) {
        return deviceParamsWaveElecDao.findByDeviceId(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsWaveElecDao.deleteByDeviceId(deviceId);
    }
}
