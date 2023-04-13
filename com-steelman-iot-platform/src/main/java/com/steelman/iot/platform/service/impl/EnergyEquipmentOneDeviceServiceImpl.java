package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentOneDeviceDao;
import com.steelman.iot.platform.service.EnergyEquipmentOneDeviceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service("energyEquipmentOneDeviceService")
@Slf4j
public class EnergyEquipmentOneDeviceServiceImpl implements EnergyEquipmentOneDeviceService {

    @Resource
    private EnergyEquipmentOneDeviceDao energyEquipmentOneDeviceDao;

    @Override
    public Integer getEnergyEquipmentOneCount() {
        int energyEquipmentOneCount = energyEquipmentOneDeviceDao.getEnergyEquipmentOneCount();

        return energyEquipmentOneCount;

    }
}
