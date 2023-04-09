package com.steelman.iot.platform.service.impl;
import com.steelman.iot.platform.dao.DeviceTypeAlarmDao;
import com.steelman.iot.platform.entity.DeviceTypeAlarm;
import com.steelman.iot.platform.service.DeviceTypeAlarmService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("deviceTypeAlarmService")
public class DeviceTypeAlarmServiceImpl extends BaseService implements DeviceTypeAlarmService {
    @Resource
    private DeviceTypeAlarmDao deviceTypeAlarmDao;

    public void insertSelective(DeviceTypeAlarm record) {
        deviceTypeAlarmDao.insertSelective(record);
    }
    public void update(DeviceTypeAlarm record) {
        deviceTypeAlarmDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceTypeAlarmDao.deleteByPrimaryKey(id);
    }
    public DeviceTypeAlarm findByid(Long id) {
       return deviceTypeAlarmDao.selectByPrimaryKey(id);
    }
    public List<DeviceTypeAlarm> selectByTypeAlarm(DeviceTypeAlarm record) {
        return deviceTypeAlarmDao.selectByTypeAlarm(record);
    }

    @Override
    public List<DeviceTypeAlarm> selectByDeviceTypeAndSystem(Long deviceTypeId, List<Long> systemList) {
        return deviceTypeAlarmDao.selectByDeviceTypeAndSystem(deviceTypeId,systemList);
    }
}
