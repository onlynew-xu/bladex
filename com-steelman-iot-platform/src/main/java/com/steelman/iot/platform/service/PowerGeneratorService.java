package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerGenerator;
import com.steelman.iot.platform.entity.dto.GeneratorDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/11 0011 14:00
 * @Description:
 */
public interface PowerGeneratorService {
    void insert(PowerGenerator generator);

    void update(PowerGenerator generator);

    List<PowerGenerator> getGeneratorList(Long transformerId);

    PowerGenerator selectById(Long id);

    List<DeviceCenterVo> getPowerGenerator(Long id);

    Map<String, Object> getGeneratorInfo(Long id);

    Long selectCountByTransformId(Long transformId, Long projectId);

    PowerGenerator getByTransformerANDName(Long transformerId, String name);

    int removeById(Long generatorId);

    Long selectCountByPowerId(Long id);

    List<PowerGenerator> findByPowerId(Long id);

    GeneratorDto getGeneratorDto(Long generatorId);
}
