package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.TransformerInfoDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 14:05
 * @Description:
 */
public interface PowerTransformerService {
    void insert(PowerTransformer transformer);

    void update(PowerTransformer transformer);

    List<PowerTransformer> selectByPowerId(Long powerId);

    void delete(Long id);

    void deleteByPowerId(Long id);

    PowerTransformer getTransformerInfo(Long transformerId);

    List<DeviceCenterVo> getPowerTransformer(Long powerId);

    PowerTransformer selectByTransformNameAndPowerId(String name, Long powerId);

    Map<Integer,String> selectCount(Long transformId, Long projectId);

    Integer updateRelationStatus(Long transformerId,Integer status);

    List<PowerTransformer> getUnBandingTransformer(Long powerId);

    TransformerInfoDto getTransformerInfoDto(Long transformerId);
}
