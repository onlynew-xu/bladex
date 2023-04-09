package com.steelman.iot.platform.service.impl;
import com.steelman.iot.platform.dao.DeviceDataSmokeElecDao;
import com.steelman.iot.platform.entity.DeviceDataSmokeElec;
import com.steelman.iot.platform.service.DeviceDataSmokeElecService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service("deviceDataSmokeElecService")
public class DeviceDataSmokeElecServiceImpl extends BaseService implements DeviceDataSmokeElecService {
    @Resource
    private DeviceDataSmokeElecDao deviceDataSmokeElecDao;

    public void insertSelective(DeviceDataSmokeElec record) {
        deviceDataSmokeElecDao.insertSelective(record);
    }
    public void update(DeviceDataSmokeElec record) {
        deviceDataSmokeElecDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceDataSmokeElecDao.deleteByPrimaryKey(id);
    }
    public DeviceDataSmokeElec findByid(Long id) {
       return deviceDataSmokeElecDao.selectByPrimaryKey(id);
    }
    public DeviceDataSmokeElec selectBySbidLimit(Long deviceId) {
        return deviceDataSmokeElecDao.selectBySbidLimit(deviceId);
    }

    @Override
    public DeviceDataSmokeElec selectByLastedData(Long deviceId) {
        return deviceDataSmokeElecDao.selectByLastedData(deviceId);
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceDataSmokeElecDao.deleteByDeviceId(deviceId);
    }
}
