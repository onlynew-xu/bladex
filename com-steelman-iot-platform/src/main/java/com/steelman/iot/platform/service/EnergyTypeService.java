package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyType;

import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/25 0025 10:10
 * @Description:
 */
public interface EnergyTypeService {
    void save(EnergyType energyType);

    List<EnergyType> getListByProjectSystem(Long projectId, Long systemId);

    EnergyType getInfo(Long id);

    /**
     * 获取所有能耗设备类型
     * @param projectId
     * @return
     */
    List<EnergyType> selectAll(Long projectId);

    /**
     * 获取能耗设备类型
     * @param energyTypeName
     * @param projectId
     * @return
     */
    EnergyType selectByName(String energyTypeName, Long projectId);

    /**
     * 更新能耗设备类型
     * @param energyType
     * @return
     */
    int  updateEnergyType(EnergyType energyType);

    /**
     * 删除能耗设备类型
     * @param energyTypeId
     * @return
     */
    Boolean deleteById(Long energyTypeId,Long projectId);

    /**
     * 根据Id获取能耗设备类型
     * @param energyTypeId
     * @return
     */
    EnergyType selectByPrimaryKey(Long energyTypeId);
}
