package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DataTemperaturehumidityDao;
import com.steelman.iot.platform.entity.DataTemperaturehumidity;
import com.steelman.iot.platform.service.DataTempatureHumidityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("dataTempatureHumidityService")
public class DataTempatureHumidityServiceImpl extends BaseService implements DataTempatureHumidityService {
    @Resource
    private DataTemperaturehumidityDao dataTemperaturehumidityDao;

    @Override
   public DataTemperaturehumidity  getLastedData(Long deviceId){
       return  dataTemperaturehumidityDao.getLastedData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return dataTemperaturehumidityDao.deleteByDeviceId(deviceId);
    }
}
