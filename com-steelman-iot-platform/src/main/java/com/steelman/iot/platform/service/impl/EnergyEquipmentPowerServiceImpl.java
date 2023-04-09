package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentPowerDao;
import com.steelman.iot.platform.entity.EnergyEquipmentPower;
import com.steelman.iot.platform.service.EnergyEquipmentPowerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author lsj
 * @DATE 2021/3/30 0030 17:31
 * @Description:
 */
@Service("energyEquipmentPowerService")
public class EnergyEquipmentPowerServiceImpl extends BaseService implements EnergyEquipmentPowerService {

    @Resource
    private EnergyEquipmentPowerDao energyEquipmentPowerDao;

    @Override
    public void save(EnergyEquipmentPower equipmentPower) {
        energyEquipmentPowerDao.insert(equipmentPower);
    }

    @Override
    public void deleteByEquipmentId(Long equipmentId) {
        energyEquipmentPowerDao.deleteByEquipmentId(equipmentId);
    }

    @Override
    public EnergyEquipmentPower getInfoByEquipmentId(Long equipmentId) {
        return energyEquipmentPowerDao.selectByEquipmentId(equipmentId);
    }

    @Override
    public void update(EnergyEquipmentPower equipmentPower) {
        energyEquipmentPowerDao.updateByPrimaryKeySelective(equipmentPower);
    }
}
