package com.steelman.iot.platform.service;


import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentOneDevice;

import java.util.List;

public interface EnergyEquipmentOneDeviceService {

    /**
     * 获取一级能源总数
     */
    Integer getEnergyEquipmentOneCount();

    /**
     * 查询表
     */
    List<EnergyEquipmentOneDevice> findAll();
}
