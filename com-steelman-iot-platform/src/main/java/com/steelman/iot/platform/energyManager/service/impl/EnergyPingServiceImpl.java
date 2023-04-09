package com.steelman.iot.platform.energyManager.service.impl;

import com.steelman.iot.platform.energyManager.dto.*;
import com.steelman.iot.platform.energyManager.entity.EquipmentDateTotalMeasure;
import com.steelman.iot.platform.energyManager.entity.MonthMeasureStatistic;
import com.steelman.iot.platform.energyManager.service.EnergyPingService;
import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.EnergyConsumeType;
import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.dto.EnergyReportDataDetail;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.EquipmentReportInfo;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.service.impl.BaseService;
import com.steelman.iot.platform.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service("energyPingService")
public class EnergyPingServiceImpl extends BaseService implements EnergyPingService {
    @Resource
    private DeviceService deviceService;
    @Resource
    private EnergyEquipmentService energyEquipmentService;
    @Resource
    private EnergyEquipmentDeviceService energyEquipmentDeviceService;
    @Resource
    private EnergyConsumeTypeService energyConsumeTypeService;
    @Resource
    private ProjectService projectService;
    @Resource
    private AlarmWarnService alarmWarnService;


    @Override
    public CountStatistic countStatistic(Long projectId) {
        CountStatistic countStatistic = new CountStatistic(0, 0, 0);
        List<EnergyEquipment> energyEquipmentList = energyEquipmentService.getByProjectId(projectId);
        if (!CollectionUtils.isEmpty(energyEquipmentList)) {
            List<Device> deviceList = deviceService.getByProjectId(projectId);
            Map<Long, Integer> deviceStatus = new HashMap<>();
            if (!CollectionUtils.isEmpty(deviceList)) {
                for (Device device : deviceList) {
                    deviceStatus.put(device.getId(), device.getStatus());
                }
            }
            List<EnergyEquipmentDevice> energyEquipmentDeviceList = energyEquipmentDeviceService.findByProjectId(projectId);
            Map<Long, Long> equipmentDeviceMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(energyEquipmentDeviceList)) {
                for (EnergyEquipmentDevice energyEquipmentDevice : energyEquipmentDeviceList) {
                    equipmentDeviceMap.put(energyEquipmentDevice.getEquipmentId(), energyEquipmentDevice.getDeviceId());
                }
            }
            Integer total = energyEquipmentList.size();
            Integer inline = 0;
            Integer outLine = 0;
            for (EnergyEquipment energyEquipment : energyEquipmentList) {
                Long equipmentId = energyEquipment.getId();
                if (equipmentDeviceMap.containsKey(equipmentId)) {
                    Long deviceId = equipmentDeviceMap.get(equipmentId);
                    if (deviceStatus.containsKey(deviceId)) {
                        Integer status = deviceStatus.get(deviceId);
                        if (status.equals(2) || status.equals(4)) {
                            outLine++;
                        } else {
                            inline++;
                        }
                    } else {
                        outLine++;

                    }
                } else {
                    outLine++;
                }
            }
            countStatistic.setTotal(total);
            countStatistic.setInline(inline);
            countStatistic.setOutLine(outLine);
        }
        return countStatistic;
    }

    @Override
    public MeasureDayMonthStatistic dayStatistic(Long projectId) {
        MeasureDayMonthStatistic measureDayMonthStatistic = new MeasureDayMonthStatistic("0", "0", "0", "0");
        List<Map<String, Object>> dataMap = energyEquipmentService.getDayMonthStatistic(projectId);
        Integer yesterdayStatistic = energyEquipmentService.getYesterDayStatistic(projectId);
        Long dayTotal = 0l;
        Long monthTotal = 0l;
        if (!CollectionUtils.isEmpty(dataMap)) {
            for (Map<String, Object> stringObjectMap : dataMap) {
                dayTotal = Long.parseLong(stringObjectMap.get("dayTotal").toString());
                monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
            }
        }

        BigDecimal bigDecimal1 = new BigDecimal("997");
        BigDecimal bigDecimal2 = new BigDecimal("1000");
        if (!dayTotal.equals(0)) {
            BigDecimal bigDecimal = new BigDecimal(dayTotal);
            BigDecimal divide1 = bigDecimal.divide(bigDecimal2, 3, BigDecimal.ROUND_HALF_UP);
            measureDayMonthStatistic.setDayTotal(divide1.toString());
            BigDecimal divide = bigDecimal.divide(bigDecimal1, 3, BigDecimal.ROUND_HALF_UP);
            measureDayMonthStatistic.setDayEmissions(divide.toString());
        }
        if (yesterdayStatistic != null && (!yesterdayStatistic.equals(0))) {
            BigDecimal bigDecimal = new BigDecimal(yesterdayStatistic);
            BigDecimal divide = bigDecimal.divide(bigDecimal2, 3, BigDecimal.ROUND_HALF_UP);
            measureDayMonthStatistic.setYesterDayTotal(divide.toString());
        }
        if (!monthTotal.equals(0)) {
            BigDecimal bigDecimal = new BigDecimal(monthTotal);
            BigDecimal divide = bigDecimal.divide(bigDecimal2, 3, BigDecimal.ROUND_HALF_UP);
            measureDayMonthStatistic.setMonthTotal(divide.toString());
        }

        return measureDayMonthStatistic;
    }

    @Override
    public Map<String, Object> getHourMeasureStatistic(Long projectId) {
        List<Map<String, Object>> dataList = energyEquipmentService.getTodayMeasure(projectId);
        List<String> hourDate = DateUtils.getDayHour();
        Map<String, HourMeasureDataStatistic> dataMap = new HashMap<>();
        Map<String, HourMeasureDataStatistic> hourMeasureMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(dataList)) {
            BigDecimal bigDecimal = new BigDecimal("1000");
            for (Map<String, Object> stringObjectMap : dataList) {

                HourMeasureDataStatistic hourMeasureDataStatistic = new HourMeasureDataStatistic();

                String dateTime = stringObjectMap.get("dateTime").toString();
                String hour = dateTime.split(" ")[1].split(":")[0];
                if (hour.equals("00")) {
                    hour = "24";
                }
                hourMeasureDataStatistic.setHour(hour);
                String yearMonthDay = stringObjectMap.get("yearMonthDay").toString();
                hourMeasureDataStatistic.setDateDetail(yearMonthDay);
                String daySpike = stringObjectMap.get("daySpike").toString();
                if (daySpike.equals("0")) {
                    hourMeasureDataStatistic.setDaySpike(0);
                } else {
                    BigDecimal bigDecimalSpike = new BigDecimal(daySpike);
                    BigDecimal divideSpike = bigDecimalSpike.divide(bigDecimal, 3, BigDecimal.ROUND_HALF_UP);
                    hourMeasureDataStatistic.setDaySpike(divideSpike.floatValue());
                }


                String dayPeak = stringObjectMap.get("dayPeak").toString();
                if (dayPeak.equals("0")) {
                    hourMeasureDataStatistic.setDayPeak(0);
                } else {
                    BigDecimal bigDecimalPeak = new BigDecimal(dayPeak);
                    BigDecimal dividePeak = bigDecimalPeak.divide(bigDecimal, 3, BigDecimal.ROUND_HALF_UP);
                    hourMeasureDataStatistic.setDayPeak(dividePeak.floatValue());
                }

                String dayNormal = stringObjectMap.get("dayNormal").toString();
                if (dayNormal.equals("0")) {
                    hourMeasureDataStatistic.setDayNormal(0);
                } else {
                    BigDecimal bigDecimalNormal = new BigDecimal(dayNormal);
                    BigDecimal divideNormal = bigDecimalNormal.divide(bigDecimal, 3, BigDecimal.ROUND_HALF_UP);
                    hourMeasureDataStatistic.setDayNormal(divideNormal.floatValue());
                }

                String dayValley = stringObjectMap.get("dayValley").toString();
                if (dayValley.equals("0")) {
                    hourMeasureDataStatistic.setDayValley(0);
                } else {
                    BigDecimal bigDecimalValley = new BigDecimal(dayValley);
                    BigDecimal divideValley = bigDecimalValley.divide(bigDecimal, 3, BigDecimal.ROUND_HALF_UP);
                    hourMeasureDataStatistic.setDayValley(divideValley.floatValue());
                }

                String dayTotal = stringObjectMap.get("dayTotal").toString();
                if (dayTotal.equals("0")) {
                    hourMeasureDataStatistic.setDayTotal(0);
                } else {
                    BigDecimal bigDecimalTotal = new BigDecimal(dayTotal);
                    BigDecimal divideTotal = bigDecimalTotal.divide(bigDecimal, 3, BigDecimal.ROUND_HALF_UP);
                    hourMeasureDataStatistic.setDayTotal(divideTotal.floatValue());
                }
                dataMap.put(hour, hourMeasureDataStatistic);
            }
        }
        String format = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        for (String hour : hourDate) {
            if (hour.equals("24")) {
                HourMeasureDataStatistic hourMeasureDataStatistic = dataMap.get("00");
                if (hourMeasureDataStatistic != null) {
                    hourMeasureDataStatistic.setHour("24");
                    hourMeasureMap.put(hour, hourMeasureDataStatistic);
                } else {
                    hourMeasureMap.put(hour, new HourMeasureDataStatistic(hour, format, 0, 0, 0, 0, 0));
                }
            } else {
                HourMeasureDataStatistic hourMeasureDataStatistic = dataMap.get(hour);
                if (hourMeasureDataStatistic != null) {
                    hourMeasureMap.put(hour, hourMeasureDataStatistic);
                } else {
                    hourMeasureMap.put(hour, new HourMeasureDataStatistic(hour, format, 0, 0, 0, 0, 0));
                }
            }
        }
        Map<String, Object> finalDataMap = new LinkedHashMap<>();
        finalDataMap.put("xData", hourDate);
        finalDataMap.put("measureData", hourMeasureMap);
        return finalDataMap;
    }

    @Override
    public List<EnergyConsumeStatisticDetail> monthCompareStatistic(Long projectId) {
        //处理日期
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String monthStr = dateTimeFormatter.format(now);
        LocalDate localDate = now.plusMonths(-1);
        String format = dateTimeFormatter.format(localDate);
        List<String> dayList = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            dayList.add(String.valueOf(i));
        }
        List<Map<String, Object>> lastMonthMap = energyEquipmentService.getLastMonthMeasure(projectId, format);
        List<EnergyConsumeStatisticDetail> finalDataList = new ArrayList<>();
        //处理上个月
        BigDecimal bigQian = new BigDecimal("1000");
        if (!CollectionUtils.isEmpty(lastMonthMap)) {
            Map<String, DayConsumeData> dataMap = new HashMap<>();
            List<DayConsumeData> dayConsumeDataList = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : lastMonthMap) {
                String day = stringObjectMap.get("day").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                float monthTotalFloat = 0f;
                if (!monthTotal.equals(0)) {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigQian, 3, BigDecimal.ROUND_HALF_UP);
                    monthTotalFloat = divide.floatValue();
                }
                dataMap.put(day, new DayConsumeData(day, monthTotalFloat));
            }
            if (!CollectionUtils.isEmpty(dataMap)) {
                for (String s : dayList) {
                    if (dataMap.containsKey(s)) {
                        dayConsumeDataList.add(dataMap.get(s));
                    }
                }
                EnergyConsumeStatisticDetail energyConsumeStatisticDetail = new EnergyConsumeStatisticDetail();
                energyConsumeStatisticDetail.setMonth(format);
                energyConsumeStatisticDetail.setDayConsume(dayConsumeDataList);
                finalDataList.add(energyConsumeStatisticDetail);
            }
        } else {
            LocalDate with = localDate.with(TemporalAdjusters.lastDayOfMonth());
            String format1 = dateTimeFormatter1.format(with);
            Integer dayNum = Integer.parseInt(format1.split("-")[2]);
            List<DayConsumeData> dayConsumeDataList = new ArrayList<>();
            for (int i = 1; i < dayNum + 1; i++) {
                dayConsumeDataList.add(new DayConsumeData(String.valueOf(i), 0));
            }
            EnergyConsumeStatisticDetail energyConsumeStatisticDetail = new EnergyConsumeStatisticDetail();
            energyConsumeStatisticDetail.setMonth(format);
            energyConsumeStatisticDetail.setDayConsume(dayConsumeDataList);
            finalDataList.add(energyConsumeStatisticDetail);
        }
        //获取本月
        List<Map<String, Object>> monthMap = energyEquipmentService.getMonthMeasure(projectId, monthStr);
        //获取本日
        List<Map<String, Object>> dayMap = energyEquipmentService.getMonthTodayMeasure(projectId, dateTimeFormatter1.format(now));
        List<DayConsumeData> dayConsumeDataList = new ArrayList<>();
        Map<String, DayConsumeData> dataMap = new HashMap<>();
        //处理本月
        if (!CollectionUtils.isEmpty(monthMap)) {
            for (Map<String, Object> stringObjectMap : monthMap) {
                String day = stringObjectMap.get("day").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                float monthTotalFloat = 0f;
                if (!monthTotal.equals(0)) {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigQian, 3, BigDecimal.ROUND_HALF_UP);
                    monthTotalFloat = divide.floatValue();
                }
                dataMap.put(day, new DayConsumeData(day, monthTotalFloat));
            }
        }
        //处理当天
        if (!CollectionUtils.isEmpty(dayMap)) {
            for (Map<String, Object> stringObjectMap : dayMap) {
                String day = stringObjectMap.get("day").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                float monthTotalFloat = 0f;
                if (!monthTotal.equals(0l)) {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigQian, 3, BigDecimal.ROUND_HALF_UP);
                    monthTotalFloat = divide.floatValue();
                }
                dataMap.put(day, new DayConsumeData(day, monthTotalFloat));
            }
        }
        if (!CollectionUtils.isEmpty(dataMap)) {
            for (String day : dayList) {
                if (dataMap.containsKey(day)) {
                    dayConsumeDataList.add(dataMap.get(day));
                }
            }
            EnergyConsumeStatisticDetail energyConsumeStatisticDetail = new EnergyConsumeStatisticDetail();
            energyConsumeStatisticDetail.setMonth(monthStr);
            energyConsumeStatisticDetail.setDayConsume(dayConsumeDataList);
            finalDataList.add(energyConsumeStatisticDetail);
        }
        return finalDataList;
    }

    @Override
    public TotalMeasure totalStatistic(Long projectId, Integer type) {
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yearMonthDay = dateTimeFormatter1.format(dateNow);
        String yearLast = "";
        List<EquipmentDateTotalMeasure> todayMeasure = energyEquipmentService.getTotalTotalMeasure(projectId, yearMonthDay);
        List<EquipmentDateTotalMeasure> lastMeasure = null;
        //年
        if (type.equals(1)) {
            LocalDate localDate = dateNow.plus(-1, ChronoUnit.YEARS).withMonth(12).withDayOfMonth(31);
            yearLast = dateTimeFormatter1.format(localDate);
            lastMeasure = energyEquipmentService.getLastMeasure(projectId, yearLast);
            //季度
        } else if (type.equals(2)) {
            int value = dateNow.getMonth().getValue();
            int num = value / 3 + 1;
            if (num == 1) {
                LocalDate localDate = dateNow.plus(-1, ChronoUnit.YEARS).withMonth(12).withDayOfMonth(31);
                yearLast = dateTimeFormatter1.format(localDate);
            } else if (num == 2) {
                LocalDate localDate = dateNow.withMonth(3).withDayOfMonth(31);
                yearLast = dateTimeFormatter1.format(localDate);
            } else if (num == 3) {
                LocalDate localDate = dateNow.withMonth(6).withDayOfMonth(30);
                yearLast = dateTimeFormatter1.format(localDate);
            } else if (num == 4) {
                LocalDate localDate = dateNow.withMonth(9).withDayOfMonth(30);
                yearLast = dateTimeFormatter1.format(localDate);
            }
            lastMeasure = energyEquipmentService.getLastMeasure(projectId, yearLast);
            //月
        } else if (type.equals(3)) {
            //查询当月总电度
            todayMeasure = energyEquipmentService.getMonthTotalMeasure(projectId, yearMonthDay);
            lastMeasure = null;
        } else if (type.equals(4)) {
            todayMeasure = energyEquipmentService.getTodayTotalMeasure(projectId, yearMonthDay);
            lastMeasure = null;
        }
        Map<Long, EquipmentDateTotalMeasure> todayMap = new LinkedHashMap<>();
        Map<Long, EquipmentDateTotalMeasure> lastMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(todayMeasure)) {
            for (EquipmentDateTotalMeasure equipmentDateTotalMeasure : todayMeasure) {
                todayMap.put(equipmentDateTotalMeasure.getId(), equipmentDateTotalMeasure);
            }
        }
        if (!CollectionUtils.isEmpty(lastMeasure)) {
            for (EquipmentDateTotalMeasure equipmentDateTotalMeasure : lastMeasure) {
                lastMap.put(equipmentDateTotalMeasure.getId(), equipmentDateTotalMeasure);
            }
        }
        //处理数据
        Long totalTotal = 0l;
        Map<Long, Long> consumeMap = new LinkedHashMap<>();
        Map<Long, String> consumeNameMap = new HashMap<>();
        BigDecimal bigTho = new BigDecimal("1000");
        Map<Long, List<EquipmentMeasure>> equipmentMeasureMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(todayMap)) {
            for (Map.Entry<Long, EquipmentDateTotalMeasure> dataEntity : todayMap.entrySet()) {
                Long equipmentId = dataEntity.getKey();
                EquipmentDateTotalMeasure measure = dataEntity.getValue();
                Long total = Long.parseLong(measure.getTotal());
                String equipmentName = measure.getName();
                if (lastMap.containsKey(equipmentId)) {
                    EquipmentDateTotalMeasure measure1 = lastMap.get(equipmentId);
                    Long total1 = Long.parseLong(measure1.getTotal());
                    total = total - total1;
                }
                BigDecimal bigDecimal = new BigDecimal(total);
                BigDecimal totalBig = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);

                Long consumeId = measure.getConsumeId();
                consumeNameMap.put(consumeId, measure.getConsumeName());
                if (consumeMap.containsKey(consumeId)) {
                    Long consumeTotal = consumeMap.get(consumeId);
                    consumeTotal = consumeTotal + total;
                    consumeMap.put(consumeId, consumeTotal);
                } else {
                    consumeMap.put(consumeId, total);
                }
                if (equipmentMeasureMap.containsKey(consumeId)) {
                    EquipmentMeasure equipmentMeasure = new EquipmentMeasure(equipmentName, totalBig.toString());
                    equipmentMeasureMap.get(consumeId).add(equipmentMeasure);
                } else {
                    EquipmentMeasure equipmentMeasure = new EquipmentMeasure(equipmentName, totalBig.toString());
                    List<EquipmentMeasure> equipmentMeasureList = new ArrayList<>();
                    equipmentMeasureList.add(equipmentMeasure);
                    equipmentMeasureMap.put(consumeId, equipmentMeasureList);
                }
                totalTotal = totalTotal + total;
            }
        }
        TotalMeasure totalMeasure = new TotalMeasure();
        BigDecimal bigDecimal = new BigDecimal(totalTotal);
        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
        totalMeasure.setMeasure(divide.toString());
        List<ConsumeMeasure> consumeNumList = new ArrayList<>();
        for (Map.Entry<Long, Long> longFloatEntry : consumeMap.entrySet()) {
            Long consumeId = longFloatEntry.getKey();
            Long consumeLong = longFloatEntry.getValue();
            BigDecimal bigDecimalConsume = new BigDecimal(consumeLong);
            BigDecimal divide1 = bigDecimalConsume.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
            String consumeName = consumeNameMap.get(consumeId);
            List<EquipmentMeasure> equipmentMeasureList = equipmentMeasureMap.get(consumeId);
            ConsumeMeasure consumeMeasure = new ConsumeMeasure();
            consumeMeasure.setName(consumeName);
            consumeMeasure.setMeasure(divide1.toString());
            consumeMeasure.setEquipmentMeasureList(equipmentMeasureList);
            consumeNumList.add(consumeMeasure);
        }
        totalMeasure.setConsumeMeasure(consumeNumList);
        return totalMeasure;
    }

    @Override
    public List<EntityDto> getConsumeList(Long projectId) {
        List<EnergyConsumeType> energyConsumeTypeServiceList = energyConsumeTypeService.getList(projectId);
        List<EntityDto> entityDtoList = new ArrayList<>();
        entityDtoList.add(new EntityDto(-1l, "全部", -1l));
        if (!CollectionUtils.isEmpty(energyConsumeTypeServiceList)) {
            for (EnergyConsumeType energyConsumeType : energyConsumeTypeServiceList) {
                entityDtoList.add(new EntityDto(energyConsumeType.getId(), energyConsumeType.getName(), energyConsumeType.getProjectId()));
            }
        }
        return entityDtoList;
    }

    @Override
    public Map<String, Object> getHourDeviceData(Long projectId, Long consumeTypeId, String dataType) {
        if (dataType.equals("activePower")) {
            BigDecimal bigShi = new BigDecimal("10000");
            List<Map<String, Object>> activePowerSmartMap = energyEquipmentService.getSmartActivePowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSmartActivePower = new HashMap<>();
            if (!CollectionUtils.isEmpty(activePowerSmartMap)) {
                for (Map<String, Object> stringObjectMap : activePowerSmartMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Integer activePower = Integer.parseInt(stringObjectMap.get("activePower").toString());
                    BigDecimal bigDecimal = new BigDecimal(activePower);
                    BigDecimal divide = bigDecimal.divide(bigShi, 1, BigDecimal.ROUND_HALF_UP);
                    dateTimeSmartActivePower.put(hourTime, divide.floatValue());
                }
            }
            List<Map<String, Object>> activePowerSuperMap = energyEquipmentService.getSuperActivePowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSuperActivePower = new HashMap<>();
            if (!CollectionUtils.isEmpty(activePowerSuperMap)) {
                for (Map<String, Object> stringObjectMap : activePowerSuperMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Float activePower = Float.parseFloat(stringObjectMap.get("activePower").toString());
                    dateTimeSuperActivePower.put(hourTime, activePower);
                }
            }
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> finalDataMap = new LinkedHashMap<>();
            for (String hour : dayHour) {
                Float activePowerTotal = null;
                if (dateTimeSmartActivePower.containsKey(hour)) {
                    activePowerTotal = dateTimeSmartActivePower.get(hour);
                }
                if (dateTimeSuperActivePower.containsKey(hour)) {
                    Float aFloat = dateTimeSuperActivePower.get(hour);
                    if (activePowerTotal == null) {
                        activePowerTotal = aFloat;
                    } else {
                        activePowerTotal = activePowerTotal + aFloat;
                    }
                }
                if (activePowerTotal != null) {
                    finalDataMap.put(hour, activePowerTotal);
                }
            }
            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("xData", dayHour);
            dataMap.put("xDanWei", "时");
            dataMap.put("yDate", finalDataMap);
            dataMap.put("yDanWei", "kw");
            return dataMap;
        } else if (dataType.equals("reactivePower")) {
            BigDecimal bigShi = new BigDecimal("10000");
            List<Map<String, Object>> reactivePowerSmartMap = energyEquipmentService.getSmartReactivePowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSmartReactivePower = new HashMap<>();
            if (!CollectionUtils.isEmpty(reactivePowerSmartMap)) {
                for (Map<String, Object> stringObjectMap : reactivePowerSmartMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Integer reactivePower = Integer.parseInt(stringObjectMap.get("reactivePower").toString());
                    BigDecimal bigDecimal = new BigDecimal(reactivePower);
                    BigDecimal divide = bigDecimal.divide(bigShi, 1, BigDecimal.ROUND_HALF_UP);
                    dateTimeSmartReactivePower.put(hourTime, divide.floatValue());
                }
            }
            List<Map<String, Object>> reactivePowerSuperMap = energyEquipmentService.getSuperReactivePowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSuperReactivePower = new HashMap<>();
            if (!CollectionUtils.isEmpty(reactivePowerSuperMap)) {
                for (Map<String, Object> stringObjectMap : reactivePowerSuperMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Float reactivePower = Float.parseFloat(stringObjectMap.get("reactivePower").toString());
                    dateTimeSuperReactivePower.put(hourTime, reactivePower);
                }
            }
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> finalDataMap = new LinkedHashMap<>();
            for (String hour : dayHour) {
                Float reactivePowerTotal = null;
                if (dateTimeSmartReactivePower.containsKey(hour)) {
                    reactivePowerTotal = dateTimeSmartReactivePower.get(hour);
                }
                if (dateTimeSuperReactivePower.containsKey(hour)) {
                    Float aFloat = dateTimeSuperReactivePower.get(hour);
                    if (reactivePowerTotal == null) {
                        reactivePowerTotal = aFloat;
                    } else {
                        reactivePowerTotal = reactivePowerTotal + aFloat;
                    }
                }
                if (reactivePowerTotal != null) {
                    finalDataMap.put(hour, reactivePowerTotal);
                }
            }
            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("xData", dayHour);
            dataMap.put("xDanWei", "时");
            dataMap.put("yDate", finalDataMap);
            dataMap.put("yDanWei", "kvar");
            return dataMap;
        } else if (dataType.equals("apparentPower")) {

            BigDecimal bigShi = new BigDecimal("10000");
            List<Map<String, Object>> apparentPowerSmartMap = energyEquipmentService.getSmartApparentPowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSmartApparentPower = new HashMap<>();
            if (!CollectionUtils.isEmpty(apparentPowerSmartMap)) {
                for (Map<String, Object> stringObjectMap : apparentPowerSmartMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Integer apparentPower = Integer.parseInt(stringObjectMap.get("apparentPower").toString());
                    BigDecimal bigDecimal = new BigDecimal(apparentPower);
                    BigDecimal divide = bigDecimal.divide(bigShi, 1, BigDecimal.ROUND_HALF_UP);
                    dateTimeSmartApparentPower.put(hourTime, divide.floatValue());
                }
            }
            List<Map<String, Object>> apparentPowerSuperMap = energyEquipmentService.getSuperApparentPowerData(projectId, consumeTypeId);
            Map<String, Float> dateTimeSuperApparentPower = new HashMap<>();
            if (!CollectionUtils.isEmpty(apparentPowerSuperMap)) {
                for (Map<String, Object> stringObjectMap : apparentPowerSuperMap) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Float apparentPower = Float.parseFloat(stringObjectMap.get("apparentPower").toString());
                    dateTimeSuperApparentPower.put(hourTime, apparentPower);
                }
            }
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> finalDataMap = new LinkedHashMap<>();
            for (String hour : dayHour) {
                Float apparentPowerTotal = null;
                if (dateTimeSmartApparentPower.containsKey(hour)) {
                    apparentPowerTotal = dateTimeSmartApparentPower.get(hour);
                }
                if (dateTimeSuperApparentPower.containsKey(hour)) {
                    Float aFloat = dateTimeSuperApparentPower.get(hour);
                    if (apparentPowerTotal == null) {
                        apparentPowerTotal = aFloat;
                    } else {
                        apparentPowerTotal = apparentPowerTotal + aFloat;
                    }
                }
                if (apparentPowerTotal != null) {
                    finalDataMap.put(hour, apparentPowerTotal);
                }
            }
            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("xData", dayHour);
            dataMap.put("xDanWei", "时");
            dataMap.put("yDate", finalDataMap);
            dataMap.put("yDanWei", "kva");
            return dataMap;
        } else if (dataType.equals("powerFactor")) {

            List<Map<String, Object>> powerFactoryApparentPowerList = energyEquipmentService.getPowerAndApparentPowerList(projectId, consumeTypeId);
            Map<String, Long> dateTimeSmartApparentPower = new HashMap<>();
            Map<String, Long> dateTimeSmartActivePower = new HashMap<>();
            if (!CollectionUtils.isEmpty(powerFactoryApparentPowerList)) {
                for (Map<String, Object> stringObjectMap : powerFactoryApparentPowerList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    Long activePower = Long.parseLong(stringObjectMap.get("activePower").toString());
                    Long apparentPower = Long.parseLong(stringObjectMap.get("apparentPower").toString());
                    dateTimeSmartApparentPower.put(hourTime, apparentPower);
                    dateTimeSmartActivePower.put(hourTime, activePower);
                }
            }
            List<Map<String, Object>> powerFactoryApparentPowerSuperList = energyEquipmentService.getPowerAndApparentPowerSuperList(projectId, consumeTypeId);
            if (!CollectionUtils.isEmpty(powerFactoryApparentPowerSuperList)) {
                for (Map<String, Object> stringObjectMap : powerFactoryApparentPowerSuperList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    Long activePower = Long.parseLong(stringObjectMap.get("activePower").toString());
                    Long apparentPower = Long.parseLong(stringObjectMap.get("apparentPower").toString());
                    String hourTime = dateTime.split(" ")[1].split(":")[0];
                    if(hourTime.equals("00")){
                        hourTime="24";
                    }
                    if (dateTimeSmartApparentPower.containsKey(hourTime)) {
                        Long integer = dateTimeSmartApparentPower.get(hourTime);
                        integer = integer + apparentPower;
                        dateTimeSmartApparentPower.put(hourTime, integer);
                    } else {
                        dateTimeSmartApparentPower.put(hourTime, apparentPower);
                    }
                    if (dateTimeSmartActivePower.containsKey(hourTime)) {
                        Long integer = dateTimeSmartActivePower.get(hourTime);
                        integer = integer + activePower;
                        dateTimeSmartActivePower.put(hourTime, integer);
                    } else {
                        dateTimeSmartActivePower.put(hourTime, activePower);
                    }
                }
            }
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> finalDataMap = new LinkedHashMap<>();
            BigDecimal bigTho=new BigDecimal("1000");
            for (String hour : dayHour) {
                Long activePower = dateTimeSmartActivePower.get(hour);
                Long apparentPower = dateTimeSmartApparentPower.get(hour);
                if (activePower != null && !apparentPower.equals(0) && apparentPower != null) {
                    Long powerFactory = (activePower * 1000) / apparentPower;
                    BigDecimal bigDecimal=new BigDecimal(powerFactory);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    finalDataMap.put(hour, divide.floatValue());
                }
            }
            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("xData", dayHour);
            dataMap.put("xDanWei", "时");
            dataMap.put("yDate", finalDataMap);
            dataMap.put("yDanWei", "1");
            return dataMap;

        } else if (dataType.equals("thdv")) {
            List<Map<String, Object>> thdvList = energyEquipmentService.getThdvSmartList(projectId, consumeTypeId);
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> thdvFloatMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(thdvList)) {
                for (Map<String, Object> stringObjectMap : thdvList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    Float thdvFloat = Float.parseFloat(stringObjectMap.get("thdv").toString());
                    String hour = dateTime.split(" ")[1].split(":")[0];
                    if (hour.equals("00")) {
                        hour = "24";
                    }
                    thdvFloatMap.put(hour, thdvFloat);
                }
            }
            List<Map<String, Object>> thdvSuperList = energyEquipmentService.getThdvSuperList(projectId, consumeTypeId);
            if (!CollectionUtils.isEmpty(thdvSuperList)) {
                for (Map<String, Object> stringObjectMap : thdvSuperList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    Float thdvFloat = Float.parseFloat(stringObjectMap.get("thdv").toString());
                    String hour = dateTime.split(" ")[1].split(":")[0];
                    if (hour.equals("00")) {
                        hour = "24";
                    }
                    if (thdvFloatMap.containsKey(hour)) {
                        Float aFloat = thdvFloatMap.get(hour);
                        if (thdvFloat > aFloat) {
                            thdvFloatMap.put(hour, thdvFloat);
                        }
                    } else {
                        thdvFloatMap.put(hour, thdvFloat);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(thdvFloatMap)) {
                Map<String, Float> finalDataMap = new LinkedHashMap<>();
                for (String hour : dayHour) {
                    if (thdvFloatMap.containsKey(hour)) {
                        Float aFloat = thdvFloatMap.get(hour);
                        finalDataMap.put(hour, aFloat);
                    }
                }
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("xData", dayHour);
                dataMap.put("xDanWei", "时");
                dataMap.put("yDate", finalDataMap);
                dataMap.put("yDanWei", "%");
                return dataMap;
            }
        }else if(dataType.equals("thdi")){
            List<Map<String, Object>> thdiList = energyEquipmentService.getThdiSmartList(projectId, consumeTypeId);
            List<String> dayHour = DateUtils.getDayHour();
            Map<String, Float> thdiFloatMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(thdiList)) {
                for (Map<String, Object> stringObjectMap : thdiList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    Float thdiFloat = Float.parseFloat(stringObjectMap.get("thdi").toString());
                    String hour = dateTime.split(" ")[1].split(":")[0];
                    if (hour.equals("00")) {
                        hour = "24";
                    }
                    thdiFloatMap.put(hour, thdiFloat);
                }
            }
            List<Map<String, Object>> thdiSuperList = energyEquipmentService.getThdiSuperList(projectId, consumeTypeId);
            if (!CollectionUtils.isEmpty(thdiSuperList)) {
                for (Map<String, Object> stringObjectMap : thdiSuperList) {
                    String dateTime = stringObjectMap.get("dateTime").toString();
                    Float thdiFloat = Float.parseFloat(stringObjectMap.get("thdi").toString());
                    String hour = dateTime.split(" ")[1].split(":")[0];
                    if (hour.equals("00")) {
                        hour = "24";
                    }
                    if (thdiFloatMap.containsKey(hour)) {
                        Float aFloat = thdiFloatMap.get(hour);
                        if (thdiFloat > aFloat) {
                            thdiFloatMap.put(hour, thdiFloat);
                        }
                    } else {
                        thdiFloatMap.put(hour, thdiFloat);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(thdiFloatMap)) {
                Map<String, Float> finalDataMap = new LinkedHashMap<>();
                for (String hour : dayHour) {
                    if (thdiFloatMap.containsKey(hour)) {
                        Float aFloat = thdiFloatMap.get(hour);
                        finalDataMap.put(hour, aFloat);
                    }
                }
                Map<String, Object> dataMap = new LinkedHashMap<>();
                dataMap.put("xData", dayHour);
                dataMap.put("xDanWei", "时");
                dataMap.put("yDate", finalDataMap);
                dataMap.put("yDanWei", "%");
                return dataMap;
            }
        }
        return null;
    }

    @Override
    public List<EnergyMeasureDateTime> measureDateTime(Long projectId, Long consumeTypeId, String begTime, String endTime, String order) {
        //先处理开始的时间
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        List<EnergyMeasureDataEntity> endData = null;
        List<EnergyMeasureDataEntity> beginData = null;
        if (consumeTypeId.equals(-1l)) {
            consumeTypeId = null;
        }
        if (begTime.equals(format)) {
            LocalDate localDate = now.plusDays(-1);
            begTime = dateTimeFormatter.format(localDate);
            beginData = energyEquipmentService.getEnergyPastDateStatistic(projectId, consumeTypeId, begTime);
            endData = energyEquipmentService.getEnergyPastTodayDateStatistic(projectId, consumeTypeId, format);
        } else {
            LocalDate begParse = LocalDate.parse(begTime, dateTimeFormatter);
            LocalDate begDate = begParse.plusDays(-1);
            begTime = dateTimeFormatter.format(begDate);
            beginData = energyEquipmentService.getEnergyPastDateStatistic(projectId, consumeTypeId, begTime);

            LocalDate parse = LocalDate.parse(endTime, dateTimeFormatter);
            boolean before = now.isBefore(parse);
            if (endTime.equals(format) || before) {
                endData = energyEquipmentService.getEnergyPastTodayDateStatistic(projectId, consumeTypeId, format);
            } else {
                endData = energyEquipmentService.getEnergyPastDateStatistic(projectId, consumeTypeId, endTime);
            }
        }
        List<EnergyMeasureDateTime> dateTimeList = new ArrayList<>();
        Map<Long, EnergyMeasureDataEntity> beginMeasure = new HashMap<>();
        if (!CollectionUtils.isEmpty(beginData)) {
            for (EnergyMeasureDataEntity beginDatum : beginData) {
                beginMeasure.put(beginDatum.getEquipmentId(), beginDatum);
            }
        }
        if (!CollectionUtils.isEmpty(endData)) {
            BigDecimal bigTho = new BigDecimal(1000);
            for (EnergyMeasureDataEntity endDatum : endData) {
                Long equipmentId = endDatum.getEquipmentId();
                String equipmentName = endDatum.getEquipmentName();
                String consumeTypeName = endDatum.getConsumeName();
                String energyTypeName = endDatum.getEnergyTypeName();

                Long total = Long.parseLong(endDatum.getTotal());
                Long spike = Long.parseLong(endDatum.getSpike());
                Long peak = Long.parseLong(endDatum.getPeak());
                Long normal = Long.parseLong(endDatum.getNormal());
                Long valley = Long.parseLong(endDatum.getValley());

                if (beginMeasure.containsKey(equipmentId)) {
                    EnergyMeasureDataEntity energyMeasureDateData = beginMeasure.get(equipmentId);
                    Long begTotal = Long.parseLong(energyMeasureDateData.getTotal());
                    Long begSpike = Long.parseLong(energyMeasureDateData.getSpike());
                    Long begPeak = Long.parseLong(energyMeasureDateData.getPeak());
                    Long begNormal = Long.parseLong(energyMeasureDateData.getNormal());
                    Long begValley = Long.parseLong(energyMeasureDateData.getValley());
                    total = total - begTotal;
                    spike = spike - begSpike;
                    peak = peak - begPeak;
                    normal = normal - begNormal;
                    valley = valley - begValley;
                }
                EnergyMeasureDateTime energyMeasureDateTime = new EnergyMeasureDateTime();
                energyMeasureDateTime.setEquipmentId(equipmentId);
                energyMeasureDateTime.setEquipmentName(equipmentName);
                energyMeasureDateTime.setConsumeTypeName(consumeTypeName);
                energyMeasureDateTime.setEnergyTypeName(energyTypeName);

                if (total.equals(0l)) {
                    energyMeasureDateTime.setTotalMeasure("0");
                    energyMeasureDateTime.setTotalOrder(0f);
                } else {
                    BigDecimal totalDecimal = new BigDecimal(total);
                    BigDecimal divide = totalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    energyMeasureDateTime.setTotalMeasure(divide.toString());
                    energyMeasureDateTime.setTotalOrder(divide.floatValue());
                }

                if (spike.equals(0l)) {
                    energyMeasureDateTime.setSpikeMeasure("0");
                } else {
                    BigDecimal spikeDecimal = new BigDecimal(spike);
                    BigDecimal divide = spikeDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    energyMeasureDateTime.setSpikeMeasure(divide.toString());
                }

                if (peak.equals(0l)) {
                    energyMeasureDateTime.setPeakMeasure("0");
                } else {
                    BigDecimal peakDecimal = new BigDecimal(peak);
                    BigDecimal divide = peakDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    energyMeasureDateTime.setPeakMeasure(divide.toString());
                }

                if (normal.equals(0l)) {
                    energyMeasureDateTime.setNormalMeasure("0");
                } else {
                    BigDecimal normalDecimal = new BigDecimal(normal);
                    BigDecimal divide = normalDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    energyMeasureDateTime.setNormalMeasure(divide.toString());
                }

                if (valley.equals(0l)) {
                    energyMeasureDateTime.setValleyMeasure("0");
                } else {
                    BigDecimal valleyDecimal = new BigDecimal(valley);
                    BigDecimal divide = valleyDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    energyMeasureDateTime.setValleyMeasure(divide.toString());
                }
                dateTimeList.add(energyMeasureDateTime);
            }
        }
        if (!CollectionUtils.isEmpty(dateTimeList)) {
            if (order.equals("asc")) {
                dateTimeList.sort(Comparator.comparing(EnergyMeasureDateTime::getTotalOrder));
            } else {
                dateTimeList.sort(Comparator.comparing(EnergyMeasureDateTime::getTotalOrder).reversed());
            }

            return dateTimeList;
        }
        return null;
    }

    @Override
    public List<EntityDto> getProjectList(Long userId) {
        List<ProjectSimInfo> projectSimInfoByUserId = projectService.getProjectSimInfoByUserId(userId);
        if (!CollectionUtils.isEmpty(projectSimInfoByUserId)) {
            List<EntityDto> entityDtoList = new ArrayList<>();
            for (ProjectSimInfo projectSimInfo : projectSimInfoByUserId) {
                entityDtoList.add(new EntityDto(projectSimInfo.getProjectId(), projectSimInfo.getProjectName(), projectSimInfo.getProjectId()));
            }
            return entityDtoList;
        }
        return null;
    }

    @Override
    public List<MonthMeasureStatistic> getMonthMeasureStatistic(Long projectId, String year) {
        List<Map<String, Object>> monthMeasure = energyEquipmentService.getYearMonthMeasure(projectId, year);
        List<Map<String, Object>> dayMonthStatistic = energyEquipmentService.getMonthTotal(projectId);
        Long count = energyEquipmentService.getCount(projectId);
        int i = Integer.parseInt(year);
        //处理去年的电度数据
        List<Map<String, Object>> yearMonthMeasure = energyEquipmentService.getYearMonthMeasure(projectId, String.valueOf(i - 1));
        Map<Integer, String> lastYearMonthMeasure = new HashMap<>();
        BigDecimal bigTHo = new BigDecimal("1000");
        if (!CollectionUtils.isEmpty(yearMonthMeasure)) {
            for (Map<String, Object> stringObjectMap : yearMonthMeasure) {
                Integer month = Integer.parseInt(stringObjectMap.get("month").toString());
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                if (monthTotal.equals(0)) {
                    lastYearMonthMeasure.put(month, "0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTHo, 3, BigDecimal.ROUND_HALF_UP);
                    lastYearMonthMeasure.put(month, divide.toString());
                }

            }
        }
        //处理今年的电度数据
        //a 处理之前月的电度数据
        List<MonthMeasureStatistic> dataList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(monthMeasure)) {
            for (Map<String, Object> stringObjectMap : monthMeasure) {
                String yearNow = stringObjectMap.get("year").toString();
                String month = stringObjectMap.get("month").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                MonthMeasureStatistic monthMeasureStatistic = new MonthMeasureStatistic();
                monthMeasureStatistic.setYear(yearNow);
                monthMeasureStatistic.setMonth(month);
                monthMeasureStatistic.setEnergyCount(Integer.parseInt(count.toString()));
                monthMeasureStatistic.setProjectId(projectId);
                if (monthTotal.equals(0)) {
                    monthMeasureStatistic.setTotalConsume("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTHo, 3, BigDecimal.ROUND_HALF_UP);
                    monthMeasureStatistic.setTotalConsume(divide.toString());
                }
                String yearOverYearConsume = lastYearMonthMeasure.get(Integer.parseInt(month));
                if (StringUtils.isEmpty(yearOverYearConsume)) {
                    monthMeasureStatistic.setYearOverYearConsume("0");
                } else {
                    monthMeasureStatistic.setYearOverYearConsume(yearOverYearConsume);
                }
                String yearMonthDay = stringObjectMap.get("yearMonthDay").toString();
                monthMeasureStatistic.setYearMonthDay(yearMonthDay);
                dataList.add(monthMeasureStatistic);
            }
        }
        //b 处理本月的电度数据
        if (!CollectionUtils.isEmpty(dayMonthStatistic)) {
            for (Map<String, Object> stringObjectMap : dayMonthStatistic) {
                String yearNow = stringObjectMap.get("year").toString();
                String month = stringObjectMap.get("month").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                MonthMeasureStatistic monthMeasureStatistic = new MonthMeasureStatistic();
                monthMeasureStatistic.setYear(yearNow);
                monthMeasureStatistic.setMonth(month);
                monthMeasureStatistic.setEnergyCount(Integer.parseInt(count.toString()));
                monthMeasureStatistic.setProjectId(projectId);
                if (monthTotal.equals(0)) {
                    monthMeasureStatistic.setTotalConsume("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTHo, 3, BigDecimal.ROUND_HALF_UP);
                    monthMeasureStatistic.setTotalConsume(divide.toString());
                }
                String yearOverYearConsume = lastYearMonthMeasure.get(Integer.parseInt(month));
                if (StringUtils.isEmpty(yearOverYearConsume)) {
                    monthMeasureStatistic.setYearOverYearConsume("0");
                } else {
                    monthMeasureStatistic.setYearOverYearConsume(yearOverYearConsume);
                }
                String yearMonthDay = stringObjectMap.get("yearMonthDay").toString();
                monthMeasureStatistic.setYearMonthDay(yearMonthDay);
                dataList.add(monthMeasureStatistic);
            }
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        return null;
    }

    @Override
    public List<MonthDetailStatistic> getMonthDetail(Long projectId, String year, String month) {
        //判断是不是当前月
        LocalDate now = LocalDate.now();
        int yearInt = now.getYear();
        int monthValue = now.getMonthValue();
        String yearNow = String.valueOf(yearInt);
        String monthNow = String.valueOf(monthValue);
        BigDecimal bigTho = new BigDecimal("1000");
        List<MonthDetailStatistic> monthDetailStatisticList = new ArrayList<>();
        //当前月
        List<Map<String, Object>> dataMap = null;
        List<Map<String, Object>> lastDataMap = null;
        if (yearNow.equals(year) && monthNow.equals(month)) {
            dataMap = energyEquipmentService.getMonthDataDetail(projectId);
            lastDataMap = energyEquipmentService.getLastMonthDetail(projectId, String.valueOf(yearInt - 1), month);
        } else {
            int monthS = Integer.parseInt(month);
            if(monthS<10){
                month="0"+ month;
            }
            dataMap = energyEquipmentService.getLastMonthDetail(projectId, year, month);
            int i = Integer.parseInt(year);
            lastDataMap = energyEquipmentService.getLastMonthDetail(projectId, String.valueOf(i - 1), month);
        }
        Map<Long, Long> lastMonthDataMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(lastDataMap)) {
            for (Map<String, Object> stringObjectMap : lastDataMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                lastMonthDataMap.put(equipmentId, monthTotal);
            }
        }
        if (!CollectionUtils.isEmpty(dataMap)) {
            for (Map<String, Object> stringObjectMap : dataMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                Object yearMonthDay = stringObjectMap.get("yearMonthDay");
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                MonthDetailStatistic monthDetailStatistic = new MonthDetailStatistic();
                monthDetailStatistic.setConsumeTypeId(consumeTypeId);
                monthDetailStatistic.setConsumeTypeName(consumeTypeName);
                monthDetailStatistic.setEquipmentId(equipmentId);
                monthDetailStatistic.setEquipmentName(equipmentName);
                monthDetailStatistic.setYear(year);
                monthDetailStatistic.setMonth(month);
                monthDetailStatistic.setYearMonthDay(yearMonthDay==null?"":yearMonthDay.toString());
                monthDetailStatistic.setMonthTotalLong(monthTotal);
                if (monthTotal.equals(0)) {
                    monthDetailStatistic.setMonthTotal("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    monthDetailStatistic.setMonthTotal(divide.toString());
                }
                if (lastMonthDataMap.containsKey(equipmentId)) {
                    Long integer = lastMonthDataMap.get(equipmentId);
                    BigDecimal bigDecimal = new BigDecimal(integer);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    monthDetailStatistic.setMonthTotal(divide.toString());
                } else {
                    monthDetailStatistic.setYearOverYearMonthTotal("0");
                }
                monthDetailStatisticList.add(monthDetailStatistic);
            }
            Collections.sort(monthDetailStatisticList, new Comparator<MonthDetailStatistic>() {
                @Override
                public int compare(MonthDetailStatistic o1, MonthDetailStatistic o2) {
                    Long cha=o2.getMonthTotalLong()- o1.getMonthTotalLong();
                    return Integer.parseInt(cha.toString());
                }
            });
        }

        return monthDetailStatisticList;
    }

    @Override
    public List<EquipmentDetailNow> getEquipmentDetail(Long projectId) {
        List<Map<String, Object>> dataMap = energyEquipmentService.getMonthDetail(projectId);
        List<EquipmentDetailNow> equipmentDetailNowList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dataMap)) {
            BigDecimal bigTho = new BigDecimal("1000");
            for (Map<String, Object> stringObjectMap : dataMap) {
                Long equipmentId = Long.parseLong(stringObjectMap.get("equipmentId").toString());
                String equipmentName = stringObjectMap.get("equipmentName").toString();
                Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                Long dayTotal = Long.parseLong(stringObjectMap.get("dayTotal").toString());
                Long totalTotal = Long.parseLong(stringObjectMap.get("totalTotal").toString());
                String time = stringObjectMap.get("time").toString();
                EquipmentDetailNow equipmentDetailNow = new EquipmentDetailNow();
                equipmentDetailNow.setEquipmentId(equipmentId);
                equipmentDetailNow.setEquipmentName(equipmentName);
                equipmentDetailNow.setConsumeTypeId(consumeTypeId);
                equipmentDetailNow.setConsumeTypeName(consumeTypeName);
                equipmentDetailNow.setTime(time);
                if (dayTotal.equals(0)) {
                    equipmentDetailNow.setDayTotal("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(dayTotal);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    equipmentDetailNow.setDayTotal(divide.toString());
                }
                if (monthTotal.equals(0)) {
                    equipmentDetailNow.setMonthTotal("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    equipmentDetailNow.setMonthTotal(divide.toString());
                }
                if (totalTotal.equals(0)) {
                    equipmentDetailNow.setTotalTotal("0");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(totalTotal);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    equipmentDetailNow.setTotalTotal(divide.toString());
                }
                equipmentDetailNowList.add(equipmentDetailNow);
            }
        }
        return equipmentDetailNowList;
    }

    @Override
    public List<EquipmentReportDetail> getEquipmentReport(Long projectId, Integer type) {
        //日
        if (type.equals(1)) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            int monthValue = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String yearMonthDay = dateTimeFormatter.format(now);
            LocalDate localDateLastDay = now.plusDays(-1);
            String tomorrow = dateTimeFormatter.format(localDateLastDay);
            LocalDate localDateLastMonth = now.plusMonths(-1);
            String lastMonth = dateTimeFormatter.format(localDateLastMonth);
            //今日电度数据
            List<Map<String, Object>> dataMap = energyEquipmentService.getDayAndMonthMeasure(projectId, yearMonthDay);
            //环比数据(昨日)
            List<Map<String, Object>> tomorrowDataMap = energyEquipmentService.getLastDayConsumeMeasure(projectId, tomorrow);
            //类比数据(上个月日)
            List<Map<String, Object>> lastMonthDataMap = energyEquipmentService.getLastDayConsumeMeasure(projectId, lastMonth);
            //处理报警数量
            List<Map<String, Object>> alarmCount = alarmWarnService.getDayAndMonthAlarmCount(projectId, String.valueOf(year), String.valueOf(monthValue), String.valueOf(dayOfMonth));
            //处理数据
            Map<Long, String> tomorrowData = new HashMap<>();
            Map<Long, String> lastMonthData = new HashMap<>();
            Map<Long, Integer> alarmCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(tomorrowDataMap)) {
                for (Map<String, Object> stringObjectMap : tomorrowDataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String dayTotal = stringObjectMap.get("dayTotal").toString();
                    tomorrowData.put(consumeTypeId, dayTotal);
                }
            }
            if (!CollectionUtils.isEmpty(lastMonthDataMap)) {
                for (Map<String, Object> stringObjectMap : lastMonthDataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String dayTotal = stringObjectMap.get("dayTotal").toString();
                    lastMonthData.put(consumeTypeId, dayTotal);
                }
            }
            if (!CollectionUtils.isEmpty(alarmCount)) {
                for (Map<String, Object> stringObjectMap : alarmCount) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                    alarmCountMap.put(consumeTypeId, num);
                }
            }
            List<EquipmentReportDetail> equipmentReportDetails = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataMap)) {
                BigDecimal bigTho = new BigDecimal("1000");
                for (Map<String, Object> stringObjectMap : dataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                    String dayTotal = stringObjectMap.get("dayTotal").toString();
                    String daySpike = stringObjectMap.get("daySpike").toString();
                    String dayPeak = stringObjectMap.get("dayPeak").toString();
                    String dayNormal = stringObjectMap.get("dayNormal").toString();
                    String dayValley = stringObjectMap.get("dayValley").toString();
                    EquipmentReportDetail detail = new EquipmentReportDetail();
                    detail.setConsumeTypeId(consumeTypeId);
                    detail.setConsumeTypeName(consumeTypeName);
                    if (dayTotal.equals("0")) {
                        detail.setTotalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(dayTotal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setTotalMeasure(divide.toString());
                    }
                    if (daySpike.equals("0")) {
                        detail.setSpikeMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(daySpike);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setSpikeMeasure(divide.toString());
                    }
                    if (dayPeak.equals("0")) {
                        detail.setPeakMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(dayPeak);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setPeakMeasure(divide.toString());
                    }
                    if (dayNormal.equals("0")) {
                        detail.setNormalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(dayNormal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setNormalMeasure(divide.toString());
                    }
                    if (dayValley.equals("0")) {
                        detail.setValleyMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(dayValley);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setValleyMeasure(divide.toString());
                    }
                    if (tomorrowData.containsKey(consumeTypeId)) {
                        String tomorrowTotal = tomorrowData.get(consumeTypeId);
                        if (tomorrowTotal.equals("0")) {
                            detail.setRingRatioMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(tomorrowTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setRingRatioMeasure(divide.toString());
                        }
                    } else {
                        detail.setRingRatioMeasure("0");
                    }
                    if (lastMonthData.containsKey(consumeTypeId)) {
                        String lastMonthTotal = lastMonthData.get(consumeTypeId);
                        if (lastMonthTotal.equals("0")) {
                            detail.setAnalogyMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(lastMonthTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setAnalogyMeasure(divide.toString());
                        }
                    } else {
                        detail.setAnalogyMeasure("0");
                    }
                    if (alarmCountMap.containsKey(consumeTypeId)) {
                        Integer integer = alarmCountMap.get(consumeTypeId);
                        detail.setAlarmCount(integer);
                    } else {
                        detail.setAlarmCount(0);
                    }
                    equipmentReportDetails.add(detail);
                }
            }
            return equipmentReportDetails;
            //月
        } else if (type.equals(2)) {

            LocalDate now = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String yearMonthDay = dateTimeFormatter.format(now);
            String[] split2 = yearMonthDay.split("-");
            String yearStr = split2[0];
            String monthStr = split2[1];

            LocalDate lastMonthDate = now.plusMonths(-1);
            String lastMonth = dateTimeFormatter.format(lastMonthDate);
            String[] split = lastMonth.split("-");
            String lastMonthStr = split[1];

            LocalDate localDateLastMonth = now.plusYears(-1);
            String lastYearMonth = dateTimeFormatter.format(localDateLastMonth);
            String[] split1 = lastYearMonth.split("-");
            String lastYearStr = split1[0];
            //今日电度数据
            List<Map<String, Object>> dataMap = energyEquipmentService.getDayAndMonthMeasure(projectId, yearMonthDay);
            //环比数据(上个月的总能耗)
            List<Map<String, Object>> lastMonthDataMap = energyEquipmentService.getLastMonthTotalMeasure(projectId, yearStr, lastMonthStr);
            //类比数据  获取去年的当前月的数据
            List<Map<String, Object>> lastYearMonthDataMap = energyEquipmentService.getLastMonthTotalMeasure(projectId, lastYearStr, monthStr);
            //获取本月的报警数据
            List<Map<String, Object>> alarmCount = alarmWarnService.getMonthConsumeCount(projectId, yearStr, monthStr);
            //处理数据
            Map<Long, String> lastMonthData = new HashMap<>();
            Map<Long, String> lastYearMonthData = new HashMap<>();
            Map<Long, Integer> alarmCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(lastMonthDataMap)) {
                for (Map<String, Object> stringObjectMap : lastMonthDataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    lastMonthData.put(consumeTypeId, monthTotal);
                }
            }
            if (!CollectionUtils.isEmpty(lastYearMonthDataMap)) {
                for (Map<String, Object> stringObjectMap : lastYearMonthDataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    lastYearMonthData.put(consumeTypeId, monthTotal);
                }
            }
            if (!CollectionUtils.isEmpty(alarmCount)) {
                for (Map<String, Object> stringObjectMap : alarmCount) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                    alarmCountMap.put(consumeTypeId, num);
                }
            }
            List<EquipmentReportDetail> equipmentReportDetails = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataMap)) {
                BigDecimal bigTho = new BigDecimal("1000");
                for (Map<String, Object> stringObjectMap : dataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    String monthSpike = stringObjectMap.get("monthSpike").toString();
                    String monthPeak = stringObjectMap.get("monthPeak").toString();
                    String monthNormal = stringObjectMap.get("monthNormal").toString();
                    String monthValley = stringObjectMap.get("monthValley").toString();
                    EquipmentReportDetail detail = new EquipmentReportDetail();
                    detail.setConsumeTypeId(consumeTypeId);
                    detail.setConsumeTypeName(consumeTypeName);
                    if (monthTotal.equals("0")) {
                        detail.setTotalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthTotal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setTotalMeasure(divide.toString());
                    }
                    if (monthSpike.equals("0")) {
                        detail.setSpikeMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthSpike);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setSpikeMeasure(divide.toString());
                    }
                    if (monthPeak.equals("0")) {
                        detail.setPeakMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthPeak);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setPeakMeasure(divide.toString());
                    }
                    if (monthNormal.equals("0")) {
                        detail.setNormalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthNormal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setNormalMeasure(divide.toString());
                    }
                    if (monthValley.equals("0")) {
                        detail.setValleyMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthValley);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setValleyMeasure(divide.toString());
                    }
                    if (lastMonthData.containsKey(consumeTypeId)) {
                        String tomorrowTotal = lastMonthData.get(consumeTypeId);
                        if (tomorrowTotal.equals("0")) {
                            detail.setRingRatioMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(tomorrowTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setRingRatioMeasure(divide.toString());
                        }
                    } else {
                        detail.setRingRatioMeasure("0");
                    }
                    if (lastYearMonthData.containsKey(consumeTypeId)) {
                        String lastMonthTotal = lastYearMonthData.get(consumeTypeId);
                        if (lastMonthTotal.equals("0")) {
                            detail.setAnalogyMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(lastMonthTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setAnalogyMeasure(divide.toString());
                        }
                    } else {
                        detail.setAnalogyMeasure("0");
                    }
                    if (alarmCountMap.containsKey(consumeTypeId)) {
                        Integer integer = alarmCountMap.get(consumeTypeId);
                        detail.setAlarmCount(integer);
                    } else {
                        detail.setAlarmCount(0);
                    }
                    equipmentReportDetails.add(detail);
                }
            }
            return equipmentReportDetails;
            //季
        } else if (type.equals(3)) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = dateTimeFormatter.format(now);
            String[] split = format.split("-");
            String year = split[0];
            String lastMonthYear = year;
            int monthValue = now.getMonthValue();
            int yearValue = now.getYear();
            int quarter = monthValue / 3 + 1;
            List<String> monthListNow = new ArrayList<>();
            List<String> lastMonthNow = new ArrayList<>();

            if (quarter == 1) {
                monthListNow.add("01");
                monthListNow.add("02");
                monthListNow.add("03");
                lastMonthNow.add("10");
                lastMonthNow.add("11");
                lastMonthNow.add("12");
                lastMonthYear = String.valueOf(yearValue - 1);
            } else if (quarter == 2) {
                monthListNow.add("04");
                monthListNow.add("05");
                monthListNow.add("06");
                lastMonthNow.add("01");
                lastMonthNow.add("02");
                lastMonthNow.add("03");
            } else if (quarter == 3) {
                monthListNow.add("07");
                monthListNow.add("08");
                monthListNow.add("09");
                lastMonthNow.add("04");
                lastMonthNow.add("05");
                lastMonthNow.add("06");
            } else {
                monthListNow.add("10");
                monthListNow.add("11");
                monthListNow.add("12");
                lastMonthNow.add("07");
                lastMonthNow.add("08");
                lastMonthNow.add("09");
            }
            //本月
            List<Map<String, Object>> dataMap = energyEquipmentService.getDayAndMonthMeasure(projectId, format);
            //季度其他月
            List<Map<String, Object>> dateMonthMap = energyEquipmentService.getDataMonthMapByList(projectId, year, monthListNow);

            //环比 上个季度
            List<Map<String, Object>> lastQuarterMap = energyEquipmentService.getDataMonthMapByList(projectId, lastMonthYear, lastMonthNow);

            //类比   去年的这个季度
            List<Map<String, Object>> lastYearQuarterMap = energyEquipmentService.getDataMonthMapByList(projectId, String.valueOf(yearValue - 1), monthListNow);

            //获取本季度的报警数量
            List<Map<String, Object>> alarmCount = alarmWarnService.getQuarterCount(projectId, year, monthListNow);

            Map<Long, Long> dateMonthSpike = new HashMap<>();
            Map<Long, Long> dateMonthPeak = new HashMap<>();
            Map<Long, Long> dateMonthNormal = new HashMap<>();
            Map<Long, Long> dateMonthValley = new HashMap<>();
            Map<Long, Long> dateMonthTotal = new HashMap<>();
            //处理本季度历时月能耗数据
            if (!CollectionUtils.isEmpty(dateMonthMap)) {
                for (Map<String, Object> stringObjectMap : dateMonthMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                    Long monthSpike = Long.parseLong(stringObjectMap.get("monthSpike").toString());
                    Long monthPeak = Long.parseLong(stringObjectMap.get("monthPeak").toString());
                    Long monthNormal = Long.parseLong(stringObjectMap.get("monthNormal").toString());
                    Long monthValley = Long.parseLong(stringObjectMap.get("monthValley").toString());
                    dateMonthSpike.put(consumeTypeId, monthSpike);
                    dateMonthPeak.put(consumeTypeId, monthPeak);
                    dateMonthNormal.put(consumeTypeId, monthNormal);
                    dateMonthValley.put(consumeTypeId, monthValley);
                    dateMonthTotal.put(consumeTypeId, monthTotal);
                }
            }
            //处理环比上季度数据
            Map<Long, String> lastQuarterMonthMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(lastQuarterMap)) {
                for (Map<String, Object> stringObjectMap : lastQuarterMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    lastQuarterMonthMap.put(consumeTypeId, monthTotal);
                }
            }
            //处理类比 去年本季度的数据
            Map<Long, String> lastYearQuarter = new HashMap<>();
            if (!CollectionUtils.isEmpty(lastYearQuarterMap)) {
                for (Map<String, Object> stringObjectMap : lastYearQuarterMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    lastYearQuarter.put(consumeTypeId, monthTotal);
                }
            }
            //处理季度报警数量
            Map<Long, Integer> alarmCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(alarmCount)) {
                for (Map<String, Object> stringObjectMap : alarmCount) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                    alarmCountMap.put(consumeTypeId, num);
                }
            }
            List<EquipmentReportDetail> equipmentReportDetails = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dataMap)) {
                BigDecimal bigTho = new BigDecimal("1000");
                for (Map<String, Object> stringObjectMap : dataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                    Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                    Long monthSpike = Long.parseLong(stringObjectMap.get("monthSpike").toString());
                    Long monthPeak = Long.parseLong(stringObjectMap.get("monthPeak").toString());
                    Long monthNormal = Long.parseLong(stringObjectMap.get("monthNormal").toString());
                    Long monthValley = Long.parseLong(stringObjectMap.get("monthValley").toString());
                    EquipmentReportDetail detail = new EquipmentReportDetail();
                    detail.setConsumeTypeId(consumeTypeId);
                    detail.setConsumeTypeName(consumeTypeName);
                    if (dateMonthTotal.containsKey(consumeTypeId)) {
                        monthTotal = monthTotal + dateMonthTotal.get(consumeTypeId);
                    }
                    if (dateMonthSpike.containsKey(consumeTypeId)) {
                        monthSpike = monthSpike + dateMonthSpike.get(consumeTypeId);
                    }
                    if (dateMonthPeak.containsKey(consumeTypeId)) {
                        monthPeak = monthPeak + dateMonthPeak.get(consumeTypeId);
                    }
                    if (dateMonthNormal.containsKey(consumeTypeId)) {
                        monthNormal = monthNormal + dateMonthNormal.get(consumeTypeId);
                    }
                    if (dateMonthValley.containsKey(consumeTypeId)) {
                        monthValley = monthValley + dateMonthValley.get(consumeTypeId);
                    }
                    if (monthTotal.equals(0l)) {
                        detail.setTotalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthTotal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setTotalMeasure(divide.toString());
                    }
                    if (monthSpike.equals(0l)) {
                        detail.setSpikeMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthSpike);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setSpikeMeasure(divide.toString());
                    }
                    if (monthPeak.equals(0l)) {
                        detail.setPeakMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthPeak);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setPeakMeasure(divide.toString());
                    }
                    if (monthNormal.equals(0l)) {
                        detail.setNormalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthNormal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setNormalMeasure(divide.toString());
                    }
                    if (monthValley.equals(0l)) {
                        detail.setValleyMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthValley);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setValleyMeasure(divide.toString());
                    }
                    //处理环比
                    if (lastQuarterMonthMap.containsKey(consumeTypeId)) {
                        String tomorrowTotal = lastQuarterMonthMap.get(consumeTypeId);
                        if (tomorrowTotal.equals("0")) {
                            detail.setRingRatioMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(tomorrowTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setRingRatioMeasure(divide.toString());
                        }
                    } else {
                        detail.setRingRatioMeasure("0");
                    }
                    //处理类比
                    if (lastYearQuarter.containsKey(consumeTypeId)) {
                        String lastMonthTotal = lastYearQuarter.get(consumeTypeId);
                        if (lastMonthTotal.equals("0")) {
                            detail.setAnalogyMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(lastMonthTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setAnalogyMeasure(divide.toString());
                        }
                    } else {
                        detail.setAnalogyMeasure("0");
                    }
                    //处理报警数量
                    if (alarmCountMap.containsKey(consumeTypeId)) {
                        Integer integer = alarmCountMap.get(consumeTypeId);
                        detail.setAlarmCount(integer);
                    } else {
                        detail.setAlarmCount(0);
                    }
                    equipmentReportDetails.add(detail);

                }
            }
            return equipmentReportDetails;
            //年
        } else if (type.equals(4)) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String format = dateTimeFormatter.format(now);
            int year = now.getYear();
            List<Map<String, Object>> dataMap = energyEquipmentService.getDayAndMonthMeasure(projectId, format);
            List<Map<String, Object>> dateMonthMap = energyEquipmentService.getDataYearMeasure(projectId, String.valueOf(year));
            List<Map<String, Object>> lastYearDataMap = energyEquipmentService.getDataYearTotalMeasure(projectId, String.valueOf(year - 1));
            List<Map<String, Object>> alarmCount = alarmWarnService.getMonthConsumeCount(projectId, String.valueOf(year - 1), null);


            Map<Long, Long> dateMonthSpike = new HashMap<>();
            Map<Long, Long> dateMonthPeak = new HashMap<>();
            Map<Long, Long> dateMonthNormal = new HashMap<>();
            Map<Long, Long> dateMonthValley = new HashMap<>();
            Map<Long, Long> dateMonthTotal = new HashMap<>();
            List<EquipmentReportDetail> equipmentReportDetails = new ArrayList<>();
            if (!CollectionUtils.isEmpty(dateMonthMap)) {
                for (Map<String, Object> stringObjectMap : dateMonthMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                    Long monthSpike = Long.parseLong(stringObjectMap.get("monthSpike").toString());
                    Long monthPeak = Long.parseLong(stringObjectMap.get("monthPeak").toString());
                    Long monthNormal = Long.parseLong(stringObjectMap.get("monthNormal").toString());
                    Long monthValley = Long.parseLong(stringObjectMap.get("monthValley").toString());
                    dateMonthSpike.put(consumeTypeId, monthSpike);
                    dateMonthPeak.put(consumeTypeId, monthPeak);
                    dateMonthNormal.put(consumeTypeId, monthNormal);
                    dateMonthValley.put(consumeTypeId, monthValley);
                    dateMonthTotal.put(consumeTypeId, monthTotal);
                }
            }
            Map<Long, String> lastYearData = new HashMap<>();
            if (!CollectionUtils.isEmpty(lastYearDataMap)) {
                for (Map<String, Object> stringObjectMap : lastYearDataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String monthTotal = stringObjectMap.get("monthTotal").toString();
                    lastYearData.put(consumeTypeId, monthTotal);
                }
            }
            Map<Long, Integer> alarmCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(alarmCount)) {
                for (Map<String, Object> stringObjectMap : alarmCount) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    Integer num = Integer.parseInt(stringObjectMap.get("num").toString());
                    alarmCountMap.put(consumeTypeId, num);

                }
            }
            if (!CollectionUtils.isEmpty(dataMap)) {
                BigDecimal bigTho = new BigDecimal("1000");
                for (Map<String, Object> stringObjectMap : dataMap) {
                    Long consumeTypeId = Long.parseLong(stringObjectMap.get("consumeTypeId").toString());
                    String consumeTypeName = stringObjectMap.get("consumeTypeName").toString();
                    Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                    Long monthSpike = Long.parseLong(stringObjectMap.get("monthSpike").toString());
                    Long monthPeak = Long.parseLong(stringObjectMap.get("monthPeak").toString());
                    Long monthNormal = Long.parseLong(stringObjectMap.get("monthNormal").toString());
                    Long monthValley =Long.parseLong(stringObjectMap.get("monthValley").toString());
                    EquipmentReportDetail detail = new EquipmentReportDetail();
                    detail.setConsumeTypeId(consumeTypeId);
                    detail.setConsumeTypeName(consumeTypeName);
                    if (dateMonthTotal.containsKey(consumeTypeId)) {
                        monthTotal = monthTotal + dateMonthTotal.get(consumeTypeId);
                    }
                    if (dateMonthSpike.containsKey(consumeTypeId)) {
                        monthSpike = monthSpike + dateMonthSpike.get(consumeTypeId);
                    }
                    if (dateMonthPeak.containsKey(consumeTypeId)) {
                        monthPeak = monthPeak + dateMonthPeak.get(consumeTypeId);
                    }
                    if (dateMonthNormal.containsKey(consumeTypeId)) {
                        monthNormal = monthNormal + dateMonthNormal.get(consumeTypeId);
                    }
                    if (dateMonthValley.containsKey(consumeTypeId)) {
                        monthValley = monthValley + dateMonthValley.get(consumeTypeId);
                    }
                    if (monthTotal.equals(0l)) {
                        detail.setTotalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthTotal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setTotalMeasure(divide.toString());
                    }
                    if (monthSpike.equals(0l)) {
                        detail.setSpikeMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthSpike);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setSpikeMeasure(divide.toString());
                    }
                    if (monthPeak.equals(0l)) {
                        detail.setPeakMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthPeak);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setPeakMeasure(divide.toString());
                    }
                    if (monthNormal.equals(0l)) {
                        detail.setNormalMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthNormal);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setNormalMeasure(divide.toString());
                    }
                    if (monthValley.equals(0l)) {
                        detail.setValleyMeasure("0");
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(monthValley);
                        BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                        detail.setValleyMeasure(divide.toString());
                    }
                    //处理环比
                    if (lastYearData.containsKey(consumeTypeId)) {
                        String tomorrowTotal = lastYearData.get(consumeTypeId);
                        if (tomorrowTotal.equals("0")) {
                            detail.setRingRatioMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(tomorrowTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setRingRatioMeasure(divide.toString());
                        }
                    } else {
                        detail.setRingRatioMeasure("0");
                    }
                    //处理类比
                    if (lastYearData.containsKey(consumeTypeId)) {
                        String lastMonthTotal = lastYearData.get(consumeTypeId);
                        if (lastMonthTotal.equals("0")) {
                            detail.setAnalogyMeasure("0");
                        } else {
                            BigDecimal bigDecimal = new BigDecimal(lastMonthTotal);
                            BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                            detail.setAnalogyMeasure(divide.toString());
                        }
                    } else {
                        detail.setAnalogyMeasure("0");
                    }
                    //处理报警数量
                    if (alarmCountMap.containsKey(consumeTypeId)) {
                        Integer integer = alarmCountMap.get(consumeTypeId);
                        detail.setAlarmCount(integer);
                    } else {
                        detail.setAlarmCount(0);
                    }
                    equipmentReportDetails.add(detail);

                }
            }
            return equipmentReportDetails;
        } else {

            return null;
        }
    }

    @Override
    public Map<String, Object> getRingRatioMeasure(Long projectId) {
        List<Map<String, Object>> todayMonth = energyEquipmentService.getMonthTotal(projectId);
        LocalDate now = LocalDate.now();
        List<String> yearMonthList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (long i = -5; i < 0; i++) {
            LocalDate localDate = now.plusMonths(i);
            yearMonthList.add(formatter.format(localDate));
        }
        List<String> xData = new ArrayList<>();
        List<Object> yData = new ArrayList<>();
        BigDecimal bigTho = new BigDecimal("1000");
        for (String s : yearMonthList) {
            String[] split = s.split("-");
            String year = split[0];
            String month = split[1];
            xData.add(s);
            List<Map<String, Object>> dataList = energyEquipmentService.getMonthTotalLastMeasure(projectId, year, month);
            if (!CollectionUtils.isEmpty(dataList)) {
                for (Map<String, Object> stringObjectMap : dataList) {
                    Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                    BigDecimal bigDecimal = new BigDecimal(monthTotal);
                    BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                    yData.add(divide.toString());
                }
            }else{
                yData.add("0");
            }
        }
        if (!CollectionUtils.isEmpty(todayMonth)) {
            for (Map<String, Object> stringObjectMap : todayMonth) {
                String yearMonth = stringObjectMap.get("yearMonth").toString();
                Long monthTotal = Long.parseLong(stringObjectMap.get("monthTotal").toString());
                xData.add(yearMonth);
                BigDecimal bigDecimal = new BigDecimal(monthTotal);
                BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                yData.add(divide.toString());
            }
        }
        Map<String, Object> map = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(xData)) {
            map.put("xData", xData);
            map.put("yData", yData);
        }
        return map;
    }

    @Override
    public Map<String, Object> getMonthAnalogyDetail(Long projectId) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        BigDecimal bigTho = new BigDecimal("1000");
        List<Map<String, Object>> todayMonth = energyEquipmentService.getMonthTotal(projectId);
        Map<String, Long> yearMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(todayMonth)) {
            for (Map<String, Object> map : todayMonth) {
                String yearMonth = map.get("yearMonth").toString();
                Long monthTotal = Long.parseLong(map.get("monthTotal").toString());
                String[] split = yearMonth.split("-");
                yearMap.put(split[1], monthTotal);
            }
        }
        List<Map<String, Object>> monthTotalLastMeasure = energyEquipmentService.getMonthTotalLastMeasure(projectId, String.valueOf(year), null);
        if (!CollectionUtils.isEmpty(monthTotalLastMeasure)) {
            for (Map<String, Object> map : monthTotalLastMeasure) {
                String month = map.get("month").toString();
                Long monthTotal = Long.parseLong(map.get("monthTotal").toString());
                yearMap.put(month, monthTotal);
            }
        }
        Map<String, Long> lastYearMap = new HashMap<>();
        List<Map<String, Object>> lastYearMonthMap = energyEquipmentService.getMonthTotalLastMeasure(projectId, String.valueOf(year - 1), null);
        if (!CollectionUtils.isEmpty(lastYearMonthMap)) {
            for (Map<String, Object> map : lastYearMonthMap) {
                String month = map.get("month").toString();
                Long monthTotal = Long.parseLong(map.get("monthTotal").toString());
                lastYearMap.put(month, monthTotal);
            }
        }
        List<String> xData = DateUtils.getMonth();
        List<Object> yYearData = new ArrayList<>();
        List<Object> yLastYearData = new ArrayList<>();
        for (String xDatum : xData) {
            if (yearMap.containsKey(xDatum)) {
                Long integer = yearMap.get(xDatum);
                BigDecimal bigDecimal = new BigDecimal(integer);
                BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                yYearData.add(divide.toString());
            }else{
                yYearData.add(null);
            }
            if (lastYearMap.containsKey(xDatum)) {
                Long integer = lastYearMap.get(xDatum);
                BigDecimal bigDecimal = new BigDecimal(integer);
                BigDecimal divide = bigDecimal.divide(bigTho, 3, BigDecimal.ROUND_HALF_UP);
                yLastYearData.add(divide.toString());
            } else {
                yLastYearData.add(0);
            }
        }
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("xData", xData);
        Map<String, Object> yearDataMap = new LinkedHashMap<>();
        yearDataMap.put(String.valueOf(year), yYearData);
        yearDataMap.put(String.valueOf(year - 1), yLastYearData);
        dataMap.put("yDate", yearDataMap);
        return dataMap;
    }

    @Override
    public EnergyReportDataDetail getReportData(Long projectId, Integer typeId) {
        EnergyReportDataDetail energyReportDataDetail=new EnergyReportDataDetail();
        //处理基础信息
        List<EnergyEquipment> energyEquipmentList=energyEquipmentService.getByProjectId(projectId);
        if(CollectionUtils.isEmpty(energyEquipmentList)){
          return null;
        }else{
            List<EquipmentReportInfo> equipmentReportInfos=new ArrayList<>();
            for (EnergyEquipment energyEquipment : energyEquipmentList) {
                equipmentReportInfos.add(new EquipmentReportInfo(energyEquipment.getId(),energyEquipment.getName(),energyEquipment.getRatedPower(),energyEquipment.getProjectId()) );
            }
            energyReportDataDetail.setEquipmentReportInfos(equipmentReportInfos);

        }
        //处理总能耗相关信息
        BigDecimal bigThouDec=new BigDecimal(1000);
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);

        //日总能耗和月总能耗
        List<Map<String,Object>> dataTotalMap=energyEquipmentService.getDayMeasure(projectId);
        if(dataTotalMap!=null){
            Object dayTotal = dataTotalMap.get(0).get("dayTotal");
            BigDecimal bigDecimal=new BigDecimal(dayTotal.toString());
            BigDecimal divide = bigDecimal.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
            energyReportDataDetail.setDayTotal(divide.toString());
        }else{
            energyReportDataDetail.setDayTotal("0");
        }
        //处理季度能耗
        String[] split = format.split("-");
        String year = split[0];
        int monthValue = now.getMonthValue();
        int quarter = monthValue / 3 + 1;
        List<String> monthListNow = new ArrayList<>();
        if(quarter==1){
            monthListNow.add("01");
            monthListNow.add("02");
            monthListNow.add("03");
        }else if(quarter==2){
            monthListNow.add("04");
            monthListNow.add("05");
            monthListNow.add("06");
        }else if(quarter==3){
            monthListNow.add("07");
            monthListNow.add("08");
            monthListNow.add("09");
        }else if(quarter==4){
            monthListNow.add("10");
            monthListNow.add("11");
            monthListNow.add("12");
        }
        Long pastDataEnergyTotal=energyEquipmentService.getPastMonth(monthListNow,projectId,year);
        Object monthTotal = dataTotalMap.get(0).get("monthTotal");
        Long jiEnergy= Long.parseLong(monthTotal.toString());
        if(pastDataEnergyTotal!=null){
            jiEnergy=jiEnergy+pastDataEnergyTotal;
        }
        BigDecimal jiEnergyBig=new BigDecimal(jiEnergy.toString());
        BigDecimal divide = jiEnergyBig.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
        energyReportDataDetail.setJiTotal(divide.toString());
        //处理年总能耗
        Long pastYearMonth=energyEquipmentService.getPastMonth(null,projectId,year);
        Long yearEnergy= Long.parseLong(monthTotal.toString());
        if(pastDataEnergyTotal!=null){
            yearEnergy=yearEnergy+pastYearMonth;
        }
        BigDecimal yearEnergyBig=new BigDecimal(yearEnergy.toString());
        BigDecimal divideYear = yearEnergyBig.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
        energyReportDataDetail.setYearTotal(divideYear.toString());
        //处理年总碳排放量
        BigDecimal bigDecimal1 = new BigDecimal("997");
        BigDecimal divide1 = yearEnergyBig.divide(bigDecimal1, 0, BigDecimal.ROUND_UP);
        energyReportDataDetail.setYearCoTotal(divide1.toString());
        //处理能耗总消耗分布图
        List<EquipmentDateTotalMeasure> totalMeasureFinal=new ArrayList<>();
        if(typeId.equals(1)){
            //月总能耗流向图
            List<EquipmentDateTotalMeasure> monthTotalMeasure = energyEquipmentService.getMonthTotalMeasure(projectId, format);
            totalMeasureFinal=monthTotalMeasure;
        }else{
            //获取本月的能耗
            List<EquipmentDateTotalMeasure> monthTotalMeasure = energyEquipmentService.getMonthTotalMeasure(projectId, format);
            //获取本年其他月的能耗数据
            Map<Long,Long> yearTotalMap=new HashMap<>();
            List<EquipmentDateTotalMeasure> equipmentDateTotalMeasures = energyEquipmentService.getPastYearMonth(projectId, year);
            if(!CollectionUtils.isEmpty(equipmentDateTotalMeasures)){
                for (EquipmentDateTotalMeasure equipmentDateTotalMeasure : equipmentDateTotalMeasures) {
                    yearTotalMap.put(equipmentDateTotalMeasure.getId(),Long.parseLong(equipmentDateTotalMeasure.getTotal()));
                }
            }
            if(!CollectionUtils.isEmpty(monthTotalMeasure)){
                for (EquipmentDateTotalMeasure measure : monthTotalMeasure) {
                    Long id = measure.getId();
                    String total = measure.getTotal();
                    if(yearTotalMap.containsKey(id)){
                        total= (Long.parseLong(total)+yearTotalMap.get(id))+"";
                        measure.setTotal(total);
                    }
                    totalMeasureFinal.add(measure);
                }
            }
        }
        //处理数据
        if(!CollectionUtils.isEmpty(totalMeasureFinal)){
            Map<Long,Long> consumeEnergy=new LinkedHashMap<>();
            Map<Long,String> consumeIdNameMap=new LinkedHashMap<>();
            Map<Long,List<EquipmentMeasure>> consumeEnergyMeasureMap=new HashMap<>();
            List<EquipmentMeasure> equipmentMeasure=new ArrayList<>();
            for (EquipmentDateTotalMeasure measure : totalMeasureFinal) {
                String name = measure.getName();
                Long consumeId = measure.getConsumeId();
                String consumeName = measure.getConsumeName();
                String total = measure.getTotal();
                Long totalEnergy=Long.parseLong(total);
                BigDecimal bigTotal=new BigDecimal(total);
                BigDecimal divide2 = bigTotal.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
                EquipmentMeasure equipmentMeasure1 = new EquipmentMeasure(name, divide2.toString());
                equipmentMeasure.add(equipmentMeasure1);
                if(consumeIdNameMap.containsKey(consumeId)){
                    consumeEnergyMeasureMap.get(consumeId).add(equipmentMeasure1);
                    Long aLong = consumeEnergy.get(consumeId);
                    aLong=aLong+totalEnergy;
                    consumeEnergy.put(consumeId,aLong);
                }else{
                    List<EquipmentMeasure> equipmentMeasures = new ArrayList<>();
                    equipmentMeasures.add(equipmentMeasure1);
                    consumeEnergyMeasureMap.put(consumeId,equipmentMeasures);
                    consumeEnergy.put(consumeId,totalEnergy);
                    consumeIdNameMap.put(consumeId,consumeName);
                }
            }
            energyReportDataDetail.setEquipmentMeasure(equipmentMeasure);
            if(!CollectionUtils.isEmpty(consumeIdNameMap)){
                List<ConsumeMeasure> consumeMeasures=new ArrayList<>();
                Long totalMeasure=0l;
                for (Map.Entry<Long, String> longStringEntry : consumeIdNameMap.entrySet()) {
                    Long key = longStringEntry.getKey();
                    String value = longStringEntry.getValue();
                    ConsumeMeasure consumeMeasure=new ConsumeMeasure();
                    consumeMeasure.setName(value);
                    Long aLong = consumeEnergy.get(key);
                    totalMeasure=totalMeasure+aLong;
                    BigDecimal totalBig=new BigDecimal(aLong);
                    BigDecimal divide2 = totalBig.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
                    consumeMeasure.setMeasure(divide2.toString());
                    List<EquipmentMeasure> equipmentMeasureList = consumeEnergyMeasureMap.get(key);
                    consumeMeasure.setEquipmentMeasureList(equipmentMeasureList);
                    consumeMeasures.add(consumeMeasure);
                }
                TotalMeasure totalMeasure1=new TotalMeasure();
                BigDecimal totalBig=new BigDecimal(totalMeasure);
                BigDecimal divide2 = totalBig.divide(bigThouDec, 0, BigDecimal.ROUND_UP);
                totalMeasure1.setMeasure(divide2.toString());
                totalMeasure1.setConsumeMeasure(consumeMeasures);
                energyReportDataDetail.setTotalMeasure(totalMeasure1);
            }

        }
        return energyReportDataDetail;
    }

    @Override
    public List<EnergyStatisticYearDetail> getReportYearData(Long projectId) {
        List<String> monthList = DateUtils.getMonth();
        LocalDate now = LocalDate.now();
        List<EnergyStatisticYearDetail> energyStatisticYearDetails=new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String format = dateTimeFormatter.format(now);
        List<Map<String,Object>> dataTotalMap=energyEquipmentService.getDayMeasure(projectId);
        Long monthTotal = Long.parseLong(dataTotalMap.get(0).get("monthTotal").toString());
        int year = now.getYear();
        String[] split = format.split("-");
        String monthStr=split[1];
        BigDecimal bigThou=new BigDecimal("1000");

        List<Map<String,Object>> yearDataMap=energyEquipmentService.getLastYearMonth(projectId,String.valueOf(year));
        List<Map<String,Object>> lastYearMap=energyEquipmentService.getLastYearMonth(projectId,String.valueOf(year-1));
        Map<String,Long> yearMapData=new HashMap<>();
        Map<String,Long> lastYearMapData =new HashMap<>();
        if(!CollectionUtils.isEmpty(yearDataMap)){
            for (Map<String, Object> map : yearDataMap) {
                String monthData = map.get("month").toString();
                Long monthTotalData = Long.parseLong(map.get("monthTotal").toString());
                yearMapData.put(monthData,monthTotalData);
            }
        }
        if(!CollectionUtils.isEmpty(lastYearMap)){
            for (Map<String, Object> map : lastYearMap) {
                String monthData = map.get("month").toString();
                Long monthTotalData = Long.parseLong(map.get("monthTotal").toString());
                lastYearMapData.put(monthData,monthTotalData);
            }
        }
        yearMapData.put(monthStr,monthTotal);
        EnergyStatisticYearDetail yearDetail=new EnergyStatisticYearDetail();
        yearDetail.setYear(String.valueOf(year));
        List<MonthConsumeData> monthConsumeDataList = yearDetail.getMonthConsumeDataList();

        EnergyStatisticYearDetail lastYearDetail=new EnergyStatisticYearDetail();
        lastYearDetail.setYear(String.valueOf(year-1));
        List<MonthConsumeData> monthConsumeDataList1 = lastYearDetail.getMonthConsumeDataList();

        for (String month : monthList) {
            MonthConsumeData monthConsumeData=new MonthConsumeData();
            MonthConsumeData monthLastConsumeData=new MonthConsumeData();
            //处理本年
            Long aLong = yearMapData.get(month);
            monthConsumeData.setMonth(month);
            if(aLong!=null){
                BigDecimal bigDecimal=new BigDecimal(aLong.toString());
                BigDecimal divide = bigDecimal.divide(bigThou, 1, BigDecimal.ROUND_UP);
                monthConsumeData.setCount(divide.toString());
            }else{
                monthConsumeData.setCount(null);
            }
            monthConsumeDataList.add(monthConsumeData);

            //处理去年
            Long aLong1 = lastYearMapData.get(month);
            monthLastConsumeData.setMonth(month);
            if(aLong1!=null){
                BigDecimal bigDecimal=new BigDecimal(aLong1.toString());
                BigDecimal divide = bigDecimal.divide(bigThou, 1, BigDecimal.ROUND_UP);
                monthLastConsumeData.setCount(divide.toString());
            }else{
                monthLastConsumeData.setCount(null);
            }
            monthConsumeDataList1.add(monthLastConsumeData);
        }
        yearDetail.setMonthConsumeDataList(monthConsumeDataList);
        lastYearDetail.setMonthConsumeDataList(monthConsumeDataList1);
        energyStatisticYearDetails.add(yearDetail);
        energyStatisticYearDetails.add(lastYearDetail);
        return energyStatisticYearDetails;
    }

    @Override
    public List<EquipmentRankReport> getReportRankData(Long projectId, Integer typeId) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yearMonthDay = dateTimeFormatter.format(now);
        if(typeId.equals(1)){
            String[] yearMonthDayArr = yearMonthDay.split("-");
            List<EquipmentRankReport> equipmentRankReports=energyEquipmentService.getRankReportMonthData(projectId,yearMonthDayArr[0],yearMonthDayArr[1]);
            Long totalTotal=0l;
            List<EquipmentRankReport> equipmentRankFinalData=new ArrayList<>();
            if(!CollectionUtils.isEmpty(equipmentRankReports)){
                for (EquipmentRankReport equipmentRankReport : equipmentRankReports) {
                    Long totalCount = equipmentRankReport.getTotalCount();
                    totalTotal=totalTotal+totalCount;
                }
                BigDecimal bigDecimal=new BigDecimal(100);
                BigDecimal bigTh0=new BigDecimal(1000);
                BigDecimal totalBig=new BigDecimal(totalTotal);
                for (EquipmentRankReport equipmentRankReport : equipmentRankReports) {
                    BigDecimal dataBig=new BigDecimal(equipmentRankReport.getTotalCount());
                    BigDecimal multiply = dataBig.multiply(bigDecimal);
                    BigDecimal divide = multiply.divide(totalBig, 1, BigDecimal.ROUND_UP);
                    equipmentRankReport.setPercent(divide.toString());
                    //所有能耗
                    String total = equipmentRankReport.getTotal();
                    BigDecimal bigDecTotal=new BigDecimal(total);
                    BigDecimal divide1 = bigDecTotal.divide(bigTh0, 1, BigDecimal.ROUND_UP);
                    equipmentRankReport.setTotal(divide1.toString());
                    //尖时能耗
                    String spike = equipmentRankReport.getSpike();
                    if(spike.equals("0")){
                        equipmentRankReport.setSpike("0");
                    }else{
                        BigDecimal bigDecSpike=new BigDecimal(spike);
                        BigDecimal divide2 = bigDecSpike.divide(bigTh0, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setSpike(divide2.toString());
                    }
                    //峰时能耗
                    String peak = equipmentRankReport.getPeak();
                    if(peak.equals("0")){
                        equipmentRankReport.setPeak("0");
                    }else{
                        BigDecimal bigDecPeak=new BigDecimal(peak);
                        BigDecimal divide3 = bigDecPeak.divide(bigTh0, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setPeak(divide3.toString());
                    }
                    //平时能耗
                    String normal = equipmentRankReport.getNormal();
                    if(normal.equals("0")){
                        equipmentRankReport.setNormal("0");
                    }else{
                        BigDecimal bigDecNormal=new BigDecimal(normal);
                        BigDecimal divide4 = bigDecNormal.divide(bigTh0, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setNormal(divide4.toString());
                    }
                    //谷时能耗
                    String valley = equipmentRankReport.getValley();
                    if(valley.equals("0")){
                        equipmentRankReport.setValley("0");
                    }else{
                        BigDecimal bigDecValley=new BigDecimal(valley);
                        BigDecimal divide5 = bigDecValley.divide(bigTh0, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setValley(divide5.toString());
                    }
                    equipmentRankFinalData.add(equipmentRankReport);
                }
                return equipmentRankFinalData;
            }
            return null;
        }else{
            String[] yearMonthDayArr = yearMonthDay.split("-");
            //处理本月的数据
            List<EquipmentRankReport> equipmentRankReports=energyEquipmentService.getRankReportMonthData(projectId,yearMonthDayArr[0],yearMonthDayArr[1]);
            //处理之前月的数据
            List<EquipmentRankReport> equipmentLastReports=energyEquipmentService.getRankReportLast(projectId,String.valueOf(year));
            //最终数据
            List<EquipmentRankReport> finalDataMap=new ArrayList<>();
            List<EquipmentRankReport> finalDataMapNew=new ArrayList<>();
            if(CollectionUtils.isEmpty(equipmentLastReports)){
                if(CollectionUtils.isEmpty(equipmentRankReports)){
                    return null;
                }else{
                    finalDataMap= equipmentRankReports;
                }
            }else{
                if(CollectionUtils.isEmpty(equipmentRankReports)){
                    finalDataMap=equipmentLastReports;
                }else{
                    Map<Long,EquipmentRankReport> lastMap=new HashMap<>();
                    for (EquipmentRankReport equipmentLastReport : equipmentLastReports) {
                        lastMap.put(equipmentLastReport.getEquipmentId(),equipmentLastReport);
                    }
                    for (EquipmentRankReport equipmentRankReport : equipmentRankReports) {
                        Long equipmentId = equipmentRankReport.getEquipmentId();
                        if(lastMap.containsKey(equipmentId)){
                            EquipmentRankReport equipmentRankReport1 = lastMap.get(equipmentId);
                            Long total = Long.parseLong(equipmentRankReport1.getTotal());
                            Long spike = Long.parseLong(equipmentRankReport1.getSpike());
                            Long peak = Long.parseLong(equipmentRankReport1.getPeak());
                            Long normal = Long.parseLong(equipmentRankReport1.getNormal());
                            Long valley = Long.parseLong(equipmentRankReport1.getValley());
                            Long total1 = Long.parseLong(equipmentRankReport.getTotal())+total;
                            Long spike1 = Long.parseLong(equipmentRankReport.getSpike())+spike;
                            Long peak1 = Long.parseLong(equipmentRankReport.getPeak())+peak;
                            Long normal1 = Long.parseLong(equipmentRankReport.getNormal())+normal;
                            Long valley1 = Long.parseLong(equipmentRankReport.getValley())+valley;
                            equipmentRankReport.setTotal(total1.toString());
                            equipmentRankReport.setSpike(spike1.toString());
                            equipmentRankReport.setPeak(peak1.toString());
                            equipmentRankReport.setNormal(normal1.toString());
                            equipmentRankReport.setValley(valley1.toString());
                            equipmentRankReport.setTotalCount(total1);
                            finalDataMap.add(equipmentRankReport);
                        }
                    }
                }
            }
            if(!CollectionUtils.isEmpty(finalDataMap)){
                Long totalTotal=0l;
                for (EquipmentRankReport equipmentRankReport : finalDataMap) {
                    Long totalCount = equipmentRankReport.getTotalCount();
                    totalTotal=totalTotal+totalCount ;
                }
                //排序
                equipmentRankReports.sort(new Comparator<EquipmentRankReport>() {
                    @Override
                    public int compare(EquipmentRankReport o1, EquipmentRankReport o2) {
                        Long cha=o1.getTotalCount()-o2.getTotalCount();
                        return Integer.parseInt(cha.toString());
                    }
                });
                //最后处理
                BigDecimal bigDecTho=new BigDecimal("1000");
                BigDecimal bigDecBai=new BigDecimal("100");
                BigDecimal totalTotalBig=new BigDecimal(totalTotal);
                for (EquipmentRankReport equipmentRankReport : finalDataMap) {
                    //计算占比
                    String total = equipmentRankReport.getTotal();
                    BigDecimal bigDecimal=new BigDecimal(total);
                    BigDecimal multiply = bigDecimal.multiply(bigDecBai);
                    BigDecimal divide = multiply.divide(totalTotalBig, 1, BigDecimal.ROUND_UP);
                    equipmentRankReport.setPercent(divide.toString());
                    //计算总能耗
                    BigDecimal divide1 = bigDecimal.divide(bigDecTho, 1,BigDecimal.ROUND_UP);
                    equipmentRankReport.setTotal(divide1.toString());
                    //计算尖时能耗
                    String spike = equipmentRankReport.getSpike();
                    if(spike.equals("0")){
                        equipmentRankReport.setSpike("0");
                    }else{
                        BigDecimal spikeBig=new BigDecimal(spike);
                        BigDecimal divide2 = spikeBig.divide(bigDecTho, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setSpike(divide2.toString());
                    }
                    //计算峰时能耗
                    String peak = equipmentRankReport.getPeak();
                    if(peak.equals("0")){
                        equipmentRankReport.setPeak("0");
                    }else{
                        BigDecimal peakBig=new BigDecimal(peak);
                        BigDecimal divide3 = peakBig.divide(bigDecTho, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setPeak(divide3.toString());
                    }
                    //计算平时能耗
                    String normal = equipmentRankReport.getNormal();
                    if(normal.equals("0")){
                        equipmentRankReport.setNormal("0");
                    }else{
                        BigDecimal normalBig=new BigDecimal(normal);
                        BigDecimal divide4 = normalBig.divide(bigDecTho, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setNormal(divide4.toString());
                    }
                    //计算谷时能耗
                    String valley = equipmentRankReport.getValley();
                    if(valley.equals("0")){
                        equipmentRankReport.setValley("0");
                    }else{
                        BigDecimal valleyBig =new BigDecimal(valley);
                        BigDecimal divide5 = valleyBig.divide(bigDecTho, 1, BigDecimal.ROUND_UP);
                        equipmentRankReport.setValley(divide5.toString());
                    }
                    finalDataMapNew.add(equipmentRankReport);
                }
            }
            return finalDataMapNew;
        }
    }



}
