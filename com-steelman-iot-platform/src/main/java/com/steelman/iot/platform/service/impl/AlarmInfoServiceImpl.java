package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AlarmInfoDao;
import com.steelman.iot.platform.service.AlarmInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("alarmInfoService")
public class AlarmInfoServiceImpl extends BaseService implements AlarmInfoService {
   @Resource
   private AlarmInfoDao alarmInfoDao;
    @Override
    public void removeDeviceSystem(Long projectId, Long deviceId, Long systemId) {
        alarmInfoDao.removeDeviceSystem(projectId,deviceId,systemId);
    }

    @Override
    public int removeByDeviceId(Long deviceId) {
        return alarmInfoDao.removeDeviceId(deviceId);
    }
}
