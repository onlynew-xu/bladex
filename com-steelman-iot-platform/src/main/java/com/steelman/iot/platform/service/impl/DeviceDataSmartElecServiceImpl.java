package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataSmartElecDao;
import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.service.DeviceDataSmartElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("deviceDataSmartElecService")
public class DeviceDataSmartElecServiceImpl extends BaseService implements DeviceDataSmartElecService {
    @Resource
    private DeviceDataSmartElecDao deviceDataSmartElecDao;


    @Override
    public List<DeviceDataSmartElec> getLastedTenData(Long deviceId) {
        return deviceDataSmartElecDao.getLastedTenData(deviceId);
    }

    @Override
    public DeviceDataSmartElec getLastData(Long deviceId) {
        return deviceDataSmartElecDao.selectLastData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectPowerVoltData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerVoltData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectPowerAmpData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerAmpData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectPowerFactorData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerFactorData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectPowerActiveData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerActiveData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectPowerReactiveData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerReactiveData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectThdiData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerThdiData(deviceId);
    }

    @Override
    public List<Map<String, Object>> selectThdvData(Long deviceId) {
        return deviceDataSmartElecDao.selectPowerThdvData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataSmartElecDao.deleteByDeviceId(deviceId);
    }
}
