package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerMatriculation;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.PowerMatriculationDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 15:36
 * @Description:
 */
public interface PowerMatriculationService {
    void insert(PowerMatriculation matriculation);

    void update(PowerMatriculation matriculation);

    List<PowerMatriculation> getMatriculationList(Long transformerId);

    Long selectCountByPowerId(Long powerId);

    List<PowerTransformer> selectMatriculation(Long matriculationId);

    List<DeviceCenterVo> getPowerMatriculation(Long powerId);

    Long selectCount(Long transformId, Long projectId);

    PowerMatriculation selectByPowerAndName(Long powerId, String name);

    List<PowerMatriculationDto> getAllMatriculation(Long transformerId);

    PowerMatriculation findById(Long matriculationId);

    List<PowerMatriculation> findByTransformerId(Long transformerId, Long secondTransformerId);

    int removeById(Long matriculationId);

    List<PowerMatriculation> findByPowerId(Long powerId);

    PowerMatriculationDto getPowerMatriculationInfo(Long matriculationId);
}
