package com.steelman.iot.platform.service.impl;
import com.steelman.iot.platform.dao.DeviceParamsWaterDao;
import com.steelman.iot.platform.entity.DeviceParamsWater;
import com.steelman.iot.platform.service.DeviceParamsWaterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamsWaterService")
public class DeviceParamsWaterServiceImpl extends BaseService implements DeviceParamsWaterService {
    @Resource
    private DeviceParamsWaterDao deviceParamsWaterDao;

    public void insertSelective(DeviceParamsWater record) {
        deviceParamsWaterDao.insertSelective(record);
    }
    public void update(DeviceParamsWater record) {
        deviceParamsWaterDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceParamsWaterDao.deleteByPrimaryKey(id);
    }
    public DeviceParamsWater findByid(Long id) {
       return deviceParamsWaterDao.selectByPrimaryKey(id);
    }
    public DeviceParamsWater findByDeviceId(Long deviceId) {
        return deviceParamsWaterDao.findByDeviceId(deviceId);
    }

    @Override
    public void updateVariableParams(DeviceParamsWater variableParamsWater) {
        deviceParamsWaterDao.updateVariableParams(variableParamsWater);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsWaterDao.deleteByDeviceId(deviceId);
    }
}
