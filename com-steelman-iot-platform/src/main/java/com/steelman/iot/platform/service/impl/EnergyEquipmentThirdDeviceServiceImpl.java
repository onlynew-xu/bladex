package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentThirdDeviceDao;
import com.steelman.iot.platform.entity.EnergyEquipmentThirdDevice;
import com.steelman.iot.platform.service.EnergyEquipmentThirdDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("energyEquipmentThirdDeviceService")
public class EnergyEquipmentThirdDeviceServiceImpl extends BaseService implements EnergyEquipmentThirdDeviceService {
    @Resource
    private EnergyEquipmentThirdDeviceDao energyEquipmentThirdDeviceDao;

    /**
     * 获取三级设备总数
     */

    @Override
    public int getEnergyEquipmentThirdCount() {
        Integer count = energyEquipmentThirdDeviceDao.getEnergyEquipmentThirdCount();
        System.out.println(count);
        return energyEquipmentThirdDeviceDao.getEnergyEquipmentThirdCount();
    }

    /**
     * 查询三级设备表
     */
    @Override
    public List<EnergyEquipmentThirdDevice> findAll() {
        return energyEquipmentThirdDeviceDao.findAll();
    }
}
