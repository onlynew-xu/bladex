package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.energyManager.dao.DeviceHourDataSmartSuperDao;
import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartSuper;
import com.steelman.iot.platform.service.DeviceHourDataSmartSuperService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceHourDataSmartSuperService")
public class DeviceHourDataSmartSuperServiceImpl extends BaseService implements DeviceHourDataSmartSuperService {
    @Resource
    private DeviceHourDataSmartSuperDao deviceHourDataSmartSuperDao;

    @Override
    public List<DeviceHourDataSmartSuper> getDayData(Long deviceId, String yearMonthDay) {
        return deviceHourDataSmartSuperDao.getDayData(deviceId,yearMonthDay);
    }
}
