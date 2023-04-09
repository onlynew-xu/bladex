package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceTypeAlarm;

import java.util.List;


/**
 * @author tang
 * date 2020-11-23
 */
public interface DeviceTypeAlarmService {

    void insertSelective(DeviceTypeAlarm record);
    void update(DeviceTypeAlarm record);
    void deleteById(Long id);
    DeviceTypeAlarm findByid(Long id);
    List<DeviceTypeAlarm> selectByTypeAlarm(DeviceTypeAlarm record);

    /**
     * 根据设备类型Id 和系统Id 获取配置项
     * @param deviceTypeId
     * @param systemList
     * @return
     */
    List<DeviceTypeAlarm> selectByDeviceTypeAndSystem(Long deviceTypeId,List<Long> systemList);


}
