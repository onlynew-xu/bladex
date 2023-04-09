package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerWaveComponents;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 15:49
 * @Description:
 */
public interface PowerWaveComponentsService {
    void update(PowerWaveComponents waveComponents);

    void insert(PowerWaveComponents waveComponents);

    void delete(Long id);

    List<PowerWaveComponents> getComponentsList(Long waveId);

    PowerWaveComponents selectById(Long id);

    Boolean updateInfo(Map<String, Object> paramMap);
}
