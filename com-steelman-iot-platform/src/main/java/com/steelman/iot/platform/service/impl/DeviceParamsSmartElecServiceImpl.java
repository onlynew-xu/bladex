package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceParamsSmartElecDao;
import com.steelman.iot.platform.entity.DeviceParamsSmartElec;
import com.steelman.iot.platform.service.DeviceParamsSmartElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceParamsSmartElecService")
public class DeviceParamsSmartElecServiceImpl extends BaseService implements DeviceParamsSmartElecService {
    @Resource
    private DeviceParamsSmartElecDao deviceParamsSmartElecDao;

    public void insertSelective(DeviceParamsSmartElec record) {
        deviceParamsSmartElecDao.insertSelective(record);
    }
    public void update(DeviceParamsSmartElec record) {
        deviceParamsSmartElecDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceParamsSmartElecDao.deleteByPrimaryKey(id);
    }
    public DeviceParamsSmartElec findByid(Long id) {
       return deviceParamsSmartElecDao.selectByPrimaryKey(id);
    }
    public DeviceParamsSmartElec findByDeviceId(Long deviceId) {
        return deviceParamsSmartElecDao.findByDeviceId(deviceId);
    }

    @Override
    public List<DeviceParamsSmartElec> selectPowerIncomingDevice(Long powerId) {
        return deviceParamsSmartElecDao.selectDeviceByPowerId(powerId);
    }

    @Override
    public List<DeviceParamsSmartElec> selectTransformerIncomingDevice(Long transformerId) {
      return deviceParamsSmartElecDao.selectTransformerIncomingDevice(transformerId);
    }

    @Override
    public int updateVariableParams(DeviceParamsSmartElec variableSmartParams) {
        return deviceParamsSmartElecDao.updateVariableParams(variableSmartParams);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceParamsSmartElecDao.deleteByDeviceId(deviceId);
    }
}
