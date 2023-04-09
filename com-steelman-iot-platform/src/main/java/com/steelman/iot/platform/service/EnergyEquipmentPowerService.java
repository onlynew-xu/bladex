package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipmentPower;

/**
 * @Author lsj
 * @DATE 2021/3/30 0030 17:30
 * @Description:
 */
public interface EnergyEquipmentPowerService {
    void save(EnergyEquipmentPower equipmentPower);

    void deleteByEquipmentId(Long equipmentId);

    EnergyEquipmentPower getInfoByEquipmentId(Long equipmentId);

    void update(EnergyEquipmentPower equipmentPower);
}
