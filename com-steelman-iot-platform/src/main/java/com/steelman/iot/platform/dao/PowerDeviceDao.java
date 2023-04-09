package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DataTemperaturehumidity;
import com.steelman.iot.platform.entity.PowerDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PowerDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerDevice record);

    int insertSelective(PowerDevice record);

    PowerDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerDevice record);

    int updateByPrimaryKey(PowerDevice record);

    List<PowerDevice> selectByPowerId(Long id);

    void deleteByPowerId(Long id);

    List<PowerDeviceInfo> selectPowerDevice(@Param("powerId") Long powerId);

    DataTemperaturehumidity selectTemperatureDevice(Long powerId);

    PowerDevice selectByDeviceId(Long deviceId);


    /**
     * 删除绑定关系
     * @param deviceId
     */
    void deleteByDeviceId(@Param("deviceId") Long deviceId);

    String selectPowerNameByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);
}