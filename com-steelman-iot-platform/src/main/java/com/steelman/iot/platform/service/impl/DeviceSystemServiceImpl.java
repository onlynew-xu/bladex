package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceSystemDao;
import com.steelman.iot.platform.entity.DeviceSystem;
import com.steelman.iot.platform.service.DeviceSystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceSystemService")
public class DeviceSystemServiceImpl extends BaseService implements DeviceSystemService {
    @Resource
    private DeviceSystemDao deviceSystemDao;

    public void insertSelective(DeviceSystem record) {
        deviceSystemDao.insertSelective(record);
    }
    public void update(DeviceSystem record) {
        deviceSystemDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceSystemDao.deleteByPrimaryKey(id);
    }
    public void deleteByDeviceId(Long deviceId) {
        deviceSystemDao.deleteByDeviceId(deviceId);
    }

    @Override
    public void removeDeviceSystem(Long projectId, Long deviceId, Long systemId) {
        deviceSystemDao.removeDeviceSystem(projectId,deviceId,systemId);
    }

    @Override
    public int insertList(List<DeviceSystem> deviceSystemList) {
        return deviceSystemDao.insertList(deviceSystemList);
    }

    @Override
    public List<DeviceSystem> selectByDeviceId(Long deviceId) {
        return deviceSystemDao.selectByDeviceId(deviceId);
    }

    @Override
    public int updateDeviceNameByDeviceId(Long deviceId, String deviceName) {
        return deviceSystemDao.updateDeviceNameByDeviceId(deviceId,deviceName);
    }
}
