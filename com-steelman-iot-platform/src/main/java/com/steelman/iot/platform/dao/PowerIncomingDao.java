package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerIncomingDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerIncoming record);

    int insertSelective(PowerIncoming record);

    PowerIncoming selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerIncoming record);

    int updateByPrimaryKey(PowerIncoming record);

    List<PowerIncoming> selectByTransformerId( Long transformerId);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> selectPowerIncomingCenter(Long powerId);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerIncoming selectByName(@Param("incomingName") String incomingName, @Param("transformerId") Long transformerId);

    List<PowerIncoming> findByPowerId(@Param("powerId") Long powerId);

    List<PowerIncoming> getByTransformerId(@Param("transformerId") Long transformerId);
}