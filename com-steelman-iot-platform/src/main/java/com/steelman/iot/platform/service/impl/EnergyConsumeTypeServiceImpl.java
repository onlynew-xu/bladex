package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyConsumeTypeDao;
import com.steelman.iot.platform.entity.EnergyConsumeType;
import com.steelman.iot.platform.service.EnergyConsumeTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lsj
 * @DATE 2021/3/30 0030 14:17
 * @Description:
 */
@Service("energyConsumeTypeService")
public class EnergyConsumeTypeServiceImpl extends BaseService implements EnergyConsumeTypeService {
    @Resource
    private EnergyConsumeTypeDao energyConsumeTypeDao;

    @Override
    public void save(EnergyConsumeType energyConsumeType) {
        energyConsumeTypeDao.insert(energyConsumeType);
    }

    @Override
    public void update(EnergyConsumeType energyConsumeType) {
        energyConsumeTypeDao.updateByPrimaryKeySelective(energyConsumeType);
    }

    @Override
    public List<EnergyConsumeType> getList(Long projectId) {
        return energyConsumeTypeDao.selectByProjectId(projectId);
    }

    @Override
    public void delete(Long id) {
        energyConsumeTypeDao.deleteByPrimaryKey(id);
    }

    @Override
    public EnergyConsumeType findById(Long consumeTypeId) {
        return energyConsumeTypeDao.selectByPrimaryKey(consumeTypeId);
    }

    @Override
    public EnergyConsumeType findByName(Long projectId, String energyConsumeName) {
        return energyConsumeTypeDao.findByName(projectId,energyConsumeName);
    }
}
