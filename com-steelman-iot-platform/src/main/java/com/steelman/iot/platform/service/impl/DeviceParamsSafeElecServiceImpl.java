package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsSafeElecDao;
import com.steelman.iot.platform.entity.DeviceParamsSafeElec;
import com.steelman.iot.platform.service.DeviceParamsSafeElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamsSafeElecService")
public class DeviceParamsSafeElecServiceImpl extends BaseService implements DeviceParamsSafeElecService {
    @Resource
    private DeviceParamsSafeElecDao deviceParamsSafeElecDao;

    public void insertSelective(DeviceParamsSafeElec record) {
        deviceParamsSafeElecDao.insertSelective(record);
    }
    public void update(DeviceParamsSafeElec record) {
        deviceParamsSafeElecDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceParamsSafeElecDao.deleteByPrimaryKey(id);
    }
    public DeviceParamsSafeElec findByid(Long id) {
       return deviceParamsSafeElecDao.selectByPrimaryKey(id);
    }
    public DeviceParamsSafeElec findByDeviceId(Long deviceId) {
        return deviceParamsSafeElecDao.findByDeviceId(deviceId);
    }

    @Override
    public int updateVariableParams(DeviceParamsSafeElec variableSafeParams) {
        return deviceParamsSafeElecDao.updateVariableParams(variableSafeParams);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsSafeElecDao.deleteByDeviceId(deviceId);
    }
}
