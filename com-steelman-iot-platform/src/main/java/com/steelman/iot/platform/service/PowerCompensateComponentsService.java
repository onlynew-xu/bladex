package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.PowerCompensateComponents;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/15 0015 15:54
 * @Description:
 */
public interface PowerCompensateComponentsService {
    void insert(PowerCompensateComponents components);

    void update(PowerCompensateComponents components);

    void delete(Long id);

    List<PowerCompensateComponents> getComponentsList(Long compensateId);

    PowerCompensateComponents selectById(Long compensateComponentsId);

    Boolean updateInfo(Map<String, Object> paramMap);
}
