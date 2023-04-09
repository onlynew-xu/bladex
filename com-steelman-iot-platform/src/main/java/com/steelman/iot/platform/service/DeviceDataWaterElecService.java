package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceDataWaterElec;
import org.apache.ibatis.annotations.Param;

/**
 * @author tang
 * date 2020-11-23
 */
public interface DeviceDataWaterElecService {

    void insertSelective(DeviceDataWaterElec record);
    void update(DeviceDataWaterElec record);
    void deleteById(Long id);
    DeviceDataWaterElec findByid(Long id);
    DeviceDataWaterElec findRecentData(Long deviceId);

    /**
     * 删除设备数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);
}
