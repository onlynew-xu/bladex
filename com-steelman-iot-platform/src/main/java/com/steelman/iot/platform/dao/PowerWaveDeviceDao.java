package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerWave;
import com.steelman.iot.platform.entity.PowerWaveDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PowerWaveDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerWaveDevice record);

    int insertSelective(PowerWaveDevice record);

    PowerWaveDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerWaveDevice record);

    int updateByPrimaryKey(PowerWaveDevice record);

    List<PowerDeviceInfo> selectByWaveId(@Param("waveId") Long waveId);

    PowerWaveDevice selectByDeviceId(Long deviceId);

    List<PowerWaveDevice> selectWaveDevice(Long waveId);

    boolean selectWaveWhetherAlarm(Long waveId);

    boolean selectWaveWhetherOffLine(Long waveId);

    void deleteByDeviceId(@Param("deviceId") Long deviceId);

    PowerWave selectWaveByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);

    List<Long> getPowerDeviceList(@Param("projectId") Long projectId, @Param("powerId") Long powerId);
}