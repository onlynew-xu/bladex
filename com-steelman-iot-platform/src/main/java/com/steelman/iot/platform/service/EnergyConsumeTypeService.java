package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyConsumeType;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/30 0030 14:17
 * @Description:
 */
public interface EnergyConsumeTypeService {
    void save(EnergyConsumeType energyConsumeType);

    void update(EnergyConsumeType energyConsumeType);

    List<EnergyConsumeType> getList(Long projectId);

    void delete(Long id);

    EnergyConsumeType findById(Long consumeTypeId);

    /**
     * 根据名称获取能耗类型
     * @param projectId
     * @param energyConsumeName
     * @return
     */
    EnergyConsumeType findByName(Long projectId, String energyConsumeName);
}
