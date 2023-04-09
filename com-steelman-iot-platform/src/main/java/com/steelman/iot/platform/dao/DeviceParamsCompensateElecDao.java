package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceParamsCompensateElec;
import org.apache.ibatis.annotations.Param;

public interface DeviceParamsCompensateElecDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceParamsCompensateElec record);

    int insertSelective(DeviceParamsCompensateElec record);

    DeviceParamsCompensateElec selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceParamsCompensateElec record);

    int updateByPrimaryKey(DeviceParamsCompensateElec record);

    void updateVariableParams(DeviceParamsCompensateElec deviceParamsCompensateElec);

    /**
     * 删除设备参数
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 获取参数
     * @param deviceId
     * @return
     */
    DeviceParamsCompensateElec findByDeviceId(@Param("deviceId") Long deviceId);
}