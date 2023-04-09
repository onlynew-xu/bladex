package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerGeneratorDao;
import com.steelman.iot.platform.entity.PowerGenerator;
import com.steelman.iot.platform.entity.dto.GeneratorDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.service.PowerGeneratorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/11 0011 14:00
 * @Description:
 */
@Service("powerGeneratorService")
public class PowerGeneratorServiceImpl extends BaseService implements PowerGeneratorService {
    @Resource
    private PowerGeneratorDao generatorDao;

    @Override
    public void insert(PowerGenerator generator) {
        generatorDao.insert(generator);
    }

    @Override
    public void update(PowerGenerator generator) {
        generatorDao.updateByPrimaryKeySelective(generator);
    }

    @Override
    public List<PowerGenerator> getGeneratorList(Long transformerId) {
        List<PowerGenerator> list = generatorDao.selectByTransformerId( transformerId);
        return list;
    }

    @Override
    public PowerGenerator selectById(Long id) {
        return generatorDao.selectByPrimaryKey(id);
    }

    @Override
    public List<DeviceCenterVo> getPowerGenerator(Long powerId) {
        return generatorDao.selectPowerGeneratorCenter(powerId);
    }

    @Override
    public Map<String, Object> getGeneratorInfo(Long id) {
        return generatorDao.selectCenterGeneratorInfo(id);
    }

    @Override
    public Long selectCountByTransformId(Long transformId, Long projectId) {
        return generatorDao.selectCountByTransformId(transformId,projectId);
    }

    @Override
    public PowerGenerator getByTransformerANDName(Long transformerId, String name) {
        return generatorDao.getByTransformerANDName(transformerId,name);
    }

    @Override
    public int removeById(Long generatorId) {
        return generatorDao.deleteByPrimaryKey(generatorId);
    }

    @Override
    public Long selectCountByPowerId(Long id) {
        return generatorDao.selectCountByPowerId(id);
    }

    @Override
    public List<PowerGenerator> findByPowerId(Long id) {
        return generatorDao.findByPowerId(id);
    }

    @Override
    public GeneratorDto getGeneratorDto(Long generatorId) {
        GeneratorDto generatorDto = generatorDao.getGeneratorDto(generatorId);

        return generatorDto ;
    }
}
