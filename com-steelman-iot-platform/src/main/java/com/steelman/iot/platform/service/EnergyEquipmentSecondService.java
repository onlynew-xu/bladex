package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipmentSecond;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;

import java.util.List;
import java.util.Map;


public interface EnergyEquipmentSecondService {


    List<EnergyStatus> getEnergyStatistic(Long projectId);

    List<Map<String, Object>> getEquipmentTotal(Long projectId);

    /**
     * 获取二级设备详情
     * @param id
     * @return
     */
    EnergyEquipmentSecond selectByPrimaryKey(Long id);



}
