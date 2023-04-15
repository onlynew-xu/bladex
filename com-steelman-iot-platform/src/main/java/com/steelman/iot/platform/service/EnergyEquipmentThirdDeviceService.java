package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipmentThirdDevice;

import java.util.List;

public interface EnergyEquipmentThirdDeviceService {
    /**
     * 获取三级设备总数
     */
    int getEnergyEquipmentThirdCount();

    /**
     * 查询三级设备表
     */
    List<EnergyEquipmentThirdDevice> findAll();
}
