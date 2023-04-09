package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsDoorDao;
import com.steelman.iot.platform.entity.DeviceParamsDoor;
import com.steelman.iot.platform.service.DeviceParamsDoorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("deviceParamsDoorService")
public class DeviceParamsDoorServiceImpl extends BaseService implements DeviceParamsDoorService {
    @Resource
    private DeviceParamsDoorDao deviceParamsDoorDao;

    public void insertSelective(DeviceParamsDoor record) {
        deviceParamsDoorDao.insertSelective(record);
    }
    public void update(DeviceParamsDoor record) {
        deviceParamsDoorDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceParamsDoorDao.deleteByPrimaryKey(id);
    }
    public DeviceParamsDoor findByid(Long id) {
       return deviceParamsDoorDao.selectByPrimaryKey(id);
    }
    public DeviceParamsDoor findByDeviceId(Long deviceId) {
        return deviceParamsDoorDao.findByDeviceId(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsDoorDao.deleteByDeviceId(deviceId);
    }

    @Override
    public int updateVariableParams(DeviceParamsDoor deviceParamsDoor) {
        return deviceParamsDoorDao.updateVariableParams(deviceParamsDoor);
    }
}
