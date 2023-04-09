package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamSmartSuperDao;
import com.steelman.iot.platform.entity.DeviceParamSmartSuper;
import com.steelman.iot.platform.service.DeviceParamSmartSuperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamSmartSuperService")
public class DeviceParamSmartSuperServiceImpl extends BaseService implements DeviceParamSmartSuperService {
    @Resource
    private DeviceParamSmartSuperDao deviceParamSmartSuperDao;

    @Override
    public int saveParam(DeviceParamSmartSuper deviceParamSmartSuper) {
        return deviceParamSmartSuperDao.insertSelective(deviceParamSmartSuper);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamSmartSuperDao.deleteByDeviceId(deviceId);
    }

    @Override
    public DeviceParamSmartSuper findByDeviceId(Long deviceId) {
        return deviceParamSmartSuperDao.findByDeviceId(deviceId);
    }

    @Override
    public int updateVariableParams(DeviceParamSmartSuper deviceParamSmartSuper) {
        return deviceParamSmartSuperDao.updateVariableParams(deviceParamSmartSuper);
    }
}
