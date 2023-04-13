package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentFourthDeviceDao;
import com.steelman.iot.platform.service.EnergyEquipmentFourthDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("energyEquipmentFourthDeviceService")
public class EnergyEquipmentFourthDeviceServiceImpl extends BaseService implements EnergyEquipmentFourthDeviceService {
    @Resource
    private EnergyEquipmentFourthDeviceDao energyEquipmentFourthDeviceDao;

    /**
     * 获取四级设备总数
     */

    @Override
    public int getEnergyEquipmentFourCount() {
        return energyEquipmentFourthDeviceDao.getEnergyEquipmentFourCount();
    }
}
