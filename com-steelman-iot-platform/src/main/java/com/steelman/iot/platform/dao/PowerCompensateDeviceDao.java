package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.PowerCompensateDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PowerCompensateDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerCompensateDevice record);

    int insertSelective(PowerCompensateDevice record);

    PowerCompensateDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerCompensateDevice record);

    int updateByPrimaryKey(PowerCompensateDevice record);

    List<PowerDeviceInfo> selectByCompensateId(Long compensateId);

    PowerCompensateDevice selectByDeviceId(Long deviceId);

    List<PowerCompensateDevice> selectCompensateDevice(Long compensateId);

    boolean selectCompensateWhetherAlarm(Long compensateId);

    boolean selectCompensateWhetherOffLine(Long compensateId);

    void deleteByDeviceId(@Param("deviceId") Long deviceId);

    PowerCompensate selectCompensateByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);

    List<Long> getPowerDeviceList(@Param("projectId") Long projectId, @Param("powerId") Long powerId);
}