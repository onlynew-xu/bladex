package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentSecondDao;
import com.steelman.iot.platform.entity.EnergyEquipmentSecond;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.service.EnergyEquipmentSecondService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("energyEquipmentSecondService")
public class EnergyEquipmentSecondServiceImpl extends BaseService implements EnergyEquipmentSecondService {


    @Resource
    private EnergyEquipmentSecondDao energyEquipmentSecondDao;







    @Override
    public List<EnergyStatus> getEnergyStatistic(Long projectId) {
        return energyEquipmentSecondDao.getEnergyStatistic(projectId);
    }

    @Override
    public List<Map<String, Object>> getEquipmentTotal(Long projectId) {
        return energyEquipmentSecondDao.getEquipmentTotal(projectId);
    }

    /**
     * 获取二级设备详情
     *
     * @param id
     * @return
     */
    @Override
    public EnergyEquipmentSecond selectByPrimaryKey(Long id) {
        return energyEquipmentSecondDao.selectByPrimaryKey(id);
    }


}
