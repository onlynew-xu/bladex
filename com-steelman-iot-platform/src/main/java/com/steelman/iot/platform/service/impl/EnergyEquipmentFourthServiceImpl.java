package com.steelman.iot.platform.service.impl;


import com.steelman.iot.platform.dao.EnergyEquipmentFourthDao;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.service.EnergyEquipmentFourthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("EnergyEquipmentFourthService")
public class EnergyEquipmentFourthServiceImpl extends BaseService implements EnergyEquipmentFourthService {

    @Resource
    private EnergyEquipmentFourthDao energyEquipmentFourthDao;
    @Override
    public List<EnergyStatus> getEnergyStatistic(Long projectId) {
        return energyEquipmentFourthDao.getEnergyStatistic(projectId);
    }

    @Override
    public List<Map<String, Object>> getEquipmentTotal(Long projectId) {
        return energyEquipmentFourthDao.getEquipmentTotal(projectId);
    }
}
