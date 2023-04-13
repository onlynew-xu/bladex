package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentOneDao;
import com.steelman.iot.platform.entity.EnergyEquipmentOne;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.service.EnergyEquipmentOneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Service("energyEquipmentOneService")

public class EnergyEquipmentOneServiceImpl implements EnergyEquipmentOneService {

    @Resource
    private EnergyEquipmentOneDao energyEquipmentOneDao;


    @Override
    public List<EnergyStatus> getEnergyStatistic(Long projectId) {
        return energyEquipmentOneDao.getEnergyStatistic(projectId);
    }



    @Override
    public List<Map<String, Object>> consumeStatistic(Long projectId) {
        return energyEquipmentOneDao.consumeStatistic(projectId);
    }

    @Override
    public List<Map<String, Object>> getEquipmentTotal(Long projectId) {
        return energyEquipmentOneDao.getEquipmentTotal(projectId);
    }

    /**
     * 根据id查询整条数据
     * @param id
     * @return
     */
    @Override
    public EnergyEquipmentOne selectByPrimaryKey(Long id) {
        return energyEquipmentOneDao.selectByPrimaryKey(id);
    }
}
