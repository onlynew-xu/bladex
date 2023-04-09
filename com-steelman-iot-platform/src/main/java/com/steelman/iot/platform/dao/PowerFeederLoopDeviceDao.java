package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerFeederLoop;
import com.steelman.iot.platform.entity.PowerFeederLoopDevice;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PowerFeederLoopDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerFeederLoopDevice record);

    int insertSelective(PowerFeederLoopDevice record);

    PowerFeederLoopDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerFeederLoopDevice record);

    int updateByPrimaryKey(PowerFeederLoopDevice record);

    List<PowerDeviceInfo> selectByLoopId(Long loopId);

    PowerFeederLoopDevice selectByDeviceId(Long deviceId);

    List<PowerFeederLoopDevice> selectLoopDevice(Long loopId);

    boolean selectFeederWhetherOffLine(Long feederId);

    boolean selectFeederWhetherAlarm(Long feederId);

    int removeByDeviceId( @Param("deviceId") Long deviceId);

    PowerFeederLoop selectFeederLoopByDeviceId(Long id);

    List<PowerDeviceDto> findByDeviceSet(@Param("projectId") Long projectId, @Param("deviceIdSet") Set<Long> value);

    List<PowerDeviceInfo> getDeviceByFeederId(@Param("feederId") Long feederId);

    List<Long> getPowerDeviceList(@Param("projectId") Long projectId, @Param("powerId") Long powerId);
}