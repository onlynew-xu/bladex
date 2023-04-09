package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerCompensateDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.CompensateDto;
import com.steelman.iot.platform.entity.dto.DeviceDataSmartElecFloatDto;
import com.steelman.iot.platform.entity.dto.PowerComponentsDto;
import com.steelman.iot.platform.entity.dto.PowerEntityDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/15 0015 14:39
 * @Description:
 */
@Service("powerCompensateService")
public class PowerCompensateServiceImpl extends BaseService implements PowerCompensateService {
    @Resource
    private PowerCompensateDao compensateDao;
    @Resource
    private PowerComponentsService powerComponentsService;
    @Resource
    private PowerCompensateComponentsService powerCompensateComponentsService;
    @Resource
    private PowerCompensateDeviceService powerCompensateDeviceService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private PowerService powerService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private PowerTypePictureService powerTypePictureService;
    @Resource
    private DeviceDataSmartSuperService deviceDataSmartSuperService;


    @Override
    public void insert(PowerCompensate compensate) {
        compensateDao.insert(compensate);
    }

    @Override
    public void update(PowerCompensate compensate) {
        compensateDao.updateByPrimaryKeySelective(compensate);
    }

    @Override
    public PowerCompensate getCompensateInfo(Long compensateId) {
        return compensateDao.selectByPrimaryKey(compensateId);
    }

    @Override
    public  List<PowerCompensate> selectByTransformerId( Long transformerId) {
        List<PowerCompensate> list = compensateDao.selectByTransformerId(transformerId);
        return list;
    }

    @Override
    public Long selectCountByPowerId(Long powerId) {
        return compensateDao.selectCountByPowerId(powerId);
    }

    @Override
    public List<DeviceCenterVo> getPowerCompensate(Long powerId) {
        return compensateDao.selectPowerCompensateCenter(powerId);
    }

    @Override
    public Long selectCountByTransformId(Long transformId, Long projectId) {
        return compensateDao.selectCountByTransformId(transformId,projectId);
    }

    @Override
    public PowerCompensate selectByTransformerAndName(String compensateName, Long transformerId) {
        return compensateDao.selectByTransformerAndName(compensateName,transformerId);
    }

    @Override
    public int remove(Long compensateId) {
        return compensateDao.deleteByPrimaryKey(compensateId);
    }

    @Override
    public List<PowerComponentsDto> getComponents(Long compensateId, Integer type) {
        PowerCompensate powerCompensate = compensateDao.selectByPrimaryKey(compensateId);
        List<PowerComponentsDto> finalComponents=null;
        if(type.equals(0)){
            List<PowerComponentsDto> powerComponentsDtoList=powerComponentsService.selectComponentsByPowerType(2,compensateId,null,powerCompensate.getProjectId());
            List<PowerCompensateComponents> componentsList = powerCompensateComponentsService.getComponentsList(compensateId);
            if(!CollectionUtils.isEmpty(powerComponentsDtoList)){
                finalComponents=powerComponentsDtoList;
            }else{
                finalComponents=new ArrayList<>();
            }
            if(!CollectionUtils.isEmpty(componentsList)){
                for (PowerCompensateComponents powerCompensateComponents : componentsList) {
                    finalComponents.add(new PowerComponentsDto(powerCompensateComponents));
                }
            }
        }else if(type.equals(1)){
            List<PowerComponentsDto> powerComponentsDtoList=powerComponentsService.selectComponentsByPowerType(2,compensateId,1,powerCompensate.getProjectId());
            if(!CollectionUtils.isEmpty(powerComponentsDtoList)){
                finalComponents=powerComponentsDtoList;
            }
        }else if(type.equals(2)){
            List<PowerComponentsDto> powerComponentsDtoList=powerComponentsService.selectComponentsByPowerType(2,compensateId,2,powerCompensate.getProjectId());
            if(!CollectionUtils.isEmpty(powerComponentsDtoList)){
                finalComponents=powerComponentsDtoList;
            }
        }else if(type.equals(3)){
            List<PowerCompensateComponents> componentsList = powerCompensateComponentsService.getComponentsList(compensateId);
            if(!CollectionUtils.isEmpty(componentsList)){
                finalComponents=new ArrayList<>();
                for (PowerCompensateComponents powerCompensateComponents : componentsList) {
                    finalComponents.add(new PowerComponentsDto(powerCompensateComponents));
                }
            }
        }
        if(!CollectionUtils.isEmpty(finalComponents)){
            return finalComponents;
        }
        return null;
    }

    @Override
    public List<PowerCompensate> findByPowerId(Long powerId) {
        return compensateDao.findByPowerId(powerId);
    }

    @Override
    public CompensateDto getCompensateInfoDetail(Long compensateId) {
        CompensateDto compensateDto=new CompensateDto();
        PowerCompensate powerCompensate = compensateDao.selectByPrimaryKey(compensateId);
        compensateDto.setId(powerCompensate.getId());
        compensateDto.setName(powerCompensate.getName());
        PowerTypePicture powerTypePicture = powerTypePictureService.getByType(3);
        compensateDto.setPictureUrl(powerTypePicture.getUrl());
        List<PowerDeviceInfo> deviceList = powerCompensateDeviceService.getDeviceList(compensateId);
        PowerDeviceInfo powerDeviceInfo=null;
        compensateDto.setStatus(2);
        if(!CollectionUtils.isEmpty(deviceList)){
            for (PowerDeviceInfo powerDeviceInfoVar : deviceList) {
                Integer status = powerDeviceInfoVar.getStatus();
                if(status.equals(3)||status.equals(1)){
                    powerDeviceInfo=powerDeviceInfoVar;
                }
            }
        }

        if(powerDeviceInfo!=null){
            compensateDto.setStatus(powerDeviceInfo.getStatus());
            List<PowerAlarmWarnVo> message = powerService.getMessage(powerCompensate.getPowerId(), powerDeviceInfo.getDeviceId(), 2, powerCompensate.getId(), null, null);
            compensateDto.setPowerAlarmWarnVoList(message);
            Device device = deviceService.findById(powerDeviceInfo.getDeviceId());
            compensateDto.setDeviceId(device.getId());
            compensateDto.setDeviceTypeId(device.getDeviceTypeId());
            String location = powerService.getLocation(device.getId());
            compensateDto.setLocation(location);
            //处理设备数据
            Boolean superFlag=false;
            if(powerDeviceInfo.getDeviceTypeId().equals(13l)){
                superFlag=true;
            }
            compensateDto.setDataFlag(0);
            if(superFlag){
                DeviceDataSmartSuper lastData= deviceDataSmartSuperService.getLastData(powerDeviceInfo.getDeviceId());
                if(lastData!=null){
                    compensateDto.setDataFlag(1);
                    compensateDto.setSmartData(powerService.getSuperFloatData(lastData));
                }
            }else{
                DeviceDataSmartElec lastData= deviceDataSmartElecService.getLastData(powerDeviceInfo.getDeviceId());
                if(lastData!=null){
                    compensateDto.setDataFlag(1);
                    compensateDto.setSmartData(powerService.getFloatData(lastData));
                }
            }
            List<DeviceDataSmartElec> lastedTenData=null;
            if(superFlag){
                lastedTenData=deviceDataSmartSuperService.getLastedTenDataElec(powerDeviceInfo.getDeviceId());
            }else{
                lastedTenData= deviceDataSmartElecService.getLastedTenData(powerDeviceInfo.getDeviceId());
            }
            List<Integer> xdata = new ArrayList<>();
            List<Float> dianyaA = new ArrayList<>();
            List<Float> dianyaB = new ArrayList<>();
            List<Float> dianyaC = new ArrayList<>();
            List<Float> dianliuA = new ArrayList<>();
            List<Float> dianliuB = new ArrayList<>();
            List<Float> dianliuC = new ArrayList<>();
            List<Float> activePowerA = new ArrayList<>();
            List<Float> activePowerB = new ArrayList<>();
            List<Float> activePowerC = new ArrayList<>();
            List<Float> reactivePowerA = new ArrayList<>();
            List<Float> reactivePowerB = new ArrayList<>();
            List<Float> reactivePowerC = new ArrayList<>();
            long current = System.currentTimeMillis();
            BigDecimal bigDecimalBai = new BigDecimal("100");
            BigDecimal bigDecimalThou = new BigDecimal("1000");
            BigDecimal bigDecimalShi = new BigDecimal("10");
            if(!CollectionUtils.isEmpty(lastedTenData)){
                for (DeviceDataSmartElec deviceDataSmartElec : lastedTenData) {
                    Date createTime = deviceDataSmartElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = deviceDataSmartElec.getVoltRmsA();
                    if (org.springframework.util.StringUtils.isEmpty(voltRmsA)) {
                        dianyaA.add(220f);
                    } else {
                        BigDecimal voltABig = new BigDecimal(voltRmsA);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= voltABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide = voltABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }
                        dianyaA.add(divide.floatValue());
                    }

                    String voltRmsB = deviceDataSmartElec.getVoltRmsB();
                    if (org.springframework.util.StringUtils.isEmpty(voltRmsB)) {
                        dianyaB.add(240f);
                    } else {

                        BigDecimal voltBBig = new BigDecimal(voltRmsB);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= voltBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide = voltBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }
                        dianyaB.add(divide.floatValue());
                    }

                    String voltRmsC = deviceDataSmartElec.getVoltRmsC();
                    if (org.springframework.util.StringUtils.isEmpty(voltRmsC)) {
                        dianyaC.add(260f);
                    } else {
                        BigDecimal voltCBig = new BigDecimal(voltRmsC);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= voltCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide = voltCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }
                        dianyaC.add(divide.floatValue());
                    }

                    String ampRmsA = deviceDataSmartElec.getAmpRmsA();
                    if (org.springframework.util.StringUtils.isEmpty(ampRmsA)) {
                        dianliuA.add(100f);
                    } else {
                        BigDecimal ampRmsABig = new BigDecimal(ampRmsA);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= ampRmsABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide= ampRmsABig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);
                        }
                        dianliuA.add(divide.floatValue());
                    }

                    String ampRmsB = deviceDataSmartElec.getAmpRmsB();
                    if (org.springframework.util.StringUtils.isEmpty(ampRmsB)) {
                        dianliuB.add(120f);
                    } else {
                        BigDecimal ampRmsBBig = new BigDecimal(ampRmsB);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= ampRmsBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide= ampRmsBBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);
                        }
                        dianliuB.add(divide.floatValue());
                    }

                    String ampRmsC = deviceDataSmartElec.getAmpRmsB();
                    if (org.springframework.util.StringUtils.isEmpty(ampRmsC)) {
                        dianliuC.add(140f);
                    } else {
                        BigDecimal ampRmsCBig = new BigDecimal(ampRmsB);
                        BigDecimal divide=null;
                        if(superFlag){
                            divide= ampRmsCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                        }else{
                            divide= ampRmsCBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);
                        }
                        dianliuC.add(divide.floatValue());
                    }

                    //有功功率
                    String activePowerA1 = deviceDataSmartElec.getActivePowerA();
                    if (org.springframework.util.StringUtils.isEmpty(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(1000f);
                    } else {
                        if(superFlag){
                            activePowerA.add(Float.parseFloat(activePowerA1));
                        }else{
                            BigDecimal activePowerA1Big = new BigDecimal(activePowerA1);
                            BigDecimal divide = activePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            activePowerA.add(divide.floatValue());
                        }
                    }

                    String activePowerB1 = deviceDataSmartElec.getActivePowerA();
                    if (org.springframework.util.StringUtils.isEmpty(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(2000f);
                    } else {
                        if(superFlag){
                            activePowerB.add(Float.parseFloat(activePowerB1));
                        }else{
                            BigDecimal activePowerB1Big = new BigDecimal(activePowerB1);
                            BigDecimal divide = activePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            activePowerB.add(divide.floatValue());
                        }

                    }

                    String activePowerC1 = deviceDataSmartElec.getActivePowerA();
                    if (org.springframework.util.StringUtils.isEmpty(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(3000f);
                    } else {
                        if(superFlag){
                            activePowerC.add(Float.parseFloat(activePowerC1));
                        }else{
                            BigDecimal activePowerC1Big = new BigDecimal(activePowerC1);
                            BigDecimal divide = activePowerC1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            activePowerC.add(divide.floatValue());
                        }
                    }
                    //无功功率
                    String reactivePowerA1 = deviceDataSmartElec.getReactivePowerA();
                    if (org.springframework.util.StringUtils.isEmpty(reactivePowerA1) || reactivePowerA1.equals("0")) {
                        reactivePowerA.add(100f);
                    } else {
                        if(superFlag){
                            reactivePowerA.add(Float.parseFloat(reactivePowerA1));
                        }else{
                            BigDecimal reactivePowerA1Big = new BigDecimal(reactivePowerA1);
                            BigDecimal divide = reactivePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            reactivePowerA.add(divide.floatValue());
                        }
                    }

                    String reactivePowerB1 = deviceDataSmartElec.getReactivePowerB();
                    if (org.springframework.util.StringUtils.isEmpty(reactivePowerB1) || reactivePowerB1.equals("0")) {
                        reactivePowerB.add(200f);
                    } else {
                        if(superFlag){
                            reactivePowerB.add(Float.parseFloat(reactivePowerB1));
                        }else{
                            BigDecimal reactivePowerB1Big = new BigDecimal(reactivePowerB1);
                            BigDecimal divide = reactivePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            reactivePowerB.add(divide.floatValue());
                        }
                    }

                    String reactivePowerC1 = deviceDataSmartElec.getReactivePowerC();
                    if (org.springframework.util.StringUtils.isEmpty(reactivePowerC1) || reactivePowerC1.equals("0")) {
                        reactivePowerC.add(300f);
                    } else {
                        if(superFlag){
                            reactivePowerC.add(Float.parseFloat(reactivePowerC1));
                        }else{
                            BigDecimal reactivePowerC1Big = new BigDecimal(reactivePowerB1);
                            BigDecimal divide = reactivePowerC1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            reactivePowerC.add(divide.floatValue());
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(dianyaA)) {
                Map<String, Object> dianYaMap = new LinkedHashMap<>();
                dianYaMap.put("xDanWei", "s前");
                dianYaMap.put("yDanWei", "V");
                dianYaMap.put("xData", xdata);
                Map<String, Object> dianYaDataMap = new LinkedHashMap<>();
                dianYaDataMap.put("dianYaA", dianyaA);
                dianYaDataMap.put("dianYaB", dianyaB);
                dianYaDataMap.put("dianYaC", dianyaC);
                dianYaMap.put("dianYa", dianYaDataMap);
                compensateDto.setVoltDataMap(dianYaMap);
            }
            if (!CollectionUtils.isEmpty(dianliuA)) {
                Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                dianLiuMap.put("xDanWei", "s前");
                dianLiuMap.put("yDanWei", "V");
                dianLiuMap.put("xData", xdata);
                Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                dianLiuDataMap.put("dianLiuA", dianliuA);
                dianLiuDataMap.put("dianLiuB", dianliuB);
                dianLiuDataMap.put("dianLiuC", dianliuC);
                dianLiuMap.put("dianLiu", dianLiuDataMap);
                compensateDto.setAmpDataMap(dianLiuMap);
            }
            //有功功率
            if (!CollectionUtils.isEmpty(activePowerA)) {
                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                activePowerMap.put("xDanWei", "s前");
                activePowerMap.put("yDanWei", "w");
                activePowerMap.put("xData", xdata);
                Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
                activePowerDataMap.put("activePowerA", activePowerA);
                activePowerDataMap.put("activePowerB", activePowerB);
                activePowerDataMap.put("activePowerC", activePowerC);
                activePowerMap.put("activePower", activePowerDataMap);
                compensateDto.setActivePowerMap(activePowerMap);
            }
            //无功功率
            if (!CollectionUtils.isEmpty(reactivePowerA)) {
                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                reactivePowerMap.put("xDanWei", "s前");
                reactivePowerMap.put("yDanWei", "var");
                reactivePowerMap.put("xData", xdata);
                Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
                reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
                reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
                reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
                reactivePowerMap.put("reactivePower", reactivePowerDataMap);
                compensateDto.setReactivePowerMap(reactivePowerMap);
            }


        }else{
            compensateDto.setDataFlag(0);
        }
        if(compensateId.equals(1l)){
            compensateDto.setDataFlag(1);
            List<Integer> xdata = new ArrayList<>();
            for (int i = 5; i <605 ; i=i+60) {
                xdata.add(i);
            }
            List<Float> dianyaA = new ArrayList<>();
            List<Float> dianyaB = new ArrayList<>();
            List<Float> dianyaC = new ArrayList<>();
            List<Float> dianliuA = new ArrayList<>();
            List<Float> dianliuB = new ArrayList<>();
            List<Float> dianliuC = new ArrayList<>();
            List<Float> activePowerA = new ArrayList<>();
            List<Float> activePowerB = new ArrayList<>();
            List<Float> activePowerC = new ArrayList<>();
            List<Float> reactivePowerA = new ArrayList<>();
            List<Float> reactivePowerB = new ArrayList<>();
            List<Float> reactivePowerC = new ArrayList<>();
            for (int i = 0; i <10 ; i++) {
                dianyaA.add(220f);
                dianyaB.add(230f);
                dianyaC.add(240f);

                dianliuA.add(50f);
                dianliuB.add(70f);
                dianliuC.add(90f);

                activePowerA.add(1000f);
                activePowerB.add(1200f);
                activePowerC.add(1400f);

                reactivePowerA.add(20f);
                reactivePowerB.add(30f);
                reactivePowerC.add(40f);
            }
            Map<String, Object> dianYaMap = new LinkedHashMap<>();
            dianYaMap.put("xDanWei", "s前");
            dianYaMap.put("yDanWei", "V");
            dianYaMap.put("xData", xdata);
            Map<String, Object> dianYaDataMap = new LinkedHashMap<>();
            dianYaDataMap.put("dianYaA", dianyaA);
            dianYaDataMap.put("dianYaB", dianyaB);
            dianYaDataMap.put("dianYaC", dianyaC);
            dianYaMap.put("dianYa", dianYaDataMap);
            compensateDto.setVoltDataMap(dianYaMap);

            Map<String, Object> dianLiuMap = new LinkedHashMap<>();
            dianLiuMap.put("xDanWei", "s前");
            dianLiuMap.put("yDanWei", "V");
            dianLiuMap.put("xData", xdata);
            Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
            dianLiuDataMap.put("dianLiuA", dianliuA);
            dianLiuDataMap.put("dianLiuB", dianliuB);
            dianLiuDataMap.put("dianLiuC", dianliuC);
            dianLiuMap.put("dianLiu", dianLiuDataMap);
            compensateDto.setAmpDataMap(dianLiuMap);


            Map<String, Object> activePowerMap = new LinkedHashMap<>();
            activePowerMap.put("xDanWei", "s前");
            activePowerMap.put("yDanWei", "w");
            activePowerMap.put("xData", xdata);
            Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
            activePowerDataMap.put("activePowerA", activePowerA);
            activePowerDataMap.put("activePowerB", activePowerB);
            activePowerDataMap.put("activePowerC", activePowerC);
            activePowerMap.put("activePower", activePowerDataMap);
            compensateDto.setActivePowerMap(activePowerMap);

            Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
            reactivePowerMap.put("xDanWei", "s前");
            reactivePowerMap.put("yDanWei", "var");
            reactivePowerMap.put("xData", xdata);
            Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
            reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
            reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
            reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
            reactivePowerMap.put("reactivePower", reactivePowerDataMap);
            compensateDto.setReactivePowerMap(reactivePowerMap);

            DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto=new DeviceDataSmartElecFloatDto();
            deviceDataSmartElecFloatDto.setVoltRmsA(220f);
            deviceDataSmartElecFloatDto.setVoltRmsB(240f);
            deviceDataSmartElecFloatDto.setVoltRmsC(260f);

            deviceDataSmartElecFloatDto.setAmpRmsA(100f);
            deviceDataSmartElecFloatDto.setAmpRmsB(130f);
            deviceDataSmartElecFloatDto.setAmpRmsC(160f);

            deviceDataSmartElecFloatDto.setPowerFactorA(500);
            deviceDataSmartElecFloatDto.setPowerFactorB(700);
            deviceDataSmartElecFloatDto.setPowerFactorC(600);

            deviceDataSmartElecFloatDto.setActivePowerA(1000F);
            deviceDataSmartElecFloatDto.setActivePowerB(1200F);
            deviceDataSmartElecFloatDto.setActivePowerC(1400F);

            deviceDataSmartElecFloatDto.setThdiA(20f);
            deviceDataSmartElecFloatDto.setThdiB(20f);
            deviceDataSmartElecFloatDto.setThdiC(20f);

            deviceDataSmartElecFloatDto.setThdvA(15f);
            deviceDataSmartElecFloatDto.setThdvB(15f);
            deviceDataSmartElecFloatDto.setThdvC(15f);
            deviceDataSmartElecFloatDto.setReactivePowerA(10f);
            deviceDataSmartElecFloatDto.setReactivePowerB(10f);
            deviceDataSmartElecFloatDto.setReactivePowerC(10f);
            compensateDto.setSmartData(deviceDataSmartElecFloatDto);
            compensateDto.setPowerFactory(400);

        }


        return compensateDto;
    }

    @Override
    public List<PowerEntityDto> getCompensateComponentsInfo(Long compensateId) {
        List<PowerCompensateComponents> componentsList = powerCompensateComponentsService.getComponentsList(compensateId);
        if(!CollectionUtils.isEmpty(componentsList)){
            List<PowerEntityDto> powerEntityDtoList=new ArrayList<>();
            for (PowerCompensateComponents powerCompensateComponents : componentsList) {
                PowerEntityDto powerEntityDto=new PowerEntityDto();
                powerEntityDto.setId(powerCompensateComponents.getId());
                powerEntityDto.setName(powerCompensateComponents.getName());
                powerEntityDto.setPowerId(powerCompensateComponents.getPowerId());
                powerEntityDto.setStatus(powerCompensateComponents.getStatus());
                powerEntityDto.setProjectId(powerCompensateComponents.getProjectId());
                powerEntityDtoList.add(powerEntityDto);
            }
            return powerEntityDtoList;
        }
        return null;
    }
}
