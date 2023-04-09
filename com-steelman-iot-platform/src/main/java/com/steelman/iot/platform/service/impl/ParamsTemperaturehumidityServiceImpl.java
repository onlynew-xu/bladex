package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsTemperaturehumidityDao;
import com.steelman.iot.platform.entity.DeviceParamsTemperaturehumidity;
import com.steelman.iot.platform.service.ParamsTemperaturehumidityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("paramsTemperaturehumidityService")

public class ParamsTemperaturehumidityServiceImpl extends BaseService implements ParamsTemperaturehumidityService {
    @Resource
    private DeviceParamsTemperaturehumidityDao  deviceParamsTemperaturehumidityDao;

    @Override
    public Integer insertRecord(DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity) {
        return deviceParamsTemperaturehumidityDao.insertSelective(deviceParamsTemperaturehumidity);
    }

    @Override
    public void updateVariableParams(DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity) {
        deviceParamsTemperaturehumidityDao.updateVariableParams(deviceParamsTemperaturehumidity);
    }

    @Override
    public DeviceParamsTemperaturehumidity findByDeviceId(Long deviceId) {
        return deviceParamsTemperaturehumidityDao.findByDeviceId(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsTemperaturehumidityDao.deleteByDeviceId(deviceId);
    }
}
