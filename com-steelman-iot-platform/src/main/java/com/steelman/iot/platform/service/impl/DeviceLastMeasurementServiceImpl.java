package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceLastMeasureMentDao;
import com.steelman.iot.platform.entity.DeviceMeasurement;
import com.steelman.iot.platform.service.DeviceLastMeasurementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("deviceLastMeasurementService")
public class DeviceLastMeasurementServiceImpl extends BaseService implements DeviceLastMeasurementService  {
    @Resource
    private DeviceLastMeasureMentDao deviceLastMeasureMentDao;

    @Override
    public List<DeviceMeasurement> getMeasureData(String[] pastDateStrArr, Long deviceId) {
        return deviceLastMeasureMentDao.getMeasureData(pastDateStrArr,deviceId);
    }
}
