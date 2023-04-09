package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsCompensateElecDao;
import com.steelman.iot.platform.entity.DeviceParamsCompensateElec;
import com.steelman.iot.platform.service.DeviceParamsCompensateElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamsCompensateElecService")
public class DeviceParamsCompensateElecServiceImpl extends BaseService implements DeviceParamsCompensateElecService {
    @Resource
    private DeviceParamsCompensateElecDao deviceParamsCompensateElecDao;

    @Override
    public Integer insertRecored(DeviceParamsCompensateElec deviceParamsCompensateElec) {
        return deviceParamsCompensateElecDao.insert(deviceParamsCompensateElec);
    }

    @Override
    public void updateVariableParams(DeviceParamsCompensateElec deviceParamsCompensateElec) {
        deviceParamsCompensateElecDao.updateVariableParams(deviceParamsCompensateElec);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsCompensateElecDao.deleteByDeviceId(deviceId);
    }

    @Override
    public DeviceParamsCompensateElec findByDeviceId(Long deviceId) {
        return deviceParamsCompensateElecDao.findByDeviceId(deviceId);
    }
}
