package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerMatriculation;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerMatriculationDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerMatriculation record);

    int insertSelective(PowerMatriculation record);

    PowerMatriculation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerMatriculation record);

    int updateByPrimaryKey(PowerMatriculation record);

    List<PowerMatriculation> selectByTransformerId (Long transformerId);

    Long selectCountByPowerId(Long powerId);

    List<PowerTransformer> selectMatriculationTransformer(Long matriculationId);

    List<DeviceCenterVo> selectPowerMatriculationCenter(Long powerId);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerMatriculation selectByPowerAndName(@Param("powerId") Long powerId,@Param("name") String name);

    List<PowerMatriculation> selectAllByTransformerId(@Param("transformerId") Long transformerId);

    List<PowerMatriculation> findByTransformerId(@Param("transformerId") Long transformerId, @Param("secondTransformerId") Long secondTransformerId);

    List<PowerMatriculation> findByPowerId(@Param("powerId") Long powerId);
}