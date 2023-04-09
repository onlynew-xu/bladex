package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerMatriculationDao;
import com.steelman.iot.platform.entity.PowerMatriculation;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.PowerMatriculationDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.service.PowerMatriculationService;
import com.steelman.iot.platform.service.PowerTransformerService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/16 0016 15:36
 * @Description:
 */
@Service("powerMatriculationService")
public class PowerMatriculationServiceImpl extends BaseService implements PowerMatriculationService {
    @Resource
    private PowerMatriculationDao matriculationDao;
    @Resource
    private PowerTransformerService powerTransformerService;

    @Override
    public void insert(PowerMatriculation matriculation) {
        matriculationDao.insert(matriculation);
    }

    @Override
    public void update(PowerMatriculation matriculation) {
        matriculationDao.updateByPrimaryKeySelective(matriculation);
    }

    @Override
    public  List<PowerMatriculation> getMatriculationList( Long transformerId) {
        List<PowerMatriculation> list = matriculationDao.selectByTransformerId(transformerId);
        return list;
    }

    @Override
    public Long selectCountByPowerId(Long powerId) {
        return matriculationDao.selectCountByPowerId(powerId);
    }

    @Override
    public List<PowerTransformer> selectMatriculation(Long matriculationId) {
        return matriculationDao.selectMatriculationTransformer(matriculationId);
    }

    @Override
    public List<DeviceCenterVo> getPowerMatriculation(Long powerId) {

        return matriculationDao.selectPowerMatriculationCenter(powerId);
    }

    @Override
    public Long selectCount(Long transformId, Long projectId) {
        return matriculationDao.selectCountByTransformId(transformId,projectId);
    }

    @Override
    public PowerMatriculation selectByPowerAndName(Long powerId, String name) {
        return matriculationDao.selectByPowerAndName(powerId,name);
    }

    @Override
    public List<PowerMatriculationDto> getAllMatriculation(Long transformerId) {
        List<PowerMatriculationDto> powerMatriculationDtoList=new ArrayList<>();
        List<PowerMatriculation> list = matriculationDao.selectAllByTransformerId(transformerId);
        if(!CollectionUtils.isEmpty(list)){
            PowerTransformer transformerInfo = powerTransformerService.getTransformerInfo(transformerId);
            List<PowerTransformer> powerTransformerList = powerTransformerService.selectByPowerId(transformerInfo.getPowerId());
            Map<Long,String>  transformerMap=new HashMap<>();
            if(!CollectionUtils.isEmpty(powerTransformerList)){
                for (PowerTransformer powerTransformer : powerTransformerList) {
                    transformerMap.put(powerTransformer.getId(),powerTransformer.getName());
                }
            }
            for (PowerMatriculation powerMatriculation : list) {
                PowerMatriculationDto powerMatriculationDto=new PowerMatriculationDto(powerMatriculation);
                Long firstTransformerId = powerMatriculation.getFirstTransformerId();
                if(transformerId.equals(firstTransformerId)){
                    powerMatriculationDto.setFirstLocal(1);
                }else{
                    powerMatriculationDto.setSecondLocal(1);
                }
                powerMatriculationDto.setFirstTransformerName(transformerMap.get(powerMatriculation.getFirstTransformerId()));
                powerMatriculationDto.setSecondTransformerName(transformerMap.get(powerMatriculation.getSecondTransformerId()));
                powerMatriculationDtoList.add(powerMatriculationDto);
            }
        }
        return powerMatriculationDtoList;
    }

    @Override
    public PowerMatriculation findById(Long matriculationId) {
        return matriculationDao.selectByPrimaryKey(matriculationId);
    }

    @Override
    public List<PowerMatriculation> findByTransformerId(Long transformerId, Long secondTransformerId) {
        return matriculationDao.findByTransformerId(transformerId,secondTransformerId);
    }

    @Override
    public int removeById(Long matriculationId) {
        return matriculationDao.deleteByPrimaryKey(matriculationId);
    }

    @Override
    public List<PowerMatriculation> findByPowerId(Long powerId) {
        return matriculationDao.findByPowerId(powerId);
    }

    @Override
    public PowerMatriculationDto getPowerMatriculationInfo(Long matriculationId) {
        PowerMatriculation powerMatriculation = matriculationDao.selectByPrimaryKey(matriculationId);
        if(powerMatriculation!=null){
            PowerMatriculationDto powerMatriculationDto=new PowerMatriculationDto();
            powerMatriculationDto.setId(powerMatriculation.getId());
            powerMatriculationDto.setName(powerMatriculation.getName());
            powerMatriculationDto.setBrand(powerMatriculationDto.getBrand());
            powerMatriculationDto.setCreateTime(powerMatriculation.getCreateTime());
            powerMatriculationDto.setPowerId(powerMatriculation.getPowerId());
            powerMatriculationDto.setProjectId(powerMatriculation.getProjectId());
            powerMatriculationDto.setStatus(powerMatriculation.getStatus());
            powerMatriculationDto.setUpdateTime(powerMatriculation.getUpdateTime());

            Long firstTransformerId = powerMatriculation.getFirstTransformerId();
            if(firstTransformerId!=null){
                PowerTransformer transformerInfoA = powerTransformerService.getTransformerInfo(firstTransformerId);
                powerMatriculationDto.setFirstTransformerId(transformerInfoA.getId());
                powerMatriculationDto.setFirstTransformerName(transformerInfoA.getName());
                powerMatriculationDto.setFirstTransformerCapaticy(transformerInfoA.getCapacity());
                powerMatriculationDto.setFirstTransformerBrand(transformerInfoA.getBrand());
            }
            Long secondTransformerId = powerMatriculation.getSecondTransformerId();
            if(secondTransformerId!=null){
                PowerTransformer transformerInfoB = powerTransformerService.getTransformerInfo(secondTransformerId);
                powerMatriculationDto.setSecondTransformerId(transformerInfoB.getId());
                powerMatriculationDto.setSecondTransformerName(transformerInfoB.getName());
                powerMatriculationDto.setSecondTransformerCapaticy(transformerInfoB.getCapacity());
                powerMatriculationDto.setSecondTransformerBrand(transformerInfoB.getBrand());
            }
            return powerMatriculationDto;
        }
        return null;
    }
}

