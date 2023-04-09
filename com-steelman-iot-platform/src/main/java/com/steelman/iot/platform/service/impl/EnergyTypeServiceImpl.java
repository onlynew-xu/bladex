package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyTypeDao;
import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyType;
import com.steelman.iot.platform.service.EnergyEquipmentService;
import com.steelman.iot.platform.service.EnergyTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/25 0025 10:10
 * @Description:
 */
@Service("energyTypeService")
public class EnergyTypeServiceImpl extends BaseService implements EnergyTypeService {
    @Resource
    private EnergyTypeDao energyTypeDao;
    @Resource
    private EnergyEquipmentService energyEquipmentService;

    @Override
    public void save(EnergyType energyType) {
        energyTypeDao.insert(energyType);
    }

    @Override
    public List<EnergyType> getListByProjectSystem(Long projectId, Long systemId) {
        return energyTypeDao.selectByProjectSystem(projectId,systemId);
    }

    @Override
    public EnergyType getInfo(Long id) {

        return energyTypeDao.selectByPrimaryKey(id);
    }
    @Override
    public List<EnergyType> selectAll(Long projectId) {
        return energyTypeDao.selectAll(projectId);
    }

    @Override
    public EnergyType selectByName(String energyTypeName, Long projectId) {
        return energyTypeDao.selectByName(energyTypeName,projectId);
    }

    @Override
    public int updateEnergyType(EnergyType energyType) {
        return energyTypeDao.updateByPrimaryKey(energyType);
    }

    @Override
    public Boolean deleteById(Long energyTypeId,Long projectId) {
        List<EnergyEquipment> energyEquipmentList=energyEquipmentService.selectByEnergyTypeId(energyTypeId,projectId);
        if (CollectionUtils.isEmpty(energyEquipmentList)){
            energyTypeDao.deleteByPrimaryKey(energyTypeId);
            return  true;
        }else{
            return false;
        }

    }

    @Override
    public EnergyType selectByPrimaryKey(Long energyTypeId) {
        return energyTypeDao.selectByPrimaryKey(energyTypeId);
    }
}
