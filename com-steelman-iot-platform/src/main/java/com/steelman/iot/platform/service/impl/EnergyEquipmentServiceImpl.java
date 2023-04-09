package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.EnergyEquipmentDao;
import com.steelman.iot.platform.energyManager.dto.EnergyMeasureDataEntity;
import com.steelman.iot.platform.energyManager.entity.EquipmentDateTotalMeasure;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author lsj
 * @DATE 2021/3/29 0029 10:49
 * @Description:
 */
@Service("energyEquipmentService")
public class EnergyEquipmentServiceImpl extends BaseService implements EnergyEquipmentService {

    @Resource
    private EnergyEquipmentDao energyEquipmentDao;

    @Resource
    private EnergyTypeService energyTypeService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private EnergyEquipmentDeviceService equipmentDeviceService;
    @Resource
    private EnergyEquipmentPowerService energyEquipmentPowerService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private EnergyTypePictureService energyTypePictureService;
    @Resource
    private CompanyService companyService;
    @Resource
    private DeviceTypeService deviceTypeService;

    @Override
    public void save(EnergyEquipment equipment) {
        energyEquipmentDao.insert(equipment);
    }

    @Override
    public void delete(Long id) {
        energyEquipmentDao.deleteByPrimaryKey(id);
    }

    @Override
    public List<EnergyEquipment> getList(Long boxLoopId, Long energyTypeId) {
        List<EnergyEquipment> list = energyEquipmentDao.selectByLoop(boxLoopId, energyTypeId);
        return list;
    }

    @Override
    public EnergyEquipment getInfo(Long id) {
        return energyEquipmentDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(EnergyEquipment equipment) {
        energyEquipmentDao.updateByPrimaryKeySelective(equipment);
    }

    @Override
    public List<Map<String, Object>> countEnergy(Long projectId) {
        return energyEquipmentDao.selectCountGroupEnergyType(projectId);
    }

    @Override
    public List<Map<String, Object>> countConsume(Long projectId) {
        return energyEquipmentDao.selectCountGroupConsumeType(projectId);
    }

    @Override
    public List<Map<String, Object>> getEnergyEquipment(Long projectId) {
        List<EnergyType> energyTypes = energyTypeService.getListByProjectSystem(projectId, 3000L);
        List<Map<String, Object>> list = new ArrayList<>();
        for (EnergyType type : energyTypes) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", type.getName());
            List<EquipmentItemVo> voList = energyEquipmentDao.selectProjectEnergyType(projectId, type.getId());
            map.put("data", voList);
            list.add(map);
        }
        return list;
    }

    @Resource
    private EnergyConsumeTypeService energyConsumeTypeService;

    @Override
    public List<Map<String, Object>> getConsumeEquipment(Long projectId) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<EnergyConsumeType> consumeTypes = energyConsumeTypeService.getList(projectId);
        for (EnergyConsumeType consumeType : consumeTypes) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", consumeType.getName());
            List<EquipmentItemVo> voList = energyEquipmentDao.selectProjectConsumeType(projectId, consumeType.getId());
            map.put("data", voList);
            list.add(map);

        }
        return list;
    }

    @Resource
    private EnergyEquipmentDeviceService energyEquipmentDeviceService;

    @Override
    public EquipmentInfoVo getCenterInfo(Long id) {
        EquipmentInfoVo vo = energyEquipmentDao.selectCenterInfo(id);
        List<Map<String, Object>> maps = energyEquipmentDeviceService.selectDeviceCountMeasurement(id);
        vo.setTotalEnergy(maps);

        Date date = new Date();
        Date lastDate = DateUtils.getLastMonthDate(date);
        String[] lastDateArr = DateUtils.getPastDateStrArr(lastDate, 7);

        List<Map<String, Object>> lastEnergyTrend = energyEquipmentDeviceService.selectEnergyTrend(id, lastDateArr);
        vo.setLastTrendEnergy(lastEnergyTrend);

        String[] dateStrArr = DateUtils.getPastDateStrArr(date, 7);
        List<Map<String, Object>> energyTrend = energyEquipmentDeviceService.selectEnergyTrend(id, dateStrArr);
        vo.setTrendEnergy(energyTrend);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveEnergyDevice(EnergyEquipment equipment, Map<String, Object> paramMap, Boolean bindingStatus) {
        Date date = new Date();
        equipment.setCreateTime(date);
        equipment.setUpdateTime(date);
        energyEquipmentDao.insert(equipment);
        Long projectId = Long.parseLong(paramMap.get("projectId").toString());
        if (bindingStatus) {
            Long deviceId = Long.parseLong(paramMap.get("deviceId").toString());
            Long deviceTypeId = Long.parseLong(paramMap.get("deviceTypeId").toString());
            EnergyEquipmentDevice energyEquipmentDevice = new EnergyEquipmentDevice();
            energyEquipmentDevice.setProjectId(projectId);
            energyEquipmentDevice.setCreateTime(date);
            energyEquipmentDevice.setUpdateTime(date);
            energyEquipmentDevice.setDeviceId(deviceId);
            energyEquipmentDevice.setDeviceTypeId(deviceTypeId);
            energyEquipmentDevice.setEquipmentId(equipment.getId());
            energyEquipmentDevice.setSystemId(3000L);
            equipmentDeviceService.save(energyEquipmentDevice);
            Device device = new Device();
            device.setId(deviceId);
            device.setBindingType(6);
            device.setBindingStatus(1);
            device.setUpdateTime(date);
            deviceService.update(device);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteEquitment(Long id) {
        energyEquipmentDao.deleteByPrimaryKey(id);
        energyEquipmentPowerService.deleteByEquipmentId(id);
        EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(id);
        if (energyEquipmentDevice != null) {
            Long deviceId = energyEquipmentDevice.getDeviceId();
            Device device = new Device();
            device.setBindingType(-1);
            device.setBindingStatus(0);
            device.setId(deviceId);
            deviceService.update(device);
            energyEquipmentDeviceService.selectByEquipmentId(id);
        }
        return true;
    }

    @Override
    public List<EnergyEquipment> getByEnergyTypeId(Long projectId, Long energyTypeId) {
        return energyEquipmentDao.getByEnergyTypeId(projectId, energyTypeId);
    }

    @Override
    public List<EnergyEquipment> getByEnergyConsumeTypeId(Long projectId, Long consumeTypeId) {
        return energyEquipmentDao.getByEnergyConsumeTypeId(projectId,consumeTypeId);
    }

    @Override
    public MeasureData getTotalMeasurement(Long projectId) {
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        MeasureData measureData = new MeasureData(0f, 0f, 0f, 0f, 0f);
        List<DeviceMeasurement> measurementList = energyEquipmentDao.getAllEnergyEquitmentMeausure(projectId,simpleDateFormat.format(date));
        if (!CollectionUtils.isEmpty(measurementList)) {
            Integer total = 0;
            Integer totalSpike = 0;
            Integer totalPeak = 0;
            Integer totalNormal = 0;
            Integer totalVally = 0;
            for (DeviceMeasurement deviceMeasurement : measurementList) {
                total =  total + Integer.parseInt(deviceMeasurement.getTotalTotal());
                totalSpike = totalSpike + Integer.parseInt(deviceMeasurement.getTotalSpike());
                totalPeak = totalPeak + Integer.parseInt(deviceMeasurement.getTotalPeak());
                totalNormal = totalNormal + Integer.parseInt(deviceMeasurement.getTotalNormal());
                totalVally = totalVally + Integer.parseInt(deviceMeasurement.getTotalValley());
            }
            if (total.equals(0)) {
                return new MeasureData(2000f, 200f, 800f, 600f, 400f);
            } else {
                BigDecimal totalDe = new BigDecimal(total);
                BigDecimal totalThoud = new BigDecimal(1000);
                BigDecimal divideTotal = totalDe.divide(totalThoud, 3, BigDecimal.ROUND_HALF_UP);
                measureData.setTotal(divideTotal.floatValue());
                if (!totalSpike.equals(0)) {
                    BigDecimal SpikeDe = new BigDecimal(totalSpike);
                    BigDecimal divideSpike = SpikeDe.divide(totalThoud, 3, BigDecimal.ROUND_HALF_UP);
                    measureData.setSpike(divideSpike.floatValue());
                }
                if (!totalPeak.equals(0)) {
                    BigDecimal peakDe = new BigDecimal(totalPeak);
                    BigDecimal dividePeak = peakDe.divide(totalThoud, 3, BigDecimal.ROUND_HALF_UP);
                    measureData.setPeak(dividePeak.floatValue());
                }
                if (!totalNormal.equals(0)) {
                    BigDecimal normalDe = new BigDecimal(totalNormal);
                    BigDecimal divideNormal = normalDe.divide(totalThoud, 3, BigDecimal.ROUND_HALF_UP);
                    measureData.setNormal(divideNormal.floatValue());
                }
                if (!totalVally.equals(0)) {
                    BigDecimal vallyDe = new BigDecimal(totalVally);
                    BigDecimal divideVally = vallyDe.divide(totalThoud, 3, BigDecimal.ROUND_HALF_UP);
                    measureData.setValley(divideVally.floatValue());
                }
                return measureData;
            }
        } else {
            return new MeasureData(2000f, 200f, 800f, 600f, 400f);
        }

    }

    @Override
    public List<EnergyEquipmentStatistic> statisticEnergyType(Long projectId) {
        List<Map<String, Object>> dataList = energyEquipmentDao.countEnergyTypeNum(projectId);
        if (!CollectionUtils.isEmpty(dataList)) {
            List<EnergyType> energyTypeList = energyTypeService.selectAll(projectId);
            Map<Long, Integer> dataMap = new HashMap<>();
            for (Map<String, Object> stringObjectMap : dataList) {
                Object num = stringObjectMap.get("num");
                Object typeId = stringObjectMap.get("typeId");
                dataMap.put(Long.parseLong(typeId.toString()), Integer.parseInt(num.toString()));
            }
            List<EnergyEquipmentStatistic> energyList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(energyTypeList)) {
                Integer max = 0;
                for (EnergyType energyType : energyTypeList) {
                    Long id = energyType.getId();
                    String name = energyType.getName();
                    if (dataMap.containsKey(id)) {
                        Integer num = dataMap.get(id);
                        if (max < num) {
                            max = num + 2;
                        }
                        EnergyEquipmentStatistic energyEquipmentStatistic = new EnergyEquipmentStatistic(0, name, num);
                        energyList.add(energyEquipmentStatistic);
                    } else {
                        EnergyEquipmentStatistic energyEquipmentStatistic = new EnergyEquipmentStatistic(0, name, 0);
                        energyList.add(energyEquipmentStatistic);
                    }
                }
                if (!CollectionUtils.isEmpty(energyList)) {
                    for (EnergyEquipmentStatistic energyEquipmentStatistic : energyList) {
                        energyEquipmentStatistic.setMax(max);
                    }
                }
                return energyList;
            }
        }
        return null;
    }

    @Override
    public List<EnergyConsumeStatistic> consumeStatistic(Long projectId) {
        List<Map<String, Object>> dataList = energyEquipmentDao.consumeStatistic(projectId);
        if (!CollectionUtils.isEmpty(dataList)) {
            Map<Long, String> consumeTypeMap = new HashMap<>();
            List<EnergyConsumeType> energyConsumeTypeList = energyConsumeTypeService.getList(projectId);
            if (!CollectionUtils.isEmpty(energyConsumeTypeList)) {
                for (EnergyConsumeType energyConsumeType : energyConsumeTypeList) {
                    consumeTypeMap.put(energyConsumeType.getId(), energyConsumeType.getName());
                }
            }
            List<EnergyConsumeStatistic> energyConsumeStatisticList = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : dataList) {
                Object num = stringObjectMap.get("num");
                Long typeId = Long.parseLong(stringObjectMap.get("typeId").toString());
                EnergyConsumeStatistic energyConsumeStatistic = new EnergyConsumeStatistic(consumeTypeMap.get(typeId), Integer.parseInt(num.toString()));
                energyConsumeStatisticList.add(energyConsumeStatistic);
            }
            return energyConsumeStatisticList;
        }
        return null;
    }

    @Override
    public Map<String, List<WeekEnergyConsumeStatistic>> getWeekEnergyStatistic(Long projectId) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<WeekEnergyConsumeStatistic>> dataMap = new LinkedHashMap<>();
        Date begDate = DateUtils.getPastDate(date, 6);
        List<String> dateList = new ArrayList<>();
        String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
        //处理前六天的数据
        List<MeasureIntegerDate> deviceMeasurementList = energyEquipmentDao.getWeekCousume(projectId, begDate,date);
        Map<String, Integer> totalMap = new LinkedHashMap<>();
        Map<String, Integer> spikeMap = new LinkedHashMap<>();
        Map<String, Integer> peakMap = new LinkedHashMap<>();
        Map<String, Integer> normalMap = new LinkedHashMap<>();
        Map<String, Integer> valleyMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(deviceMeasurementList)) {
            for (MeasureIntegerDate deviceMeasurement : deviceMeasurementList) {
                String yearMonthDay = deviceMeasurement.getYearMonthDay();
                Integer total = deviceMeasurement.getTotal();
                Integer spike = deviceMeasurement.getSpike();
                Integer peak = deviceMeasurement.getPeak();
                Integer normal = deviceMeasurement.getNormal();
                Integer valley = deviceMeasurement.getValley();
                totalMap.put(yearMonthDay,total);
                spikeMap.put(yearMonthDay,spike);
                peakMap.put(yearMonthDay,peak);
                normalMap.put(yearMonthDay,normal);
                valleyMap.put(yearMonthDay,valley);
            }
        }
        //处理今日数据
        List<MeasureIntegerDate> todayMeasure = energyEquipmentDao.getTodayMeasure(projectId, simpleDateFormat.format(date));
        if (!CollectionUtils.isEmpty(todayMeasure)) {
            for (MeasureIntegerDate deviceMeasurement : todayMeasure) {
                String yearMonthDay = deviceMeasurement.getYearMonthDay();
                Integer total = deviceMeasurement.getTotal();
                Integer spike = deviceMeasurement.getSpike();
                Integer peak = deviceMeasurement.getPeak();
                Integer normal = deviceMeasurement.getNormal();
                Integer valley = deviceMeasurement.getValley();
                totalMap.put(yearMonthDay,total);
                spikeMap.put(yearMonthDay,spike);
                peakMap.put(yearMonthDay,peak);
                normalMap.put(yearMonthDay,normal);
                valleyMap.put(yearMonthDay,valley);
            }

        }
        Map<String, List<WeekEnergyConsumeStatistic>> dataWeekMap = new LinkedHashMap<>();
        List<WeekEnergyConsumeStatistic> totalList = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> spikeList = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> peakList = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> normalList = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> valleyList = new ArrayList<>();
        BigDecimal bigThoud = new BigDecimal(1000);
        for (String str : pastDateStrArr) {
            String week = DateUtils.getWeek(str);
            Integer countTotal = totalMap.get(str);
            if (countTotal == null || countTotal.equals(0)) {
                totalList.add(new WeekEnergyConsumeStatistic(week, 0f));
            } else {
                BigDecimal bigTotal = new BigDecimal(countTotal);
                BigDecimal divideTotal = bigTotal.divide(bigThoud, 3, BigDecimal.ROUND_HALF_UP);
                totalList.add(new WeekEnergyConsumeStatistic(week, divideTotal.floatValue()));
            }
            Integer countSpike = spikeMap.get(str);
            if (countSpike == null || countSpike.equals(0)) {
                spikeList.add(new WeekEnergyConsumeStatistic(week, 0f));
            } else {
                BigDecimal bigSpike = new BigDecimal(countSpike);
                BigDecimal divideSpike = bigSpike.divide(bigThoud, 3, BigDecimal.ROUND_HALF_UP);
                spikeList.add(new WeekEnergyConsumeStatistic(week, divideSpike.floatValue()));
            }
            Integer countPeak = peakMap.get(str);
            if (countPeak == null || countPeak.equals(0)) {
                peakList.add(new WeekEnergyConsumeStatistic(week, 0f));
            } else {
                BigDecimal bigPeak = new BigDecimal(countPeak);
                BigDecimal dividePeak = bigPeak.divide(bigThoud, 3, BigDecimal.ROUND_HALF_UP);
                peakList.add(new WeekEnergyConsumeStatistic(week, dividePeak.floatValue()));
            }
            Integer countNormal = normalMap.get(str);
            if (countNormal == null || countNormal.equals(0)) {
                normalList.add(new WeekEnergyConsumeStatistic(week, 0f));
            } else {
                BigDecimal bigNormal = new BigDecimal(countNormal);
                BigDecimal divideNormal = bigNormal.divide(bigThoud, 3, BigDecimal.ROUND_HALF_UP);
                normalList.add(new WeekEnergyConsumeStatistic(week, divideNormal.floatValue()));
            }
            Integer countValley = valleyMap.get(str);
            if (countValley == null || countValley.equals(0)) {
                valleyList.add(new WeekEnergyConsumeStatistic(week, 0f));
            } else {
                BigDecimal bigValley = new BigDecimal(countValley);
                BigDecimal divideValley = bigValley.divide(bigThoud, 3, BigDecimal.ROUND_HALF_UP);
                valleyList.add(new WeekEnergyConsumeStatistic(week, divideValley.floatValue()));
            }
        }
        dataWeekMap.put("total", totalList);
        dataWeekMap.put("spike", spikeList);
        dataWeekMap.put("peak", peakList);
        dataWeekMap.put("normal", normalList);
        dataWeekMap.put("valley", valleyList);
        return dataWeekMap;
    }

    @Override
    public EquipmentCenterInfo getCenterInfoData(Long projectId) {
        EquipmentCenterInfo equipmentCenterInfo = new EquipmentCenterInfo();
        List<EquipmentSimpleInfo> dataList = energyEquipmentDao.getEquipmentSimpleInfo(projectId);
        List<EnergyConsumeType> energyConsumeTypeServiceList = energyConsumeTypeService.getList(projectId);
        List<EnergyType> energyTypeList = energyTypeService.getListByProjectSystem(projectId, 3000l);
        Map<Long, List<EquipmentSimpleInfo>> consumeMap = new LinkedHashMap<>();
        Map<Long, List<EquipmentSimpleInfo>> energyMap = new LinkedHashMap<>();
        for (EquipmentSimpleInfo equipmentSimpleInfo : dataList) {
            Long consumeId = equipmentSimpleInfo.getConsumeId();
            Long typeId = equipmentSimpleInfo.getTypeId();

            if (consumeMap.containsKey(consumeId)) {
                consumeMap.get(consumeId).add(equipmentSimpleInfo);
            } else {
                List<EquipmentSimpleInfo> consumeList = new ArrayList<>();
                consumeList.add(equipmentSimpleInfo);
                consumeMap.put(consumeId, consumeList);
            }

            if (energyMap.containsKey(typeId)) {
                energyMap.get(typeId).add(equipmentSimpleInfo);
            } else {
                List<EquipmentSimpleInfo> energyList = new ArrayList<>();
                energyList.add(equipmentSimpleInfo);
                energyMap.put(typeId, energyList);
            }
        }
        Map<String, List<EquipmentSimpleInfo>> consumeDataMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(energyConsumeTypeServiceList)) {
            for (EnergyConsumeType energyConsumeType : energyConsumeTypeServiceList) {
                Long id = energyConsumeType.getId();
                String name = energyConsumeType.getName();
                consumeDataMap.put(name, consumeMap.get(id));
            }
        }
        if (!CollectionUtils.isEmpty(consumeDataMap)) {
            equipmentCenterInfo.setConsume(consumeDataMap);
        }
        Map<String, List<EquipmentSimpleInfo>> energyDataMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(energyTypeList)) {
            for (EnergyType energyType : energyTypeList) {
                Long id = energyType.getId();
                String name = energyType.getName();
                energyDataMap.put(name, energyMap.get(id));
            }
        }
        if (!CollectionUtils.isEmpty(energyDataMap)) {
            equipmentCenterInfo.setEnergy(energyDataMap);
        }
        return equipmentCenterInfo;


    }

    @Override
    public List<EnergyEquipment> getAreaEquipment(Long areaId, Long projectId) {
        return energyEquipmentDao.getAreaEquipment(areaId, projectId);
    }

    @Override
    public EquipmentInfoDto getEquipmentInfo(Long equipmentId, Long projectId) {
        EnergyEquipment energyEquipment = energyEquipmentDao.selectByPrimaryKey(equipmentId);
        if (energyEquipment != null) {
            EquipmentInfoDto equipmentInfoDto = new EquipmentInfoDto();
            String location = energyEquipment.getLocation();
            StringBuffer locBuf = new StringBuffer();
            if (!StringUtils.isBlank(location)) {
                locBuf.append(location);
            } else {
                Long areaId = energyEquipment.getAreaId();
                Long buildingId = energyEquipment.getBuildingId();
                Long storeyId = energyEquipment.getStoreyId();
                Long roomId = energyEquipment.getRoomId();
                Area area = regionAreaService.selectByPrimaryKey(areaId);
                if (area != null) {
                    locBuf.append(area.getName());
                }
                Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                if (building != null) {
                    locBuf.append(building.getName());
                }
                Storey storey = regionStoreyService.selectByPrimaryKey(storeyId);
                if (storey != null) {
                    locBuf.append(storey.getName());
                }
                Room room = regionRoomService.selectByPrimaryKey(roomId);
                if (room != null) {
                    locBuf.append(room.getName());
                }
            }
            equipmentInfoDto.setLocation(locBuf.toString());
            equipmentInfoDto.setEquipmentId(energyEquipment.getId());
            equipmentInfoDto.setEquipmentName(energyEquipment.getName());
            equipmentInfoDto.setEnergyTypeId(energyEquipment.getEnergyTypeId());
            EnergyType info = energyTypeService.getInfo(energyEquipment.getEnergyTypeId());
            equipmentInfoDto.setEnergyTypeName(info == null ? null : info.getName());
            equipmentInfoDto.setConsumeId(energyEquipment.getConsumeTypeId());
            EnergyConsumeType energyConsumeType = energyConsumeTypeService.findById(energyEquipment.getConsumeTypeId());
            equipmentInfoDto.setConsumeName(energyConsumeType == null ? null : energyConsumeType.getName());
            EnergyTypePicture energyTypePicture = energyTypePictureService.selectByEnergyTypeId(energyEquipment.getEnergyTypeId());
            equipmentInfoDto.setPictureUrl(energyTypePicture == null ? null : energyTypePicture.getUrl());
            //查询关联设备
            EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentId);
            if (energyEquipmentDevice != null) {
                Device device = deviceService.findById(energyEquipmentDevice.getDeviceId());
                if (device != null) {
                    MeasureDataStr measureDataStr = deviceService.getMeasureTotalData(energyEquipmentDevice.getDeviceId());
                    Integer status = device.getStatus();
                    if (status.equals(1) || status.equals(3)) {
                        if (measureDataStr != null) {
                            BigDecimal bigDecTho = new BigDecimal("1000");
                            MeasureData measureData = new MeasureData();
                            String total = measureDataStr.getTotal();
                            String spike = measureDataStr.getSpike();
                            String peak = measureDataStr.getPeak();
                            String normal = measureDataStr.getNormal();
                            String valley = measureDataStr.getValley();
                            if (StringUtils.isBlank(total) || total.equals("0")) {
                                measureData.setTotal(0f);
                            } else {
                                BigDecimal totalBigDec = new BigDecimal(total);
                                float to = totalBigDec.divide(bigDecTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                                measureData.setTotal(to);
                            }
                            if (StringUtils.isBlank(spike) || spike.equals("0")) {
                                measureData.setSpike(0f);
                            } else {
                                BigDecimal spikeBigDec = new BigDecimal(spike);
                                float sp = spikeBigDec.divide(bigDecTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                                measureData.setSpike(sp);
                            }
                            if (StringUtils.isBlank(peak) || peak.equals("0")) {
                                measureData.setPeak(0f);
                            } else {
                                BigDecimal peakBigDec = new BigDecimal(peak);
                                float pe = peakBigDec.divide(bigDecTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                                measureData.setPeak(pe);
                            }
                            if (StringUtils.isBlank(normal) || normal.equals("0")) {
                                measureData.setNormal(0f);
                            } else {
                                BigDecimal normalBigDec = new BigDecimal(normal);
                                float no = normalBigDec.divide(bigDecTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                                measureData.setNormal(no);
                            }
                            if (StringUtils.isBlank(valley) || valley.equals("0")) {
                                measureData.setValley(0f);
                            } else {
                                BigDecimal valleyBigDec = new BigDecimal(valley);
                                float va = valleyBigDec.divide(bigDecTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                                measureData.setValley(va);
                            }
                            equipmentInfoDto.setMeasureData(measureData);
                        }
                    }

                }
            }
            return equipmentInfoDto;
        }
        return null;
    }

    @Override
    public List<EnergyEquipment> selectByEnergyTypeId(Long energyTypeId, Long projectId) {
        return energyEquipmentDao.selectByEnergyTypeId(energyTypeId, projectId);
    }

    @Override
    public Map<String, List<WeekEnergyConsumeStatistic>> getEquipmentWeekInfo(Long equipmentId, Long projectId) {
        Map<String, List<WeekEnergyConsumeStatistic>> finalDataMap = new LinkedHashMap<>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取过去6天的电度数据
        String format = simpleDateFormat.format(date);
        Date pastSixDate = DateUtils.getPastDate(date, 6);
        String[] pastDateStrArr = DateUtils.getPastDateStrArr(date, 6);
        EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentId);
        //未绑定设备 返回空
        if (energyEquipmentDevice == null) {
            return null;
        }
        Long deviceId = energyEquipmentDevice.getDeviceId();
        Device device = deviceService.findById(deviceId);
        if (device == null) {
            //绑定的设备不存在
            return null;
        }

        List<MeasureDateData> pastDataArr = deviceService.getPastSixDayMeasure(deviceId, projectId, pastSixDate, date);
        MeasureDateData measureDateDataNow = deviceService.getTodayMeasureData(deviceId, simpleDateFormat.format(date));
        Map<String, MeasureDateData> pastDataMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(pastDataArr)) {
            for (MeasureDateData measureDateData : pastDataArr) {
                String yearMonthDay = measureDateData.getYearMonthDay();
                pastDataMap.put(yearMonthDay, measureDateData);
            }
        }
        if (measureDateDataNow != null) {
            pastDataMap.put(format, measureDateDataNow);
        }
        List<WeekEnergyConsumeStatistic> total = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> spike = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> peak = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> normal = new ArrayList<>();
        List<WeekEnergyConsumeStatistic> valley = new ArrayList<>();
        BigDecimal bigTho = new BigDecimal(1000);
        for (String dateArr : pastDateStrArr) {
            String week = DateUtils.getWeek(dateArr);
            if (pastDataMap.containsKey(dateArr)) {
                MeasureDateData measureDateData = pastDataMap.get(dateArr);
                String totalStr = measureDateData.getTotal();
                String spikeStr = measureDateData.getSpike();
                String peakStr = measureDateData.getPeak();
                String normalStr = measureDateData.getNormal();
                String valleyStr = measureDateData.getValley();
                if (totalStr.equals("0")) {
                    total.add(new WeekEnergyConsumeStatistic(week, 0f));
                } else {
                    BigDecimal totalDecimal = new BigDecimal(totalStr);
                    float to = totalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                    total.add(new WeekEnergyConsumeStatistic(week, to));
                }
                if (spikeStr.equals("0")) {
                    spike.add(new WeekEnergyConsumeStatistic(week, 0f));
                } else {
                    BigDecimal spikeDecimal = new BigDecimal(spikeStr);
                    float sp = spikeDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                    spike.add(new WeekEnergyConsumeStatistic(week, sp));
                }
                if (peakStr.equals("0")) {
                    peak.add(new WeekEnergyConsumeStatistic(week, 0f));
                } else {
                    BigDecimal peakDecimal = new BigDecimal(peakStr);
                    float pe = peakDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                    peak.add(new WeekEnergyConsumeStatistic(week, pe));
                }
                if (normalStr.equals("0")) {
                    normal.add(new WeekEnergyConsumeStatistic(week, 0f));
                } else {
                    BigDecimal normalDecimal = new BigDecimal(normalStr);
                    float no = normalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                    normal.add(new WeekEnergyConsumeStatistic(week, no));
                }
                if (valleyStr==null||valleyStr.equals("0")) {
                    valley.add(new WeekEnergyConsumeStatistic(week, 0f));
                } else {
                    BigDecimal valleyDecimal = new BigDecimal(valleyStr);
                    float va = valleyDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP).floatValue();
                    valley.add(new WeekEnergyConsumeStatistic(week, va));
                }
            } else {
                total.add(new WeekEnergyConsumeStatistic(week, 0f));
                spike.add(new WeekEnergyConsumeStatistic(week, 0f));
                peak.add(new WeekEnergyConsumeStatistic(week, 0f));
                normal.add(new WeekEnergyConsumeStatistic(week, 0f));
                valley.add(new WeekEnergyConsumeStatistic(week, 0f));
            }

        }
        finalDataMap.put("total", total);
        finalDataMap.put("spike", spike);
        finalDataMap.put("peak", peak);
        finalDataMap.put("normal", normal);
        finalDataMap.put("valley", valley);
        return finalDataMap;
    }

    @Override
    public Map<String, List<EnergyDeviceInfo>> getEnergyDateStatistic(Long projectId, Long consumeTypeId, Long energyTypeId, String beginTime, String endTime, String order,String part) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(localDate);
        List<Area> areaList = regionAreaService.selectByProjectId(projectId);
        Map<Long, String> areaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(areaList)) {
            for (Area area : areaList) {
                areaMap.put(area.getId(), area.getName());
            }
        }
        List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
        Map<Long, String> buildingMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(buildingList)) {
            for (Building building : buildingList) {
                buildingMap.put(building.getId(), building.getName());
            }
        }
        List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
        Map<Long, String> storeyMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(storeyList)) {
            for (Storey storey : storeyList) {
                storeyMap.put(storey.getId(), storey.getName());
            }
        }
        List<Room> roomList = regionRoomService.selectByProjectId(projectId);
        Map<Long, String> roomMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(roomList)) {
            for (Room room : roomList) {
                roomMap.put(room.getId(), room.getName());
            }
        }
        List<EnergyMeasureDateData> endData = null;
        List<EnergyMeasureDateData> beginData = null;
        Map<String,List<EnergyDeviceInfo>> dataMap=new LinkedHashMap<>();
        if (format.equals(beginTime)) {
            if (consumeTypeId.equals(0l)) {
                consumeTypeId = null;
            }
            if (energyTypeId.equals(0l)) {
                energyTypeId = null;
            }

            LocalDate parse = LocalDate.parse(beginTime);
            LocalDate dateStr = parse.plusDays(-1l);
            beginTime = dateTimeFormatter.format(dateStr);
            beginData = energyEquipmentDao.getEnergyDateStatistic(projectId, consumeTypeId, energyTypeId, beginTime);
            endData = energyEquipmentDao.getTodayMeasureDate(projectId, consumeTypeId, energyTypeId, format);
            //开始时间是今天
        } else {
            LocalDate parse = LocalDate.parse(beginTime);
            LocalDate dateStr = parse.plusDays(-1l);
            beginTime = dateTimeFormatter.format(dateStr);
            LocalDate endLocalDate = LocalDate.parse(endTime);
            if (consumeTypeId.equals(0l)) {
                consumeTypeId = null;
            }
            if (energyTypeId.equals(0l)) {
                energyTypeId = null;
            }
            if (!beginTime.equals(endTime)) {
                beginData = energyEquipmentDao.getEnergyDateStatistic(projectId, consumeTypeId, energyTypeId, beginTime);
            }
            if (endTime.equals(format) || localDate.isBefore(endLocalDate)) {
                endData = energyEquipmentDao.getTodayMeasureDate(projectId, consumeTypeId, energyTypeId, format);
            } else {
                endData = energyEquipmentDao.getEnergyDateStatistic(projectId, consumeTypeId, energyTypeId, endTime);
            }
        }
        List<EnergyDeviceInfo> totalList = new ArrayList<>();
        List<EnergyDeviceInfo> spikeList = new ArrayList<>();
        List<EnergyDeviceInfo> peakList = new ArrayList<>();
        List<EnergyDeviceInfo> normalList = new ArrayList<>();
        List<EnergyDeviceInfo> valleyList = new ArrayList<>();
        Map<Long, EnergyMeasureDateData> beginDataMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(beginData)) {
            for (EnergyMeasureDateData beginDatum : beginData) {
                Long equipmentId = beginDatum.getEquipmentId();
                beginDataMap.put(equipmentId, beginDatum);
            }
        }
        if (!CollectionUtils.isEmpty(endData)) {
            BigDecimal bigTho = new BigDecimal(1000);
            for (EnergyMeasureDateData endDatum : endData) {
                Long equipmentId = endDatum.getEquipmentId();
                String equipmentName = endDatum.getEquipmentName();
                String consumeTypeName = endDatum.getConsumeName();
                String energyTypeName = endDatum.getEnergyTypeName();
                //先处理区域信息
                String location = endDatum.getLocation();
                if (StringUtils.isBlank(location)) {
                    Long areaId = endDatum.getAreaId();
                    Long buildingId = endDatum.getBuildingId();
                    Long storeyId = endDatum.getStoreyId();
                    Long roomId = endDatum.getRoomId();
                    StringBuffer loBuf = new StringBuffer();
                    if (areaMap.containsKey(areaId)) {
                        loBuf.append(areaMap.get(areaId));
                    }
                    if (buildingMap.containsKey(buildingId)) {
                        loBuf.append(buildingMap.get(buildingId));
                    }
                    if (storeyMap.containsKey(storeyId)) {
                        loBuf.append(storeyMap.get(storeyId));
                    }
                    if (roomMap.containsKey(roomId)) {
                        loBuf.append(roomMap.get(roomId));
                    }
                    location = loBuf.toString();
                }
                Integer total = Integer.parseInt(endDatum.getTotal());
                Integer spike = Integer.parseInt(endDatum.getSpike());
                Integer peak = Integer.parseInt(endDatum.getPeak());
                Integer normal = Integer.parseInt(endDatum.getNormal());
                Integer valley = Integer.parseInt(endDatum.getValley());
                if (beginDataMap.containsKey(equipmentId)) {
                    EnergyMeasureDateData energyMeasureDateData = beginDataMap.get(equipmentId);
                    Integer begTotal = Integer.parseInt(energyMeasureDateData.getTotal());
                    Integer begSpike = Integer.parseInt(energyMeasureDateData.getSpike());
                    Integer begPeak = Integer.parseInt(energyMeasureDateData.getPeak());
                    Integer begNormal = Integer.parseInt(energyMeasureDateData.getNormal());
                    Integer begValley = Integer.parseInt(energyMeasureDateData.getValley());
                    total = total - begTotal;
                    spike = spike - begSpike;
                    peak = peak - begPeak;
                    normal = normal - begNormal;
                    valley = valley - begValley;
                }
                if (total.equals(0)) {
                    totalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal totalDecimal = new BigDecimal(total);
                    BigDecimal divide = totalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    totalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (spike.equals(0)) {
                    spikeList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal spikeDecimal = new BigDecimal(spike);
                    BigDecimal divide = spikeDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    spikeList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (peak.equals(0)) {
                    peakList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal peakDecimal = new BigDecimal(peak);
                    BigDecimal divide = peakDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    peakList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (normal.equals(0)) {
                    normalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal normalDecimal = new BigDecimal(normal);
                    BigDecimal divide = normalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    normalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (valley.equals(0)) {
                    valleyList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal valleyDecimal = new BigDecimal(valley);
                    BigDecimal divide = valleyDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    valleyList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }

            }
        }
        if(StringUtils.isNotBlank(order)){
            if(order.equals("asc")){
                totalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                spikeList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                peakList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                normalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                valleyList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
            }else{
                totalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                spikeList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                peakList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                normalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                valleyList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
            }
        }
        List<EnergyDeviceInfo> finalTotal=new ArrayList<>();
        List<EnergyDeviceInfo> finalSpike=new ArrayList<>();
        List<EnergyDeviceInfo> finalPeak=new ArrayList<>();
        List<EnergyDeviceInfo> finalNormal=new ArrayList<>();
        List<EnergyDeviceInfo> finalValley=new ArrayList<>();
        if(part.equals("part")){
            if(totalList.size()>10){
                for (int i = 0; i <10 ; i++) {
                    finalTotal.add(totalList.get(i));
                }
            }else{
                finalTotal=totalList;
            }

            if(spikeList.size()>10){
                for (int i = 0; i <10 ; i++) {
                    finalSpike.add(spikeList.get(i));
                }
            }else{
                finalSpike=spikeList;
            }

            if(peakList.size()>10){
                for (int i = 0; i <10 ; i++) {
                    finalPeak.add(peakList.get(i));
                }
            }else{
                finalPeak=peakList;
            }

            if(normalList.size()>10){
                for (int i = 0; i <10 ; i++) {
                    finalNormal.add(normalList.get(i));
                }
            }else{
                finalNormal=normalList;
            }
            if(valleyList.size()>10){
                for (int i = 0; i <10 ; i++) {
                    finalValley.add(valleyList.get(i));
                }
            }else{
                finalValley=valleyList;
            }
        }else{
            finalTotal=totalList;
            finalSpike=spikeList;
            finalPeak=peakList;
            finalNormal=normalList;
            finalValley=valleyList;
        }
        if(finalTotal.size()>0){
            dataMap.put("total",finalTotal);
        }
        if(finalSpike.size()>0){
            dataMap.put("spike",finalSpike);
        }
        if(finalPeak.size()>0){
            dataMap.put("peak",finalPeak);
        }
        if(finalNormal.size()>0){
            dataMap.put("normal",finalNormal);
        }
        if(finalValley.size()>0){
            dataMap.put("valley",finalValley);
        }
        return dataMap;
    }


    @Override
    public Map<String, List<EnergyDeviceInfo>> getEnergyReading(Long projectId, Long consumeTypeId, Long energyTypeId, String order, String part) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(localDate);
        List<Area> areaList = regionAreaService.selectByProjectId(projectId);
        Map<Long, String> areaMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(areaList)) {
            for (Area area : areaList) {
                areaMap.put(area.getId(), area.getName());
            }
        }
        List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
        Map<Long, String> buildingMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(buildingList)) {
            for (Building building : buildingList) {
                buildingMap.put(building.getId(), building.getName());
            }
        }
        List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
        Map<Long, String> storeyMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(storeyList)) {
            for (Storey storey : storeyList) {
                storeyMap.put(storey.getId(), storey.getName());
            }
        }
        List<Room> roomList = regionRoomService.selectByProjectId(projectId);
        Map<Long, String> roomMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(roomList)) {
            for (Room room : roomList) {
                roomMap.put(room.getId(), room.getName());
            }
        }
        if(consumeTypeId.equals(0l)){
            consumeTypeId=null;
        }
        if(energyTypeId.equals(0l)){
            energyTypeId=null;
        }
        List<EnergyMeasureDateData> endData = energyEquipmentDao.getTodayMeasureDate(projectId, consumeTypeId, energyTypeId, format);
        if(!CollectionUtils.isEmpty(endData)){
            Map<String,List<EnergyDeviceInfo>> dataMap=new LinkedHashMap();
            List<EnergyDeviceInfo> totalList = new ArrayList<>();
            List<EnergyDeviceInfo> spikeList = new ArrayList<>();
            List<EnergyDeviceInfo> peakList = new ArrayList<>();
            List<EnergyDeviceInfo> normalList = new ArrayList<>();
            List<EnergyDeviceInfo> valleyList = new ArrayList<>();
            BigDecimal bigTho=new BigDecimal(1000);
            for (EnergyMeasureDateData endDatum : endData) {
                Long equipmentId = endDatum.getEquipmentId();
                String equipmentName = endDatum.getEquipmentName();
                String consumeTypeName = endDatum.getConsumeName();
                String energyTypeName = endDatum.getEnergyTypeName();
                //先处理区域信息
                String location = endDatum.getLocation();
                if (StringUtils.isBlank(location)) {
                    Long areaId = endDatum.getAreaId();
                    Long buildingId = endDatum.getBuildingId();
                    Long storeyId = endDatum.getStoreyId();
                    Long roomId = endDatum.getRoomId();
                    StringBuffer loBuf = new StringBuffer();
                    if (areaMap.containsKey(areaId)) {
                        loBuf.append(areaMap.get(areaId));
                    }
                    if (buildingMap.containsKey(buildingId)) {
                        loBuf.append(buildingMap.get(buildingId));
                    }
                    if (storeyMap.containsKey(storeyId)) {
                        loBuf.append(storeyMap.get(storeyId));
                    }
                    if (roomMap.containsKey(roomId)) {
                        loBuf.append(roomMap.get(roomId));
                    }
                    location = loBuf.toString();
                }
                Integer total = Integer.parseInt(endDatum.getMonthTotal());
                Integer spike = Integer.parseInt(endDatum.getMonthSpike());
                Integer peak = Integer.parseInt(endDatum.getMonthPeak());
                Integer normal = Integer.parseInt(endDatum.getMonthNormal());
                Integer valley = Integer.parseInt(endDatum.getMonthValley());
                if (total.equals(0)) {
                    totalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal totalDecimal = new BigDecimal(total);
                    BigDecimal divide = totalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    totalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (spike.equals(0)) {
                    spikeList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal spikeDecimal = new BigDecimal(spike);
                    BigDecimal divide = spikeDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    spikeList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (peak.equals(0)) {
                    peakList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal peakDecimal = new BigDecimal(peak);
                    BigDecimal divide = peakDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    peakList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (normal.equals(0)) {
                    normalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal normalDecimal = new BigDecimal(normal);
                    BigDecimal divide = normalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    normalList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
                if (valley.equals(0)) {
                    valleyList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, 0f, location));
                } else {
                    BigDecimal valleyDecimal = new BigDecimal(valley);
                    BigDecimal divide = valleyDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    valleyList.add(new EnergyDeviceInfo(equipmentId, equipmentName, energyTypeName, consumeTypeName, divide.floatValue(), location));
                }
            }
            if(StringUtils.isNotBlank(order)){
                if(order.equals("asc")){
                    totalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                    spikeList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                    peakList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                    normalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                    valleyList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData));
                }else{
                    totalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                    spikeList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                    peakList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                    normalList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                    valleyList.sort(Comparator.comparing(EnergyDeviceInfo::getTotalData).reversed());
                }
            }
            List<EnergyDeviceInfo> finalTotal=new ArrayList<>();
            List<EnergyDeviceInfo> finalSpike=new ArrayList<>();
            List<EnergyDeviceInfo> finalPeak=new ArrayList<>();
            List<EnergyDeviceInfo> finalNormal=new ArrayList<>();
            List<EnergyDeviceInfo> finalValley=new ArrayList<>();
            if(part.equals("part")){
                if(totalList.size()>10){
                    for (int i = 0; i <10 ; i++) {
                        finalTotal.add(totalList.get(i));
                    }
                }else{
                    finalTotal=totalList;
                }

                if(spikeList.size()>10){
                    for (int i = 0; i <10 ; i++) {
                        finalSpike.add(spikeList.get(i));
                    }
                }else{
                    finalSpike=spikeList;
                }

                if(peakList.size()>10){
                    for (int i = 0; i <10 ; i++) {
                        finalPeak.add(peakList.get(i));
                    }
                }else{
                    finalPeak=peakList;
                }

                if(normalList.size()>10){
                    for (int i = 0; i <10 ; i++) {
                        finalNormal.add(normalList.get(i));
                    }
                }else{
                    finalNormal=normalList;
                }
                if(valleyList.size()>10){
                    for (int i = 0; i <10 ; i++) {
                        finalValley.add(valleyList.get(i));
                    }
                }else{
                    finalValley=valleyList;
                }
            }else{
                finalTotal=totalList;
                finalSpike=spikeList;
                finalPeak=peakList;
                finalNormal=normalList;
                finalValley=valleyList;
            }
            if(finalTotal.size()>0){
                dataMap.put("total",finalTotal);
            }
            if(finalSpike.size()>0){
                dataMap.put("spike",finalSpike);
            }
            if(finalPeak.size()>0){
                dataMap.put("peak",finalPeak);
            }
            if(finalNormal.size()>0){
                dataMap.put("normal",finalNormal);
            }
            if(finalValley.size()>0){
                dataMap.put("valley",finalValley);
            }
           return  dataMap;
        }
        return null;
    }

    @Override
    public Map<String, List<EnergyConsumeStatisticDetail>> getEquipmentContrastData(Long projectId, Long equipmentId) {
        Map<String,List<EnergyConsumeStatisticDetail>> finalDataMap=new LinkedHashMap<>();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter formatter1=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date=LocalDate.now();
        String format1 = formatter.format(date);
        String[] split1 = format1.split("-");
        String month=split1[1];
        String format2 = formatter1.format(date);
        int monthValue = date.getMonthValue();
        String monthStr=String.valueOf(monthValue);

        LocalDate localDate = date.plusMonths(-1);
        int lastMonthInt = localDate.getMonthValue();
        String lastMonthStr = String.valueOf(lastMonthInt);
        String format = formatter.format(localDate);
        String[] split = format.split("-");
        String year = split[0];
        String lastMonth = split[1];

        EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentId);
        //判断绑定设备信息
        if(energyEquipmentDevice==null){
            return null;
        }
        Long deviceId = energyEquipmentDevice.getDeviceId();
        List<MeasureDateData> dataList=deviceService.getLastMonthDayMeasureData(deviceId,year,lastMonth);
        Map<Integer,MeasureDateData> dataMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(dataList)){
            for (MeasureDateData measureDateData : dataList) {
                dataMap.put(Integer.parseInt(measureDateData.getDay()),measureDateData);
            }
        }
        EnergyConsumeStatisticDetail totalLast=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail spikeLast=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail peakLast=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail normalLast=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail valleyLast=new EnergyConsumeStatisticDetail();
        BigDecimal bigTho=new BigDecimal(1000);
        for (int i = 1; i <32 ; i++) {
            if(dataMap.containsKey(i)){
                MeasureDateData measureDateData = dataMap.get(i);
                String total = measureDateData.getTotal();
                String spike = measureDateData.getSpike();
                String peak = measureDateData.getPeak();
                String normal = measureDateData.getNormal();
                String valley = measureDateData.getValley();
                Float totalFlo=0f;
                if(!total.equals("0")){
                    BigDecimal bigTotal=new BigDecimal(total);
                    BigDecimal divide = bigTotal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    totalFlo=divide.floatValue();
                }
                totalLast.setMonth(lastMonthStr);
                totalLast.getDayConsume().add(new DayConsumeData(String.valueOf(i),totalFlo));

                Float spikeFlo=0f;
                if(!spike.equals("0")){
                    BigDecimal bigSpike=new BigDecimal(spike);
                    BigDecimal divide = bigSpike.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    spikeFlo=divide.floatValue();
                }
                spikeLast.setMonth(lastMonthStr);
                spikeLast.getDayConsume().add(new DayConsumeData(String.valueOf(i),spikeFlo));

                Float peakFlo=0f;
                if(!peak.equals("0")){
                    BigDecimal bigPeak=new BigDecimal(peak);
                    BigDecimal divide = bigPeak.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    peakFlo=divide.floatValue();
                }
                peakLast.setMonth(lastMonthStr);
                peakLast.getDayConsume().add(new DayConsumeData(String.valueOf(i),peakFlo));

                Float normalFlo=0f;
                if(!normal.equals("0")){
                    BigDecimal bigNormal=new BigDecimal(normal);
                    BigDecimal divide = bigNormal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    normalFlo=divide.floatValue();
                }
                normalLast.setMonth(lastMonthStr);
                normalLast.getDayConsume().add(new DayConsumeData(String.valueOf(i),normalFlo));

                Float valleyFlo=0f;
                if(!valley.equals("0")){
                    BigDecimal bigValley=new BigDecimal(valley);
                    BigDecimal divide = bigValley.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    valleyFlo=divide.floatValue();
                }
                valleyLast.setMonth(lastMonthStr);
                valleyLast.getDayConsume().add(new DayConsumeData(String.valueOf(i),valleyFlo));
            }

        }
        List<EnergyConsumeStatisticDetail> totalList=new ArrayList<>();
        List<EnergyConsumeStatisticDetail> spikeList=new ArrayList<>();
        List<EnergyConsumeStatisticDetail> peakList=new ArrayList<>();
        List<EnergyConsumeStatisticDetail> normalList=new ArrayList<>();
        List<EnergyConsumeStatisticDetail> valleyList=new ArrayList<>();
        totalList.add(totalLast);
        spikeList.add(spikeLast);
        peakList.add(peakLast);
        normalList.add(normalLast);
        valleyList.add(valleyLast);
        //处理本月
        List<MeasureDateData> monthList=deviceService.getLastMonthDayMeasureData(deviceId,year,month);
        MeasureDateData measureDateDataToday=deviceService.getTodayMonthMeasure(deviceId,format2);
        EnergyConsumeStatisticDetail totalMonth=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail spikeMonth=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail peakMonth=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail normalMonth=new EnergyConsumeStatisticDetail();
        EnergyConsumeStatisticDetail valleyMonth=new EnergyConsumeStatisticDetail();
        totalMonth.setMonth(monthStr);
        spikeMonth.setMonth(monthStr);
        peakMonth.setMonth(monthStr);
        normalMonth.setMonth(monthStr);
        valleyMonth.setMonth(monthStr);
        if(CollectionUtils.isEmpty(monthList)){
            if(measureDateDataToday!=null){
                monthList=new ArrayList<>();
                monthList.add(measureDateDataToday);
            }
        }else{
            if(measureDateDataToday!=null){
                monthList.add(measureDateDataToday);
            }
        }
        if(!CollectionUtils.isEmpty(monthList)){
            for (MeasureDateData measureDateData : monthList) {
                String total = measureDateData.getTotal();
                String spike = measureDateData.getSpike();
                String peak = measureDateData.getPeak();
                String normal = measureDateData.getNormal();
                String valley = measureDateData.getValley();
                String day = measureDateData.getDay();
                int i = Integer.parseInt(day);

                Float totalFlo=0f;
                if(!total.equals("0")){
                    BigDecimal bigTotal=new BigDecimal(total);
                    BigDecimal divide = bigTotal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    totalFlo=divide.floatValue();
                }
                totalMonth.getDayConsume().add(new DayConsumeData(String.valueOf(i),totalFlo));

                Float spikeFlo=0f;
                if(!spike.equals("0")){
                    BigDecimal bigSpike=new BigDecimal(spike);
                    BigDecimal divide = bigSpike.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    spikeFlo=divide.floatValue();
                }
                spikeMonth.getDayConsume().add(new DayConsumeData(String.valueOf(i),spikeFlo));

                Float peakFlo=0f;
                if(!peak.equals("0")){
                    BigDecimal bigPeak=new BigDecimal(peak);
                    BigDecimal divide = bigPeak.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    peakFlo=divide.floatValue();
                }
                peakMonth.getDayConsume().add(new DayConsumeData(String.valueOf(i),peakFlo));

                Float normalFlo=0f;
                if(!normal.equals("0")){
                    BigDecimal bigNormal=new BigDecimal(normal);
                    BigDecimal divide = bigNormal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    normalFlo=divide.floatValue();
                }
                normalMonth.getDayConsume().add(new DayConsumeData(String.valueOf(i),normalFlo));

                Float valleyFlo=0f;
                if(!valley.equals("0")){
                    BigDecimal bigValley=new BigDecimal(valley);
                    BigDecimal divide = bigValley.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    valleyFlo=divide.floatValue();
                }
                valleyMonth.getDayConsume().add(new DayConsumeData(String.valueOf(i),valleyFlo));
            }


        }
        totalList.add(totalMonth);
        spikeList.add(spikeMonth);
        peakList.add(peakMonth);
        normalList.add(normalMonth);
        valleyList.add(valleyMonth);
        finalDataMap.put("total",totalList);
        finalDataMap.put("spike",spikeList);
        finalDataMap.put("peak",peakList);
        finalDataMap.put("normal",normalList);
        finalDataMap.put("valley",valleyList);
        return finalDataMap;
    }

    @Override
    public Map<String, List<EnergyYearMonthConsumeDate>> getYearMonthConsumeData(Long projectId) {
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        //设置上个月的最后一天
        LocalDate localDate2 = localDate.plusMonths(-5);
        String[] yearMonthDate = DateUtils.getPastSixYearMonth();
        Map<String, List<EnergyYearMonthConsumeDate>> map = new LinkedHashMap<>();
        List<MeasureIntegerDate> dataList = energyEquipmentDao.getYearMonthData(projectId, localDate, localDate2);
        MeasureIntegerDate dataNowMonth = energyEquipmentDao.getMonthData(projectId, dateTimeFormatter.format(localDate));
        Map<String, MeasureIntegerDate> measureIntegerDateMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (MeasureIntegerDate measureIntegerDate : dataList) {
                String yearMonth = measureIntegerDate.getYearMonth();
                measureIntegerDateMap.put(yearMonth, measureIntegerDate);
            }
        }
        if(dataNowMonth==null){
            dataNowMonth=new MeasureIntegerDate(0,0,0,0,0,yearMonthFormatter.format(localDate),"");
        }
        measureIntegerDateMap.put(yearMonthFormatter.format(localDate), dataNowMonth);
        List<EnergyYearMonthConsumeDate> totalList = new ArrayList<>();
        List<EnergyYearMonthConsumeDate> spikeList = new ArrayList<>();
        List<EnergyYearMonthConsumeDate> peakList = new ArrayList<>();
        List<EnergyYearMonthConsumeDate> normalList = new ArrayList<>();
        List<EnergyYearMonthConsumeDate> valleyList = new ArrayList<>();
        BigDecimal bigTho = new BigDecimal(1000);
        for (String yearMonth : yearMonthDate) {
            if (measureIntegerDateMap.containsKey(yearMonth)) {
                MeasureIntegerDate measureIntegerDate = measureIntegerDateMap.get(yearMonth);
                Integer total = measureIntegerDate.getTotal();
                Integer spike = measureIntegerDate.getSpike();
                Integer peak = measureIntegerDate.getPeak();
                Integer normal = measureIntegerDate.getNormal();
                Integer valley = measureIntegerDate.getValley();
                if (!total.equals(0)) {
                    BigDecimal totalDec = new BigDecimal(total);
                    BigDecimal divide = totalDec.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    float totalValue = divide.floatValue();
                    totalList.add(new EnergyYearMonthConsumeDate(yearMonth, totalValue));
                } else {
                    totalList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                }
                if (!spike.equals(0)) {
                    BigDecimal spikeDec = new BigDecimal(spike);
                    BigDecimal divide = spikeDec.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    float spikeValue = divide.floatValue();
                    spikeList.add(new EnergyYearMonthConsumeDate(yearMonth, spikeValue));
                } else {
                    spikeList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                }
                if (!peak.equals(0)) {
                    BigDecimal peakDec = new BigDecimal(peak);
                    BigDecimal divide = peakDec.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    float peakValue = divide.floatValue();
                    peakList.add(new EnergyYearMonthConsumeDate(yearMonth, peakValue));
                } else {
                    peakList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                }
                if (!normal.equals(0)) {
                    BigDecimal normalDec = new BigDecimal(normal);
                    BigDecimal divide = normalDec.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    float normalValue = divide.floatValue();
                    normalList.add(new EnergyYearMonthConsumeDate(yearMonth, normalValue));
                } else {
                    normalList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                }
                if (!valley.equals(0)) {
                    BigDecimal valleyDec = new BigDecimal(valley);
                    BigDecimal divide = valleyDec.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    float valleyValue = divide.floatValue();
                    valleyList.add(new EnergyYearMonthConsumeDate(yearMonth, valleyValue));
                } else {
                    valleyList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                }
            } else {
                totalList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                spikeList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                peakList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                normalList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
                valleyList.add(new EnergyYearMonthConsumeDate(yearMonth, 0f));
            }
        }
        map.put("total", totalList);
        map.put("spike", spikeList);
        map.put("peak", peakList);
        map.put("normal", normalList);
        map.put("valley", valleyList);
        return map;
    }

    @Override
    public EnergyEquipment findByName(String name, Long projectId) {
        return energyEquipmentDao.findByName(name,projectId);
    }

    @Override
    public EquipmentInfo getEquipmentManagerInfo(Long projectId, Long equipmentId) {
        EnergyEquipment energyEquipment = energyEquipmentDao.selectByPrimaryKey(equipmentId);
        if(energyEquipment!=null){
            EquipmentInfo equipmentInfo=new EquipmentInfo();
            equipmentInfo.setId(energyEquipment.getId());
            equipmentInfo.setBrand(energyEquipment.getBrand());
            equipmentInfo.setName(energyEquipment.getName());
            equipmentInfo.setProductionDate(energyEquipment.getProductionDate());
            equipmentInfo.setRatedPower(energyEquipment.getRatedPower());
            String location = energyEquipment.getLocation();
            if(StringUtils.isNotBlank(location)){
                equipmentInfo.setLocation(location);
            }else{
                StringBuffer locBuffer=new StringBuffer();
                Long areaId = energyEquipment.getAreaId();
                if(areaId!=null&& !areaId.equals(0l)){
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if(area!=null){
                        locBuffer.append(area.getName());
                    }
                }
                Long buildingId = energyEquipment.getBuildingId();
                Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                if(building!=null){
                    locBuffer.append(building.getName());
                }
                Long storeyId = energyEquipment.getStoreyId();
                Storey storey = regionStoreyService.selectByPrimaryKey(storeyId);
                if(storey!=null){
                    locBuffer.append(storey.getName());
                }
                Long roomId = energyEquipment.getRoomId();
                Room room = regionRoomService.selectByPrimaryKey(roomId);
                if(room!=null){
                    locBuffer.append(room.getName());
                }
                equipmentInfo.setLocation(locBuffer.toString());
            }
            EnergyType energyType=energyTypeService.selectByPrimaryKey(energyEquipment.getEnergyTypeId());
            if(energyType!=null){
                equipmentInfo.setEnergyTypeName(energyType.getName());
            }
            EnergyConsumeType energyConsumeType = energyConsumeTypeService.findById(energyEquipment.getConsumeTypeId());
            if(energyConsumeType!=null){
                equipmentInfo.setConsumeTypeName(energyConsumeType.getName());
            }
            Long companyId = energyEquipment.getCompanyId();
            if(companyId!=null&& !companyId.equals(0l)){
                Company company = companyService.findByid(companyId);
                if(company!=null){
                    equipmentInfo.setCompanyName(company.getName());
                }
            }
            EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentId);
            if(energyEquipmentDevice!=null){
                Long deviceId = energyEquipmentDevice.getDeviceId();
                Device device = deviceService.findSystemNameDevice(deviceId,3000l);
                String name = device.getName();
                equipmentInfo.setDeviceName(name);
                DeviceType deviceType = deviceTypeService.findByid(device.getDeviceTypeId());
                if(deviceType!=null){
                    equipmentInfo.setDeviceTypeName(deviceType.getName());
                }

            }
            equipmentInfo.setProjectId(projectId);
            return equipmentInfo;
        }
        return null;
    }

    @Override
    public EquipmentVariable getVariableParam(Long projectId, Long equipmentId) {
        EquipmentVariable equipmentVariable=energyEquipmentDao.getVariableParam(equipmentId,projectId);
        if(equipmentVariable!=null){
            String location = equipmentVariable.getLocation();
            equipmentVariable.setLocationFlag(1);
            if(StringUtils.isBlank(location)){
                equipmentVariable.setLocationFlag(0);
                Long areaId = equipmentVariable.getAreaId();
                if(areaId!=null && !areaId.equals(0l)){
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if(area!=null){
                        equipmentVariable.setAreaName(area.getName());
                    }
                }
                Long buildingId = equipmentVariable.getBuildingId();
                Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                if(building!=null){
                    equipmentVariable.setBuildingName(building.getName());
                }
                Long storeyId = equipmentVariable.getStoreyId();
                Storey storey = regionStoreyService.selectByPrimaryKey(storeyId);
                if(storey!=null){
                    equipmentVariable.setStoreyName(storey.getName());
                }
                Long roomId = equipmentVariable.getRoomId();
                Room room = regionRoomService.selectByPrimaryKey(roomId);
                if(room!=null){
                    equipmentVariable.setRoomName(room.getName());
                }
            }
            EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentId);
            if(energyEquipmentDevice!=null){
                Long deviceId = energyEquipmentDevice.getDeviceId();
                Device device = deviceService.findSystemNameDevice(deviceId,3000l);
                DeviceType deviceType = deviceTypeService.findByid(device.getDeviceTypeId());
                equipmentVariable.setDeviceId(deviceId);
                equipmentVariable.setDeviceName(device.getName());
                equipmentVariable.setDeviceTypeId(deviceType.getId());
                equipmentVariable.setDeviceTypeName(deviceType.getName());
            }
            return  equipmentVariable;


        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateParam(EquipmentVariable equipmentVariable) {
        String msg=checkParam(equipmentVariable);
        if(StringUtils.isNotBlank(msg)){
            return msg;
        }
        Date updateTime=new Date();
        Long id = equipmentVariable.getId();
        Boolean fla=false;
        EnergyEquipment energyEquipment=new EnergyEquipment();
        energyEquipment.setId(id);
        String name = equipmentVariable.getName();
        if(StringUtils.isNotBlank(name)){
            fla=true;
            energyEquipment.setName(name);
        }
        String brand = equipmentVariable.getBrand();
        if(StringUtils.isNotBlank(brand)){
            fla=true;
            energyEquipment.setBrand(brand);
        }
        Date productionDate = equipmentVariable.getProductionDate();
        if(productionDate!=null){
            fla=true;
            energyEquipment.setProductionDate(productionDate);
        }
        String ratedPower = equipmentVariable.getRatedPower();
        if(StringUtils.isNotBlank(ratedPower)){
            fla=true;
            energyEquipment.setRatedPower(ratedPower);
        }
        Integer locationFlag = equipmentVariable.getLocationFlag();
        if(locationFlag!=null){
            fla=true;
            if(locationFlag.equals(0)){
                Long areaId = equipmentVariable.getAreaId();
                Long buildingId = equipmentVariable.getBuildingId();
                Long storeyId = equipmentVariable.getStoreyId();
                Long roomId = equipmentVariable.getRoomId();
                if(areaId.equals(0l)){
                    energyEquipmentDao.updateAreaNull(id);
                }else{
                    energyEquipment.setAreaId(areaId);
                }
                if(buildingId!=null){
                    energyEquipment.setBuildingId(buildingId);
                }
                if(storeyId!=null){
                    energyEquipment.setStoreyId(storeyId);
                }
                if(roomId!=null) {
                    energyEquipment.setRoomId(roomId);
                }
            }else{
                String location = equipmentVariable.getLocation();
                energyEquipment.setLocation(location);
            }
        }
        Long companyId = equipmentVariable.getCompanyId();
        if(companyId!=null){
            fla=true;
            if(companyId.equals(0l)){
                energyEquipmentDao.updateCompanyIdNull(id);
            }else{
                energyEquipment.setCompanyId(companyId);
            }
        }
        if(fla){
            energyEquipment.setUpdateTime(updateTime);
            energyEquipmentDao.updateByPrimaryKeySelective(energyEquipment);
        }

        //处理关联设备的终端设备
        Long deviceId = equipmentVariable.getDeviceId();
        if(deviceId!=null){
            EnergyEquipmentDevice energyEquipmentDevice = equipmentDeviceService.selectByEquipmentId(equipmentVariable.getId());
            if(!deviceId.equals(0l)){
                Device device = deviceService.findById(deviceId);
                if(energyEquipmentDevice!=null){
                    //更新旧的绑定信息
                    Long deviceId1 = energyEquipmentDevice.getDeviceId();
                    Device deviceOld=new Device();
                    deviceOld.setId(deviceId1);
                    deviceOld.setBindingStatus(0);
                    deviceOld.setBindingType(-1);
                    deviceService.update(deviceOld);
                    //更新终端设备绑定信息
                    EnergyEquipmentDevice energyEquipmentDeviceUpdate=new EnergyEquipmentDevice();
                    energyEquipmentDeviceUpdate.setId(energyEquipmentDevice.getId());
                    energyEquipmentDeviceUpdate.setDeviceId(deviceId);
                    energyEquipmentDeviceUpdate.setDeviceTypeId(device.getDeviceTypeId());
                    energyEquipmentDeviceUpdate.setUpdateTime(updateTime);
                    energyEquipmentDeviceService.update(energyEquipmentDeviceUpdate);
                }else{
                    EnergyEquipmentDevice energyEquipmentDeviceUpdate=new EnergyEquipmentDevice();
                    energyEquipmentDeviceUpdate.setEquipmentId(id);
                    energyEquipmentDeviceUpdate.setDeviceTypeId(device.getDeviceTypeId());
                    energyEquipmentDeviceUpdate.setDeviceId(deviceId);
                    energyEquipmentDeviceUpdate.setSystemId(3000L);
                    energyEquipmentDeviceUpdate.setCreateTime(updateTime);
                    energyEquipmentDeviceUpdate.setUpdateTime(updateTime);
                    energyEquipmentDeviceUpdate.setProjectId(device.getProjectId());
                    energyEquipmentDeviceService.save(energyEquipmentDeviceUpdate);
                }
                //更新新的绑定关系
                Device deviceNew =new Device();
                deviceNew.setId(deviceId);
                deviceNew.setBindingStatus(1);
                deviceNew.setBindingType(6);
                deviceService.update(deviceNew);
            }else{
                //更新旧的绑定关系
                if(energyEquipmentDevice!=null){
                    Long deviceId1 = energyEquipmentDevice.getDeviceId();
                    Device deviceOld=new Device();
                    deviceOld.setId(deviceId1);
                    deviceOld.setBindingStatus(0);
                    deviceOld.setBindingType(-1);
                    deviceService.update(deviceOld);
                }
                energyEquipmentDeviceService.removeByEquipmentId(id);
            }
        }
        return null;
    }

    @Override
    public List<EnergyEquipment> getByProjectId(Long projectId) {
        return energyEquipmentDao.getByProjectId(projectId);
    }

    @Override
    public List<Map<String, Object>> getDayMonthStatistic(Long projectId) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        return energyEquipmentDao.getDayMonthStatistic(projectId,format);
    }

    @Override
    public Integer getYesterDayStatistic(Long projectId) {
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.plusDays(-1);
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate);
        return energyEquipmentDao.getPastStatistic(projectId,format);
    }

    @Override
    public List<Map<String, Object>> getTodayMeasure(Long projectId) {
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        return energyEquipmentDao.getDayHourMeasure(projectId,format);
    }

    @Override
    public List<Map<String, Object>> getLastMonthMeasure(Long projectId,String yearMonth) {
        String[] split = yearMonth.split("-");
        String year=split[0];
        String month=split[1];
        return energyEquipmentDao.getLastMonthMeasure(projectId,year,month);
    }

    @Override
    public List<Map<String, Object>> getMonthMeasure(Long projectId,String yearMonth) {
        String[] split = yearMonth.split("-");
        String year=split[0];
        String month=split[1];
        return energyEquipmentDao.getMonthMeasure(projectId,year,month);
    }

    @Override
    public List<Map<String, Object>> getMonthTodayMeasure(Long projectId, String format) {
        return energyEquipmentDao.getMonthTodayMeasure(projectId,format);
    }

    @Override
    public List<EquipmentDateTotalMeasure> getTotalTotalMeasure(Long projectId, String yearMonthDay) {
        return energyEquipmentDao.getTotalTotalMeasure(projectId,yearMonthDay);
    }

    @Override
    public List<EquipmentDateTotalMeasure> getLastMeasure(Long projectId, String yearLast) {
        return energyEquipmentDao.getLastMeasure(projectId,yearLast);
    }

    @Override
    public List<EquipmentDateTotalMeasure> getMonthTotalMeasure(Long projectId, String yearMonthDay) {
        return energyEquipmentDao.getMonthTotalMeasure(projectId,yearMonthDay);
    }

    @Override
    public List<EquipmentDateTotalMeasure> getTodayTotalMeasure(Long projectId, String yearMonthDay) {
        return energyEquipmentDao.getTodayTotalMeasure(projectId,yearMonthDay);
    }

    @Override
    public List<Map<String, Object>> getSmartActivePowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSmartActivePowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getSuperActivePowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSuperActivePowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getSmartReactivePowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSmartReactivePowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getSuperReactivePowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSuperReactivePowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getSmartApparentPowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSmartApparentPowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getSuperApparentPowerData(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getSuperApparentPowerData(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getPowerAndApparentPowerList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getPowerAndApparentPowerList(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getPowerAndApparentPowerSuperList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getPowerAndApparentPowerSuperList(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getThdvSmartList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getThdvSmartList(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getThdiSmartList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getThdiSmartList(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getThdvSuperList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getThdvSuperList(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getThdiSuperList(Long projectId, Long consumeTypeId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        if(consumeTypeId.equals(-1l)){
            consumeTypeId=null;
        }
        return energyEquipmentDao.getThdiSuperList(projectId,consumeTypeId,format);
    }


    @Override
    public List<EnergyHourDifferData> getHourDiffData(Long projectId, String format) {
        return energyEquipmentDao.getHourDiffData(projectId,format);
    }

    @Override
    public EnergyDayData getDayTotal(Long projectId, String format) {
        return energyEquipmentDao.getDayTotal(projectId,format);
    }

    @Override
    public EnergyMonthData getMonthLastMeasure(Long projectId) {
        LocalDate localDate=LocalDate.now();
        LocalDate localDate1 = localDate.plusMonths(-1);
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(localDate1);
        String[] split = format.split("-");
        String year=split[0];
        String month=split[1];
        return energyEquipmentDao.getMonthLastMeasure(year,month,projectId);
    }

    @Override
    public List<EnergyMeasureDataEntity> getEnergyPastDateStatistic(Long projectId, Long consumeTypeId, String begTime) {
        return energyEquipmentDao.getEnergyPastDateStatistic(projectId,consumeTypeId,begTime);
    }

    @Override
    public List<EnergyMeasureDataEntity> getEnergyPastTodayDateStatistic(Long projectId, Long consumeTypeId, String format) {
        return energyEquipmentDao.getEnergyPastTodayDateStatistic(projectId,consumeTypeId,format);
    }

    @Override
    public List<Map<String, Object>> getYearMonthMeasure(Long projectId, String year) {
        return energyEquipmentDao.getYearMonthMeasure(projectId,year);
    }

    @Override
    public List<Map<String, Object>> getMonthTotal(Long projectId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        return energyEquipmentDao.getMonthTotal(projectId,format);
    }

    @Override
    public Long getCount(Long projectId) {
        return energyEquipmentDao.getCount(projectId);
    }

    @Override
    public List<Map<String, Object>> getMonthDetail(Long projectId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        return energyEquipmentDao.getMonthDetail(projectId,format);
    }

    @Override
    public List<Map<String, Object>> getLastMonthDetail(Long projectId, String year, String month) {
        return energyEquipmentDao.getLastMonthDetail(projectId,year,month);
    }

    @Override
    public List<Map<String, Object>> getDayAndMonthMeasure(Long projectId,String yearMonthDay) {
        return energyEquipmentDao.getDayAndMonthMeasure(projectId,yearMonthDay);
    }

    @Override
    public List<Map<String, Object>> getLastDayConsumeMeasure(Long projectId, String yearMonthDay) {
        return energyEquipmentDao.getLastDayConsumeMeasure(projectId,yearMonthDay);
    }

    @Override
    public List<Map<String, Object>> getLastMonthTotalMeasure(Long projectId, String yearStr, String lastMonthStr) {
        return energyEquipmentDao.getLastMonthTotalMeasure(projectId,yearStr,lastMonthStr);
    }

    @Override
    public List<Map<String, Object>> getDataMonthMapByList(Long projectId, String year, List<String> monthList) {
        return energyEquipmentDao.getDataMonthMapByList(projectId,year,monthList);

    }

    @Override
    public List<Map<String, Object>> getDataYearMeasure(Long projectId, String year) {
        return energyEquipmentDao.getDataYearMeasure(projectId,year);
    }

    @Override
    public List<Map<String, Object>> getDataYearTotalMeasure(Long projectId, String year) {
        return energyEquipmentDao.getDataYearTotalMeasure(projectId,year);
    }

    @Override
    public List<Map<String, Object>> getMonthTotalLastMeasure(Long projectId, String year, String month) {
        return energyEquipmentDao.getMonthTotalLastMeasure(projectId,year,month);
    }

    /**
     * 检验参数信息
     * @param equipmentVariable
     * @return
     */
    private String checkParam(EquipmentVariable equipmentVariable) {
        List<String> msgList=new ArrayList<>();
        String name = equipmentVariable.getName();
        Long id = equipmentVariable.getId();
        Long projectId = equipmentVariable.getProjectId();
        EnergyEquipment energyEquipment = energyEquipmentDao.selectByPrimaryKey(id);
        if(name!=null){
           if(energyEquipment.getName().equals(name)){
               msgList.add("名称参数信息错误");
           }else{
               EnergyEquipment equipmentDaoByName = energyEquipmentDao.findByName(name, projectId);
               if(equipmentDaoByName!=null){
                   msgList.add("名称参数重复错误");
               }
           }
        }
        if(!CollectionUtils.isEmpty(msgList)){
            return StringUtils.join(msgList,",");
        }
        return null;
    }

    @Override
    public List<Map<String,Object>> getDayMeasure(Long projectId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yearMonthDay = dateTimeFormatter.format(now);
        return energyEquipmentDao.getDayMeasure(projectId,yearMonthDay);
    }

    @Override
    public Long getPastMonth(List<String> monthListNow, Long projectId, String year) {
        return energyEquipmentDao.getPastMonth(monthListNow,projectId,year);
    }

    @Override
    public List<EquipmentDateTotalMeasure> getPastYearMonth(Long projectId, String year) {
        return energyEquipmentDao.getPastYearMonth(projectId,year);
    }

    @Override
    public List<Map<String, Object>> getLastYearMonth(Long projectId, String year) {
        return energyEquipmentDao.getLastYearMonth(projectId,year);
    }

    @Override
    public List<EquipmentRankReport> getRankReportMonthData(Long projectId, String year,String month) {
        return energyEquipmentDao.getRankReportMonthData(projectId,year,month);
    }

    @Override
    public List<EquipmentRankReport> getRankReportLast(Long projectId, String year) {
        return energyEquipmentDao.getRankReportLast(projectId,year);
    }

    @Override
    public List<Map<String, Object>> getMonthDataDetail(Long projectId) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        String[] split = format.split("-");
        String year=split[0];
        String month=split[1];
        return energyEquipmentDao.getMonthDataDetail(projectId,year,month);
    }

    @Override
    public List<EnergyStatus> getEnergyStatistic(Long projectId) {
        return energyEquipmentDao.getEnergyStatistic(projectId);
    }

    @Override
    public List<Map<String, Object>> getEquipmentTotal(Long projectId) {
        return energyEquipmentDao.getEquipmentTotal(projectId);
    }
}
