package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceMeasurementDao;
import com.steelman.iot.platform.entity.DeviceMeasurement;
import com.steelman.iot.platform.service.DeviceMeasurementService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/4/15 0015 13:54
 * @Description:
 */
@Service("deviceMeasurementService")
public class DeviceMeasurementServiceImpl extends BaseService implements DeviceMeasurementService {

    @Resource
    private DeviceMeasurementDao deviceMeasurementDao;

    @Override
    public List<Map<String, Object>> getLastSevenDayTotal(Long deviceId) {
        List<Map<String, Object>> nowData = deviceMeasurementDao.selectNowDegreeByDeviceId(deviceId);
        List<Map<String, Object>> sixDaysData = deviceMeasurementDao.selectLastDegreeByDeviceId(deviceId);
        nowData.addAll(sixDaysData);
        return nowData;
    }

    @Override
    public int deleteByDeviceId(Long deviceId) {
        return deviceMeasurementDao.deleteByDeviceId(deviceId);
    }

    @Override
    public List<DeviceMeasurement> getMeasureList(String serialNum,String dateFo) {
        return deviceMeasurementDao.getMeasureList(serialNum,dateFo);
    }

    @Override
    public int updateData(List<DeviceMeasurement> deviceMeasurementList) {
        return deviceMeasurementDao.updateData(deviceMeasurementList);
    }

    @Override
    public DeviceMeasurement getLastDevice(Long deviceId) {
        return deviceMeasurementDao.getLastDevice(deviceId);
    }
}
