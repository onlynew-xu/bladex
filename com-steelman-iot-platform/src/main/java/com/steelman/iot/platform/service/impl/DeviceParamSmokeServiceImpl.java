package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsSmokeDao;
import com.steelman.iot.platform.entity.DeviceParamsSmoke;
import com.steelman.iot.platform.service.DeviceParamSmokeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamSmokeService")
public class DeviceParamSmokeServiceImpl extends BaseService implements DeviceParamSmokeService {
    @Resource
    private DeviceParamsSmokeDao deviceParamsSmokeDao;

    public void insertSelective(DeviceParamsSmoke record) {
        deviceParamsSmokeDao.insertSelective(record);
    }
    public void update(DeviceParamsSmoke record) {
        deviceParamsSmokeDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceParamsSmokeDao.deleteByPrimaryKey(id);
    }
    public DeviceParamsSmoke findByid(Long id) {
       return deviceParamsSmokeDao.selectByPrimaryKey(id);
    }
    public DeviceParamsSmoke findByDeviceId(Long deviceId) {
        return deviceParamsSmokeDao.findByDeviceId(deviceId);
    }

    @Override
    public int updateVariableParams(DeviceParamsSmoke variableParamsSmoke) {
       return deviceParamsSmokeDao.updateVariableParams(variableParamsSmoke);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsSmokeDao.deleteByDeviceId(deviceId);
    }
}
