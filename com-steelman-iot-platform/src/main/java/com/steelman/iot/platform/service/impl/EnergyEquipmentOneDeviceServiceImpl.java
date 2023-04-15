package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentOneDeviceDao;
import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentOneDevice;
import com.steelman.iot.platform.service.EnergyEquipmentOneDeviceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    /**
     * 查询表
     */
    @Override
    public List<EnergyEquipmentOneDevice> findAll() {
  //      List<EnergyEquipmentDevice> all = energyEquipmentOneDeviceDao.findAll();

//        for (EnergyEquipmentDevice device : all) {
//            System.out.println(device.toString());
//        }
//        System.out.println();
        return energyEquipmentOneDeviceDao.findAll();
    }
}
