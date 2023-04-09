package com.steelman.iot.platform.service;
import com.steelman.iot.platform.entity.DeviceParamsSmartElec;

import java.util.List;

public interface DeviceParamsSmartElecService {

    void insertSelective(DeviceParamsSmartElec record);
    void update(DeviceParamsSmartElec record);
    void deleteById(Long id);
    DeviceParamsSmartElec findByid(Long id);
    DeviceParamsSmartElec findByDeviceId(Long deviceId);

    List<DeviceParamsSmartElec> selectPowerIncomingDevice(Long powerId);

    List<DeviceParamsSmartElec> selectTransformerIncomingDevice(Long transformerId);

    /**
     * 修改设备参数接口
     * @param variableSmartParams
     * @return
     */
    int updateVariableParams(DeviceParamsSmartElec variableSmartParams);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);
}
