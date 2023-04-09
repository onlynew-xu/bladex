package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerBoxLoop;
import com.steelman.iot.platform.entity.PowerBoxLoopDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PowerBoxLoopDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerBoxLoopDevice record);

    int insertSelective(PowerBoxLoopDevice record);

    PowerBoxLoopDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerBoxLoopDevice record);

    int updateByPrimaryKey(PowerBoxLoopDevice record);

    List<PowerDeviceInfo> selectByLoopId(Long loopId);

    PowerBoxLoopDevice selectByDeviceId(Long deviceId);

    List<PowerBoxLoopDevice> selectLoopDevice(Long loopId);

    boolean selectBoxWhetherAlarm(Long boxId);

    boolean selectBoxWhetherOffLine(Long boxId);

    int removeByDeviceId(@Param("deviceId") Long deviceId);

    PowerBoxLoop selectBoxLoopByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);

    List<PowerDeviceInfo> getDeviceByBoxId(@Param("boxId") Long boxId);

    List<Long> getPowerDeviceList(@Param("projectId") Long projectId, @Param("powerId") Long powerId);
}