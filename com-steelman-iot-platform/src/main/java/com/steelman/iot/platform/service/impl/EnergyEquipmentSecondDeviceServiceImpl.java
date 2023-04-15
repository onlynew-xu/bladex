package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentSecondDeviceDao;
import com.steelman.iot.platform.entity.EnergyEquipmentSecondDevice;
import com.steelman.iot.platform.service.EnergyEquipmentSecondDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 查询二级设备表
     */
    @Override
    public List<EnergyEquipmentSecondDevice> findAll() {
        return energyEquipmentSecondDeviceDao.findAll();
    }
}
