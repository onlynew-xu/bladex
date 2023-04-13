package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentSecondDeviceDao;
import com.steelman.iot.platform.service.EnergyEquipmentSecondDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("energyEquipmentSecondDeviceService")
public class EnergyEquipmentSecondDeviceServiceImpl extends BaseService implements EnergyEquipmentSecondDeviceService {
    @Resource
    private EnergyEquipmentSecondDeviceDao energyEquipmentSecondDeviceDao;

    /**
     * 获取二级设备总数
     */
    @Override
    public int getEnergyEquipmentSecondCount() {
        return energyEquipmentSecondDeviceDao.getEnergyEquipmentSecondCount();
    }
}
