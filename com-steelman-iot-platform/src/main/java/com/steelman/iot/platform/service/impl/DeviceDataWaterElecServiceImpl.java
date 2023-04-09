package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataWaterElecDao;
import com.steelman.iot.platform.entity.DeviceDataWaterElec;
import com.steelman.iot.platform.service.DeviceDataWaterElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("deviceDataWaterElecService")
public class DeviceDataWaterElecServiceImpl extends BaseService implements DeviceDataWaterElecService {
    @Resource
    private DeviceDataWaterElecDao deviceDataWaterElecDao;

    public void insertSelective(DeviceDataWaterElec record) {
        deviceDataWaterElecDao.insertSelective(record);
    }
    public void update(DeviceDataWaterElec record) {
        deviceDataWaterElecDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceDataWaterElecDao.deleteByPrimaryKey(id);
    }
    public DeviceDataWaterElec findByid(Long id) {
       return deviceDataWaterElecDao.selectByPrimaryKey(id);
    }

    @Override
    public DeviceDataWaterElec findRecentData(Long deviceId) {
        return deviceDataWaterElecDao.findRecentData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataWaterElecDao.deleteByDeviceId(deviceId);
    }
}
