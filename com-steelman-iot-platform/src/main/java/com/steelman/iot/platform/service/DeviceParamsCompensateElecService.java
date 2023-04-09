package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceParamsCompensateElec;

public interface DeviceParamsCompensateElecService {
    /**
     * 插入新的数据
      * @param deviceParamsCompensateElec
     * @return
     */
    Integer insertRecored(DeviceParamsCompensateElec deviceParamsCompensateElec);

    /**
     * 更新设备参数
     * @param deviceParamsCompensateElec
     */
    void updateVariableParams(DeviceParamsCompensateElec deviceParamsCompensateElec);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(Long deviceId);

    DeviceParamsCompensateElec findByDeviceId(Long deviceId);
}
