package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerGenerator;
import com.steelman.iot.platform.entity.dto.GeneratorDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PowerGeneratorDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerGenerator record);

    int insertSelective(PowerGenerator record);

    PowerGenerator selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerGenerator record);

    int updateByPrimaryKey(PowerGenerator record);

    List<PowerGenerator> selectByTransformerId(Long transformerId);

    List<DeviceCenterVo> selectPowerGeneratorCenter(Long powerId);

    Map<String, Object> selectCenterGeneratorInfo(Long id);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerGenerator getByTransformerANDName(@Param("transformerId") Long transformerId, @Param("name") String name);

    Long selectCountByPowerId(@Param("powerId") Long powerId);

    List<PowerGenerator> findByPowerId(@Param("powerId") Long powerId);

    GeneratorDto getGeneratorDto(@Param("generatorId") Long generatorId);
}