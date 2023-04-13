package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipmentThird;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentThirdService {
    /**
     * 查询3级设备总数
     * @param projectId
     * @return
     */

    int getEnergyEquipmentThirdCount(Long projectId);

    List<EnergyStatus> getEnergyStatistic(Long projectId);

    List<Map<String, Object>> getEquipmentTotal(Long projectId);

    /**
     * 设备详情
     * @param id
     * @return
     */
    EnergyEquipmentThird selectByPrimaryKey(Long id);
}
