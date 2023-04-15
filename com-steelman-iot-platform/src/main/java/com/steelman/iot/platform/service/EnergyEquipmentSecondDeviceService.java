package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipmentSecondDevice;

import java.util.List;

public interface EnergyEquipmentSecondDeviceService {
    /**
     * 获取二级设备总数
     */
    int getEnergyEquipmentSecondCount();

    /**
     * 查询二级设备表
     */
    List<EnergyEquipmentSecondDevice> findAll();
}
