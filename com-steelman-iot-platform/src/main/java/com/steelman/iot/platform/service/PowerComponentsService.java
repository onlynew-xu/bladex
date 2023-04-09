package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerCompensate;
import com.steelman.iot.platform.entity.PowerComponents;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/11 0011 17:34
 * @Description:
 */
public interface PowerComponentsService {
    void insert(PowerComponents components);

    void update(PowerComponents components);

    void delete(Long id);

    List<PowerComponents> getComponentsList(Long id, Integer type,  Integer powerDeviceType);

    List<PowerComponentsDto> selectComponentsByPowerType(Integer powerType, Long powerDeviceId, Integer componentType,Long projectId);

    PowerComponentsDto getComponentsInfo(Long projectId, Long componentsId);

    Boolean updateComponents(Map<String, Object> paramMap);

    List<PowerCompensate> findByPowerId(Long powerId);
}
