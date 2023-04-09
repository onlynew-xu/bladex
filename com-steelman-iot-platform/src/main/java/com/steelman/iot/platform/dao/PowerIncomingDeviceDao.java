package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.PowerIncomingDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PowerIncomingDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerIncomingDevice record);

    int insertSelective(PowerIncomingDevice record);

    PowerIncomingDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerIncomingDevice record);

    int updateByPrimaryKey(PowerIncomingDevice record);

    List<PowerDeviceInfo> selectByIncomingId(@Param("incomingId") Long incomingId);

    List<Map<String, Object>> selectCountMeasurement(Long projectId);

    String selectEnergyTrend(@Param("projectId") Long projectId, @Param("type") Integer type,@Param("date") String date);

    PowerIncomingDevice selectByDeviceId(Long deviceId);

    List<PowerIncomingDevice> selectIncomingDevice(Long incomingId);

    boolean selectIncomingWhetherAlarm(Long incomingId);

    boolean selectIncomingWhetherOffLine(Long incomingId);

    void deleteByDeviceId(@Param("deviceId") Long deviceId);

    PowerIncoming selectPowerIncomingByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);

    Long  getBindingStatus(@Param("incomingId") Long incomingId);

    Long getBindingOnDevice(@Param("incomingId") Long incomingId);

    List<Long> getPowerDeviceList(@Param("projectId") Long projectId, @Param("powerId") Long powerId);
}