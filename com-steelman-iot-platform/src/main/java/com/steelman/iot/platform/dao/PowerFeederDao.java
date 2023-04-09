package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerFeeder;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerFeederDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerFeeder record);

    int insertSelective(PowerFeeder record);

    PowerFeeder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerFeeder record);

    int updateByPrimaryKey(PowerFeeder record);

    List<PowerFeeder> selectByTransformerIdAndLoopType(@Param("transformerId") Long transformerId,@Param("loopType") Integer loopType);

    Long selectCountByPowerId(Long powerId);

    List<DeviceCenterVo> selectPowerSignleFeederCenter(@Param("powerId") Long powerId, @Param("loopType") int loopType);

    List<DeviceCenterVo> selectPowerMultiFeederCenter(@Param("powerId") Long powerId, @Param("loopType") int loopType);

    Long selectCountByTransformId(Long transformId, Long projectId,Integer type);

    List<PowerFeeder> getPowerFeederType(@Param("transformerId") Long transformerId,@Param("type") Integer type);

    PowerFeeder selectByPowerIdAndName(@Param("feederName") String feederName, @Param("powerId") Long powerId);

    List<PowerFeeder> findByPowerId(@Param("powerId") Long powerId);
}