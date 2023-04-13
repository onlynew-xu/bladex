package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentThirdDao;
import com.steelman.iot.platform.entity.EnergyEquipmentThird;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.service.EnergyEquipmentThirdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("energyEquipmentThirdService")
public class EnergyEquipmentThirdServiceImpl extends BaseService implements EnergyEquipmentThirdService {
    @Resource
    private EnergyEquipmentThirdDao energyEquipmentThirdDao;

    @Override
    public int getEnergyEquipmentThirdCount(Long projectId) {
        return energyEquipmentThirdDao.getEnergyEquipmentThirdCount(projectId);
    }

    @Override
    public List<EnergyStatus> getEnergyStatistic(Long projectId) {
        return energyEquipmentThirdDao.getEnergyStatistic(projectId);
    }

    @Override
    public List<Map<String, Object>> getEquipmentTotal(Long projectId) {
        return energyEquipmentThirdDao.getEquipmentTotal(projectId);
    }

    /**
     * 设备详情
     *
     * @param id
     * @return
     */
    @Override
    public EnergyEquipmentThird selectByPrimaryKey(Long id) {
        return energyEquipmentThirdDao.selectByPrimaryKey(id);
    }
}
