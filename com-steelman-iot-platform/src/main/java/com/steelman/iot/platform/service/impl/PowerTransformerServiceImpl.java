package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerTransformerDao;
import com.steelman.iot.platform.entity.DeviceDataSmartElec;
import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.TransformerInfoDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 14:05
 * @Description:
 */
@Service("powerTransformerService")
public class PowerTransformerServiceImpl extends BaseService implements PowerTransformerService {

    @Resource
    private PowerTransformerDao transformerDao;
    @Resource
    private PowerIncomingService powerIncomingService;
    @Resource
    private PowerCompensateService powerCompensateService;
    @Resource
    private PowerWaveService powerWaveService;
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerGeneratorService powerGeneratorService;
    @Resource
    private PowerMatriculationService powerMatriculationService;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceParamsSmartElecService deviceParamsSmartElecService;

    @Override
    public void insert(PowerTransformer transformer) {
        transformerDao.insert(transformer);
    }

    @Override
    public void update(PowerTransformer transformer) {
        transformerDao.updateByPrimaryKeySelective(transformer);
    }

    @Override
    public List<PowerTransformer> selectByPowerId( Long powerId) {
        List<PowerTransformer> list = transformerDao.selectByPowerId(powerId);
        return list;
    }

    @Override
    public void delete(Long id) {
        transformerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByPowerId(Long id) {
        transformerDao.deleteByPowerId(id);
    }

    @Override
    public PowerTransformer getTransformerInfo(Long transformerId) {
        return transformerDao.selectByPrimaryKey(transformerId);
    }

    @Override
    public List<DeviceCenterVo> getPowerTransformer(Long powerId) {
        return transformerDao.selectPowerTransformerCenter(powerId);
    }

    @Override
    public PowerTransformer selectByTransformNameAndPowerId(String name, Long powerId) {
        return transformerDao.selectByTransformNameAndPowerId(name,powerId);
    }

    @Override
    public Map<Integer,String> selectCount(Long transformId, Long projectId) {
        Map<Integer,String> dataMap=new LinkedHashMap<>();
        Long incomingCount=powerIncomingService.selectCountByTransformId(transformId,projectId);
        if(incomingCount>0){
            dataMap.put(1,"进线柜");
        }
        Long compensateCount=powerCompensateService.selectCountByTransformId(transformId,projectId);
        if(compensateCount>0){
            dataMap.put(2,"补偿柜");
        }
        Long powerWaveCount=powerWaveService.selectCountByTransformId(transformId,projectId);
        if(powerWaveCount>0){
            dataMap.put(3,"滤波柜");
        }
        Long simpleCount= powerFeederService.selectSimpleCountByTransformId(transformId,projectId);
        if(simpleCount>0){
            dataMap.put(4,"单回路馈线柜");
        }
        Long multiCount=powerFeederService.selectMultiCountByTransformId(transformId,projectId);
        if(multiCount>0){
            dataMap.put(5,"多回路馈线柜");
        }
        Long matriculationCount= powerMatriculationService.selectCount(transformId,projectId);
        if(matriculationCount>0){
            dataMap.put(6,"母联柜");
        }
        Long generatorCount=powerGeneratorService.selectCountByTransformId(transformId,projectId);
        if(generatorCount>0){
            dataMap.put(7,"发电机");
        }
        return dataMap;
    }

    @Override
    public Integer updateRelationStatus(Long transformerId, Integer status) {
        Date updateTime=new Date();
        return transformerDao.updateRelationStatus(transformerId,status,updateTime);
    }

    @Override
    public List<PowerTransformer> getUnBandingTransformer(Long powerId) {
        return transformerDao.getUnBandingTransformer(powerId);
    }

    @Override
    public TransformerInfoDto getTransformerInfoDto(Long transformerId) {
        PowerTransformer powerTransformer = transformerDao.selectByPrimaryKey(transformerId);
        TransformerInfoDto transformerInfoDto=new TransformerInfoDto();
        transformerInfoDto.setId(powerTransformer.getId());
        transformerInfoDto.setBrand(powerTransformer.getBrand());
        transformerInfoDto.setCapacity(powerTransformer.getCapacity());
        transformerInfoDto.setName(powerTransformer.getName());
        transformerInfoDto.setLoad(0f);
        if(powerTransformer.getStatus().equals(0)){
            transformerInfoDto.setLoad(0f);
        }else{
            Integer load=0;
            List<PowerIncoming> powerIncomingList=powerIncomingService.getByTransformerId(transformerId);
            if(!CollectionUtils.isEmpty(powerIncomingList)){
                BigDecimal bigDecimalTho =new BigDecimal("1000");
                for (PowerIncoming powerIncoming : powerIncomingList) {
                    List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
                    for (PowerDeviceInfo powerDeviceInfo : deviceList) {
                        Integer status = powerDeviceInfo.getStatus();
                        if(status.equals(3)||status.equals(1)){
                            DeviceDataSmartElec deviceDataSmartElec = deviceDataSmartElecService.getLastData(powerDeviceInfo.getDeviceId());
                            if(deviceDataSmartElec==null){
                                continue;
                            }
                            String activePowerA = deviceDataSmartElec.getActivePowerA();
                            BigDecimal activePowerABigA=new BigDecimal(activePowerA);
                            BigDecimal divideA = activePowerABigA.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);

                            String activePowerB = deviceDataSmartElec.getActivePowerB();
                            BigDecimal activePowerABigB=new BigDecimal(activePowerB);
                            BigDecimal divideB = activePowerABigB.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);

                            String activePowerC = deviceDataSmartElec.getActivePowerC();
                            BigDecimal activePowerABigC=new BigDecimal(activePowerC);
                            BigDecimal divideC = activePowerABigC.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                            BigDecimal bigTotal=divideA.add(divideB).add(divideC);
                            transformerInfoDto.setLoad(bigTotal.floatValue());
                            break;
                        }
                    }
                }
            }
        }
        return transformerInfoDto;
    }

}
