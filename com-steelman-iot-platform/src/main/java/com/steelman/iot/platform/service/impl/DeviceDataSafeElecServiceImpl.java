package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDataSafeElecDao;
import com.steelman.iot.platform.entity.DeviceDataSafeElec;
import com.steelman.iot.platform.service.DeviceDataSafeElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("deviceDataSafeElecService")
public class DeviceDataSafeElecServiceImpl extends BaseService implements DeviceDataSafeElecService {
    @Resource
    private DeviceDataSafeElecDao deviceDataSafeElecDao;

    public void insertSelective(DeviceDataSafeElec record) {
        deviceDataSafeElecDao.insertSelective(record);
    }
    public void update(DeviceDataSafeElec record) {
        deviceDataSafeElecDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceDataSafeElecDao.deleteByPrimaryKey(id);
    }
    public DeviceDataSafeElec findByid(Long id) {
       return deviceDataSafeElecDao.selectByPrimaryKey(id);
    }
    public List<DeviceDataSafeElec> selectBySbidLimit(Long deviceId) {
        return deviceDataSafeElecDao.selectBySbidLimit(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataSafeElecDao.deleteByDeviceId(deviceId);
    }
}
