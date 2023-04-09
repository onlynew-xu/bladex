package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AlarmWarnDao;
import com.steelman.iot.platform.dao.PowerDao;
import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartElec;
import com.steelman.iot.platform.energyManager.entity.DeviceHourDataSmartSuper;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.PowerDataInfo;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceCount;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceStatus;
import com.steelman.iot.platform.largescreen.vo.PowerRecentInfo;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/10 0010 10:29
 * @Description:
 */
@Service("powerService")
@Slf4j
public class PowerServiceImpl extends BaseService implements PowerService {

    @Resource
    private PowerDao powerDao;
    @Resource
    private PowerDeviceService powerDeviceService;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private PowerIncomingService powerIncomingService;
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerWaveService powerWaveService;
    @Resource
    private PowerCompensateService powerCompensateService;
    @Resource
    private PowerCompensateDeviceService powerCompensateDeviceService;
    @Resource
    private PowerMatriculationService powerMatriculationService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceParamsSmartElecService deviceParamsSmartElecService;
    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerGeneratorService powerGeneratorService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private PowerService powerService;
    @Resource
    private PowerCompensateDeviceService compensateDeviceService;
    @Resource
    private PowerWaveDeviceService powerWaveDeviceService;
    @Resource
    private PowerFeederLoopDeviceService powerFeederLoopDeviceService;
    @Resource
    private PowerBoxLoopDeviceService powerBoxLoopDeviceService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private PowerFeederLoopService powerFeederLoopService;
    @Resource
    private DeviceDataSmartSuperService deviceDataSmartSuperService;
    @Resource
    private AlarmWarnDao warnDao;
    @Resource
    private PowerBoxLoopService powerBoxLoopService;
    @Resource
    private DataTemperaturehumidity dataTemperaturehumidity;
    @Resource
    private DeviceHourDataSmartElecService deviceHourDataSmartElecService;
    @Resource
    private DeviceHourDataSmartSuperService deviceHourDataSmartSuperService;
    @Resource
    private PowerPictureService powerPictureService;

    @Override
    public void insert(Power power) {
        powerDao.insert(power);
    }

    @Override
    public List<Power> selectByProjectId(Long projectId) {
        List<Power> list = powerDao.selectByProjectId(projectId);
        return list;
    }

    @Override
    public void deleteById(Long id) {
        powerDao.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Power power) {
        powerDao.updateByPrimaryKeySelective(power);
    }

    @Override
    public Power getPowerInfo(Long powerId) {
        return powerDao.selectByPrimaryKey(powerId);
    }

    @Override
    public List<PowerDataVo> getPowerData(Long projectId) {
        List<PowerDataVo> vos = new ArrayList<>();
        PowerDataVo vo = new PowerDataVo();
        List<Power> list = powerDao.selectByProjectId(projectId);
        BigDecimal scale = new BigDecimal("1000");
        for (Power power : list) {
            vo.setId(power.getId());
            vo.setName(power.getName());

            DataTemperaturehumidity humidity = powerDeviceService.selectPowerTemperatureHumidity(power.getId());
            if (!Objects.isNull(humidity)) {
                vo.setHumidity(humidity.getHumidity());
                vo.setTemperature(humidity.getTemperature());
            }
            List<DeviceParamsSmartElec> dataList = deviceParamsSmartElecService.selectPowerIncomingDevice(power.getId());
            BigDecimal total = new BigDecimal("0");
            for (DeviceParamsSmartElec paramsSmartElec : dataList) {
                DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(paramsSmartElec.getDeviceId());
                BigDecimal activePowerA = new BigDecimal(dataSmartElec.getActivePowerA()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorA()));
                BigDecimal activePowerB = new BigDecimal(dataSmartElec.getActivePowerB()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorB()));
                BigDecimal activePowerC = new BigDecimal(dataSmartElec.getActivePowerC()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorC()));
                BigDecimal transLoad = activePowerA.add(activePowerB).add(activePowerC).multiply(new BigDecimal(paramsSmartElec.getTransCapacity()));
                total = total.add(transLoad);
            }
            vo.setTransLoad(total);
            Long incomingCount = powerIncomingService.selectCountByPowerId(power.getId());
            Long feederCount = powerFeederService.selectCountByPowerId(power.getId());
            Long waveCount = powerWaveService.selectCountByPowerId(power.getId());
            Long compensateCount = powerCompensateService.selectCountByPowerId(power.getId());
            Long matriculationCount = powerMatriculationService.selectCountByPowerId(power.getId());
            vo.setCabinetCount(incomingCount + feederCount + waveCount + compensateCount + matriculationCount);
            vos.add(vo);
        }
        return vos;
    }


    @Resource
    private PowerBoxService powerBoxService;

//    @Override
//    public List<PowerInfoVo> getPowerDataInfo(Long powerId) {
//        List<PowerInfoVo> voList = new ArrayList<>();
//        BigDecimal scale = new BigDecimal("1000");
//        List<PowerTransformer> transformerList = powerTransformerService.selectByPowerId(powerId);
//        for (PowerTransformer transformer : transformerList) {
//            PowerInfoVo vo = new PowerInfoVo();
//            vo.setTransformerName(transformer.getName());
//            vo.setTransformerId(transformer.getId());
//            DeviceParamsSmartElec paramsSmartElec = deviceParamsSmartElecService.selectTransformerIncomingDevice(transformer.getId()).get(0);
//            DeviceDataSmartElec dataSmartElec = deviceDataSmartElecService.getLastData(paramsSmartElec.getDeviceId());
//            BigDecimal activePowerC = new BigDecimal(dataSmartElec.getActivePowerC()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorC()));
//            BigDecimal activePowerB = new BigDecimal(dataSmartElec.getActivePowerB()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorB()));
//            BigDecimal activePowerA = new BigDecimal(dataSmartElec.getActivePowerA()).divide(scale).divide(new BigDecimal(dataSmartElec.getPowerFactorA()));
//            BigDecimal transLoad = activePowerA.add(activePowerB).add(activePowerC).multiply(new BigDecimal(paramsSmartElec.getTransCapacity()));
//            //电房实时数据
//            vo.setSmartElec(dataSmartElec);
//            //变压器负荷率
//            vo.setTransLoad(transLoad.toString());
//            //电压图数据
//            List<Map<String, Object>> voltDataList = deviceDataSmartElecService.selectPowerVoltData(dataSmartElec.getDeviceId());
//            vo.setVoltDataList(voltDataList);
//            //电流图数据
//            List<Map<String, Object>> ampDataList = deviceDataSmartElecService.selectPowerAmpData(dataSmartElec.getDeviceId());
//            vo.setAmpDataList(ampDataList);
//            //功率因数数据
//            List<Map<String, Object>> factorDataList = deviceDataSmartElecService.selectPowerFactorData(dataSmartElec.getDeviceId());
//            vo.setFactorDataList(factorDataList);
//            //关联配电柜
//
//            //进线柜
//            List<PowerIncoming> powerIncomings = powerIncomingService.getIncomingList(vo.getTransformerId());
//            vo.setPowerIncomings(powerIncomings);
//
//            //滤波柜
//            List<PowerWave> powerWaves = powerWaveService.selectByTransformerId(vo.getTransformerId());
//            vo.setPowerWaves(powerWaves);
//
//            //补偿柜
//            List<PowerCompensate> powerCompensates = powerCompensateService.selectByTransformerId(vo.getTransformerId());
//            vo.setPowerCompensates(powerCompensates);
//            //单线馈线柜
//            List<PowerFeeder> feeders = powerFeederService.getFeederList(0, vo.getTransformerId());
//            //多线馈线柜
//            List<PowerFeeder> feederList = powerFeederService.getFeederList(1, vo.getTransformerId());
//            feederList.addAll(feeders);
//            vo.setPowerFeeders(feeders);
//
//            //母联柜
//            List<PowerMatriculation> powerMatriculations = powerMatriculationService.getMatriculationList(vo.getTransformerId());
//            vo.setPowerMatriculations(powerMatriculations);
//
//            //配电箱
//            List<PowerBox> boxes = powerBoxService.getTransformPowerBox(vo.getTransformerId());
//            vo.setPowerBoxes(boxes);
//            voList.add(vo);
//        }
//
//
//        return voList;
//    }

    @Override
    public Power selectByNameAndProjectId(String powerName, Long projectId) {
        return powerDao.selectByNameAndProjectId(powerName, projectId);
    }

    @Override
    public List<PowerDataSimple> getPowerSimple(Long projectId) {
        List<Power> powerList = powerDao.selectByProjectId(projectId);
        List<PowerDataSimple> powerDataSimpleList = new ArrayList<>();
        for (Power power : powerList) {
            PowerDataSimple powerDataSimple = new PowerDataSimple();
            powerDataSimple.setDefaultFlag(0);
            powerDataSimple.setId(power.getId());
            powerDataSimple.setName(power.getName());
            DataTemperaturehumidity humidity = powerDeviceService.selectPowerTemperatureHumidity(power.getId());
            if (!Objects.isNull(humidity)) {
                powerDataSimple.setHumidity(humidity.getHumidity());
                powerDataSimple.setTemperature(humidity.getTemperature());
            }
            Integer total = 0;
            List<PowerTransformer> powerTransformers = powerTransformerService.selectByPowerId(power.getId());
            for (PowerTransformer powerTransformer : powerTransformers) {
                String capacity = powerTransformer.getCapacity();
                total = total + Integer.parseInt(capacity);
            }

            powerDataSimple.setTransLoad(total);
            //进线柜数量
            Long incomingCount = powerIncomingService.selectCountByPowerId(power.getId());
            //馈线柜数量
            Long feederCount = powerFeederService.selectCountByPowerId(power.getId());
            //滤波柜数量
            Long waveCount = powerWaveService.selectCountByPowerId(power.getId());
            //补偿柜数量
            Long compensateCount = powerCompensateService.selectCountByPowerId(power.getId());
            //母联柜数量
            Long matriculationCount = powerMatriculationService.selectCountByPowerId(power.getId());

            powerDataSimple.setCabinetCount(incomingCount + feederCount + waveCount + compensateCount + matriculationCount);
            powerDataSimpleList.add(powerDataSimple);
        }
        return powerDataSimpleList;
    }

    @Override
    public PowerInfoVo getPowerDetail(Long powerId) {
        PowerInfoVo powerInfoVo = new PowerInfoVo();
        List<PowerTransformer> transformerList = powerTransformerService.selectByPowerId(powerId);
        Map<Long, TransformLoadDto> transformLoadDtoMap = new LinkedHashMap<>();
        Long transformerId = null;
        if (!CollectionUtils.isEmpty(transformerList)) {
            List<TransformLoadDto> transformers = new ArrayList<>();
            for (PowerTransformer powerTransformer : transformerList) {
                Integer status = powerTransformer.getStatus();
                TransformLoadDto transformLoadDto = new TransformLoadDto();
                transformLoadDto.setTransformId(powerTransformer.getId());
                transformLoadDto.setTransformName(powerTransformer.getName());
                if (status == 1) {
                    //获取进线柜
                    List<PowerIncoming> powerIncomingList = powerIncomingService.getIncomingList(powerTransformer.getId());
                    if (!CollectionUtils.isEmpty(powerIncomingList)) {
                        PowerIncoming powerIncoming = powerIncomingList.get(0);
                        //获取绑定的设备
                        Long deviceId = powerIncomingDeviceService.getBindingOnDevice(powerIncoming.getId());
                        DeviceDataSmartElec lastData = deviceDataSmartElecService.getLastData(deviceId);
                        DeviceParamsSmartElec byDeviceId = deviceParamsSmartElecService.findByDeviceId(deviceId);
                        if (lastData != null) {
                            String activePowerA = lastData.getActivePowerA();
                            String activePowerB = lastData.getActivePowerB();
                            String activePowerC = lastData.getActivePowerC();
                            BigDecimal activePowerTotal = new BigDecimal("0");
                            String capacity = byDeviceId.getTransCapacity();
                            BigDecimal capacityTotal = new BigDecimal(capacity);
                            if (StringUtils.isNotBlank(activePowerA)) {
                                BigDecimal activePowerABig = new BigDecimal(activePowerA);
                                activePowerTotal = activePowerTotal.add(activePowerABig);
                            }
                            if (StringUtils.isNotBlank(activePowerB)) {
                                BigDecimal activePowerBBig = new BigDecimal(activePowerB);
                                activePowerTotal = activePowerTotal.add(activePowerBBig);
                            }
                            if (StringUtils.isNotBlank(activePowerC)) {
                                BigDecimal activePowerCBig = new BigDecimal(activePowerC);
                                activePowerTotal = activePowerTotal.add(activePowerCBig);
                            }
                            BigDecimal divide = activePowerTotal.divide(capacityTotal, 1, BigDecimal.ROUND_HALF_UP);
                            float v = divide.floatValue();
                            transformLoadDto.setDefaultFlag(1);
                            transformLoadDto.setPercent(String.valueOf(v));
                        } else {
                            transformLoadDto.setDefaultFlag(1);
                            transformerId = powerTransformer.getId();
                            transformLoadDto.setPercent("0");
                        }

                    } else {
                        transformLoadDto.setDefaultFlag(1);
                        transformerId = powerTransformer.getId();
                        transformLoadDto.setPercent("0");
                    }
                } else {
                    transformLoadDto.setDefaultFlag(0);
                    transformLoadDto.setPercent("0");
                }
                transformLoadDtoMap.put(powerTransformer.getId(), transformLoadDto);
            }
            for (Map.Entry<Long, TransformLoadDto> longTransformLoadDtoEntry : transformLoadDtoMap.entrySet()) {
                TransformLoadDto value = longTransformLoadDtoEntry.getValue();
                transformers.add(value);
            }
            powerInfoVo.setTransformInfo(transformers);

        }

        //获取进线柜
        List<PowerIncoming> powerIncomingList = powerIncomingService.getIncomingList(transformerId);
        if (CollectionUtils.isEmpty(powerIncomingList)) {
            powerInfoVo.setDataFlag(0);
        } else {
            PowerIncoming powerIncoming = powerIncomingList.get(0);
            //获取绑定的设备
            Long deviceId = powerIncomingDeviceService.getBindingOnDevice(powerIncoming.getId());
            //获取设备数据
            DeviceDataSmartElec lastData = deviceDataSmartElecService.getLastData(deviceId);
            if (lastData == null) {
                //添加示例数据
                if (deviceId.equals(115l)) {
                    powerInfoVo.setDataFlag(1);
                    lastData = new DeviceDataSmartElec();
                    List<Integer> xdata = new ArrayList<>();
                    List<Float> dianyaA = new ArrayList<>();
                    List<Float> dianyaB = new ArrayList<>();
                    List<Float> dianyaC = new ArrayList<>();
                    List<Float> dianliuA = new ArrayList<>();
                    List<Float> dianliuB = new ArrayList<>();
                    List<Float> dianliuC = new ArrayList<>();
                    List<Integer> powerFactoryA = new ArrayList<>();
                    List<Integer> powerFactoryB = new ArrayList<>();
                    List<Integer> powerFactoryC = new ArrayList<>();
                    xdata.add(5);
                    xdata.add(65);
                    xdata.add(125);
                    xdata.add(185);
                    xdata.add(245);
                    xdata.add(305);
                    xdata.add(365);
                    xdata.add(425);
                    xdata.add(485);
                    xdata.add(545);
                    for (int i = 0; i < 10; i++) {
                        dianyaA.add(220f);
                        dianyaB.add(240f);
                        dianyaC.add(260f);
                        dianliuA.add(100f);
                        dianliuB.add(130f);
                        dianliuC.add(160f);
                        powerFactoryA.add(500);
                        powerFactoryB.add(700);
                        powerFactoryC.add(800);
                    }
                    DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
                    deviceDataSmartElecFloatDto.setVoltRmsA(220f);
                    deviceDataSmartElecFloatDto.setVoltRmsB(240f);
                    deviceDataSmartElecFloatDto.setVoltRmsC(260f);
                    deviceDataSmartElecFloatDto.setAmpRmsA(100f);
                    deviceDataSmartElecFloatDto.setAmpRmsB(130f);
                    deviceDataSmartElecFloatDto.setAmpRmsC(160f);
                    deviceDataSmartElecFloatDto.setPowerFactorA(500);
                    deviceDataSmartElecFloatDto.setPowerFactorB(700);
                    deviceDataSmartElecFloatDto.setPowerFactorC(800);
                    deviceDataSmartElecFloatDto.setActivePowerA(1000F);
                    deviceDataSmartElecFloatDto.setActivePowerB(1200F);
                    deviceDataSmartElecFloatDto.setActivePowerC(1400F);
                    deviceDataSmartElecFloatDto.setThdiA(20f);
                    deviceDataSmartElecFloatDto.setThdiB(20f);
                    deviceDataSmartElecFloatDto.setThdiC(20f);
                    deviceDataSmartElecFloatDto.setThdvA(15f);
                    deviceDataSmartElecFloatDto.setThdvB(15f);
                    deviceDataSmartElecFloatDto.setThdvC(15f);

                    powerInfoVo.setSmartData(deviceDataSmartElecFloatDto);
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        Map<String, Object> voltDataList = new LinkedHashMap<>();
                        voltDataList.put("xDanWei", "s前");
                        voltDataList.put("yDanWei", "V");
                        voltDataList.put("xData", xdata);
                        Map<String, Object> dianYaData = new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        voltDataList.put("dianya", dianYaData);
                        powerInfoVo.setVoltDataMap(voltDataList);
                    }
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                        dianLiuMap.put("xDanWei", "s前");
                        dianLiuMap.put("yDanWei", "A");
                        dianLiuMap.put("xData", xdata);
                        Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                        dianLiuDataMap.put("dianLiuA", dianliuA);
                        dianLiuDataMap.put("dianLiuB", dianliuB);
                        dianLiuDataMap.put("dianLiuC", dianliuC);
                        dianLiuMap.put("dianliu", dianLiuDataMap);
                        powerInfoVo.setAmpDataMap(dianLiuMap);
                    }
                    if (!CollectionUtils.isEmpty(powerFactoryA)) {
                        Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                        powerFactoryMap.put("xDanWei", "s前");
                        powerFactoryMap.put("yDanWei", "0.001");
                        powerFactoryMap.put("xData", xdata);
                        Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                        powerFactoryDataMap.put("powerFactoryA", powerFactoryA);
                        powerFactoryDataMap.put("powerFactoryB", powerFactoryB);
                        powerFactoryDataMap.put("powerFactoryC", powerFactoryC);
                        powerFactoryMap.put("powerFactory", powerFactoryDataMap);
                        powerInfoVo.setFactorDataMap(powerFactoryMap);
                    }
                } else {
                    powerInfoVo.setDataFlag(0);
                }
            } else {
                BigDecimal bigDecimalBai = new BigDecimal("100");
                BigDecimal bigDecimalShi = new BigDecimal("10");
                BigDecimal bigDecimalQian = new BigDecimal("100");
                DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
                //电流
                String ampRmsA1 = lastData.getAmpRmsA();
                String ampRmsB1 = lastData.getAmpRmsB();
                String ampRmsC1 = lastData.getAmpRmsC();
                //电压
                String voltRmsA1 = lastData.getVoltRmsA();
                String voltRmsB1 = lastData.getVoltRmsB();
                String voltRmsC1 = lastData.getVoltRmsC();
                //功率因数
                String powerFactorA1 = lastData.getPowerFactorA();
                String powerFactorB1 = lastData.getPowerFactorB();
                String powerFactorC1 = lastData.getPowerFactorC();
                //有功功率
                String activePowerA1 = lastData.getActivePowerA();
                String activePowerB1 = lastData.getActivePowerB();
                String activePowerC1 = lastData.getActivePowerC();
                //电流谐波
                String thdiA1 = lastData.getThdiA();
                String thdiB1 = lastData.getThdiB();
                String thdiC1 = lastData.getThdiC();
                //电压谐波
                String thdvA1 = lastData.getThdvA();
                String thdvB1 = lastData.getThdvB();
                String thdvC1 = lastData.getThdvC();
                //处理电流
                if (StringUtils.isBlank(ampRmsA1) || ampRmsA1.equals("0")) {
                    deviceDataSmartElecFloatDto.setAmpRmsA(0f);
                } else {
                    BigDecimal ampRmsABig = new BigDecimal(ampRmsA1);
                    BigDecimal divide = ampRmsABig.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setAmpRmsA(divide.floatValue());
                }

                if (StringUtils.isBlank(ampRmsB1) || ampRmsB1.equals("0")) {
                    deviceDataSmartElecFloatDto.setAmpRmsB(0f);
                } else {
                    BigDecimal ampRmsBBig = new BigDecimal(ampRmsB1);
                    BigDecimal divide = ampRmsBBig.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setAmpRmsB(divide.floatValue());
                }

                if (StringUtils.isBlank(ampRmsC1) || ampRmsC1.equals("0")) {
                    deviceDataSmartElecFloatDto.setAmpRmsC(0f);
                } else {
                    BigDecimal ampRmsCBig = new BigDecimal(ampRmsC1);
                    BigDecimal divide = ampRmsCBig.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setAmpRmsC(divide.floatValue());
                }
                // 处理电压
                if (StringUtils.isBlank(voltRmsA1) || voltRmsA1.equals("0")) {
                    deviceDataSmartElecFloatDto.setVoltRmsA(0f);
                } else {
                    BigDecimal voltRmsABig = new BigDecimal(voltRmsA1);
                    BigDecimal divide = voltRmsABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setVoltRmsA(divide.floatValue());
                }
                if (StringUtils.isBlank(voltRmsB1) || voltRmsB1.equals("0")) {
                    deviceDataSmartElecFloatDto.setVoltRmsB(0f);
                } else {
                    BigDecimal voltRmsBBig = new BigDecimal(voltRmsB1);
                    BigDecimal divide = voltRmsBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setVoltRmsB(divide.floatValue());
                }

                if (StringUtils.isBlank(voltRmsC1) || voltRmsC1.equals("0")) {
                    deviceDataSmartElecFloatDto.setVoltRmsC(0f);
                } else {
                    BigDecimal voltRmsCBig = new BigDecimal(voltRmsC1);
                    BigDecimal divide = voltRmsCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setVoltRmsC(divide.floatValue());
                }
                //功率因数
                if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                    deviceDataSmartElecFloatDto.setPowerFactorA(0);
                } else {
                    deviceDataSmartElecFloatDto.setPowerFactorA(Integer.parseInt(powerFactorA1));
                }
                if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                    deviceDataSmartElecFloatDto.setPowerFactorB(0);
                } else {
                    deviceDataSmartElecFloatDto.setPowerFactorB(Integer.parseInt(powerFactorB1));
                }
                if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                    deviceDataSmartElecFloatDto.setPowerFactorC(0);
                } else {
                    deviceDataSmartElecFloatDto.setPowerFactorC(Integer.parseInt(powerFactorC1));
                }
                //有功功率
                if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                    deviceDataSmartElecFloatDto.setActivePowerA(0f);
                } else {
                    BigDecimal activePowerABig = new BigDecimal(activePowerA1);
                    BigDecimal divide = activePowerABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setActivePowerA(divide.floatValue());
                }
                if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                    deviceDataSmartElecFloatDto.setActivePowerB(0f);
                } else {
                    BigDecimal activePowerBBig = new BigDecimal(activePowerB1);
                    BigDecimal divide = activePowerBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setActivePowerB(divide.floatValue());
                }
                if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                    deviceDataSmartElecFloatDto.setActivePowerC(0f);
                } else {
                    BigDecimal activePowerCBig = new BigDecimal(activePowerC1);
                    BigDecimal divide = activePowerCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setActivePowerC(divide.floatValue());
                }
                //电流谐波
                if (StringUtils.isBlank(thdiA1) || thdiA1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdiA(0f);
                } else {
                    BigDecimal thdiABig = new BigDecimal(thdiA1);
                    BigDecimal divide = thdiABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdiA(divide.floatValue());
                }
                if (StringUtils.isBlank(thdiB1) || thdiB1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdiB(0f);
                } else {
                    BigDecimal thdiBBig = new BigDecimal(thdiB1);
                    BigDecimal divide = thdiBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdiB(divide.floatValue());
                }
                if (StringUtils.isBlank(thdiC1) || thdiC1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdiC(0f);
                } else {
                    BigDecimal thdiCBig = new BigDecimal(thdiC1);
                    BigDecimal divide = thdiCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdiC(divide.floatValue());
                }
                //电压谐波
                if (StringUtils.isBlank(thdvA1) || thdvA1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdvA(0f);
                } else {
                    BigDecimal thdvABig = new BigDecimal(thdvA1);
                    BigDecimal divide = thdvABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdvA(divide.floatValue());
                }
                if (StringUtils.isBlank(thdvB1) || thdvB1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdvB(0f);
                } else {
                    BigDecimal thdvBBig = new BigDecimal(thdvB1);
                    BigDecimal divide = thdvBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdvB(divide.floatValue());
                }
                if (StringUtils.isBlank(thdvC1) || thdvC1.equals("0.0%")) {
                    deviceDataSmartElecFloatDto.setThdvC(0f);
                } else {
                    BigDecimal thdvCBig = new BigDecimal(thdvC1);
                    BigDecimal divide = thdvCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                    deviceDataSmartElecFloatDto.setThdvC(divide.floatValue());
                }
                powerInfoVo.setSmartData(deviceDataSmartElecFloatDto);
                //处理折线图数据
                List<DeviceDataSmartElec> lastedTenData = deviceDataSmartElecService.getLastedTenData(deviceId);
                if (!CollectionUtils.isEmpty(lastedTenData)) {
                    List<Integer> xdata = new ArrayList<>();
                    List<Float> dianyaA = new ArrayList<>();
                    List<Float> dianyaB = new ArrayList<>();
                    List<Float> dianyaC = new ArrayList<>();
                    List<Float> dianliuA = new ArrayList<>();
                    List<Float> dianliuB = new ArrayList<>();
                    List<Float> dianliuC = new ArrayList<>();
                    List<Integer> powerFactoryA = new ArrayList<>();
                    List<Integer> powerFactoryB = new ArrayList<>();
                    List<Integer> powerFactoryC = new ArrayList<>();
                    long current = System.currentTimeMillis();

                    for (DeviceDataSmartElec lastedTenDatum : lastedTenData) {
                        Date createTime = lastedTenDatum.getCreateTime();
                        long time = createTime.getTime();
                        int sec = (int) (current - time) / 1000;
                        xdata.add(sec);

                        String voltAStr = lastedTenDatum.getVoltRmsA();
                        if (voltAStr == null || voltAStr.equals("0")) {
                            //添加示例数据
                            dianyaA.add(220f);
                        } else {
                            BigDecimal voltA = new BigDecimal(voltAStr);
                            BigDecimal divide = voltA.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            dianyaA.add(divide.floatValue());
                        }

                        String voltBStr = lastedTenDatum.getVoltRmsB();
                        if (voltBStr == null || voltBStr.equals("0")) {
                            //添加示例数据
                            dianyaB.add(240f);
                        } else {
                            BigDecimal voltB = new BigDecimal(voltBStr);
                            BigDecimal divide = voltB.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            dianyaB.add(divide.floatValue());
                        }

                        String voltCStr = lastedTenDatum.getVoltRmsC();
                        if (voltCStr == null || voltCStr.equals("0")) {
                            //添加示例数据
                            dianyaC.add(260f);
                        } else {
                            BigDecimal voltC = new BigDecimal(voltCStr);
                            BigDecimal divide = voltC.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            dianyaC.add(divide.floatValue());
                        }

                        String ampRmsAStr = lastedTenDatum.getAmpRmsA();
                        if (ampRmsAStr == null || ampRmsAStr.equals("0")) {
                            //添加示例数据
                            dianliuA.add(20f);
                        } else {
                            BigDecimal ampRmsA = new BigDecimal(ampRmsAStr);
                            BigDecimal divide = ampRmsA.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                            dianliuA.add(divide.floatValue());
                        }

                        String ampRmsBStr = lastedTenDatum.getAmpRmsB();
                        if (ampRmsBStr == null || ampRmsBStr.equals("0")) {
                            //添加示例数据
                            dianliuB.add(40f);
                        } else {
                            BigDecimal ampRmsB = new BigDecimal(ampRmsBStr);
                            BigDecimal divide = ampRmsB.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                            dianliuB.add(divide.floatValue());
                        }

                        String ampRmsCStr = lastedTenDatum.getAmpRmsC();
                        if (ampRmsCStr == null || ampRmsCStr.equals("0")) {
                            //添加示例数据
                            dianliuC.add(60f);
                        } else {
                            BigDecimal ampRmsC = new BigDecimal(ampRmsCStr);
                            BigDecimal divide = ampRmsC.divide(bigDecimalQian, 3, BigDecimal.ROUND_HALF_UP);
                            dianliuC.add(divide.floatValue());
                        }

                        String powerFactorAStr = lastedTenDatum.getPowerFactorA();
                        if (powerFactorAStr == null || powerFactorAStr.equals("0")) {
                            //添加示例数据
                            powerFactoryA.add(500);
                        } else {
                            powerFactoryA.add(Integer.parseInt(powerFactorAStr));
                        }

                        String powerFactorBStr = lastedTenDatum.getPowerFactorB();
                        if (powerFactorBStr == null || powerFactorBStr.equals("0")) {
                            //添加示例数据
                            powerFactoryB.add(600);
                        } else {
                            powerFactoryB.add(Integer.parseInt(powerFactorBStr));
                        }

                        String powerFactorCStr = lastedTenDatum.getPowerFactorC();
                        if (powerFactorCStr == null || powerFactorCStr.equals("0")) {
                            //添加示例数据
                            powerFactoryC.add(600);
                        } else {
                            powerFactoryC.add(Integer.parseInt(powerFactorCStr));
                        }
                    }
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        Map<String, Object> voltDataList = new LinkedHashMap<>();
                        voltDataList.put("xDanWei", "s前");
                        voltDataList.put("yDanWei", "V");
                        voltDataList.put("xData", xdata);
                        Map<String, Object> dianYaData = new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        voltDataList.put("dianya", dianYaData);
                        powerInfoVo.setVoltDataMap(voltDataList);
                    }
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                        dianLiuMap.put("xDanWei", "s前");
                        dianLiuMap.put("yDanWei", "A");
                        dianLiuMap.put("xData", xdata);
                        Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                        dianLiuDataMap.put("dianLiuA", dianliuA);
                        dianLiuDataMap.put("dianLiuB", dianliuB);
                        dianLiuDataMap.put("dianLiuC", dianliuC);
                        dianLiuMap.put("dianliu", dianLiuDataMap);
                        powerInfoVo.setAmpDataMap(dianLiuMap);
                    }
                    if (!CollectionUtils.isEmpty(powerFactoryA)) {
                        Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                        powerFactoryMap.put("xDanWei", "s前");
                        powerFactoryMap.put("yDanWei", "0.001");
                        powerFactoryMap.put("xData", xdata);
                        Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                        powerFactoryDataMap.put("powerFactoryA", powerFactoryA);
                        powerFactoryDataMap.put("powerFactoryB", powerFactoryB);
                        powerFactoryDataMap.put("powerFactoryC", powerFactoryC);
                        powerFactoryMap.put("powerFactory", powerFactoryDataMap);
                        powerInfoVo.setFactorDataMap(powerFactoryMap);
                    }
                }

            }
        }
        Power powerInfo = powerService.getPowerInfo(powerId);
        List<PowerFacility> powerdeviceList = new ArrayList<>();
        //处理所有设备
        //处理进线柜
        List<PowerIncoming> powerIncomings = powerIncomingService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerIncomings)) {
            for (PowerIncoming incoming : powerIncomings) {
                PowerFacility powerFacility = new PowerFacility();
                powerFacility.setPowerId(powerInfo.getId());
                powerFacility.setPowerName(powerInfo.getName());
                powerFacility.setPowerFacilityId(incoming.getId());
                powerFacility.setPowerFacilityName(incoming.getName());
                powerFacility.setType(3);
                powerdeviceList.add(powerFacility);
            }
        }
        //处理补偿柜
        List<PowerCompensate> powerCompensates = powerCompensateService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerCompensates)) {
            for (PowerCompensate powerCompensate : powerCompensates) {
                PowerFacility powerFacility = new PowerFacility();
                powerFacility.setPowerId(powerInfo.getId());
                powerFacility.setPowerName(powerInfo.getName());
                powerFacility.setPowerFacilityId(powerCompensate.getId());
                powerFacility.setPowerFacilityName(powerCompensate.getName());
                powerFacility.setType(4);
                powerdeviceList.add(powerFacility);
            }
        }
        //处理滤波柜
        List<PowerWave> powerWaves = powerWaveService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerWaves)) {
            for (PowerWave powerWave : powerWaves) {
                PowerFacility powerFacility = new PowerFacility();
                powerFacility.setPowerId(powerInfo.getId());
                powerFacility.setPowerName(powerInfo.getName());
                powerFacility.setPowerFacilityId(powerWave.getId());
                powerFacility.setPowerFacilityName(powerWave.getName());
                powerFacility.setType(5);
                powerdeviceList.add(powerFacility);
            }
        }
        //处理馈线柜
        List<PowerFeeder> powerFeederList = powerFeederService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerFeederList)) {
            for (PowerFeeder powerFeeder : powerFeederList) {
                PowerFacility powerFacility = new PowerFacility();
                powerFacility.setPowerId(powerInfo.getId());
                powerFacility.setPowerName(powerInfo.getName());
                powerFacility.setPowerFacilityId(powerFeeder.getId());
                powerFacility.setPowerFacilityName(powerFeeder.getName());
                Integer loopType = powerFeeder.getLoopType();
                if (loopType.equals(0)) {
                    powerFacility.setType(6);
                } else {
                    powerFacility.setType(7);
                }
                powerdeviceList.add(powerFacility);
            }
        }
        powerInfoVo.setPowerFacilityList(powerdeviceList);
        return powerInfoVo;
    }

    @Override
    public PowerCenterInfo getPowerCenter(Long projectId) {

        PowerCenterInfo powerCenterInfo = new PowerCenterInfo();
        List<Power> powerList = powerDao.selectByProjectId(projectId);
        Integer totalPower = 0;
        List<PowerDataSimple> powerDataSimpleList = new ArrayList<>();
        for (Power power : powerList) {
            Integer total = 0;
            PowerDataSimple powerDataSimple = new PowerDataSimple();
            powerDataSimple.setDefaultFlag(0);
            powerDataSimple.setId(power.getId());
            powerDataSimple.setName(power.getName());
            DataTemperaturehumidity humidity = powerDeviceService.selectPowerTemperatureHumidity(power.getId());
            if (!Objects.isNull(humidity)) {
                powerDataSimple.setHumidity(humidity.getHumidity());
                powerDataSimple.setTemperature(humidity.getTemperature());
            }
            List<PowerTransformer> powerTransformers = powerTransformerService.selectByPowerId(power.getId());
            if (!CollectionUtils.isEmpty(powerTransformers)) {
                for (PowerTransformer powerTransformer : powerTransformers) {
                    String capacity = powerTransformer.getCapacity();
                    total = total + Integer.parseInt(capacity);
                }
            }
            totalPower = totalPower + total;
            powerDataSimple.setTransLoad(total);
            //进线柜数量
            Long incomingCount = powerIncomingService.selectCountByPowerId(power.getId());
            //馈线柜数量
            Long feederCount = powerFeederService.selectCountByPowerId(power.getId());
            //滤波柜数量
            Long waveCount = powerWaveService.selectCountByPowerId(power.getId());
            //补偿柜数量
            Long compensateCount = powerCompensateService.selectCountByPowerId(power.getId());
            //母联柜数量
            Long matriculationCount = powerMatriculationService.selectCountByPowerId(power.getId());
            //配电箱数量
            Long powerBoxCount = powerBoxService.selectCountByPowerId(power.getId());
            //发电机数量
            Long powerGeneratorCount = powerGeneratorService.selectCountByPowerId(power.getId());

            powerDataSimple.setCabinetCount(incomingCount + feederCount + waveCount + compensateCount + matriculationCount + powerBoxCount + powerGeneratorCount);
            powerDataSimpleList.add(powerDataSimple);
        }
        PowerDataSimple powerDataSimple = powerDataSimpleList.get(0);
        powerDataSimple.setDefaultFlag(1);
        powerCenterInfo.setPowerDataSimpleList(powerDataSimpleList);
        powerCenterInfo.setPowerCount(powerList.size());
        powerCenterInfo.setTotalLoad(totalPower);
        //处理设备中心
        List<PowerFacilityCenterInfo> powerFacilityCenterInfoList = getPowerCenterData(powerDataSimple.getId(), powerDataSimple.getName(), projectId);
        powerCenterInfo.setPowerCenter(powerFacilityCenterInfoList);
        return powerCenterInfo;
    }

    @Override
    public List<PowerFacilityCenterInfo> getPowerCenterDevice(Long powerId) {
        Power powerInfo = powerService.getPowerInfo(powerId);
        List<PowerFacilityCenterInfo> powerCenterData = getPowerCenterData(powerInfo.getId(), powerInfo.getName(), powerInfo.getProjectId());
        return powerCenterData;
    }

    private List<PowerFacilityCenterInfo> getPowerCenterData(Long powerId, String powerName, Long projectId) {
        List<PowerFacilityCenterInfo> powerFacilityCenterInfoList = new ArrayList<>();
        List<PowerFacilityCenter> alarmList = new ArrayList<>();
        List<PowerFacilityCenter> allLineList = new ArrayList<>();
        List<PowerFacilityCenter> offLineList = new ArrayList<>();
        List<PowerFacilityCenter> transformFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> generatorFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> incomingFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> compensateFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> waveFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> feederSimpleFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> feederComplexFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> boxFacilityCenter = new ArrayList<>();
        List<PowerFacilityCenter> matriculationFacilityCenter = new ArrayList<>();
        //处理变压器
        Map<Long, Integer> deviceStatus = new LinkedHashMap<>();
        List<Map<String, Object>> deviceStatusMap = deviceService.getPowerDeviceList(projectId);
        if (!CollectionUtils.isEmpty(deviceStatusMap)) {
            for (Map<String, Object> stringObjectMap : deviceStatusMap) {
                Long deviceId = Long.parseLong(stringObjectMap.get("deviceId").toString());
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                deviceStatus.put(deviceId, status);
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        List<PowerTransformer> powerTransformers = powerTransformerService.selectByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerTransformers)) {
            for (PowerTransformer powerTransformer : powerTransformers) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerTransformer.getId());
                powerFacilityCenter.setPowerFacilityName(powerTransformer.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerTransformer.getCreateTime()));
                powerFacilityCenter.setStatus(powerTransformer.getStatus().equals(0) ? 2 : 3);
                powerFacilityCenter.setType(1);
                allLineList.add(powerFacilityCenter);
                transformFacilityCenter.add(powerFacilityCenter);
            }
        }
        //处理发电机
        List<PowerGenerator> powerGeneratorList = powerGeneratorService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerGeneratorList)) {
            for (PowerGenerator powerGenerator : powerGeneratorList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerGenerator.getId());
                powerFacilityCenter.setPowerFacilityName(powerGenerator.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerGenerator.getCreateTime()));
                powerFacilityCenter.setStatus(powerGenerator.getStatus().equals(0) ? 2 : 4);
                powerFacilityCenter.setType(2);
                allLineList.add(powerFacilityCenter);
                generatorFacilityCenter.add(powerFacilityCenter);
            }
        }
        //进线柜
        List<PowerIncoming> powerIncomingList = powerIncomingService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerIncomingList)) {
            for (PowerIncoming powerIncoming : powerIncomingList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerIncoming.getId());
                powerFacilityCenter.setPowerFacilityName(powerIncoming.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerIncoming.getCreateTime()));
                List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
                Integer status = 2;
                if (!CollectionUtils.isEmpty(deviceList)) {
                    for (PowerDeviceInfo powerDeviceInfo : deviceList) {
                        Long deviceId = powerDeviceInfo.getDeviceId();
                        Integer integer = deviceStatus.get(deviceId);
                        if (integer.equals(1)) {
                            status = 1;
                            break;
                        } else if (integer.equals(3)) {
                            status = 3;
                        }
                    }
                }
                powerFacilityCenter.setStatus(status);
                if (status.equals(1)) {
                    alarmList.add(powerFacilityCenter);
                } else if (status.equals(2) || status.equals(4)) {
                    offLineList.add(powerFacilityCenter);
                }
                powerFacilityCenter.setType(3);
                allLineList.add(powerFacilityCenter);
                incomingFacilityCenter.add(powerFacilityCenter);
            }
        }
        //处理补偿柜
        List<PowerCompensate> powerCompensateList = powerCompensateService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerCompensateList)) {
            for (PowerCompensate powerCompensate : powerCompensateList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerCompensate.getId());
                powerFacilityCenter.setPowerFacilityName(powerCompensate.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerCompensate.getCreateTime()));
                List<PowerDeviceInfo> deviceList = compensateDeviceService.getDeviceList(powerCompensate.getId());
                Integer status = 2;
                if (!CollectionUtils.isEmpty(deviceList)) {
                    for (PowerDeviceInfo powerDeviceInfo : deviceList) {
                        Long deviceId = powerDeviceInfo.getDeviceId();
                        Integer integer = deviceStatus.get(deviceId);
                        if (integer.equals(1)) {
                            status = 1;
                            break;
                        } else if (integer.equals(3)) {
                            status = 3;
                        }
                    }
                }
                powerFacilityCenter.setStatus(status);
                if (status.equals(1)) {
                    alarmList.add(powerFacilityCenter);
                } else if (status.equals(2) || status.equals(4)) {
                    offLineList.add(powerFacilityCenter);
                }
                powerFacilityCenter.setType(4);
                allLineList.add(powerFacilityCenter);
                compensateFacilityCenter.add(powerFacilityCenter);
            }

        }
        //处理滤波柜
        List<PowerWave> powerWaveList = powerWaveService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerWaveList)) {
            for (PowerWave powerWave : powerWaveList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerWave.getId());
                powerFacilityCenter.setPowerFacilityName(powerWave.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerWave.getCreateTime()));
                List<PowerDeviceInfo> deviceList = powerWaveDeviceService.getDeviceList(powerWave.getId());
                Integer status = 2;
                if (!CollectionUtils.isEmpty(deviceList)) {
                    for (PowerDeviceInfo powerDeviceInfo : deviceList) {
                        Long deviceId = powerDeviceInfo.getDeviceId();
                        Integer integer = deviceStatus.get(deviceId);
                        if (integer.equals(1)) {
                            status = 1;
                            break;
                        } else if (integer.equals(3)) {
                            status = 3;
                        }
                    }
                }

                powerFacilityCenter.setStatus(status);
                if (status.equals(1)) {
                    alarmList.add(powerFacilityCenter);
                } else if (status.equals(2) || status.equals(4)) {
                    offLineList.add(powerFacilityCenter);
                }
                powerFacilityCenter.setType(5);
                allLineList.add(powerFacilityCenter);
                waveFacilityCenter.add(powerFacilityCenter);
            }
        }
        //处理馈线柜
        List<PowerFeeder> powerFeederList = powerFeederService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerFeederList)) {
            for (PowerFeeder powerFeeder : powerFeederList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerFeeder.getId());
                powerFacilityCenter.setPowerFacilityName(powerFeeder.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerFeeder.getCreateTime()));
                List<PowerDeviceInfo> powerDeviceInfoList = powerFeederLoopDeviceService.getDeviceByFeederId(powerFeeder.getId());
                Integer status = 2;
                if (!CollectionUtils.isEmpty(powerDeviceInfoList)) {
                    for (PowerDeviceInfo powerDeviceInfo : powerDeviceInfoList) {
                        Long deviceId = powerDeviceInfo.getDeviceId();
                        Integer integer = deviceStatus.get(deviceId);
                        if (integer.equals(1)) {
                            status = 1;
                            break;
                        } else if (integer.equals(3)) {
                            status = 3;
                        }
                    }
                }
                powerFacilityCenter.setStatus(status);
                if (status.equals(1)) {
                    alarmList.add(powerFacilityCenter);
                } else if (status.equals(2) || status.equals(4)) {
                    offLineList.add(powerFacilityCenter);
                }
                allLineList.add(powerFacilityCenter);
                Integer loopType = powerFeeder.getLoopType();
                if (loopType.equals(0)) {
                    powerFacilityCenter.setType(6);
                    feederSimpleFacilityCenter.add(powerFacilityCenter);
                } else {
                    powerFacilityCenter.setType(7);
                    feederComplexFacilityCenter.add(powerFacilityCenter);
                }
            }
        }
        //配电箱
        List<PowerBox> powerBoxList = powerBoxService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerBoxList)) {
            for (PowerBox powerBox : powerBoxList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerBox.getId());
                powerFacilityCenter.setPowerFacilityName(powerBox.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerBox.getCreateTime()));
                List<PowerDeviceInfo> powerDeviceInfoList = powerBoxLoopDeviceService.getDeviceByBoxId(powerBox.getId());
                Integer status = 2;
                if (!CollectionUtils.isEmpty(powerDeviceInfoList)) {
                    for (PowerDeviceInfo powerDeviceInfo : powerDeviceInfoList) {
                        Long deviceId = powerDeviceInfo.getDeviceId();
                        Integer integer = deviceStatus.get(deviceId);
                        if (integer.equals(1)) {
                            status = 1;
                            break;
                        } else if (integer.equals(3)) {
                            status = 3;
                        }
                    }
                }
                powerFacilityCenter.setStatus(status);
                if (status.equals(1)) {
                    alarmList.add(powerFacilityCenter);
                } else if (status.equals(2) || status.equals(4)) {
                    offLineList.add(powerFacilityCenter);
                }
                powerFacilityCenter.setType(8);
                allLineList.add(powerFacilityCenter);
                boxFacilityCenter.add(powerFacilityCenter);
            }
        }
        //母联柜
        List<PowerMatriculation> powerMatriculationList = powerMatriculationService.findByPowerId(powerId);
        if (!CollectionUtils.isEmpty(powerMatriculationList)) {
            for (PowerMatriculation powerMatriculation : powerMatriculationList) {
                PowerFacilityCenter powerFacilityCenter = new PowerFacilityCenter();
                powerFacilityCenter.setPowerId(powerId);
                powerFacilityCenter.setPowerName(powerName);
                powerFacilityCenter.setPowerFacilityId(powerMatriculation.getId());
                powerFacilityCenter.setPowerFacilityName(powerMatriculation.getName());
                powerFacilityCenter.setCreateTime(simpleDateFormat.format(powerMatriculation.getCreateTime()));
                powerFacilityCenter.setStatus(powerMatriculation.getStatus().equals(0) ? 2 : 3);
                powerFacilityCenter.setType(9);
                allLineList.add(powerFacilityCenter);
                matriculationFacilityCenter.add(powerFacilityCenter);
            }
        }
        if (!CollectionUtils.isEmpty(alarmList)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("预警设备");
            powerFacilityCenterInfo.setContent(alarmList);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(offLineList)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("离线设备");
            powerFacilityCenterInfo.setContent(offLineList);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(allLineList)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("所有设备");
            powerFacilityCenterInfo.setContent(allLineList);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }

        if (!CollectionUtils.isEmpty(transformFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("变压器");
            powerFacilityCenterInfo.setContent(transformFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(generatorFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("发电机");
            powerFacilityCenterInfo.setContent(generatorFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(incomingFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("进线柜");
            powerFacilityCenterInfo.setContent(incomingFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(compensateFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("补偿柜");
            powerFacilityCenterInfo.setContent(compensateFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(waveFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("滤波柜");
            powerFacilityCenterInfo.setContent(waveFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(feederSimpleFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("单回路馈线柜");
            powerFacilityCenterInfo.setContent(feederSimpleFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(feederComplexFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("多回路馈线柜");
            powerFacilityCenterInfo.setContent(feederComplexFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(boxFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("配电箱");
            powerFacilityCenterInfo.setContent(boxFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        if (!CollectionUtils.isEmpty(matriculationFacilityCenter)) {
            PowerFacilityCenterInfo powerFacilityCenterInfo = new PowerFacilityCenterInfo();
            powerFacilityCenterInfo.setTitle("母联柜");
            powerFacilityCenterInfo.setContent(matriculationFacilityCenter);
            powerFacilityCenterInfoList.add(powerFacilityCenterInfo);
        }
        return powerFacilityCenterInfoList;
    }


    public List<PowerAlarmWarnVo> getMessage(Long powerId, Long deviceId, Integer type, Long id, Long loopId, String loopName) {
        List<PowerAlarmWarnVo> finalData = new ArrayList<>();

        Power powerInfo = powerService.getPowerInfo(powerId);
        Map<Long, String> areaMap = regionAreaService.getAreaMap(powerInfo.getProjectId());
        Map<Long, String> buildingMap = regionBuildingService.getBuildingMap(powerInfo.getProjectId());
        Map<Long, String> storeyMap = regionStoreyService.getStoreyMap(powerInfo.getProjectId());
        Map<Long, String> roomMap = regionRoomService.getRoomMap(powerInfo.getProjectId());
        EntityDto entityDto = new EntityDto();
        if (type.equals(1)) {
            PowerIncoming powerIncoming = powerIncomingService.getIncomingInfo(id);
            entityDto.setId(powerIncoming.getId());
            entityDto.setName(powerIncoming.getName());
        } else if (type.equals(2)) {
            PowerCompensate compensateInfo = powerCompensateService.getCompensateInfo(id);
            entityDto.setId(compensateInfo.getId());
            entityDto.setName(compensateInfo.getName());
        } else if (type.equals(3)) {
            PowerWave waveInfo = powerWaveService.getWaveInfo(id);
            entityDto.setId(waveInfo.getId());
            entityDto.setName(waveInfo.getName());
        } else if (type.equals(4) || type.equals(5)) {
            PowerFeeder feederInfo = powerFeederService.getFeederInfo(id);
            entityDto.setId(feederInfo.getId());
            entityDto.setName(feederInfo.getName());
        } else if (type.equals(6)) {
            PowerBox boxInfo = powerBoxService.getBoxInfo(id);
            entityDto.setId(boxInfo.getId());
            entityDto.setName(boxInfo.getName());
        }
        List<AlarmWarnPower> alarmWarnPowerList = alarmWarnService.getAlaramWarnByDeviceId(deviceId, 2000L);
        if (!CollectionUtils.isEmpty(alarmWarnPowerList)) {
            for (AlarmWarnPower alarmWarnPower : alarmWarnPowerList) {
                PowerAlarmWarnVo powerAlarmWarnVo = new PowerAlarmWarnVo();
                powerAlarmWarnVo.setPowerDeviceId(entityDto.getId());
                powerAlarmWarnVo.setPowerDeviceName(entityDto.getName());
                powerAlarmWarnVo.setPowerId(powerInfo.getId());
                powerAlarmWarnVo.setPowerName(powerInfo.getName());
                powerAlarmWarnVo.setLoopId(loopId);
                powerAlarmWarnVo.setLoopName(loopName);
                powerAlarmWarnVo.setAlarmWarnId(alarmWarnPower.getId());
                powerAlarmWarnVo.setAlarmItemId(alarmWarnPower.getAlarmItemId());
                powerAlarmWarnVo.setAlarmItemName(alarmWarnPower.getAlarmItemName());
                powerAlarmWarnVo.setCreateTime(alarmWarnPower.getCreateTime());
                String location = alarmWarnPower.getLocation();
                if (org.springframework.util.StringUtils.isEmpty(location)) {
                    StringBuffer strBuf = new StringBuffer();
                    Long areaId = alarmWarnPower.getAreaId();
                    Long buildingId = alarmWarnPower.getBuildingId();
                    Long storeyId = alarmWarnPower.getStoreyId();
                    Long roomId = alarmWarnPower.getRoomId();
                    if (areaId != null) {
                        strBuf.append(areaMap.get(areaId));
                    }
                    if (buildingId != null) {
                        strBuf.append(buildingMap.get(buildingId));
                    }
                    if (storeyId != null) {
                        strBuf.append(storeyMap.get(storeyId));
                    }
                    if (roomId != null) {
                        strBuf.append(roomMap.get(roomId));
                    }
                    powerAlarmWarnVo.setLocation(strBuf.toString());
                } else {
                    powerAlarmWarnVo.setLocation(location);
                }
                finalData.add(powerAlarmWarnVo);

            }

        }
        if (!CollectionUtils.isEmpty(finalData)) {
            return finalData;
        } else {
            return null;
        }

    }

    @Override
    public List<DeviceTypeAlarmStatistic> alarmStatistic(Integer type, Long id, Object loopIdObj) {
        List<PowerDeviceInfo> deviceList = null;
        if (type.equals(3)) {
            deviceList = powerIncomingDeviceService.getDeviceList(id);
        } else if (type.equals(4)) {
            deviceList = powerCompensateDeviceService.getDeviceList(id);
        } else if (type.equals(5)) {
            deviceList = powerWaveDeviceService.getDeviceList(id);
        } else if (type.equals(6)) {
            List<PowerFeederLoop> loopList = powerFeederLoopService.getLoopList(id);
            PowerFeederLoop powerFeederLoopTotal = null;
            for (PowerFeederLoop powerFeederLoop : loopList) {
                Integer totalFlag = powerFeederLoop.getTotalFlag();
                if (totalFlag.equals(1)) {
                    powerFeederLoopTotal = powerFeederLoop;
                    break;
                }
            }
            deviceList = powerFeederLoopDeviceService.getDeviceList(powerFeederLoopTotal.getId());
        } else if (type.equals(7)) {
            Long loopId = Long.parseLong(loopIdObj.toString());
            deviceList = powerFeederLoopDeviceService.getDeviceList(loopId);
        } else if (type.equals(8)) {
            Long loopId = Long.parseLong(loopIdObj.toString());
            deviceList = powerBoxLoopDeviceService.getDeviceList(loopId);
        }
        if (!CollectionUtils.isEmpty(deviceList)) {
            List<Map<String, Object>> alarmMap = alarmWarnService.getPowerAlarmCount(deviceList);
            if (!CollectionUtils.isEmpty(alarmMap)) {
                List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics = new ArrayList<>();
                for (Map<String, Object> stringObjectMap : alarmMap) {
                    Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                    String itemName = stringObjectMap.get("itemName").toString();
                    DeviceTypeAlarmStatistic deviceTypeAlarmStatistic = new DeviceTypeAlarmStatistic(itemName, num);
                    deviceTypeAlarmStatistics.add(deviceTypeAlarmStatistic);
                }
                return deviceTypeAlarmStatistics;
            }
        }
        return null;
    }

    @Override
    public List<AlarmWarnItemVo> alarmHistory(Integer type, Long id, Object loopObj) {
        List<PowerDeviceInfo> deviceList = null;
        if (type.equals(3)) {
            //进线柜
            deviceList = powerIncomingDeviceService.getDeviceList(id);
        } else if (type.equals(4)) {
            //补偿柜
            deviceList = powerCompensateDeviceService.getDeviceList(id);
        } else if (type.equals(5)) {
            //滤波柜
            deviceList = powerWaveDeviceService.getDeviceList(id);
        } else if (type.equals(6)) {
            //单回路馈线柜
            List<PowerFeederLoop> loopList = powerFeederLoopService.getLoopList(id);
            PowerFeederLoop powerFeederLoopTotal = null;
            for (PowerFeederLoop powerFeederLoop : loopList) {
                Integer totalFlag = powerFeederLoop.getTotalFlag();
                if (totalFlag.equals(1)) {
                    powerFeederLoopTotal = powerFeederLoop;
                    break;
                }
            }
            deviceList = powerFeederLoopDeviceService.getDeviceList(powerFeederLoopTotal.getId());
        } else if (type.equals(7)) {
            //多回路馈线柜
            Long loopId = Long.parseLong(loopObj.toString());
            deviceList = powerFeederLoopDeviceService.getDeviceList(loopId);
        } else if (type.equals(8)) {
            //配电箱
            Long loopId = Long.parseLong(loopObj.toString());
            deviceList = powerBoxLoopDeviceService.getDeviceList(loopId);
        }
        if (!CollectionUtils.isEmpty(deviceList)) {
            List<AlarmWarnItemVo> alarmWarnItemVoList = alarmWarnService.getPowerHistory(deviceList);
            if (!CollectionUtils.isEmpty(alarmWarnItemVoList)) {
                return alarmWarnItemVoList;
            }
        }
        return null;
    }

    @Override
    public String getLocation(Long deviceId) {
        Device device = deviceService.findById(deviceId);
        if (device != null) {
            String location = device.getLocation();
            if (StringUtils.isEmpty(location)) {
                StringBuffer locationBuff = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuff.append(area.getName());
                        locationBuff.append("-");
                    }
                }
                if (buildingId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuff.append(building.getName());
                        locationBuff.append("-");
                    }
                }
                if (storeyId != null) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(storeyId);
                    if (storey != null) {
                        locationBuff.append(storey.getName());
                        locationBuff.append("-");
                    }
                }
                if (roomId != null) {
                    Room room = regionRoomService.selectByPrimaryKey(roomId);
                    if (room != null) {
                        locationBuff.append(room.getName());
                    }
                }
                if (!StringUtils.isEmpty(locationBuff.toString())) {
                    return locationBuff.toString();
                }
            } else {
                return location;
            }
        }
        return null;
    }

    @Override
    public DeviceDataSmartElecFloatDto getFloatData(DeviceDataSmartElec deviceDataSmartElec) {

        if (deviceDataSmartElec != null) {

            BigDecimal bigDeQian = new BigDecimal("1000");
            BigDecimal bigDeBai = new BigDecimal("100");
            BigDecimal bigDeShi = new BigDecimal("10");

            DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
            Long deviceId = deviceDataSmartElec.getDeviceId();

            String ampRmsA = deviceDataSmartElec.getAmpRmsA();
            String ampRmsB = deviceDataSmartElec.getAmpRmsB();
            String ampRmsC = deviceDataSmartElec.getAmpRmsC();

            String voltRmsA = deviceDataSmartElec.getVoltRmsA();
            String voltRmsB = deviceDataSmartElec.getVoltRmsB();
            String voltRmsC = deviceDataSmartElec.getVoltRmsC();

            String activePowerA = deviceDataSmartElec.getActivePowerA();
            String activePowerB = deviceDataSmartElec.getActivePowerB();
            String activePowerC = deviceDataSmartElec.getActivePowerC();

            String reactivePowerA = deviceDataSmartElec.getReactivePowerA();
            String reactivePowerB = deviceDataSmartElec.getReactivePowerB();
            String reactivePowerC = deviceDataSmartElec.getReactivePowerC();

            String powerFactorA = deviceDataSmartElec.getPowerFactorA();
            String powerFactorB = deviceDataSmartElec.getPowerFactorA();
            String powerFactorC = deviceDataSmartElec.getPowerFactorA();
            String thdiA = deviceDataSmartElec.getThdiA();
            String thdiB = deviceDataSmartElec.getThdiB();
            String thdiC = deviceDataSmartElec.getThdiC();
            String thdvA = deviceDataSmartElec.getThdvA();
            String thdvB = deviceDataSmartElec.getThdvB();
            String thdvC = deviceDataSmartElec.getThdvC();

            if (StringUtils.isBlank(thdiA) || thdiA.equals("0")) {
                deviceDataSmartElecFloatDto.setThdiA(0f);
            } else {
                thdiA = thdiA.replace("%", "");
                BigDecimal thdiABig = new BigDecimal(thdiA);
                BigDecimal divide = thdiABig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdiA(divide.floatValue());
            }

            if (StringUtils.isBlank(thdiB) || thdiB.equals("0")) {
                deviceDataSmartElecFloatDto.setThdiB(0f);
            } else {
                thdiB = thdiB.replace("%", "");
                BigDecimal thdiBBig = new BigDecimal(thdiB);
                BigDecimal divide = thdiBBig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdiB(divide.floatValue());
            }

            if (StringUtils.isBlank(thdiC) || thdiC.equals("0")) {
                deviceDataSmartElecFloatDto.setThdiC(0f);
            } else {
                thdiC = thdiC.replace("%", "");
                BigDecimal thdiCBig = new BigDecimal(thdiC);
                BigDecimal divide = thdiCBig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdiC(divide.floatValue());
            }

            if (StringUtils.isBlank(thdvA) || thdvA.equals("0")) {
                deviceDataSmartElecFloatDto.setThdvA(0f);
            } else {
                thdvA = thdvA.replace("%", "");
                BigDecimal thdvABig = new BigDecimal(thdvA);
                BigDecimal divide = thdvABig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdvA(divide.floatValue());
            }

            if (StringUtils.isBlank(thdvB) || thdvB.equals("0")) {
                deviceDataSmartElecFloatDto.setThdvB(0f);
            } else {
                thdvB = thdvB.replace("%", "");
                BigDecimal thdvBBig = new BigDecimal(thdvB);
                BigDecimal divide = thdvBBig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdvB(divide.floatValue());
            }

            if (StringUtils.isBlank(thdvC) || thdvC.equals("0")) {
                deviceDataSmartElecFloatDto.setThdvC(0f);
            } else {
                thdvC = thdvC.replace("%", "");
                BigDecimal thdvCBig = new BigDecimal(thdvC);
                BigDecimal divide = thdvCBig.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setThdvC(divide.floatValue());
            }
            if (StringUtils.isEmpty(ampRmsA) || ampRmsA.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsA(0f);
            } else {
                BigDecimal bigDeAmpRmsA = new BigDecimal(ampRmsA);
                BigDecimal divide = bigDeAmpRmsA.divide(bigDeQian, 3, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsA(divide.floatValue());
            }

            if (StringUtils.isEmpty(ampRmsB) || ampRmsB.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsB(0f);
            } else {
                BigDecimal bigDeAmpRmsB = new BigDecimal(ampRmsB);
                BigDecimal divide = bigDeAmpRmsB.divide(bigDeQian, 3, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsB(divide.floatValue());
            }

            if (StringUtils.isEmpty(ampRmsC) || ampRmsC.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsC(0f);
            } else {
                BigDecimal bigDeAmpRmsC = new BigDecimal(ampRmsC);
                BigDecimal divide = bigDeAmpRmsC.divide(bigDeQian, 3, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsC(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsA) || voltRmsA.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsA(0f);
            } else {
                BigDecimal bigDeVoltRmsA = new BigDecimal(voltRmsA);
                BigDecimal divide = bigDeVoltRmsA.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsA(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsB) || voltRmsB.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsB(0f);
            } else {
                BigDecimal bigDeVoltRmsB = new BigDecimal(voltRmsB);
                BigDecimal divide = bigDeVoltRmsB.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsB(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsC) || voltRmsC.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsC(0f);
            } else {
                BigDecimal bigDeVoltRmsC = new BigDecimal(voltRmsC);
                BigDecimal divide = bigDeVoltRmsC.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsC(divide.floatValue());
            }
            if (StringUtils.isEmpty(activePowerA) || activePowerA.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerA(0f);
            } else {
                BigDecimal bigDeActivePowerA = new BigDecimal(activePowerA);
                BigDecimal divide = bigDeActivePowerA.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setActivePowerA(divide.floatValue());
            }

            if (StringUtils.isEmpty(activePowerB) || activePowerB.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerB(0f);
            } else {
                BigDecimal bigDeActivePowerB = new BigDecimal(activePowerB);
                BigDecimal divide = bigDeActivePowerB.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setActivePowerB(divide.floatValue());
            }

            if (StringUtils.isEmpty(activePowerC) || activePowerC.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerC(0f);
            } else {
                BigDecimal bigDeActivePowerC = new BigDecimal(activePowerC);
                BigDecimal divide = bigDeActivePowerC.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setActivePowerC(divide.floatValue());
            }

            if (StringUtils.isEmpty(reactivePowerA) || reactivePowerA.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerA(0f);
            } else {
                BigDecimal bigDeReactivePowerA = new BigDecimal(reactivePowerA);
                BigDecimal divide = bigDeReactivePowerA.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setReactivePowerA(divide.floatValue());
            }

            if (StringUtils.isEmpty(reactivePowerB) || reactivePowerB.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerB(0f);
            } else {
                BigDecimal bigDeReactivePowerB = new BigDecimal(reactivePowerB);
                BigDecimal divide = bigDeReactivePowerB.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setReactivePowerB(divide.floatValue());
            }

            if (StringUtils.isEmpty(reactivePowerC) || reactivePowerC.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerC(0f);
            } else {
                BigDecimal bigDeReactivePowerC = new BigDecimal(reactivePowerC);
                BigDecimal divide = bigDeReactivePowerC.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setReactivePowerC(divide.floatValue());
            }

            if (StringUtils.isEmpty(powerFactorA) || powerFactorA.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorA(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorA(Integer.parseInt(powerFactorA));
            }

            if (StringUtils.isEmpty(powerFactorB) || powerFactorB.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorB(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorB(Integer.parseInt(powerFactorB));
            }

            if (StringUtils.isEmpty(powerFactorC) || powerFactorC.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorC(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorC(Integer.parseInt(powerFactorC));
            }
            deviceDataSmartElecFloatDto.setDeviceId(deviceId);
            return deviceDataSmartElecFloatDto;
        }
        return null;
    }

    @Override
    public DeviceDataSmartElecFloatDto getSuperFloatData(DeviceDataSmartSuper deviceDataSmartSuper) {
        if (deviceDataSmartSuper != null) {

            BigDecimal bigDeBai = new BigDecimal("100");
            BigDecimal bigDeShi = new BigDecimal("10");

            DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto = new DeviceDataSmartElecFloatDto();
            Long deviceId = deviceDataSmartSuper.getDeviceId();

            String ampRmsA = deviceDataSmartSuper.getAmpRmsA();
            String ampRmsB = deviceDataSmartSuper.getAmpRmsB();
            String ampRmsC = deviceDataSmartSuper.getAmpRmsC();

            String voltRmsA = deviceDataSmartSuper.getVoltRmsA();
            String voltRmsB = deviceDataSmartSuper.getVoltRmsB();
            String voltRmsC = deviceDataSmartSuper.getVoltRmsC();

            String activePowerA = deviceDataSmartSuper.getActivePowerA();
            String activePowerB = deviceDataSmartSuper.getActivePowerB();
            String activePowerC = deviceDataSmartSuper.getActivePowerC();

            String reactivePowerA = deviceDataSmartSuper.getReactivePowerA();
            String reactivePowerB = deviceDataSmartSuper.getReactivePowerB();
            String reactivePowerC = deviceDataSmartSuper.getReactivePowerC();

            String powerFactorA = deviceDataSmartSuper.getPowerFactorA();
            String powerFactorB = deviceDataSmartSuper.getPowerFactorA();
            String powerFactorC = deviceDataSmartSuper.getPowerFactorA();
            String thdiA = deviceDataSmartSuper.getThdiA();
            String thdiB = deviceDataSmartSuper.getThdiB();
            String thdiC = deviceDataSmartSuper.getThdiC();
            String thdvA = deviceDataSmartSuper.getThdvA();
            String thdvB = deviceDataSmartSuper.getThdvB();
            String thdvC = deviceDataSmartSuper.getThdvC();

            if (StringUtils.isBlank(thdiA) || thdiA.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdiA(0f);
            } else {
                String replace = thdiA.replace("%", "");
                deviceDataSmartElecFloatDto.setThdiA(Float.parseFloat(replace));
            }
            if (StringUtils.isBlank(thdiB) || thdiB.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdiB(0f);
            } else {
                String replace = thdiB.replace("%", "");
                deviceDataSmartElecFloatDto.setThdiB(Float.parseFloat(replace));
            }
            if (StringUtils.isBlank(thdiC) || thdiC.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdiC(0f);
            } else {
                String replace = thdiC.replace("%", "");
                deviceDataSmartElecFloatDto.setThdiC(Float.parseFloat(replace));
            }
            if (StringUtils.isBlank(thdvA) || thdvA.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdvA(0f);
            } else {
                String replace = thdvA.replace("%", "");
                deviceDataSmartElecFloatDto.setThdvA(Float.parseFloat(replace));
            }
            if (StringUtils.isBlank(thdvB) || thdvB.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdvB(0f);
            } else {
                String replace = thdvB.replace("%", "");
                deviceDataSmartElecFloatDto.setThdvB(Float.parseFloat(replace));
            }
            if (StringUtils.isBlank(thdvC) || thdvC.equals("0.0%")) {
                deviceDataSmartElecFloatDto.setThdvC(0f);
            } else {
                String replace = thdvC.replace("%", "");
                deviceDataSmartElecFloatDto.setThdvC(Float.parseFloat(replace));
            }


            if (StringUtils.isEmpty(ampRmsA) || ampRmsA.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsA(0f);
            } else {
                BigDecimal bigDeAmpRmsA = new BigDecimal(ampRmsA);
                BigDecimal divide = bigDeAmpRmsA.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsA(divide.floatValue());
            }

            if (StringUtils.isEmpty(ampRmsB) || ampRmsB.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsB(0f);
            } else {
                BigDecimal bigDeAmpRmsB = new BigDecimal(ampRmsB);
                BigDecimal divide = bigDeAmpRmsB.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsB(divide.floatValue());
            }

            if (StringUtils.isEmpty(ampRmsC) || ampRmsC.equals("0")) {
                deviceDataSmartElecFloatDto.setAmpRmsC(0f);
            } else {
                BigDecimal bigDeAmpRmsC = new BigDecimal(ampRmsC);
                BigDecimal divide = bigDeAmpRmsC.divide(bigDeBai, 2, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setAmpRmsC(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsA) || voltRmsA.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsA(0f);
            } else {
                BigDecimal bigDeVoltRmsA = new BigDecimal(voltRmsA);
                BigDecimal divide = bigDeVoltRmsA.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsA(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsB) || voltRmsB.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsB(0f);
            } else {
                BigDecimal bigDeVoltRmsB = new BigDecimal(voltRmsB);
                BigDecimal divide = bigDeVoltRmsB.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsB(divide.floatValue());
            }

            if (StringUtils.isEmpty(voltRmsC) || voltRmsC.equals("0")) {
                deviceDataSmartElecFloatDto.setVoltRmsC(0f);
            } else {
                BigDecimal bigDeVoltRmsC = new BigDecimal(voltRmsC);
                BigDecimal divide = bigDeVoltRmsC.divide(bigDeShi, 1, BigDecimal.ROUND_HALF_UP);
                deviceDataSmartElecFloatDto.setVoltRmsC(divide.floatValue());
            }
            if (StringUtils.isEmpty(activePowerA) || activePowerA.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerA(0f);
            } else {
                deviceDataSmartElecFloatDto.setActivePowerA(Float.parseFloat(activePowerA));
            }

            if (StringUtils.isEmpty(activePowerB) || activePowerB.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerB(0f);
            } else {
                deviceDataSmartElecFloatDto.setActivePowerB(Float.parseFloat(activePowerB));
            }

            if (StringUtils.isEmpty(activePowerC) || activePowerC.equals("0")) {
                deviceDataSmartElecFloatDto.setActivePowerC(0f);
            } else {
                deviceDataSmartElecFloatDto.setActivePowerC(Float.parseFloat(activePowerC));
            }

            if (StringUtils.isEmpty(reactivePowerA) || reactivePowerA.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerA(0f);
            } else {
                deviceDataSmartElecFloatDto.setReactivePowerA(Float.parseFloat(reactivePowerA));
            }

            if (StringUtils.isEmpty(reactivePowerB) || reactivePowerB.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerB(0f);
            } else {
                deviceDataSmartElecFloatDto.setReactivePowerB(Float.parseFloat(reactivePowerB));
            }

            if (StringUtils.isEmpty(reactivePowerC) || reactivePowerC.equals("0")) {
                deviceDataSmartElecFloatDto.setReactivePowerC(0f);
            } else {
                deviceDataSmartElecFloatDto.setReactivePowerC(Float.parseFloat(reactivePowerC));
            }

            if (StringUtils.isEmpty(powerFactorA) || powerFactorA.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorA(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorA(Integer.parseInt(powerFactorA));
            }

            if (StringUtils.isEmpty(powerFactorB) || powerFactorB.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorB(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorB(Integer.parseInt(powerFactorB));
            }

            if (StringUtils.isEmpty(powerFactorC) || powerFactorC.equals("0")) {
                deviceDataSmartElecFloatDto.setPowerFactorC(0);
            } else {
                deviceDataSmartElecFloatDto.setPowerFactorC(Integer.parseInt(powerFactorC));
            }
            deviceDataSmartElecFloatDto.setDeviceId(deviceId);
            return deviceDataSmartElecFloatDto;
        }
        return null;
    }

    @Override
    public Map<String, String[][]> getAlarmDownLoadData(Long projectId) {
        List<AlarmWarnItemVo> voList = warnDao.selectProjectAlarm(projectId, 2000l, null);
        List<Power> powerList = powerService.selectByProjectId(projectId);
        Map<Long, String> powerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(powerList)) {
            for (Power power : powerList) {
                powerMap.put(power.getId(), power.getName());
            }
        }
        if (!CollectionUtils.isEmpty(voList)) {
            String[][] strings = new String[voList.size()][7];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < voList.size(); i++) {
                AlarmWarnItemVo alarmWarnItemVo = voList.get(i);
                String format = simpleDateFormat.format(alarmWarnItemVo.getCreateTime());
                Integer bindingType = alarmWarnItemVo.getBindingType();
                if (bindingType.equals(0)) {
                    PowerDevice powerDevice = powerDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    strings[i][0] = powerMap.get(powerDevice.getPowerId());
                    strings[i][1] = "---";
                    strings[i][2] = "---";
                } else if (bindingType.equals(1)) {
                    PowerIncomingDevice device = powerIncomingDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    PowerIncoming powerIncoming = powerIncomingService.getIncomingInfo(device.getIncomingId());
                    strings[i][0] = powerMap.get(device.getPowerId());
                    strings[i][1] = powerIncoming.getName();
                    strings[i][2] = "---";
                } else if (bindingType.equals(2)) {
                    PowerCompensateDevice device = powerCompensateDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    PowerCompensate compensateInfo = powerCompensateService.getCompensateInfo(device.getCompensateId());
                    strings[i][0] = powerMap.get(device.getPowerId());
                    strings[i][1] = compensateInfo.getName();
                    strings[i][2] = "---";
                } else if (bindingType.equals(3)) {
                    PowerWaveDevice device = powerWaveDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    PowerWave waveInfo = powerWaveService.getWaveInfo(device.getWaveId());
                    strings[i][0] = powerMap.get(device.getPowerId());
                    strings[i][1] = waveInfo.getName();
                    strings[i][2] = "---";
                } else if (bindingType.equals(4)) {
                    PowerFeederLoopDevice device = powerFeederLoopDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    PowerFeeder feederInfo = powerFeederService.getFeederInfo(device.getFeederId());
                    PowerFeederLoop feederLoopInfo = powerFeederLoopService.getFeederLoopInfo(device.getFeederLoopId());
                    strings[i][0] = powerMap.get(device.getPowerId());
                    strings[i][1] = feederInfo.getName();
                    strings[i][2] = feederLoopInfo.getName();

                } else if (bindingType.equals(5)) {
                    PowerBoxLoopDevice device = powerBoxLoopDeviceService.getInfoByDeviceId(alarmWarnItemVo.getDeviceId());
                    PowerBox boxInfo = powerBoxService.getBoxInfo(device.getBoxId());
                    PowerBoxLoop boxLoopInfo = powerBoxLoopService.getBoxLoopInfo(device.getBoxLoopId());
                    strings[i][0] = powerMap.get(device.getPowerId());
                    strings[i][1] = boxInfo.getName();
                    strings[i][2] = boxLoopInfo.getName();
                }
                strings[i][3] = alarmWarnItemVo.getAlarmItemName();
                strings[i][4] = format;
                strings[i][5] = alarmWarnItemVo.getHandleFlag().equals(0) ? "未处理" : "已处理";
                Integer status = alarmWarnItemVo.getStatus();
                if (status.equals(2) || status.equals(4)) {
                    strings[i][6] = "离线";
                } else if (status.equals(1)) {
                    strings[i][6] = "告警中";
                } else {
                    strings[i][6] = "在线";
                }
            }
            Map<String, String[][]> dataMap = new HashMap<>();
            dataMap.put("电房告警历史信息", strings);
            return dataMap;

        }
        return null;
    }

    @Override
    public PowerDeviceCount getPowerCountInfo(Long projectId) {
        List<Power> powerList = powerDao.selectByProjectId(projectId);
        if (!CollectionUtils.isEmpty(powerList)) {
            Integer totalTotal = 0;
            List<Map<String, Object>> dataList = new ArrayList<>();
            for (Power power : powerList) {
                String powerName = power.getName();
                Long incomingCount = powerIncomingService.selectCountByPowerId(power.getId());
                //馈线柜数量
                Long feederCount = powerFeederService.selectCountByPowerId(power.getId());
                //滤波柜数量
                Long waveCount = powerWaveService.selectCountByPowerId(power.getId());
                //补偿柜数量
                Long compensateCount = powerCompensateService.selectCountByPowerId(power.getId());
                //母联柜数量
                Long matriculationCount = powerMatriculationService.selectCountByPowerId(power.getId());
                if (incomingCount == null) {
                    incomingCount = 0l;
                }
                if (feederCount == null) {
                    feederCount = 0l;
                }
                if (waveCount == null) {
                    waveCount = 0l;
                }
                if (compensateCount == null) {
                    compensateCount = 0l;
                }
                if (matriculationCount == null) {
                    matriculationCount = 0l;
                }
                Long total = incomingCount + feederCount + waveCount + compensateCount + matriculationCount;
                Integer totalCount = Integer.parseInt(total.toString());
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("powerName", powerName);
                dataMap.put("count", totalCount);
                dataList.add(dataMap);
                totalTotal = totalTotal + totalCount;
            }
            PowerDeviceCount powerDeviceCount = new PowerDeviceCount();
            powerDeviceCount.setTotal(totalTotal);
            powerDeviceCount.setPowerDevice(dataList);
            return powerDeviceCount;
        }
        return null;
    }

    @Override
    public PowerDeviceStatus getPowerDeviceStatus(Long projectId) {
        List<Map<String, Object>> dataList = deviceService.getPowerDeviceStatus(projectId);
        PowerDeviceStatus powerDeviceStatus = new PowerDeviceStatus(0, 0, 0, "0");
        if (!CollectionUtils.isEmpty(dataList)) {
            Integer inline = 0;
            Integer outline = 0;
            Integer total = 0;
            for (Map<String, Object> stringObjectMap : dataList) {
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                if (status.equals(2) || status.equals(4)) {
                    outline++;
                } else {
                    inline++;
                }
                total++;
            }
            if (inline.equals(0)) {
                powerDeviceStatus.setInline(0);
                powerDeviceStatus.setOutLine(outline);
                powerDeviceStatus.setTotal(total);
            } else if (inline.equals(total)) {
                powerDeviceStatus.setInline(inline);
                powerDeviceStatus.setTotal(total);
                powerDeviceStatus.setPercent("100");
            } else {
                Integer inlineBai = inline * 100;
                BigDecimal bigDecimal = new BigDecimal(inlineBai);
                BigDecimal totalBig = new BigDecimal(total);
                BigDecimal divide = bigDecimal.divide(totalBig, 1, BigDecimal.ROUND_HALF_UP);
                powerDeviceStatus.setInline(inline);
                powerDeviceStatus.setOutLine(outline);
                powerDeviceStatus.setTotal(total);
                powerDeviceStatus.setPercent(divide.toString());
            }
        }
        return powerDeviceStatus;
    }

    @Override
    public List<PowerDataInfo> getPowerDataInfo(Long projectId) {
        List<Power> powers = powerDao.selectByProjectId(projectId);
        List<PowerDataInfo> powerDataInfoList = new ArrayList<>();
        for (Power power : powers) {
            DataTemperaturehumidity humidity = powerDeviceService.selectPowerTemperatureHumidity(power.getId());
            PowerDataInfo powerDataInfo = new PowerDataInfo();
            powerDataInfo.setPowerId(power.getId());
            powerDataInfo.setPowerName(power.getName());
            powerDataInfo.setTemperature(humidity.getTemperature());
            powerDataInfo.setHumidity(humidity.getHumidity());
            powerDataInfoList.add(powerDataInfo);
        }
        return powerDataInfoList;
    }

    @Override
    public List<DeviceTypeAlarmStatistic> getPowerAlarmStatistic(Long projectId, Long powerId) {
        Set<Long> deviceSet = new HashSet<>();
        List<Long> incomingDeviceList = powerIncomingDeviceService.getPowerDeviceList(projectId, powerId);
        List<Long> boxDeviceList = powerBoxLoopDeviceService.getPowerDeviceList(projectId, powerId);
        List<Long> compensateDeviceList = powerCompensateDeviceService.getPowerDeviceList(projectId, powerId);
        List<Long> feederDeviceList = powerFeederLoopDeviceService.getPowerDeviceList(projectId, powerId);
        List<Long> waveDeviceList = powerWaveDeviceService.getPowerDeviceList(projectId, powerId);
        if (!CollectionUtils.isEmpty(incomingDeviceList)) {
            deviceSet.addAll(incomingDeviceList);
        }
        if (!CollectionUtils.isEmpty(boxDeviceList)) {
            deviceSet.addAll(boxDeviceList);
        }
        if (!CollectionUtils.isEmpty(compensateDeviceList)) {
            deviceSet.addAll(compensateDeviceList);
        }
        if (!CollectionUtils.isEmpty(feederDeviceList)) {
            deviceSet.addAll(feederDeviceList);
        }
        if (!CollectionUtils.isEmpty(waveDeviceList)) {
            deviceSet.addAll(waveDeviceList);
        }
        List<Map<String, Object>> dataMap = alarmWarnService.getDeviceTypeStatistic(deviceSet);
        if (!CollectionUtils.isEmpty(dataMap)) {
            List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : dataMap) {
                String alarmItemName = stringObjectMap.get("alarmItemName").toString();
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic(alarmItemName, count));
            }
            return deviceTypeAlarmStatistics;
        }
        return null;
    }

    @Override
    public List<DeviceTypeAlarmStatistic> getPowerEquipment(Long projectId, Long powerId) {
        //进线柜数量
        Long incomingCount = powerIncomingService.selectCountByPowerId(powerId);
        //补偿柜数量
        Long compensateCount = powerCompensateService.selectCountByPowerId(powerId);
        //滤波柜数量
        Long waveCount = powerWaveService.selectCountByPowerId(powerId);
        //馈线柜数量
        Long feederCount = powerFeederService.selectCountByPowerId(powerId);
        //配电箱数量
        Long boxCount = powerBoxService.selectCountByPowerId(powerId);
        List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics = new ArrayList<>();
        if (incomingCount != null) {
            deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("进线柜", Integer.parseInt(incomingCount.toString())));
        }
        if (compensateCount != null) {
            deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("补偿柜", Integer.parseInt(compensateCount.toString())));
        }
        if (waveCount != null) {
            deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("滤波柜", Integer.parseInt(waveCount.toString())));
        }
        if (feederCount != null) {
            deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("馈线柜", Integer.parseInt(feederCount.toString())));
        }
        if (boxCount != null) {
            deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("配电箱", Integer.parseInt(boxCount.toString())));
        }
        return deviceTypeAlarmStatistics;
    }

    @Override
    public PowerRecentInfo getPowerBaseInfo(Long powerId) {
        List<PowerTransformer> list = powerTransformerService.selectByPowerId(powerId);
        if (!CollectionUtils.isEmpty(list)) {
            Long transformerId = null;
            for (PowerTransformer powerTransformer : list) {
                Integer status = powerTransformer.getStatus();
                if (status.equals(1)) {
                    transformerId = powerTransformer.getId();
                }
            }
            if (transformerId != null) {
                List<PowerIncoming> powerIncomingList = powerIncomingService.getIncomingList(transformerId);
                if (CollectionUtils.isEmpty(powerIncomingList)) {
                    PowerIncoming powerIncoming = powerIncomingList.get(0);
                    List<PowerDeviceInfo> powerDeviceInfos = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
                    if (CollectionUtils.isEmpty(powerDeviceInfos)) {
                        PowerDeviceInfo powerDeviceInfoFinal = null;
                        for (PowerDeviceInfo powerDeviceInfo : powerDeviceInfos) {
                            Long deviceTypeId = powerDeviceInfo.getDeviceTypeId();
                            Integer status = powerDeviceInfo.getStatus();
                            Boolean deviceStatus = status.equals(1) || status.equals(3);
                            Boolean deviceTypeStatus = deviceTypeId.equals(6l) || deviceTypeId.equals(13l);
                            if (deviceStatus && deviceTypeStatus) {
                                powerDeviceInfoFinal = powerDeviceInfo;
                            }
                        }
                        if (powerDeviceInfoFinal != null) {
                            Long deviceTypeId = powerDeviceInfoFinal.getDeviceTypeId();
                            Long deviceId = powerDeviceInfoFinal.getDeviceId();
                            LocalDate now = LocalDate.now();
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String format = dateTimeFormatter.format(now);
                            List<String> dayHour = DateUtils.getDayHour();
                            Map<String, DeviceHourDataSmartElec> deviceHourDataSmartElecMap = new HashMap<>();
                            List<DeviceHourDataSmartElec> deviceHourDataSmartElecList = null;
                            if (deviceTypeId.equals(6l)) {
                                deviceHourDataSmartElecList = deviceHourDataSmartElecService.getDayData(deviceId, format);
                            } else {
                                List<DeviceHourDataSmartSuper> deviceHourDataSmartSupers = deviceHourDataSmartSuperService.getDayData(deviceId, format);
                                deviceHourDataSmartElecList = getSmartElec(deviceHourDataSmartSupers);
                            }
                            if (!CollectionUtils.isEmpty(deviceHourDataSmartElecList)) {
                                for (DeviceHourDataSmartElec deviceHourDataSmartElec : deviceHourDataSmartElecList) {
                                    String dateTimeNow = deviceHourDataSmartElec.getDateTimeNow();
                                    String[] s = dateTimeNow.split(" ");
                                    String[] split = s[1].split(":");
                                    String time = split[0];
                                    if (time.equals("00")) {
                                        time = "24";
                                    }
                                    deviceHourDataSmartElecMap.put(time, deviceHourDataSmartElec);
                                }
                            }
                            List<String> dianYaA = new ArrayList<>();
                            List<String> dianYaB = new ArrayList<>();
                            List<String> dianYaC = new ArrayList<>();

                            List<String> dianLiuA = new ArrayList<>();
                            List<String> dianLiuB = new ArrayList<>();
                            List<String> dianLiuC = new ArrayList<>();

                            List<String> activePowerA = new ArrayList<>();
                            List<String> activePowerB = new ArrayList<>();
                            List<String> activePowerC = new ArrayList<>();

                            List<String> reactivePowerA = new ArrayList<>();
                            List<String> reactivePowerB = new ArrayList<>();
                            List<String> reactivePowerC = new ArrayList<>();
                            BigDecimal bigBai = new BigDecimal("100");
                            BigDecimal bigQian = new BigDecimal("1000");
                            BigDecimal bigShi = new BigDecimal("10");
                            PowerRecentInfo powerRecentInfo = new PowerRecentInfo();
                            for (String time : dayHour) {
                                if (deviceHourDataSmartElecMap.containsKey(time)) {
                                    DeviceHourDataSmartElec deviceHourDataSmartElec = deviceHourDataSmartElecMap.get(time);
                                    //处理电压
                                    String voltRmsA = deviceHourDataSmartElec.getVoltRmsA();
                                    String voltRmsB = deviceHourDataSmartElec.getVoltRmsB();
                                    String voltRmsC = deviceHourDataSmartElec.getVoltRmsC();
                                    //获取电流
                                    String ampRmsA = deviceHourDataSmartElec.getAmpRmsA();
                                    String ampRmsB = deviceHourDataSmartElec.getAmpRmsB();
                                    String ampRmsC = deviceHourDataSmartElec.getAmpRmsC();
                                    //有功功率
                                    String activePowerA1 = deviceHourDataSmartElec.getActivePowerA();
                                    String activePowerB1 = deviceHourDataSmartElec.getActivePowerB();
                                    String activePowerC1 = deviceHourDataSmartElec.getActivePowerC();
                                    //无功功率
                                    String reactivePowerA1 = deviceHourDataSmartElec.getReactivePowerA();
                                    String reactivePowerB1 = deviceHourDataSmartElec.getReactivePowerB();
                                    String reactivePowerC1 = deviceHourDataSmartElec.getReactivePowerC();
                                    //处理电压
                                    if (voltRmsA.equals("0")) {
                                        dianYaA.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(voltRmsA);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaA.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaA.add(divide.toString());
                                        }
                                    }
                                    if (voltRmsB.equals("0")) {
                                        dianYaB.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(voltRmsB);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaB.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaB.add(divide.toString());
                                        }
                                    }

                                    if (voltRmsC.equals("0")) {
                                        dianYaC.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(voltRmsC);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaC.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            dianYaC.add(divide.toString());
                                        }
                                    }
                                    //处理电流
                                    if (ampRmsA.equals("0")) {
                                        dianLiuA.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(ampRmsA);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigQian, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuA.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuA.add(divide.toString());
                                        }
                                    }
                                    if (ampRmsB.equals("0")) {
                                        dianLiuB.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(ampRmsB);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigQian, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuB.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuB.add(divide.toString());
                                        }
                                    }
                                    if (ampRmsC.equals("0")) {
                                        dianLiuC.add("0");
                                    } else {
                                        BigDecimal bigDecimal = new BigDecimal(ampRmsC);
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal divide = bigDecimal.divide(bigQian, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuC.add(divide.toString());
                                        } else {
                                            BigDecimal divide = bigDecimal.divide(bigBai, 0, BigDecimal.ROUND_HALF_UP);
                                            dianLiuC.add(divide.toString());
                                        }
                                    }
                                    //处理有功功率
                                    if (activePowerA1.equals("0")) {
                                        activePowerA.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(activePowerA1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            activePowerA.add(divide.toString());
                                        } else {
                                            activePowerA.add(activePowerA1);
                                        }
                                    }
                                    if (activePowerB1.equals("0")) {
                                        activePowerB.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(activePowerB1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            activePowerB.add(divide.toString());
                                        } else {
                                            activePowerB.add(activePowerB1);
                                        }
                                    }
                                    if (activePowerC1.equals("0")) {
                                        activePowerC.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(activePowerC1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            activePowerC.add(divide.toString());
                                        } else {
                                            activePowerC.add(activePowerC1);
                                        }
                                    }
                                    //处理无功功率
                                    if (reactivePowerA1.equals("0")) {
                                        reactivePowerA.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(reactivePowerA1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            reactivePowerA.add(divide.toString());
                                        } else {
                                            reactivePowerA.add(reactivePowerA1);
                                        }
                                    }
                                    if (reactivePowerB1.equals("0")) {
                                        reactivePowerB.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(reactivePowerB1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            reactivePowerB.add(divide.toString());
                                        } else {
                                            reactivePowerB.add(reactivePowerB1);
                                        }
                                    }

                                    if (reactivePowerC1.equals("0")) {
                                        reactivePowerC.add("0");
                                    } else {
                                        if (deviceTypeId.equals(6l)) {
                                            BigDecimal bigDecimal = new BigDecimal(reactivePowerC1);
                                            BigDecimal divide = bigDecimal.divide(bigShi, 0, BigDecimal.ROUND_HALF_UP);
                                            reactivePowerC.add(divide.toString());
                                        } else {
                                            reactivePowerC.add(reactivePowerC1);
                                        }
                                    }
                                } else {
                                    dianYaA.add(null);
                                    dianYaB.add(null);
                                    dianYaC.add(null);
                                    dianLiuA.add(null);
                                    dianLiuB.add(null);
                                    dianLiuC.add(null);
                                    activePowerA.add(null);
                                    activePowerB.add(null);
                                    activePowerC.add(null);
                                    reactivePowerA.add(null);
                                    reactivePowerB.add(null);
                                    reactivePowerC.add(null);
                                }
                            }
                            if (!CollectionUtils.isEmpty(dianYaA)) {
                                Map<String, Object> dianYa = new LinkedHashMap<>();
                                dianYa.put("xDanWei", "时");
                                dianYa.put("yDanWei", "V");
                                dianYa.put("xData", dayHour);
                                Map<String, Object> dianYaData = new HashMap<>();
                                dianYaData.put("dianYaA", dianYaA);
                                dianYaData.put("dianYaB", dianYaB);
                                dianYaData.put("dianYaC", dianYaC);
                                dianYa.put("dianYa", dianYaData);
                                powerRecentInfo.setDianYa(dianYa);
                            }
                            if (!CollectionUtils.isEmpty(dianLiuA)) {
                                Map<String, Object> dianLiu = new LinkedHashMap<>();
                                dianLiu.put("xDanWei", "时");
                                dianLiu.put("yDanWei", "A");
                                dianLiu.put("xData", dayHour);
                                Map<String, Object> dianLiuData = new HashMap<>();
                                dianLiuData.put("dianLiuA", dianLiuA);
                                dianLiuData.put("dianLiuB", dianLiuB);
                                dianLiuData.put("dianLiuC", dianLiuC);
                                dianLiu.put("dianLiu", dianLiuData);
                                powerRecentInfo.setDianLiu(dianLiu);
                            }
                            if (!CollectionUtils.isEmpty(activePowerA)) {
                                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                                activePowerMap.put("xDanWei", "时");
                                activePowerMap.put("yDanWei", "w");
                                activePowerMap.put("xData", dayHour);
                                Map<String, Object> activePowerData = new HashMap<>();
                                activePowerData.put("activePowerA", activePowerA);
                                activePowerData.put("activePowerB", activePowerB);
                                activePowerData.put("activePowerC", activePowerC);
                                activePowerMap.put("activePower", activePowerData);
                                powerRecentInfo.setActivePower(activePowerMap);
                            }
                            if (!CollectionUtils.isEmpty(reactivePowerA)) {
                                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                                reactivePowerMap.put("xDanWei", "时");
                                reactivePowerMap.put("yDanWei", "var");
                                reactivePowerMap.put("xData", dayHour);
                                Map<String, Object> reactivePowerData = new HashMap<>();
                                reactivePowerData.put("reactivePowerA", reactivePowerA);
                                reactivePowerData.put("reactivePowerB", reactivePowerB);
                                reactivePowerData.put("reactivePowerC", reactivePowerC);
                                reactivePowerMap.put("reactivePower", reactivePowerData);
                                powerRecentInfo.setReactivePower(reactivePowerMap);
                            }
                            return powerRecentInfo;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public PowerPicture getPowerPicture(Long powerId) {
        return powerPictureService.getByPowerId(powerId);
    }

    private List<DeviceHourDataSmartElec> getSmartElec(List<DeviceHourDataSmartSuper> deviceHourDataSmartSupers) {
        if (!CollectionUtils.isEmpty(deviceHourDataSmartSupers)) {
            List<DeviceHourDataSmartElec> list = new ArrayList<>();
            for (DeviceHourDataSmartSuper deviceHourDataSmartSuper : deviceHourDataSmartSupers) {
                DeviceHourDataSmartElec deviceHourDataSmartElec = new DeviceHourDataSmartElec(deviceHourDataSmartSuper);
                list.add(deviceHourDataSmartElec);
            }
            return list;
        }
        return null;
    }
}
