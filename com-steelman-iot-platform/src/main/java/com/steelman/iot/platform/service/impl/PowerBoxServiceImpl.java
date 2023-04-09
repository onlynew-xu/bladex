package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerBoxDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.DeviceDataSmartElecFloatDto;
import com.steelman.iot.platform.entity.dto.PowerBoxInfoDto;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.entity.vo.WeekEnergyConsumeStatistic;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/18 0018 9:56
 * @Description:
 */
@Service("powerBoxService")
public class PowerBoxServiceImpl extends BaseService implements PowerBoxService {

    @Resource
    private PowerBoxDao powerBoxDao;
    @Resource
    private PowerTypePictureService powerTypePictureService;
    @Resource
    private PowerBoxLoopDeviceService powerBoxLoopDeviceService;
    @Resource
    private PowerBoxLoopService powerBoxLoopService;
    @Resource
    private PowerService powerService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private DeviceLastMeasurementService deviceLastMeasureService;
    @Resource
    private DeviceDataSmartSuperService deviceDataSmartSuperService;

    @Override
    public void insert(PowerBox box) {
        powerBoxDao.insert(box);
    }

    @Override
    public void update(PowerBox box) {
        powerBoxDao.updateByPrimaryKeySelective(box);
    }

    @Override
    public PowerBox getBoxInfo(Long boxId) {
        return powerBoxDao.selectByPrimaryKey(boxId);
    }

    @Override
    public List<PowerBox> getBoxListByLoopId(Long loopId, Integer parentType) {
        List<PowerBox> list = powerBoxDao.selectBoxByLoopId(loopId, parentType);
        return list;
    }

    @Override
    public List<PowerBox> getTransformPowerBox(Long transformerId) {
        List<PowerBox> list = powerBoxDao.selectBoxByTransformerId(transformerId);
        return list;
    }

    @Override
    public List<DeviceCenterVo> getPowerBox(Long powerId) {
        return powerBoxDao.selectPowerBoxCenter(powerId);
    }

    @Override
    public PowerBox getByBoxNameAndTransformerId(String name, Long powerId) {
        return powerBoxDao.getByBoxNameAndTransformerId(name, powerId);
    }

    @Override
    public int removePowerBox(Long boxId) {
        return powerBoxDao.deleteByPrimaryKey(boxId);
    }

    @Override
    public Long selectCountByPowerId(Long id) {
        return powerBoxDao.selectCountByPowerId(id);
    }

    @Override
    public List<PowerBox> findByPowerId(Long powerId) {
        return powerBoxDao.findByPowerId(powerId);
    }

    @Override
    public PowerBoxInfoDto getBoxInfoDto(Long boxId, Long loopId) {
        PowerBox powerBox = powerBoxDao.selectByPrimaryKey(boxId);
        PowerBoxInfoDto powerBoxInfoDto = new PowerBoxInfoDto();
        powerBoxInfoDto.setId(powerBox.getId());
        powerBoxInfoDto.setName(powerBox.getName());
        PowerTypePicture powerTypePicture = powerTypePictureService.getByType(7);
        if (powerTypePicture != null) {
            powerBoxInfoDto.setPictureUrl(powerTypePicture.getUrl());
        }
        PowerDeviceInfo deviceInfo = null;
        powerBoxInfoDto.setStatus(2);
        List<PowerDeviceInfo> deviceList = powerBoxLoopDeviceService.getDeviceList(loopId);
        if (!CollectionUtils.isEmpty(deviceList)) {
            deviceInfo = deviceList.get(0);
        }
        for (PowerDeviceInfo powerDeviceInfo : deviceList) {
            Integer status = powerDeviceInfo.getStatus();
            if (status.equals(1) || status.equals(3)) {
                deviceInfo = powerDeviceInfo;
                break;
            }
        }
        PowerBoxLoop powerBoxLoop = powerBoxLoopService.getBoxLoopInfo(loopId);
        powerBoxInfoDto.setLoopId(powerBoxLoop.getId());
        powerBoxInfoDto.setLoopName(powerBoxLoop.getName());
        if (deviceInfo != null) {
            powerBoxInfoDto.setStatus(deviceInfo.getStatus());
            List<PowerAlarmWarnVo> message = powerService.getMessage(powerBox.getPowerId(), deviceInfo.getDeviceId(), 6, powerBox.getId(), powerBoxLoop.getId(), powerBoxLoop.getName());
            powerBoxInfoDto.setPowerAlarmWarnVoList(message);
            Device device = deviceService.findById(deviceInfo.getDeviceId());
            powerBoxInfoDto.setDeviceId(device.getId());
            powerBoxInfoDto.setDeviceTypeId(device.getDeviceTypeId());
            String location = powerService.getLocation(device.getId());
            powerBoxInfoDto.setLocation(location);
            powerBoxInfoDto.setDataFlag(0);
            Boolean superFlag=false;
            if(deviceInfo.getDeviceTypeId().equals(13l)){
                superFlag=true;
            }
            if(superFlag){
                DeviceDataSmartSuper lastData= deviceDataSmartSuperService.getLastData(deviceInfo.getDeviceId());
                if(lastData!=null){
                    powerBoxInfoDto.setDataFlag(1);
                    powerBoxInfoDto.setSmartData(powerService.getSuperFloatData(lastData));
                }
            }else{
                DeviceDataSmartElec lastData= deviceDataSmartElecService.getLastData(deviceInfo.getDeviceId());
                if(lastData!=null){
                    powerBoxInfoDto.setDataFlag(1);
                    powerBoxInfoDto.setSmartData(powerService.getFloatData(lastData));
                }
            }
            List<DeviceDataSmartElec> lastedTenData=null;
            if(superFlag){
                lastedTenData=deviceDataSmartSuperService.getLastedTenDataElec(deviceInfo.getDeviceId());
            }else{
                lastedTenData= deviceDataSmartElecService.getLastedTenData(deviceInfo.getDeviceId());
            }
            //处理设备数据
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

            List<Integer> powerFactoryA = new ArrayList<>();
            List<Integer> powerFactoryB = new ArrayList<>();
            List<Integer> powerFactoryC = new ArrayList<>();

            long current = System.currentTimeMillis();
            BigDecimal bigDecimalBai = new BigDecimal("100");
            BigDecimal bigDecimalThou = new BigDecimal("1000");
            BigDecimal bigDecimalShi = new BigDecimal("10");
            if (!CollectionUtils.isEmpty(lastedTenData)) {
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
                            divide= voltABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
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
                            divide= voltBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
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
                            divide= voltCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
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
                        }else {
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
                        }else {
                            BigDecimal reactivePowerC1Big = new BigDecimal(reactivePowerB1);
                            BigDecimal divide = reactivePowerC1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            reactivePowerC.add(divide.floatValue());
                        }

                    }
                    //功率因数
                    String powerFactorA = deviceDataSmartElec.getPowerFactorA();
                    if (StringUtils.isEmpty(powerFactorA)) {
                        powerFactoryA.add(0);
                    } else {
                        powerFactoryA.add(Integer.parseInt(powerFactorA));
                    }
                    String powerFactorB = deviceDataSmartElec.getPowerFactorB();
                    if (StringUtils.isEmpty(powerFactorB)) {
                        powerFactoryB.add(0);
                    } else {
                        powerFactoryB.add(Integer.parseInt(powerFactorB));
                    }
                    String powerFactorC = deviceDataSmartElec.getPowerFactorC();
                    if (StringUtils.isEmpty(powerFactorC)) {
                        powerFactoryC.add(0);
                    } else {
                        powerFactoryC.add(Integer.parseInt(powerFactorC));
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
                powerBoxInfoDto.setVoltDataMap(dianYaMap);
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
                powerBoxInfoDto.setAmpDataMap(dianLiuMap);
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
                powerBoxInfoDto.setActivePowerMap(activePowerMap);
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
                powerBoxInfoDto.setReactivePowerMap(reactivePowerMap);
            }
            //功率因数
            if (!CollectionUtils.isEmpty(powerFactoryA)) {
                Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                powerFactoryMap.put("xDanWei", "s前");
                powerFactoryMap.put("yDanWei", "0.001");
                powerFactoryMap.put("xData", xdata);
                Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                powerFactoryDataMap.put("powerFactorA", powerFactoryA);
                powerFactoryDataMap.put("powerFactorB", powerFactoryB);
                powerFactoryDataMap.put("powerFactorC", powerFactoryC);
                powerFactoryMap.put("powerFactor", powerFactoryDataMap);
                powerBoxInfoDto.setPowerFactorMap(powerFactoryMap);
            }
            DeviceMeasurement deviceMeasurement = deviceMeasurementService.getLastDevice(deviceInfo.getDeviceId());
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date);
            String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
            List<DeviceMeasurement> deviceMeasurements = deviceLastMeasureService.getMeasureData(pastDateStrArr, deviceInfo.getDeviceId());
            Map<String, DeviceMeasurement> deviceMeasurementMap = new LinkedHashMap<>();
            if (!CollectionUtils.isEmpty(deviceMeasurements)) {
                for (DeviceMeasurement measurement : deviceMeasurements) {
                    deviceMeasurementMap.put(measurement.getYearMonthDay(), measurement);
                }
            }
            if (deviceMeasurement != null) {
                deviceMeasurementMap.put(format, deviceMeasurement);
            }
            List<WeekEnergyConsumeStatistic> totalList = new ArrayList<>();
            List<WeekEnergyConsumeStatistic> spikeList = new ArrayList<>();
            List<WeekEnergyConsumeStatistic> peakList = new ArrayList<>();
            List<WeekEnergyConsumeStatistic> normalList = new ArrayList<>();
            List<WeekEnergyConsumeStatistic> valleyList = new ArrayList<>();
            BigDecimal bigDecimalTho = new BigDecimal("1000");
            for (String dataStr : pastDateStrArr) {
                DeviceMeasurement deviceMeasurement1 = null;
                if (deviceMeasurementMap.containsKey(dataStr)) {
                    deviceMeasurement1 = deviceMeasurementMap.get(dataStr);
                } else {
                    continue;
                }
                String total = deviceMeasurement1.getDayTotal();
                String spike = deviceMeasurement1.getDaySpike();
                String peak = deviceMeasurement1.getDayPeak();
                String normal = deviceMeasurement1.getDayNormal();
                String valley = deviceMeasurement1.getDayValley();
                String week = DateUtils.getWeek(dataStr);

                BigDecimal totalDecimal = new BigDecimal(total);
                BigDecimal divideTotal = totalDecimal.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                totalList.add(new WeekEnergyConsumeStatistic(week, divideTotal.floatValue()));

                BigDecimal spikeDecimal = new BigDecimal(spike);
                BigDecimal divideSpike = spikeDecimal.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                spikeList.add(new WeekEnergyConsumeStatistic(week, divideSpike.floatValue()));

                BigDecimal peakDecimal = new BigDecimal(peak);
                BigDecimal dividePeak = peakDecimal.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                peakList.add(new WeekEnergyConsumeStatistic(week, dividePeak.floatValue()));

                BigDecimal normalDecimal = new BigDecimal(normal);
                BigDecimal divideNormal = normalDecimal.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                normalList.add(new WeekEnergyConsumeStatistic(week, divideNormal.floatValue()));

                BigDecimal valleyDecimal = new BigDecimal(valley);
                BigDecimal divideValley = valleyDecimal.divide(bigDecimalTho, 3, BigDecimal.ROUND_HALF_UP);
                valleyList.add(new WeekEnergyConsumeStatistic(week, divideValley.floatValue()));

                Map<String, List<WeekEnergyConsumeStatistic>> measureData = new LinkedHashMap<>();
                if (!CollectionUtils.isEmpty(totalList)) {
                    measureData.put("total", totalList);
                }
                if (!CollectionUtils.isEmpty(totalList)) {
                    measureData.put("spike", spikeList);
                }
                if (!CollectionUtils.isEmpty(peakList)) {
                    measureData.put("peak", peakList);
                }
                if (!CollectionUtils.isEmpty(normalList)) {
                    measureData.put("normal", normalList);
                }
                if (!CollectionUtils.isEmpty(valleyList)) {
                    measureData.put("valley", valleyList);
                }
                if (!CollectionUtils.isEmpty(measureData)) {
                    powerBoxInfoDto.setMeasureData(measureData);
                }

            }
        } else {
            powerBoxInfoDto.setDataFlag(0);
        }
        if (boxId.equals(1l)) {
            List<Integer> xdata = new ArrayList<>();
            for (int i = 5; i < 605; i = i + 60) {
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
            List<Integer> powerFactoryA = new ArrayList<>();
            List<Integer> powerFactoryB = new ArrayList<>();
            List<Integer> powerFactoryC = new ArrayList<>();
            if(loopId.equals(28l)||loopId.equals(29l)){
                powerBoxInfoDto.setDataFlag(1);
                for (int i = 0; i < 10; i++) {
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
                    powerFactoryA.add(700);
                    powerFactoryB.add(800);
                    powerFactoryC.add(900);

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
                powerBoxInfoDto.setVoltDataMap(dianYaMap);

                Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                dianLiuMap.put("xDanWei", "s前");
                dianLiuMap.put("yDanWei", "V");
                dianLiuMap.put("xData", xdata);
                Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                dianLiuDataMap.put("dianLiuA", dianliuA);
                dianLiuDataMap.put("dianLiuB", dianliuB);
                dianLiuDataMap.put("dianLiuC", dianliuC);
                dianLiuMap.put("dianLiu", dianLiuDataMap);
                powerBoxInfoDto.setAmpDataMap(dianLiuMap);


                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                activePowerMap.put("xDanWei", "s前");
                activePowerMap.put("yDanWei", "w");
                activePowerMap.put("xData", xdata);
                Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
                activePowerDataMap.put("activePowerA", activePowerA);
                activePowerDataMap.put("activePowerB", activePowerB);
                activePowerDataMap.put("activePowerC", activePowerC);
                activePowerMap.put("activePower", activePowerDataMap);
                powerBoxInfoDto.setActivePowerMap(activePowerMap);

                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                reactivePowerMap.put("xDanWei", "s前");
                reactivePowerMap.put("yDanWei", "var");
                reactivePowerMap.put("xData", xdata);
                Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
                reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
                reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
                reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
                reactivePowerMap.put("reactivePower", reactivePowerDataMap);
                powerBoxInfoDto.setReactivePowerMap(reactivePowerMap);

                Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                powerFactoryMap.put("xDanWei", "s前");
                powerFactoryMap.put("yDanWei", "0.001");
                powerFactoryMap.put("xData", xdata);
                Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                powerFactoryDataMap.put("powerFactorA", powerFactoryA);
                powerFactoryDataMap.put("powerFactorB", powerFactoryB);
                powerFactoryDataMap.put("powerFactorC", powerFactoryC);
                powerFactoryMap.put("powerFactor", powerFactoryDataMap);
                powerBoxInfoDto.setPowerFactorMap(powerFactoryMap);

                DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
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

                deviceDataSmartElecFloatDto.setReactivePowerA(10f);
                deviceDataSmartElecFloatDto.setReactivePowerB(14F);
                deviceDataSmartElecFloatDto.setReactivePowerC(15F);

                powerBoxInfoDto.setSmartData(deviceDataSmartElecFloatDto);

                List<WeekEnergyConsumeStatistic> totalList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> spikeList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> peakList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> normalList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> valleyList = new ArrayList<>();
                Date date = new Date();
                String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
                for (String s : pastDateStrArr) {
                    String week = DateUtils.getWeek(s);
                    totalList.add(new WeekEnergyConsumeStatistic(week, 1000f));
                    spikeList.add(new WeekEnergyConsumeStatistic(week, 200f));
                    peakList.add(new WeekEnergyConsumeStatistic(week, 400f));
                    normalList.add(new WeekEnergyConsumeStatistic(week, 200f));
                    valleyList.add(new WeekEnergyConsumeStatistic(week, 200f));
                }
                Map<String, List<WeekEnergyConsumeStatistic>> measureData = new LinkedHashMap<>();
                measureData.put("total", totalList);
                measureData.put("spike", spikeList);
                measureData.put("peak", peakList);
                measureData.put("normal", normalList);
                measureData.put("valley", valleyList);
                powerBoxInfoDto.setMeasureData(measureData);
            }else if(loopId.equals(3l)){
                powerBoxInfoDto.setDataFlag(1);
                for (int i = 0; i < 10; i++) {
                    dianyaA.add(440f);
                    dianyaB.add(460f);
                    dianyaC.add(480f);

                    dianliuA.add(100f);
                    dianliuB.add(140f);
                    dianliuC.add(180f);

                    activePowerA.add(2000f);
                    activePowerB.add(2400f);
                    activePowerC.add(2800f);

                    reactivePowerA.add(40f);
                    reactivePowerB.add(60f);
                    reactivePowerC.add(80f);
                    powerFactoryA.add(300);
                    powerFactoryB.add(400);
                    powerFactoryC.add(500);

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
                powerBoxInfoDto.setVoltDataMap(dianYaMap);

                Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                dianLiuMap.put("xDanWei", "s前");
                dianLiuMap.put("yDanWei", "V");
                dianLiuMap.put("xData", xdata);
                Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                dianLiuDataMap.put("dianLiuA", dianliuA);
                dianLiuDataMap.put("dianLiuB", dianliuB);
                dianLiuDataMap.put("dianLiuC", dianliuC);
                dianLiuMap.put("dianLiu", dianLiuDataMap);
                powerBoxInfoDto.setAmpDataMap(dianLiuMap);


                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                activePowerMap.put("xDanWei", "s前");
                activePowerMap.put("yDanWei", "w");
                activePowerMap.put("xData", xdata);
                Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
                activePowerDataMap.put("activePowerA", activePowerA);
                activePowerDataMap.put("activePowerB", activePowerB);
                activePowerDataMap.put("activePowerC", activePowerC);
                activePowerMap.put("activePower", activePowerDataMap);
                powerBoxInfoDto.setActivePowerMap(activePowerMap);

                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                reactivePowerMap.put("xDanWei", "s前");
                reactivePowerMap.put("yDanWei", "var");
                reactivePowerMap.put("xData", xdata);
                Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
                reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
                reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
                reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
                reactivePowerMap.put("reactivePower", reactivePowerDataMap);
                powerBoxInfoDto.setReactivePowerMap(reactivePowerMap);

                Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                powerFactoryMap.put("xDanWei", "s前");
                powerFactoryMap.put("yDanWei", "0.001");
                powerFactoryMap.put("xData", xdata);
                Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                powerFactoryDataMap.put("powerFactorA", powerFactoryA);
                powerFactoryDataMap.put("powerFactorB", powerFactoryB);
                powerFactoryDataMap.put("powerFactorC", powerFactoryC);
                powerFactoryMap.put("powerFactor", powerFactoryDataMap);
                powerBoxInfoDto.setPowerFactorMap(powerFactoryMap);

                DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
                deviceDataSmartElecFloatDto.setVoltRmsA(440f);
                deviceDataSmartElecFloatDto.setVoltRmsB(460f);
                deviceDataSmartElecFloatDto.setVoltRmsC(520f);

                deviceDataSmartElecFloatDto.setAmpRmsA(200f);
                deviceDataSmartElecFloatDto.setAmpRmsB(260f);
                deviceDataSmartElecFloatDto.setAmpRmsC(320f);

                deviceDataSmartElecFloatDto.setPowerFactorA(700);
                deviceDataSmartElecFloatDto.setPowerFactorB(800);
                deviceDataSmartElecFloatDto.setPowerFactorC(900);

                deviceDataSmartElecFloatDto.setActivePowerA(2000F);
                deviceDataSmartElecFloatDto.setActivePowerB(2400F);
                deviceDataSmartElecFloatDto.setActivePowerC(2800F);

                deviceDataSmartElecFloatDto.setReactivePowerA(20f);
                deviceDataSmartElecFloatDto.setReactivePowerB(28F);
                deviceDataSmartElecFloatDto.setReactivePowerC(30F);

                powerBoxInfoDto.setSmartData(deviceDataSmartElecFloatDto);

                List<WeekEnergyConsumeStatistic> totalList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> spikeList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> peakList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> normalList = new ArrayList<>();
                List<WeekEnergyConsumeStatistic> valleyList = new ArrayList<>();
                Date date = new Date();
                String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
                for (String s : pastDateStrArr) {
                    String week = DateUtils.getWeek(s);
                    totalList.add(new WeekEnergyConsumeStatistic(week, 2000f));
                    spikeList.add(new WeekEnergyConsumeStatistic(week, 400f));
                    peakList.add(new WeekEnergyConsumeStatistic(week, 800f));
                    normalList.add(new WeekEnergyConsumeStatistic(week, 400f));
                    valleyList.add(new WeekEnergyConsumeStatistic(week, 400f));
                }
                Map<String, List<WeekEnergyConsumeStatistic>> measureData = new LinkedHashMap<>();
                measureData.put("total", totalList);
                measureData.put("spike", spikeList);
                measureData.put("peak", peakList);
                measureData.put("normal", normalList);
                measureData.put("valley", valleyList);
                powerBoxInfoDto.setMeasureData(measureData);
            }

        }
        return powerBoxInfoDto;
    }

}
