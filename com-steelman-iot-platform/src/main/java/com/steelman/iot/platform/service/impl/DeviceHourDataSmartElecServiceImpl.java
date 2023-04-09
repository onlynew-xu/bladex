package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.energyManager.dao.DeviceHourDataSmartElecDao;
import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartElec;
import com.steelman.iot.platform.service.DeviceHourDataSmartElecService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceHourDataSmartElecService")
public class DeviceHourDataSmartElecServiceImpl extends BaseService implements DeviceHourDataSmartElecService {
    @Resource
    private DeviceHourDataSmartElecDao deviceHourDataSmartElecDao;
    @Override
    public List<DeviceHourDataSmartElec> getDayData(Long deviceId, String yearMonthDay) {
        return deviceHourDataSmartElecDao.getDayData(deviceId,yearMonthDay);
    }
}
