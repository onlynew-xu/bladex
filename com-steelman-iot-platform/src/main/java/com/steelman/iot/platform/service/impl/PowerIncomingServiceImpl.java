package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.PowerIncomingDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.DeviceDataSmartElecFloatDto;
import com.steelman.iot.platform.entity.dto.IncomingData;
import com.steelman.iot.platform.entity.dto.IncomingDto;
import com.steelman.iot.platform.entity.dto.IncomingInfo;
import com.steelman.iot.platform.entity.vo.DeviceCenterVo;
import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.entity.vo.WeekEnergyConsumeStatistic;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/12 0012 15:08
 * @Description:
 */
@Service("powerIncomingService")
public class PowerIncomingServiceImpl extends BaseService implements PowerIncomingService {
    @Resource
    private PowerIncomingDao powerIncomingDao;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private PowerService powerService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private DeviceLastMeasurementService deviceLastMeasureService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private PowerTypePictureService powerTypePictureService;
    @Resource
    private DeviceDataSmartSuperService deviceDataSmartSuperService;


    @Override
    public void insert(PowerIncoming powerIncoming) {
        powerIncomingDao.insert(powerIncoming);

    }

    @Override
    public void update(PowerIncoming powerIncoming) {
        powerIncomingDao.updateByPrimaryKeySelective(powerIncoming);
    }

    @Override
    public List<PowerIncoming> getIncomingList(Long transformerId) {
        List<PowerIncoming> list = powerIncomingDao.selectByTransformerId(transformerId);
        return list;
    }

    @Override
    public void delete(Long id) {
        powerIncomingDao.deleteByPrimaryKey(id);
    }

    @Override
    public PowerIncoming getIncomingInfo(Long id) {
        return powerIncomingDao.selectByPrimaryKey(id);
    }

    @Override
    public Long selectCountByPowerId(Long powerId) {
        return powerIncomingDao.selectCountByPowerId(powerId);
    }

    @Override
    public List<DeviceCenterVo> getPowerIncoming(Long powerId) {
        return powerIncomingDao.selectPowerIncomingCenter(powerId);
    }

    @Override
    public Long selectCountByTransformId(Long transformId, Long projectId) {
        return powerIncomingDao.selectCountByTransformId(transformId, projectId);
    }

    @Override
    public PowerIncoming selectByName(String incomingName, Long transformerId) {
        return powerIncomingDao.selectByName(incomingName, transformerId);
    }

    @Override
    public List<PowerIncoming> findByPowerId(Long powerId) {
        return powerIncomingDao.findByPowerId(powerId);
    }

    @Override
    public List<PowerIncoming> getByTransformerId(Long transformerId) {
        return powerIncomingDao.getByTransformerId(transformerId);
    }

    @Override
    public IncomingDto getIncomingDto(Long incomingId) {
        IncomingDto incomingDto = new IncomingDto();
        PowerIncoming powerIncoming = powerIncomingDao.selectByPrimaryKey(incomingId);
        if (powerIncoming != null) {
            incomingDto.setId(powerIncoming.getId());
            incomingDto.setName(powerIncoming.getName());
            PowerTypePicture byType = powerTypePictureService.getByType(3);
            if (byType != null) {
                incomingDto.setPicture(byType.getUrl());
            }
            List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
            if (CollectionUtils.isEmpty(deviceList)) {
                incomingDto.setDataFlag(0);
            } else {
                PowerDeviceInfo powerDeviceInfo = deviceList.get(0);
                Long deviceId = powerDeviceInfo.getDeviceId();
                Device device = deviceService.findById(deviceId);
                incomingDto.setDeviceId(deviceId);
                incomingDto.setVideoUrl(device.getVideoUrl());
                incomingDto.setDeviceTypeId(powerDeviceInfo.getDeviceTypeId());
                incomingDto.setStatus(device.getStatus());
                incomingDto.setLocation(powerService.getLocation(deviceId));
                //获取未处理预警信息
                List<PowerAlarmWarnVo> message = powerService.getMessage(powerIncoming.getPowerId(), deviceId, 1, powerIncoming.getId(), null, null);
                incomingDto.setPowerAlarmWarnVoList(message);
                //处理电度数据
                Boolean superFlag = false;
                if (powerDeviceInfo.getDeviceId().equals(13l)) {
                    superFlag = true;
                }
                if (superFlag) {
                    DeviceDataSmartSuper lastData = deviceDataSmartSuperService.getLastData(deviceId);
                    if (lastData == null) {
                        incomingDto.setDataFlag(0);
                    } else {
                        incomingDto.setDataFlag(1);
                        incomingDto.setDeviceDataSmartElecFloatDto(powerService.getSuperFloatData(lastData));
                    }
                } else {
                    DeviceDataSmartElec lastData = deviceDataSmartElecService.getLastData(deviceId);
                    if (lastData == null) {
                        incomingDto.setDataFlag(0);
                    } else {
                        incomingDto.setDataFlag(1);
                        incomingDto.setDeviceDataSmartElecFloatDto(powerService.getFloatData(lastData));
                    }
                }
                List<DeviceDataSmartElec> deviceDataSmartElecList = null;
                if (superFlag) {
                    deviceDataSmartElecList = deviceDataSmartSuperService.getLastedTenDataElec(deviceId);
                } else {
                    deviceDataSmartElecList = deviceDataSmartElecService.getLastedTenData(deviceId);
                }
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
                if (!CollectionUtils.isEmpty(deviceDataSmartElecList)) {
                    for (DeviceDataSmartElec deviceDataSmartElec : deviceDataSmartElecList) {
                        Date createTime = deviceDataSmartElec.getCreateTime();
                        long time = createTime.getTime();
                        int sec = (int) (current - time) / 1000;
                        xdata.add(sec);
                        String voltRmsA = deviceDataSmartElec.getVoltRmsA();
                        if (StringUtils.isEmpty(voltRmsA)) {
                            dianyaA.add(220f);
                        } else {
                            BigDecimal voltABig = new BigDecimal(voltRmsA);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaA.add(divide.floatValue());
                        }

                        String voltRmsB = deviceDataSmartElec.getVoltRmsB();
                        if (StringUtils.isEmpty(voltRmsB)) {
                            dianyaB.add(240f);
                        } else {
                            BigDecimal voltBBig = new BigDecimal(voltRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaB.add(divide.floatValue());
                        }

                        String voltRmsC = deviceDataSmartElec.getVoltRmsC();
                        if (StringUtils.isEmpty(voltRmsC)) {
                            dianyaC.add(260f);
                        } else {
                            BigDecimal voltCBig = new BigDecimal(voltRmsC);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaC.add(divide.floatValue());
                        }

                        String ampRmsA = deviceDataSmartElec.getAmpRmsA();
                        if (StringUtils.isEmpty(ampRmsA)) {
                            dianliuA.add(100f);
                        } else {
                            BigDecimal ampRmsABig = new BigDecimal(ampRmsA);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsABig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);

                            }
                            dianliuA.add(divide.floatValue());
                        }

                        String ampRmsB = deviceDataSmartElec.getAmpRmsB();
                        if (StringUtils.isEmpty(ampRmsB)) {
                            dianliuB.add(120f);
                        } else {
                            BigDecimal ampRmsBBig = new BigDecimal(ampRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsBBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);
                            }
                            dianliuB.add(divide.floatValue());
                        }

                        String ampRmsC = deviceDataSmartElec.getAmpRmsB();
                        if (StringUtils.isEmpty(ampRmsC)) {
                            dianliuC.add(140f);
                        } else {
                            BigDecimal ampRmsCBig = new BigDecimal(ampRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsCBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);

                            }
                            dianliuC.add(divide.floatValue());
                        }
                        String powerFacA = deviceDataSmartElec.getPowerFactorA();
                        if (StringUtils.isEmpty(powerFacA) || powerFacA.equals("0")) {
                            powerFactoryA.add(500);
                        } else {
                            powerFactoryA.add(Integer.parseInt(powerFacA));
                        }
                        String powerFacB = deviceDataSmartElec.getPowerFactorB();
                        if (StringUtils.isEmpty(powerFacB) || powerFacB.equals("0")) {
                            powerFactoryB.add(700);
                        } else {
                            powerFactoryB.add(Integer.parseInt(powerFacB));
                        }
                        String powerFacC = deviceDataSmartElec.getPowerFactorC();
                        if (StringUtils.isEmpty(powerFacC) || powerFacC.equals("0")) {
                            powerFactoryC.add(900);
                        } else {
                            powerFactoryC.add(Integer.parseInt(powerFacB));
                        }

                        String activePowerA1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerA1) || activePowerA1.equals("0")) {
                            activePowerA.add(1000f);
                        } else {
                            if (superFlag) {
                                activePowerA.add(Float.parseFloat(activePowerA1));
                            } else {
                                BigDecimal activePowerA1Big = new BigDecimal(activePowerA1);
                                BigDecimal divide = activePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerA.add(divide.floatValue());
                            }
                        }

                        String activePowerB1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerB1) || activePowerB1.equals("0")) {
                            activePowerB.add(2000f);
                        } else {
                            if (superFlag) {
                                activePowerB.add(Float.parseFloat(activePowerB1));
                            } else {
                                BigDecimal activePowerB1Big = new BigDecimal(activePowerB1);
                                BigDecimal divide = activePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerB.add(divide.floatValue());
                            }
                        }

                        String activePowerC1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerC1) || activePowerC1.equals("0")) {
                            activePowerC.add(3000f);
                        } else {
                            if (superFlag) {
                                activePowerC.add(Float.parseFloat(activePowerC1));
                            } else {
                                BigDecimal activePowerC1Big = new BigDecimal(activePowerC1);
                                BigDecimal divide = activePowerC1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerC.add(divide.floatValue());
                            }
                        }

                        String reactivePowerA1 = deviceDataSmartElec.getReactivePowerA();
                        if (StringUtils.isEmpty(reactivePowerA1) || reactivePowerA1.equals("0")) {
                            reactivePowerA.add(100f);
                        } else {
                            if (superFlag) {
                                reactivePowerA.add(Float.parseFloat(reactivePowerA1));
                            } else {
                                BigDecimal reactivePowerA1Big = new BigDecimal(reactivePowerA1);
                                BigDecimal divide = reactivePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                reactivePowerA.add(divide.floatValue());
                            }
                        }

                        String reactivePowerB1 = deviceDataSmartElec.getReactivePowerB();
                        if (StringUtils.isEmpty(reactivePowerB1) || reactivePowerB1.equals("0")) {
                            reactivePowerB.add(200f);
                        } else {
                            if (superFlag) {
                                reactivePowerB.add(Float.parseFloat(reactivePowerB1));
                            } else {
                                BigDecimal reactivePowerB1Big = new BigDecimal(reactivePowerB1);
                                BigDecimal divide = reactivePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                reactivePowerB.add(divide.floatValue());
                            }
                        }

                        String reactivePowerC1 = deviceDataSmartElec.getReactivePowerC();
                        if (StringUtils.isEmpty(reactivePowerC1) || reactivePowerC1.equals("0")) {
                            reactivePowerC.add(300f);
                        } else {
                            if (superFlag) {
                                reactivePowerC.add(Float.parseFloat(reactivePowerC1));
                            } else {
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
                    incomingDto.setVoltDataMap(dianYaMap);
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
                    incomingDto.setAmpDataMap(dianLiuMap);
                }
                //功率因数
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
                    incomingDto.setFactorDataMap(powerFactoryMap);
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
                    incomingDto.setActivePowerMap(activePowerMap);
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
                    incomingDto.setReactivePowerMap(reactivePowerMap);
                }
                //处理电度数据
                DeviceMeasurement deviceMeasurement = deviceMeasurementService.getLastDevice(deviceId);
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = simpleDateFormat.format(date);
                String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
                List<DeviceMeasurement> deviceMeasurements = deviceLastMeasureService.getMeasureData(pastDateStrArr, deviceId);
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
                }
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
                    incomingDto.setMeasureData(measureData);
                }
            }
            //开始添加死数据------------------------------------------
            if (powerIncoming.getId().equals(1l)) {
                incomingDto.setDataFlag(1);
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
                List<Integer> powerFactoryA = new ArrayList<>();
                List<Integer> powerFactoryB = new ArrayList<>();
                List<Integer> powerFactoryC = new ArrayList<>();
                List<Float> activePowerA = new ArrayList<>();
                List<Float> activePowerB = new ArrayList<>();
                List<Float> activePowerC = new ArrayList<>();
                List<Float> reactivePowerA = new ArrayList<>();
                List<Float> reactivePowerB = new ArrayList<>();
                List<Float> reactivePowerC = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    dianyaA.add(220f);
                    dianyaB.add(230f);
                    dianyaC.add(240f);

                    dianliuA.add(50f);
                    dianliuB.add(70f);
                    dianliuC.add(90f);

                    powerFactoryA.add(500);
                    powerFactoryB.add(700);
                    powerFactoryC.add(800);

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
                incomingDto.setVoltDataMap(dianYaMap);

                Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                dianLiuMap.put("xDanWei", "s前");
                dianLiuMap.put("yDanWei", "V");
                dianLiuMap.put("xData", xdata);
                Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                dianLiuDataMap.put("dianLiuA", dianliuA);
                dianLiuDataMap.put("dianLiuB", dianliuB);
                dianLiuDataMap.put("dianLiuC", dianliuC);
                dianLiuMap.put("dianLiu", dianLiuDataMap);
                incomingDto.setAmpDataMap(dianLiuMap);

                Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                powerFactoryMap.put("xDanWei", "s前");
                powerFactoryMap.put("yDanWei", "0.001");
                powerFactoryMap.put("xData", xdata);
                Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                powerFactoryDataMap.put("powerFactoryA", powerFactoryA);
                powerFactoryDataMap.put("powerFactoryB", powerFactoryB);
                powerFactoryDataMap.put("powerFactoryC", powerFactoryC);
                powerFactoryMap.put("powerFactory", powerFactoryDataMap);
                incomingDto.setFactorDataMap(powerFactoryMap);

                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                activePowerMap.put("xDanWei", "s前");
                activePowerMap.put("yDanWei", "w");
                activePowerMap.put("xData", xdata);
                Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
                activePowerDataMap.put("activePowerA", activePowerA);
                activePowerDataMap.put("activePowerB", activePowerB);
                activePowerDataMap.put("activePowerC", activePowerC);
                activePowerMap.put("activePower", activePowerDataMap);
                incomingDto.setActivePowerMap(activePowerMap);

                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                reactivePowerMap.put("xDanWei", "s前");
                reactivePowerMap.put("yDanWei", "var");
                reactivePowerMap.put("xData", xdata);
                Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
                reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
                reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
                reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
                reactivePowerMap.put("reactivePower", reactivePowerDataMap);
                incomingDto.setReactivePowerMap(reactivePowerMap);

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
                incomingDto.setMeasureData(measureData);
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
                deviceDataSmartElecFloatDto.setReactivePowerA(10f);
                deviceDataSmartElecFloatDto.setReactivePowerB(10f);
                deviceDataSmartElecFloatDto.setReactivePowerC(10f);
                incomingDto.setDeviceDataSmartElecFloatDto(deviceDataSmartElecFloatDto);
                incomingDto.setVideoUrl("");
            }
            return incomingDto;
        }
        return null;
    }

    @Override
    public IncomingData getIncomingData(Long incomingId) {
        PowerIncoming powerIncoming = powerIncomingDao.selectByPrimaryKey(incomingId);
        IncomingData incomingData = new IncomingData();
        if (powerIncoming != null) {
            incomingData.setId(incomingId);
            incomingData.setName(powerIncoming.getName());
        }
        List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
        if (CollectionUtils.isEmpty(deviceList)) {
            incomingData.setDataFlag(0);
        } else {
            PowerDeviceInfo powerDeviceInfo = deviceList.get(0);
            incomingData.setDeviceId(powerDeviceInfo.getDeviceId());
            Boolean superFlag = false;
            if (powerDeviceInfo.getDeviceTypeId().equals(13l)) {
                superFlag = true;
            }
            List<DeviceDataSmartElec> deviceDataSmartElecList = null;
            if (superFlag) {
                deviceDataSmartElecList = deviceDataSmartSuperService.getLastedTenDataElec(powerDeviceInfo.getDeviceId());
            } else {
                deviceDataSmartElecList = deviceDataSmartElecService.getLastedTenData(powerDeviceInfo.getDeviceId());
            }
            if (CollectionUtils.isEmpty(deviceDataSmartElecList)) {
                incomingData.setDataFlag(0);
            } else {
                incomingData.setDataFlag(1);
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
                if (!CollectionUtils.isEmpty(deviceDataSmartElecList)) {
                    for (DeviceDataSmartElec deviceDataSmartElec : deviceDataSmartElecList) {
                        Date createTime = deviceDataSmartElec.getCreateTime();
                        long time = createTime.getTime();
                        int sec = (int) (current - time) / 1000;
                        xdata.add(sec);
                        String voltRmsA = deviceDataSmartElec.getVoltRmsA();
                        if (StringUtils.isEmpty(voltRmsA)) {
                            dianyaA.add(220f);
                        } else {
                            BigDecimal voltABig = new BigDecimal(voltRmsA);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltABig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaA.add(divide.floatValue());
                        }

                        String voltRmsB = deviceDataSmartElec.getVoltRmsB();
                        if (StringUtils.isEmpty(voltRmsB)) {
                            dianyaB.add(240f);
                        } else {
                            BigDecimal voltBBig = new BigDecimal(voltRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltBBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaB.add(divide.floatValue());
                        }

                        String voltRmsC = deviceDataSmartElec.getVoltRmsC();
                        if (StringUtils.isEmpty(voltRmsC)) {
                            dianyaC.add(260f);
                        } else {
                            BigDecimal voltCBig = new BigDecimal(voltRmsC);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = voltCBig.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = voltCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            }
                            dianyaC.add(divide.floatValue());
                        }

                        String ampRmsA = deviceDataSmartElec.getAmpRmsA();
                        if (StringUtils.isEmpty(ampRmsA)) {
                            dianliuA.add(100f);
                        } else {
                            BigDecimal ampRmsABig = new BigDecimal(ampRmsA);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsABig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsABig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);

                            }
                            dianliuA.add(divide.floatValue());
                        }

                        String ampRmsB = deviceDataSmartElec.getAmpRmsB();
                        if (StringUtils.isEmpty(ampRmsB)) {
                            dianliuB.add(120f);
                        } else {
                            BigDecimal ampRmsBBig = new BigDecimal(ampRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsBBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsBBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);
                            }
                            dianliuB.add(divide.floatValue());
                        }

                        String ampRmsC = deviceDataSmartElec.getAmpRmsB();
                        if (StringUtils.isEmpty(ampRmsC)) {
                            dianliuC.add(140f);
                        } else {
                            BigDecimal ampRmsCBig = new BigDecimal(ampRmsB);
                            BigDecimal divide = null;
                            if (superFlag) {
                                divide = ampRmsCBig.divide(bigDecimalBai, 2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                divide = ampRmsCBig.divide(bigDecimalThou, 3, BigDecimal.ROUND_HALF_UP);

                            }
                            dianliuC.add(divide.floatValue());
                        }
                        String powerFacA = deviceDataSmartElec.getPowerFactorA();
                        if (StringUtils.isEmpty(powerFacA) || powerFacA.equals("0")) {
                            powerFactoryA.add(500);
                        } else {
                            powerFactoryA.add(Integer.parseInt(powerFacA));
                        }
                        String powerFacB = deviceDataSmartElec.getPowerFactorB();
                        if (StringUtils.isEmpty(powerFacB) || powerFacB.equals("0")) {
                            powerFactoryB.add(700);
                        } else {
                            powerFactoryB.add(Integer.parseInt(powerFacB));
                        }
                        String powerFacC = deviceDataSmartElec.getPowerFactorC();
                        if (StringUtils.isEmpty(powerFacC) || powerFacC.equals("0")) {
                            powerFactoryC.add(900);
                        } else {
                            powerFactoryC.add(Integer.parseInt(powerFacB));
                        }

                        String activePowerA1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerA1) || activePowerA1.equals("0")) {
                            activePowerA.add(1000f);
                        } else {
                            if (superFlag) {
                                activePowerA.add(Float.parseFloat(activePowerA1));
                            } else {
                                BigDecimal activePowerA1Big = new BigDecimal(activePowerA1);
                                BigDecimal divide = activePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerA.add(divide.floatValue());
                            }
                        }

                        String activePowerB1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerB1) || activePowerB1.equals("0")) {
                            activePowerB.add(2000f);
                        } else {
                            if (superFlag) {
                                activePowerB.add(Float.parseFloat(activePowerB1));
                            } else {
                                BigDecimal activePowerB1Big = new BigDecimal(activePowerB1);
                                BigDecimal divide = activePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerB.add(divide.floatValue());
                            }
                        }

                        String activePowerC1 = deviceDataSmartElec.getActivePowerA();
                        if (StringUtils.isEmpty(activePowerC1) || activePowerC1.equals("0")) {
                            activePowerC.add(3000f);
                        } else {
                            if (superFlag) {
                                activePowerC.add(Float.parseFloat(activePowerC1));
                            } else {
                                BigDecimal activePowerC1Big = new BigDecimal(activePowerC1);
                                BigDecimal divide = activePowerC1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                activePowerC.add(divide.floatValue());
                            }
                        }

                        String reactivePowerA1 = deviceDataSmartElec.getReactivePowerA();
                        if (StringUtils.isEmpty(reactivePowerA1) || reactivePowerA1.equals("0")) {
                            reactivePowerA.add(100f);
                        } else {
                            if (superFlag) {
                                reactivePowerA.add(Float.parseFloat(reactivePowerA1));
                            } else {
                                BigDecimal reactivePowerA1Big = new BigDecimal(reactivePowerA1);
                                BigDecimal divide = reactivePowerA1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                reactivePowerA.add(divide.floatValue());
                            }
                        }

                        String reactivePowerB1 = deviceDataSmartElec.getReactivePowerB();
                        if (StringUtils.isEmpty(reactivePowerB1) || reactivePowerB1.equals("0")) {
                            reactivePowerB.add(200f);
                        } else {
                            if (superFlag) {
                                reactivePowerB.add(Float.parseFloat(reactivePowerB1));
                            } else {
                                BigDecimal reactivePowerB1Big = new BigDecimal(reactivePowerB1);
                                BigDecimal divide = reactivePowerB1Big.divide(bigDecimalShi, 1, BigDecimal.ROUND_HALF_UP);
                                reactivePowerB.add(divide.floatValue());
                            }
                        }

                        String reactivePowerC1 = deviceDataSmartElec.getReactivePowerC();
                        if (StringUtils.isEmpty(reactivePowerC1) || reactivePowerC1.equals("0")) {
                            reactivePowerC.add(300f);
                        } else {
                            if (superFlag) {
                                reactivePowerC.add(Float.parseFloat(reactivePowerC1));
                            } else {
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
                    incomingData.setVoltDataMap(dianYaMap);
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
                    incomingData.setAmpDataMap(dianLiuMap);
                }
                //功率因数
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
                    incomingData.setFactorDataMap(powerFactoryMap);
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
                    incomingData.setActivePowerMap(activePowerMap);
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
                    incomingData.setReactivePowerMap(reactivePowerMap);
                }
            }
            if(incomingId.equals(1l)){
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
                List<Float> activePowerA = new ArrayList<>();
                List<Float> activePowerB = new ArrayList<>();
                List<Float> activePowerC = new ArrayList<>();
                List<Float> reactivePowerA = new ArrayList<>();
                List<Float> reactivePowerB = new ArrayList<>();
                List<Float> reactivePowerC = new ArrayList<>();
                for (int i = 5; i <560 ; i=i+60) {
                    xdata.add(i);
                }
                for (int i = 0; i <10 ; i++) {
                    dianyaA.add(220f);
                    dianyaB.add(240f);
                    dianyaC.add(260f);
                    dianliuA.add(10f);
                    dianliuB.add(20f);
                    dianliuC.add(30f);
                    powerFactoryA.add(300);
                    powerFactoryB.add(400);
                    powerFactoryC.add(600);
                    activePowerA.add(1000f);
                    activePowerB.add(1200f);
                    activePowerC.add(1400f);
                    reactivePowerA.add(10f);
                    reactivePowerB.add(12f);
                    reactivePowerC.add(13f);
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
                incomingData.setVoltDataMap(dianYaMap);

                Map<String, Object> dianLiuMap = new LinkedHashMap<>();
                dianLiuMap.put("xDanWei", "s前");
                dianLiuMap.put("yDanWei", "V");
                dianLiuMap.put("xData", xdata);
                Map<String, Object> dianLiuDataMap = new LinkedHashMap<>();
                dianLiuDataMap.put("dianLiuA", dianliuA);
                dianLiuDataMap.put("dianLiuB", dianliuB);
                dianLiuDataMap.put("dianLiuC", dianliuC);
                dianLiuMap.put("dianLiu", dianLiuDataMap);
                incomingData.setAmpDataMap(dianLiuMap);

                Map<String, Object> powerFactoryMap = new LinkedHashMap<>();
                powerFactoryMap.put("xDanWei", "s前");
                powerFactoryMap.put("yDanWei", "0.001");
                powerFactoryMap.put("xData", xdata);
                Map<String, Object> powerFactoryDataMap = new LinkedHashMap<>();
                powerFactoryDataMap.put("powerFactoryA", powerFactoryA);
                powerFactoryDataMap.put("powerFactoryB", powerFactoryB);
                powerFactoryDataMap.put("powerFactoryC", powerFactoryC);
                powerFactoryMap.put("powerFactory", powerFactoryDataMap);
                incomingData.setFactorDataMap(powerFactoryMap);

                Map<String, Object> activePowerMap = new LinkedHashMap<>();
                activePowerMap.put("xDanWei", "s前");
                activePowerMap.put("yDanWei", "w");
                activePowerMap.put("xData", xdata);
                Map<String, Object> activePowerDataMap = new LinkedHashMap<>();
                activePowerDataMap.put("activePowerA", activePowerA);
                activePowerDataMap.put("activePowerB", activePowerB);
                activePowerDataMap.put("activePowerC", activePowerC);
                activePowerMap.put("activePower", activePowerDataMap);
                incomingData.setActivePowerMap(activePowerMap);

                Map<String, Object> reactivePowerMap = new LinkedHashMap<>();
                reactivePowerMap.put("xDanWei", "s前");
                reactivePowerMap.put("yDanWei", "var");
                reactivePowerMap.put("xData", xdata);
                Map<String, Object> reactivePowerDataMap = new LinkedHashMap<>();
                reactivePowerDataMap.put("reactivePowerA", reactivePowerA);
                reactivePowerDataMap.put("reactivePowerB", reactivePowerB);
                reactivePowerDataMap.put("reactivePowerC", reactivePowerC);
                reactivePowerMap.put("reactivePower", reactivePowerDataMap);
                incomingData.setReactivePowerMap(reactivePowerMap);
                incomingData.setDataFlag(1);
            }

        }
        return incomingData;
    }

    @Override
    public Map<String, List<WeekEnergyConsumeStatistic>> getMeasureData(Long incomingId) {
        PowerIncoming powerIncoming = powerIncomingDao.selectByPrimaryKey(incomingId);
        IncomingData incomingData = new IncomingData();
        if (powerIncoming != null) {
            incomingData.setId(incomingId);
            incomingData.setName(powerIncoming.getName());
        }
        Map<String, List<WeekEnergyConsumeStatistic>> measureData = null;
        List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
        if (CollectionUtils.isEmpty(deviceList)) {
            incomingData.setDataFlag(0);
        } else {
            PowerDeviceInfo powerDeviceInfo = deviceList.get(0);
            DeviceMeasurement deviceMeasurement = deviceMeasurementService.getLastDevice(powerDeviceInfo.getDeviceId());
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(date);
            String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
            List<DeviceMeasurement> deviceMeasurements = deviceLastMeasureService.getMeasureData(pastDateStrArr, powerDeviceInfo.getDeviceId());
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
            }
            measureData = new LinkedHashMap<>();
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
        }
        return measureData;
    }

    @Override
    public IncomingInfo getIncomingInfoDetail(Long incomingId) {
        IncomingInfo incomingInfo = new IncomingInfo();
        PowerIncoming powerIncoming = powerIncomingDao.selectByPrimaryKey(incomingId);
        if (powerIncoming != null) {
            incomingInfo.setId(powerIncoming.getId());
            incomingInfo.setName(powerIncoming.getName());
            PowerTypePicture byType = powerTypePictureService.getByType(3);
            if (byType != null) {
                incomingInfo.setPicture(byType.getUrl());
            }
            List<PowerDeviceInfo> deviceList = powerIncomingDeviceService.getDeviceList(powerIncoming.getId());
            if (CollectionUtils.isEmpty(deviceList)) {
                incomingInfo.setDataFlag(0);
            } else {
                PowerDeviceInfo powerDeviceInfo = deviceList.get(0);
                Long deviceId = powerDeviceInfo.getDeviceId();
                Device device = deviceService.findById(deviceId);
                incomingInfo.setDeviceId(deviceId);
                incomingInfo.setVideoUrl(device.getVideoUrl());
                incomingInfo.setDeviceTypeId(powerDeviceInfo.getDeviceTypeId());
                incomingInfo.setStatus(device.getStatus());
                incomingInfo.setLocation(powerService.getLocation(deviceId));
                //获取未处理预警信息
                List<PowerAlarmWarnVo> message = powerService.getMessage(powerIncoming.getPowerId(), deviceId, 1, powerIncoming.getId(), null, null);
                incomingInfo.setPowerAlarmWarnVoList(message);
                //处理电度数据
                Boolean superFlag = false;
                if (powerDeviceInfo.getDeviceId().equals(13l)) {
                    superFlag = true;
                }
                if (superFlag) {
                    DeviceDataSmartSuper lastData = deviceDataSmartSuperService.getLastData(deviceId);
                    if (lastData == null) {
                        incomingInfo.setDataFlag(0);
                    } else {
                        incomingInfo.setDataFlag(1);
                        incomingInfo.setDeviceDataSmartElecFloatDto(powerService.getSuperFloatData(lastData));
                    }
                } else {
                    DeviceDataSmartElec lastData = deviceDataSmartElecService.getLastData(deviceId);
                    if (lastData == null) {
                        incomingInfo.setDataFlag(0);
                    } else {
                        incomingInfo.setDataFlag(1);
                        incomingInfo.setDeviceDataSmartElecFloatDto(powerService.getFloatData(lastData));
                    }
                }
            }
        }
        if(incomingId.equals(1L)){
            DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto=new DeviceDataSmartElecFloatDto();
            deviceDataSmartElecFloatDto.setThdiA(20f);
            deviceDataSmartElecFloatDto.setThdiB(20f);
            deviceDataSmartElecFloatDto.setThdiC(20f);
            deviceDataSmartElecFloatDto.setThdvA(20f);
            deviceDataSmartElecFloatDto.setThdvB(20f);
            deviceDataSmartElecFloatDto.setThdvC(20f);
            deviceDataSmartElecFloatDto.setActivePowerA(1000f);
            deviceDataSmartElecFloatDto.setActivePowerB(800f);
            deviceDataSmartElecFloatDto.setActivePowerC(500f);
            deviceDataSmartElecFloatDto.setAmpRmsA(20f);
            deviceDataSmartElecFloatDto.setAmpRmsB(30f);
            deviceDataSmartElecFloatDto.setAmpRmsC(40f);
            deviceDataSmartElecFloatDto.setVoltRmsA(230f);
            deviceDataSmartElecFloatDto.setVoltRmsB(240f);
            deviceDataSmartElecFloatDto.setVoltRmsC(250f);
            deviceDataSmartElecFloatDto.setPowerFactorA(300);
            deviceDataSmartElecFloatDto.setPowerFactorB(400);
            deviceDataSmartElecFloatDto.setPowerFactorC(500);
            deviceDataSmartElecFloatDto.setReactivePowerA(10f);
            deviceDataSmartElecFloatDto.setReactivePowerB(20f);
            deviceDataSmartElecFloatDto.setReactivePowerC(30f);
            incomingInfo.setDeviceDataSmartElecFloatDto(deviceDataSmartElecFloatDto);
            incomingInfo.setDataFlag(1);
        }
        return incomingInfo;
    }
}
