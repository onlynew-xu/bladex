package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerWave;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerWaveDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerWave record);

    int insertSelective(PowerWave record);

    PowerWave selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerWave record);

    int updateByPrimaryKey(PowerWave record);

    Long selectCountByPowerId(Long powerId);

    List<PowerWave> selectByTransformerId(Long transformerId);

    List<DeviceCenterVo> selectPowerWaveCenter(Long powerId);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerWave selectByName(@Param("transformerId") Long transformerId, @Param("waveName") String waveName);

    List<PowerWave> findByPowerId(@Param("powerId") Long powerId);
}