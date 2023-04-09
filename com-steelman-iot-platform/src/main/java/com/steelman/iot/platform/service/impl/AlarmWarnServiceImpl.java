package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AlarmWarnDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.AlarmWarnDto;
import com.steelman.iot.platform.entity.dto.AlarmWarnPower;
import com.steelman.iot.platform.entity.dto.PowerDeviceDto;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.*;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author tang
 * date 2020-12-02
 */
@Service("alarmWarnService")
public class AlarmWarnServiceImpl extends BaseService implements AlarmWarnService {
    @Resource
    private AlarmWarnDao alarmWarnDao;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private AlarmItemService alarmItemService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private PowerIncomingService powerIncomingService;
    @Resource
    private PowerCompensateService powerCompensateService;
    @Resource
    private PowerWaveService powerWaveService;
    @Resource
    private PowerFeederService powerFeederService;
    @Resource
    private PowerBoxService powerBoxService;

    @Override
    public AlarmHandleStatus getHandleStatus(Long projectId, Long systemId) {
        List<Map<String, Object>> dataMap = alarmWarnDao.getHandleStatus(projectId, systemId, null, null);
        AlarmHandleStatus alarmHandleStatus = new AlarmHandleStatus(0, 0, 0, "100");
        if (!CollectionUtils.isEmpty(dataMap)) {
            for (Map<String, Object> stringObjectMap : dataMap) {
                Object flag = stringObjectMap.get("flag");
                Object countNum = stringObjectMap.get("countNum");
                if (flag.equals(1)) {
                    alarmHandleStatus.setHandlerCount(Integer.parseInt(countNum.toString()));
                } else {
                    alarmHandleStatus.setInHandleCount(Integer.parseInt(countNum.toString()));
                }
            }
            Integer handlerCount = alarmHandleStatus.getHandlerCount();
            Integer inHandleCount = alarmHandleStatus.getInHandleCount();
            Integer total = handlerCount + inHandleCount;
            if (!total.equals(0)) {
                if (handlerCount.equals(0)) {
                    alarmHandleStatus.setPercent("0");
                } else {
                    alarmHandleStatus.setTotal(total);
                    BigDecimal handlerCountDe = new BigDecimal(handlerCount.toString());
                    BigDecimal totalDe = new BigDecimal(total.toString());
                    Double value = handlerCountDe.divide(totalDe, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    NumberFormat num = NumberFormat.getPercentInstance();
                    num.setMinimumFractionDigits(2);
                    String format = num.format(value);
                    alarmHandleStatus.setPercent(format.replace("%", ""));
                }

            }
        }
        return alarmHandleStatus;
    }

    @Override
    public List<AlarmCountStatistics> getAlarmCountByDate(Long projectId, Long systemId) {
        Date currentDate = new Date();
        Date pastDate = DateUtils.getPastDate(currentDate, 6);
        List<AlarmCountStatistics> alarmCountStatistics = new ArrayList<>();
        List<Map<String, Object>> dataList = alarmWarnDao.getAlarmCountByDate(projectId, systemId, pastDate, currentDate);
        String[] pastDateStrArr = DateUtils.getPastDateStrArr(currentDate, 6);
        Map<String, Integer> dataMap = new LinkedHashMap<>();
        for (int i = 0; i < pastDateStrArr.length; i++) {
            dataMap.put(pastDateStrArr[i], 0);
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            for (int i = 0; i < dataList.size(); i++) {
                Map<String, Object> stringObjectMap = dataList.get(i);
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                String createTime = stringObjectMap.get("createTime").toString();
                if (dataMap.containsKey(createTime)) {
                    dataMap.put(createTime, count);
                }
            }
        }
        for (Map.Entry<String, Integer> stringIntegerEntry : dataMap.entrySet()) {
            String key = stringIntegerEntry.getKey();
            Integer value = stringIntegerEntry.getValue();
            String week = DateUtils.getWeek(key);
            AlarmCountStatistics statistics = new AlarmCountStatistics(week, key, value);
            alarmCountStatistics.add(statistics);
        }
        return alarmCountStatistics;
    }

    @Override
    public List<InHandlerAlarm> inHandlerAlarm(Long projectId, long systemId) {
        List<InHandlerAlarm> inHandlerAlarms = new ArrayList<>();
        List<AlarmWarnDto> alarmWarnDtoList = alarmWarnDao.getInHandlerAlarmWarn(projectId, systemId);
        if (!CollectionUtils.isEmpty(alarmWarnDtoList)) {
            List<Area> areaList = regionAreaService.selectByProjectId(projectId);
            List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
            List<AlarmItem> alarmItemList = alarmItemService.selectAll();
            Map<Long, String> areaMap = new HashMap<>();
            Map<Long, String> buildingMap = new HashMap<>();
            Map<Long, String> alarmItemMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(areaList)) {
                for (Area area : areaList) {
                    areaMap.put(area.getId(), area.getName());
                }
            }
            if (!CollectionUtils.isEmpty(buildingList)) {
                for (Building building : buildingList) {
                    buildingMap.put(building.getId(), building.getName());
                }
            }
            if (!CollectionUtils.isEmpty(alarmItemList)) {
                for (AlarmItem alarmItem : alarmItemList) {
                    alarmItemMap.put(alarmItem.getId(), alarmItem.getParentItem());
                }
            }
            for (AlarmWarnDto alarmWarnDto : alarmWarnDtoList) {
                Long alarmWarnId = alarmWarnDto.getId();
                Long areaId = alarmWarnDto.getAreaId();
                String areaName = areaId == null ? "" : areaMap.get(areaId);
                Long buildingId = alarmWarnDto.getBuildingId();
                String buildingName = buildingId == null ? "" : buildingMap.get(buildingId);
                Long alarmItemId = alarmWarnDto.getAlarmItemId();
                String alarmItemName = alarmItemId == null ? "" : alarmItemMap.get(alarmItemId);
                String deviceTypeName = alarmWarnDto.getDeviceTypeName();
                Date createTime = alarmWarnDto.getCreateTime();
                InHandlerAlarm inHandlerAlarm = new InHandlerAlarm(alarmWarnId, areaName, buildingName, deviceTypeName, alarmItemName, createTime);
                inHandlerAlarms.add(inHandlerAlarm);
            }
        }
        return inHandlerAlarms;
    }

    @Override
    public String getHandlerStatus(Long projectId, Long systemId, Long areaId, Long buildingId) {
        List<Map<String, Object>> dataMap = alarmWarnDao.getHandleStatus(projectId, systemId, areaId, buildingId);
        String per = "100%";
        Integer total = 0;
        Integer handlerCount = 0;
        if (!CollectionUtils.isEmpty(dataMap)) {
            for (Map<String, Object> stringObjectMap : dataMap) {
                Integer status = Integer.parseInt(stringObjectMap.get("flag").toString());
                Integer count = Integer.parseInt(stringObjectMap.get("countNum").toString());
                total = total + count;
                if (status.equals(1)) {
                    handlerCount = count;
                }
            }
        }
        if (handlerCount.equals(0)) {
            per = "0%";
        } else if (handlerCount.equals(total)) {
            per = "100%";
        } else {
            BigDecimal handlerCountDe = new BigDecimal(handlerCount.toString());
            BigDecimal totalDe = new BigDecimal(total.toString());
            Double value = handlerCountDe.divide(totalDe, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            NumberFormat num = NumberFormat.getPercentInstance();
            num.setMinimumFractionDigits(2);
            String format = num.format(value);
            per = format.replace("%", "");
            int length = per.length();
            String charLast = String.valueOf(per.charAt(length - 1));
            if (charLast.equals("0")) {
                String substring = per.substring(0, length - 2);
                per = substring + "%";
            }
        }
        return per;
    }

    @Override
    public List<AlarmParentItemVo> getAlarmParentItem(Long projectId) {
        return alarmWarnDao.getAlarmParentItem(projectId);
    }

    @Override
    public List<String> getYear(Long projectId) {
        return alarmWarnDao.getYear(projectId);
    }

    @Override
    public List<String> getMonth(Long projectId, String year) {
        return alarmWarnDao.getMonth(projectId, year);
    }

    @Override
    public List<AlarmWarnInfoVo> getAlarmInfoVo(Long projectId, Long areaId, Long buildingId, Long alarmParentItemId, String year, String month) {
        return alarmWarnDao.getAlarmInfoVo(projectId, areaId, buildingId, alarmParentItemId, year, month);
    }

    @Override
    public List<AlarmCountInfo> getAlarmCountInfo(Long projectId, Long areaId, Long buildingId, Integer count, String year, String month) {
        List<AlarmCountInfo> alarmCountInfoList = new ArrayList<>();
        if (year.equals("0")) {
            year = null;
        }
        if (month.equals("0")) {
            month = null;
        }
        List<Map<String, Object>> countInfo = alarmWarnDao.getAlarmCountInfo(projectId, areaId, buildingId, year, month);
        if (CollectionUtils.isEmpty(countInfo)) {
            return null;
        }
        for (Map<String, Object> stringObjectMap : countInfo) {
            Long areaIdData = stringObjectMap.get("areaId") == null ? 0L : Long.parseLong(stringObjectMap.get("areaId").toString());
            String areaNameObj = stringObjectMap.get("areaName").toString();
            Integer countData = Integer.parseInt(stringObjectMap.get("count").toString());
            Long buildingIdData = stringObjectMap.get("buildingId") == null ? 0L : Long.parseLong(stringObjectMap.get("buildingId").toString());
            String buildingNameData = stringObjectMap.get("buildingName").toString();
            String yearDate = stringObjectMap.get("year").toString();
            String monthData = stringObjectMap.get("month").toString();
            if (countData >= count) {
                AlarmCountInfo alarmCountInfo = new AlarmCountInfo();
                alarmCountInfo.setProjectId(projectId);
                alarmCountInfo.setSystemId(1000L);
                alarmCountInfo.setAreaId(areaIdData);
                alarmCountInfo.setAreaName(areaNameObj);
                alarmCountInfo.setBuildingId(buildingIdData);
                alarmCountInfo.setBuildingName(buildingNameData);
                alarmCountInfo.setCount(countData);
                alarmCountInfo.setMonth(monthData);
                alarmCountInfo.setYear(yearDate);
                alarmCountInfoList.add(alarmCountInfo);
            }
        }

        return alarmCountInfoList;
    }

    @Override
    public Map<String, List<AlarmTypeStatistic>> statisticAlarmType(Long projectId, Long systemId) {
        Map<String, List<AlarmTypeStatistic>> finalDataList = new LinkedHashMap<>();
        Date endDate = new Date();
        Date weekDate = DateUtils.getPastDate(endDate, 6);
        Date monthDate = DateUtils.getLastMonthDate(endDate);
        Date halfYearDate = DateUtils.getLastHalfYearDate(endDate);
        Date yearDate = DateUtils.getLastYearDate(endDate);
        List<Map<String, Object>> weekData = alarmWarnDao.getAlarmTypeCountStatistic(projectId, systemId, weekDate, endDate);
        List<Map<String, Object>> monthData = alarmWarnDao.getAlarmTypeCountStatistic(projectId, systemId, monthDate, endDate);
        List<Map<String, Object>> halfYearData = alarmWarnDao.getAlarmTypeCountStatistic(projectId, systemId, halfYearDate, endDate);
        List<Map<String, Object>> yearData = alarmWarnDao.getAlarmTypeCountStatistic(projectId, systemId, yearDate, endDate);
        List<AlarmTypeStatistic> weekFinalData = new ArrayList<>();
        List<AlarmTypeStatistic> monthFinalData = new ArrayList<>();
        List<AlarmTypeStatistic> halfYearFinalData = new ArrayList<>();
        List<AlarmTypeStatistic> yearFinalData = new ArrayList<>();
        getAlarmTypeCount(weekData, weekFinalData);
        getAlarmTypeCount(monthData, monthFinalData);
        getAlarmTypeCount(halfYearData, halfYearFinalData);
        getAlarmTypeCount(yearData, yearFinalData);
        if (!CollectionUtils.isEmpty(weekFinalData)) {
            finalDataList.put("week", weekFinalData);
        }
        if (!CollectionUtils.isEmpty(monthFinalData)) {
            finalDataList.put("month", monthFinalData);
        }
        if (!CollectionUtils.isEmpty(halfYearFinalData)) {
            finalDataList.put("halfYear", halfYearFinalData);
        }
        if (!CollectionUtils.isEmpty(halfYearFinalData)) {
            finalDataList.put("yearFinalData", yearFinalData);
        }
        return finalDataList;
    }

    @Override
    public Map<String, List<DeviceTypeAlarmStatistic>> deviceTypeStatistic(Long projectId, Long systemId) {
        Map<String, List<DeviceTypeAlarmStatistic>> dataMap = new LinkedHashMap<>();
        Date endDate = new Date();
        Date weekDate = DateUtils.getPastDate(endDate, 6);
        Date monthDate = DateUtils.getLastMonthDate(endDate);
        Date halfYearDate = DateUtils.getLastHalfYearDate(endDate);
        Date yearDate = DateUtils.getLastYearDate(endDate);
        List<Map<String, Object>> weekData = alarmWarnDao.getDeviceTypeCountStatistic(projectId, systemId, weekDate, endDate);
        List<Map<String, Object>> monthData = alarmWarnDao.getDeviceTypeCountStatistic(projectId, systemId, monthDate, endDate);
        List<Map<String, Object>> halfYearData = alarmWarnDao.getDeviceTypeCountStatistic(projectId, systemId, halfYearDate, endDate);
        List<Map<String, Object>> yearData = alarmWarnDao.getDeviceTypeCountStatistic(projectId, systemId, yearDate, endDate);
        List<DeviceTypeAlarmStatistic> weekFinalData = new ArrayList<>();
        List<DeviceTypeAlarmStatistic> monthFinalData = new ArrayList<>();
        List<DeviceTypeAlarmStatistic> halfYearFinalData = new ArrayList<>();
        List<DeviceTypeAlarmStatistic> yearFinalData = new ArrayList<>();
        getFinalDeviceTypeStatistic(weekData, weekFinalData);
        getFinalDeviceTypeStatistic(monthData, monthFinalData);
        getFinalDeviceTypeStatistic(halfYearData, halfYearFinalData);
        getFinalDeviceTypeStatistic(yearData, yearFinalData);
        if (!CollectionUtils.isEmpty(weekFinalData)) {
            dataMap.put("week", weekFinalData);
        }
        if (!CollectionUtils.isEmpty(monthFinalData)) {
            dataMap.put("month", monthFinalData);
        }
        if (!CollectionUtils.isEmpty(halfYearFinalData)) {
            dataMap.put("halfYear", halfYearFinalData);
        }
        if (!CollectionUtils.isEmpty(halfYearFinalData)) {
            dataMap.put("year", yearFinalData);
        }
        if (!CollectionUtils.isEmpty(dataMap)) {
            return dataMap;
        }
        return null;
    }

    @Override
    public List<DeviceOfflineInfo> getOfflineInfo(Long projectId, Long systemId, String flag) {
        List<DeviceOfflineInfo> finalDataList = new ArrayList<>();
        List<Map<String, Object>> dataList = null;
        if ("part".equals(flag)) {
            dataList = alarmWarnDao.getOfflineInfo(projectId, systemId, flag);
        } else {
            dataList = alarmWarnDao.getOfflineInfo(projectId, systemId, null);
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            List<Area> areaList = regionAreaService.selectByProjectId(projectId);
            List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
            List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
            List<Room> roomList = regionRoomService.selectByProjectId(projectId);
            Map<Long, String> areaMap = new HashMap<>();
            Map<Long, String> buildingMap = new HashMap<>();
            Map<Long, String> storeyMap = new HashMap<>();
            Map<Long, String> roomMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(areaList)) {
                for (Area area : areaList) {
                    areaMap.put(area.getId(), area.getName());
                }
            }
            if (!CollectionUtils.isEmpty(buildingList)) {
                for (Building building : buildingList) {
                    buildingMap.put(building.getId(), building.getName());
                }
            }
            if (!CollectionUtils.isEmpty(storeyList)) {
                for (Storey storey : storeyList) {
                    storeyMap.put(storey.getId(), storey.getName());
                }
            }
            if (!CollectionUtils.isEmpty(roomList)) {
                for (Room room : roomList) {
                    roomMap.put(room.getId(), room.getName());
                }
            }
            for (Map<String, Object> stringObjectMap : dataList) {
                StringBuffer locationFinal = new StringBuffer();
                Long alarmWarnId = Long.parseLong(stringObjectMap.get("alarmWarnId").toString());
                Object location = stringObjectMap.get("location");
                if (location != null) {
                    locationFinal.append(location.toString());
                } else {
                    Object areaObj = stringObjectMap.get("areaId");
                    if (areaObj != null) {
                        Long areaId = Long.parseLong(areaObj.toString());
                        String areaName = areaMap.get(areaId);
                        if (!StringUtils.isEmpty(areaName)) {
                            locationFinal.append(areaName);
                        }
                    }
                    Object buildingObj = stringObjectMap.get("buildingId");
                    if (buildingObj != null) {
                        Long buildingId = Long.parseLong(buildingObj.toString());
                        String buildingName = buildingMap.get(buildingId);
                        if (!StringUtils.isEmpty(buildingName)) {
                            locationFinal.append(buildingName);
                        }
                    }
                    Object storeyObj = stringObjectMap.get("storeyId");
                    if (storeyObj != null) {
                        Long storeyId = Long.parseLong(storeyObj.toString());
                        String storeyName = storeyMap.get(storeyId);
                        if (!StringUtils.isEmpty(storeyName)) {
                            locationFinal.append(storeyName);
                        }
                    }
                    Object roomObj = stringObjectMap.get("roomId");
                    if (roomObj != null) {
                        Long roomId = Long.parseLong(roomObj.toString());
                        String roomName = roomMap.get(roomId);
                        if (!StringUtils.isEmpty(roomName)) {
                            locationFinal.append(roomName);
                        }
                    }
                }
                Object createTime = stringObjectMap.get("createTime");
                Date offlineDate = null;
                if (createTime != null) {
                    offlineDate = (Date) createTime;
                }
                Long deviceId = null;
                Object deviceObj = stringObjectMap.get("deviceId");
                if (deviceObj != null) {
                    deviceId = Long.parseLong(deviceObj.toString());
                }
                String serialNum = null;
                Object serialNumObj = stringObjectMap.get("serialNum");
                if (serialNumObj != null) {
                    serialNum = serialNumObj.toString();
                }
                Integer status = null;
                Object statusObj = stringObjectMap.get("status");
                if (statusObj != null) {
                    status = Integer.parseInt(statusObj.toString());
                }
                String deviceName = null;
                Object deviceNameObj = stringObjectMap.get("deviceName");
                if (deviceNameObj != null) {
                    deviceName = deviceNameObj.toString();
                }
                DeviceOfflineInfo deviceOfflineInfo = new DeviceOfflineInfo(alarmWarnId, deviceId, serialNum, deviceName, locationFinal.toString(), offlineDate, status);
                finalDataList.add(deviceOfflineInfo);

            }
        }
        return finalDataList;
    }

    @Override
    public List<DeviceRiskInfo> getRiskDevice(Long projectId, Long systemId, String part) {
        List<DeviceRiskInfo> deviceRiskInfoList = new ArrayList<>();
        List<Map<String, Object>> dataList = null;
        Date endDate = new Date();
        Date strDate = DateUtils.getPastDate(endDate, 59);
        if (part.equals("part")) {
            dataList = alarmWarnDao.getAlarmRiskByDate(projectId, systemId, part, strDate, endDate);
        } else {
            dataList = alarmWarnDao.getAlarmRiskByDate(projectId, systemId, null, strDate, endDate);
        }
        if (!CollectionUtils.isEmpty(dataList)) {
            //处理位置信息
            List<Area> areaList = regionAreaService.selectByProjectId(projectId);
            List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
            List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
            List<Room> roomList = regionRoomService.selectByProjectId(projectId);
            Map<Long, String> areaMap = new HashMap<>();
            Map<Long, String> buildingMap = new HashMap<>();
            Map<Long, String> storeyMap = new HashMap<>();
            Map<Long, String> roomMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(areaList)) {
                for (Area area : areaList) {
                    areaMap.put(area.getId(), area.getName());
                }
            }
            if (!CollectionUtils.isEmpty(buildingList)) {
                for (Building building : buildingList) {
                    buildingMap.put(building.getId(), building.getName());
                }
            }
            if (!CollectionUtils.isEmpty(storeyList)) {
                for (Storey storey : storeyList) {
                    storeyMap.put(storey.getId(), storey.getName());
                }
            }
            if (!CollectionUtils.isEmpty(roomList)) {
                for (Room room : roomList) {
                    roomMap.put(room.getId(), room.getName());
                }
            }
            for (Map<String, Object> stringObjectMap : dataList) {
                Object numObj = stringObjectMap.get("num");
                Integer num = Integer.parseInt(numObj.toString());
                if (num < 5) {
                    continue;
                }
                Object deviceIdObj = stringObjectMap.get("deviceId");
                Object alarmItemIdObj = stringObjectMap.get("alarmItemId");
                Object alarmItemNameObj = stringObjectMap.get("alarmItemName");
                Object deviceNameObj = stringObjectMap.get("deviceName");
                Object statusObj = stringObjectMap.get("status");
                Object locationObj = stringObjectMap.get("location");
                StringBuffer locationBuff = new StringBuffer("");
                if (locationObj != null) {
                    locationBuff.append(locationObj.toString());
                } else {
                    String areaObj = stringObjectMap.get("areaId").toString();
                    if (areaObj != null) {
                        Long areaId = Long.parseLong(areaObj.toString());
                        String areaName = areaMap.get(areaId);
                        if (!StringUtils.isEmpty(areaName)) {
                            locationBuff.append(areaName);
                        }
                    }
                    Object buildingObj = stringObjectMap.get("buildingId");
                    if (buildingObj != null) {
                        Long buildingId = Long.parseLong(buildingObj.toString());
                        String buildingName = buildingMap.get(buildingId);
                        if (!StringUtils.isEmpty(buildingName)) {
                            locationBuff.append(buildingName);
                        }
                    }
                    Object storeyObj = stringObjectMap.get("storeyId");
                    if (storeyObj != null) {
                        Long storeyId = Long.parseLong(storeyObj.toString());
                        String storeyName = storeyMap.get(storeyId);
                        if (!StringUtils.isEmpty(storeyName)) {
                            locationBuff.append(storeyName);
                        }
                    }
                    Object roomObj = stringObjectMap.get("roomId");
                    if (roomObj != null) {
                        Long roomId = Long.parseLong(roomObj.toString());
                        String roomName = roomMap.get(roomId);
                        if (!StringUtils.isEmpty(roomName)) {
                            locationBuff.append(roomName);
                        }
                    }
                }
                //处理完位置信息
                Long deviceId = Long.parseLong(deviceIdObj.toString());
                Long alarmItemId = Long.parseLong(alarmItemIdObj.toString());
                Integer status = Integer.parseInt(statusObj.toString());
                DeviceRiskInfo deviceRiskInfo = new DeviceRiskInfo(deviceId, deviceNameObj.toString(), locationBuff.toString(), alarmItemId, alarmItemNameObj.toString(), status);
                deviceRiskInfoList.add(deviceRiskInfo);
            }
        }
        return deviceRiskInfoList;
    }

    @Override
    public List<DeviceTypeAlarmStatistic> getSafeAlarmTypeStatistic(Long projectId, Long deviceId, Long systemId) {
        List<Map<String, Object>> alarmItemCountData = alarmWarnDao.statisticByAlarmItem(projectId, deviceId, systemId);
        List<DeviceTypeAlarmStatistic> alarmList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(alarmItemCountData)) {
            for (Map<String, Object> alarmItemCountDatum : alarmItemCountData) {
                Integer count = Integer.parseInt(alarmItemCountDatum.get("count").toString());
                String name = alarmItemCountDatum.get("alarmItemParentName").toString();
                DeviceTypeAlarmStatistic deviceTypeAlarmStatistic = new DeviceTypeAlarmStatistic(name, count);
                alarmList.add(deviceTypeAlarmStatistic);
            }
        }
        return alarmList;
    }

    @Override
    public List<AlarmSimpleInfo> getDeviceAlarmHistory(Long projectId, Long deviceId, Long systemId) {
        List<Map<String, Object>> deviceAlarmData = alarmWarnDao.getAlarmInfoByDeviceId(deviceId, projectId, systemId);
        List<AlarmSimpleInfo> alarmSimpleInfoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(deviceAlarmData)) {
            for (Map<String, Object> deviceAlarmDatum : deviceAlarmData) {
                Long alarmWarnId = Long.parseLong(deviceAlarmDatum.get("alarmWarnId").toString());
                Date alarmTime = (Date) deviceAlarmDatum.get("alarmTime");
                Long alarmItemId = Long.parseLong(deviceAlarmDatum.get("alarmItemId").toString());
                String alarmItemName = deviceAlarmDatum.get("alarmItemName").toString();
                Integer handleFlag = Integer.parseInt(deviceAlarmDatum.get("handleFlag").toString());
                Integer status = Integer.parseInt(deviceAlarmDatum.get("status").toString());
                AlarmSimpleInfo alarmSimpleInfo = new AlarmSimpleInfo(deviceId, alarmWarnId, alarmTime, alarmItemId, alarmItemName, handleFlag, status);
                alarmSimpleInfoList.add(alarmSimpleInfo);
            }
        }
        return alarmSimpleInfoList;
    }

    @Override
    public int removeByDeviceSystem(Long projectId, Long deviceId, Long systemId) {
        return alarmWarnDao.removeByDeviceSystem(projectId, deviceId, systemId);
    }

    @Override
    public List<AlarmWarnVo> getAlarmCount(Long projectId, Long systemId) {
        return alarmWarnDao.selectAlarmByProjectId(projectId, systemId);
    }

    @Override
    public List<Map<String, Object>> alarmCount(Long projectId, Long systemId) {
        List<Map<String, Object>> maps = alarmWarnDao.selectAlarmScale(projectId, systemId);
        return maps;
    }

    @Resource
    private PowerDeviceService powerDeviceService;
    @Resource
    private PowerIncomingDeviceService powerIncomingDeviceService;
    @Resource
    private PowerCompensateDeviceService powerCompensateDeviceService;
    @Resource
    private PowerWaveDeviceService powerWaveDeviceService;
    @Resource
    private PowerFeederLoopDeviceService powerFeederLoopDeviceService;
    @Resource
    private PowerBoxLoopDeviceService powerBoxLoopDeviceService;
    @Resource
    private PowerService powerService;


    @Override
    public List<AlarmWarnItemVo> getAlarmList(Long projectId, Long systemId, String part) {
        List<AlarmWarnItemVo> voList = alarmWarnDao.selectProjectAlarm(projectId, systemId, part);
        List<Power> powerList = powerService.selectByProjectId(projectId);
        Map<Long, String> powerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(powerList)) {
            for (Power power : powerList) {
                powerMap.put(power.getId(), power.getName());
            }
        }
        for (AlarmWarnItemVo vo : voList) {
            if (vo.getBindingType() == 0) {
                PowerDevice powerDevice = powerDeviceService.getInfoByDeviceId(vo.getDeviceId());
                vo.setDeviceName(powerMap.get(powerDevice.getId()));

            }
            if (vo.getBindingType() == 1) {
                PowerIncomingDevice device = powerIncomingDeviceService.getInfoByDeviceId(vo.getDeviceId());
                PowerIncoming powerIncoming = powerIncomingService.getIncomingInfo(device.getIncomingId());
                vo.setDeviceName(powerIncoming.getName());
                vo.setPowerName(powerMap.get(powerIncoming.getPowerId()));
            }
            //馈线柜
            if (vo.getBindingType() == 2) {
                PowerCompensateDevice device = powerCompensateDeviceService.getInfoByDeviceId(vo.getDeviceId());
                if(device!=null){
                    PowerCompensate compensateInfo = powerCompensateService.getCompensateInfo(device.getCompensateId());
                    vo.setDeviceName(compensateInfo.getName());
                    vo.setPowerName(powerMap.get(compensateInfo.getPowerId()));
                }
            }
            //滤波柜
            if (vo.getBindingType() == 3) {
                PowerWaveDevice device = powerWaveDeviceService.getInfoByDeviceId(vo.getDeviceId());
                PowerWave waveInfo = powerWaveService.getWaveInfo(device.getWaveId());
                vo.setDeviceName(waveInfo.getName());
                vo.setPowerName(powerMap.get(waveInfo.getPowerId()));
            }
            //馈线柜
            if (vo.getBindingType() == 4) {
                PowerFeederLoopDevice device = powerFeederLoopDeviceService.getInfoByDeviceId(vo.getDeviceId());
                PowerFeeder feederInfo = powerFeederService.getFeederInfo(device.getFeederId());
                vo.setDeviceName(feederInfo.getName());
                vo.setPowerName(powerMap.get(feederInfo.getPowerId()));
            }
            //配电箱
            if (vo.getBindingType() == 5) {
                PowerBoxLoopDevice device = powerBoxLoopDeviceService.getInfoByDeviceId(vo.getDeviceId());
                PowerBox boxInfo = powerBoxService.getBoxInfo(device.getBoxId());
                vo.setDeviceName(boxInfo.getName());
                vo.setPowerName(powerMap.get(boxInfo.getPowerId()));
            }
        }
        return voList;
    }


    @Override
    public List<AlarmWarnItemVo> getDeviceAlarmInfo(Long projectId, Long deviceId, Long systemId) {
        return alarmWarnDao.selectDeviceAlarmInfo(projectId, deviceId, systemId);
    }

    @Override
    public List<Map<String, Object>> getDeviceAlarmCount(Long projectId, Long deviceId, Long systemId) {
        return alarmWarnDao.selectDeviceAlarmCount(projectId, deviceId, systemId);
    }

    @Override
    public List<Long> getInHandlerSafeDevice(Long projectId) {
        return alarmWarnDao.getInHandlerSafeDevice(projectId);
    }

    private void getFinalDeviceTypeStatistic(List<Map<String, Object>> dataList, List<DeviceTypeAlarmStatistic> finalData) {
        if (!CollectionUtils.isEmpty(dataList)) {
            for (Map<String, Object> dataMap : dataList) {
                Integer num = Integer.parseInt(dataMap.get("num").toString());
                String deviceTypeName = dataMap.get("deviceTypeName").toString();
                DeviceTypeAlarmStatistic deviceTypeAlarmStatistic = new DeviceTypeAlarmStatistic(deviceTypeName, num);
                finalData.add(deviceTypeAlarmStatistic);
            }
        }
    }

    private void getAlarmTypeCount(List<Map<String, Object>> alarmTypeList, List<AlarmTypeStatistic> finalDataList) {
        Integer max = 0;
        Map<String, Integer> alarmItemNum = new LinkedHashMap<>();
        alarmItemNum.put("电压过压", 0);
        alarmItemNum.put("电压欠压", 0);
        alarmItemNum.put("电压缺相", 0);
        alarmItemNum.put("电流过流", 0);
        alarmItemNum.put("漏电流过流", 0);
        alarmItemNum.put("通讯异常", 0);
        if (!CollectionUtils.isEmpty(alarmTypeList)) {
            for (Map<String, Object> monthDatum : alarmTypeList) {
                Integer num = Integer.parseInt(monthDatum.get("num").toString());
                String alarmTypeName = monthDatum.get("alarmTypeName").toString();
                if (num > max) {
                    max = num;
                }
                alarmItemNum.put(alarmTypeName, num);
            }
        }
        max = max + 5;
        for (Map.Entry<String, Integer> stringIntegerEntry : alarmItemNum.entrySet()) {
            String key = stringIntegerEntry.getKey();
            Integer value = stringIntegerEntry.getValue();
            AlarmTypeStatistic alarmTypeStatistic = new AlarmTypeStatistic(max, key, value);
            finalDataList.add(alarmTypeStatistic);
        }
    }

    @Override
    public List<AlarmWarn> getByDeviceId(Long deviceId, Long l, String s, Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(s);
            List<AlarmWarn> alarmWarns = alarmWarnDao.getAlarmData(deviceId, l, parse, date);
            return alarmWarns;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int removeByDeviceId(Long deviceId) {
        return alarmWarnDao.removeByDeviceId(deviceId);
    }

    @Override
    public List<PowerAlarmWarnVo> getPowerAlarmWarnVo(Long projectId, Long systemId) {

        List<PowerAlarmWarnVo> powerAlarmWarnVoList = new ArrayList<>();
        List<AlarmWarnPower> alarmWarnInfoVoList = alarmWarnDao.getDeviceAlarmWarn(projectId, systemId);

        if (!CollectionUtils.isEmpty(alarmWarnInfoVoList)) {
            List<Area> areaList = regionAreaService.selectByProjectId(projectId);
            List<Building> buildingList = regionBuildingService.selectByProjectId(projectId);
            List<Storey> storeyList = regionStoreyService.selectByProjectId(projectId);
            List<Room> roomList = regionRoomService.selectByProjectId(projectId);
            Map<Long, String> areaMap = new HashMap<>();
            Map<Long, String> buildingMap = new HashMap<>();
            Map<Long, String> storeyMap = new HashMap<>();
            Map<Long, String> roomMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(areaList)) {
                for (Area area : areaList) {
                    areaMap.put(area.getId(), area.getName());
                }
            }
            if (!CollectionUtils.isEmpty(buildingList)) {
                for (Building building : buildingList) {
                    buildingMap.put(building.getId(), building.getName());
                }
            }
            if (!CollectionUtils.isEmpty(storeyList)) {
                for (Storey storey : storeyList) {
                    storeyMap.put(storey.getId(), storey.getName());
                }
            }
            if (!CollectionUtils.isEmpty(roomList)) {
                for (Room room : roomList) {
                    roomMap.put(room.getId(), room.getName());
                }
            }

            Map<Integer, Set<Long>> dataMap = new LinkedHashMap<>();
            Set<Integer> integerSet = new HashSet<>();
            for (int i = 0; i < 6; i++) {
                integerSet.add(i);
            }
            List<AlarmWarnPower> alarmWarnPowerList = new ArrayList<>();
            for (AlarmWarnPower alarmWarnPower : alarmWarnInfoVoList) {
                Integer bindingType1 = alarmWarnPower.getBindingType();
                if (integerSet.contains(bindingType1)) {
                    alarmWarnPowerList.add(alarmWarnPower);
                }
                Long deviceId = alarmWarnPower.getDeviceId();
                Integer bindingType = alarmWarnPower.getBindingType();
                if (dataMap.containsKey(bindingType)) {
                    Set<Long> deviceIdSet = dataMap.get(bindingType);
                    if (!deviceIdSet.contains(deviceId)) {
                        deviceIdSet.add(deviceId);
                    }
                } else {
                    Set<Long> deviceIdSet = new HashSet<>();
                    deviceIdSet.add(deviceId);
                    dataMap.put(bindingType, deviceIdSet);
                }
            }
            Map<Long, PowerDeviceDto> powerDeviceDtoMap = new HashMap<>();
            for (Map.Entry<Integer, Set<Long>> integerSetEntry : dataMap.entrySet()) {
                Integer key = integerSetEntry.getKey();
                Set<Long> value = integerSetEntry.getValue();
                if (key.equals(0)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                } else if (key.equals(1)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerIncomingDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                } else if (key.equals(2)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerCompensateDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                } else if (key.equals(3)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerWaveDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                } else if (key.equals(4)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerFeederLoopDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                } else if (key.equals(5)) {
                    List<PowerDeviceDto> powerDeviceDtoList = powerBoxLoopDeviceService.findByDeviceSet(projectId, value);
                    if (!CollectionUtils.isEmpty(powerDeviceDtoList)) {
                        for (PowerDeviceDto powerDeviceDto : powerDeviceDtoList) {
                            powerDeviceDtoMap.put(powerDeviceDto.getDeviceId(), powerDeviceDto);
                        }
                    }
                }
            }
            for (AlarmWarnPower alarmWarnPower : alarmWarnPowerList) {
                Long deviceId = alarmWarnPower.getDeviceId();
                String location = alarmWarnPower.getLocation();
                if (StringUtils.isEmpty(location)) {
                    StringBuffer loBuf = new StringBuffer();
                    Long areaId = alarmWarnPower.getAreaId();
                    Long buildingId = alarmWarnPower.getBuildingId();
                    Long storeyId = alarmWarnPower.getStoreyId();
                    Long roomId = alarmWarnPower.getRoomId();
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
                Long alarmItemId = alarmWarnPower.getAlarmItemId();
                String alarmItemName = alarmWarnPower.getAlarmItemName();
                Date createTime = alarmWarnPower.getCreateTime();
                PowerAlarmWarnVo powerAlarmWarnVo = new PowerAlarmWarnVo();
                powerAlarmWarnVo.setAlarmWarnId(alarmWarnPower.getId());
                powerAlarmWarnVo.setAlarmItemId(alarmItemId);
                powerAlarmWarnVo.setAlarmItemName(alarmItemName);
                powerAlarmWarnVo.setCreateTime(createTime);
                powerAlarmWarnVo.setLocation(location);
                PowerDeviceDto powerDeviceDto = powerDeviceDtoMap.get(deviceId);
                if (powerDeviceDto != null) {
                    powerAlarmWarnVo.setLoopId(powerDeviceDto.getLoopId());
                    powerAlarmWarnVo.setLoopName(powerDeviceDto.getLoopName());
                    powerAlarmWarnVo.setPowerDeviceId(powerDeviceDto.getPowerDeviceId());
                    powerAlarmWarnVo.setPowerName(powerDeviceDto.getPowerDeviceName());
                    powerAlarmWarnVo.setPowerId(powerDeviceDto.getPowerId());
                    powerAlarmWarnVo.setPowerName(powerDeviceDto.getPowerName());
                }
                powerAlarmWarnVoList.add(powerAlarmWarnVo);
            }
        }
        if (!CollectionUtils.isEmpty(powerAlarmWarnVoList)) {
            return powerAlarmWarnVoList;
        }
        return null;
    }

    @Override
    public Integer getPowerAlarmWarnCount(Long projectId, Long systemId) {
        List<AlarmWarnPower> alarmWarnInfoVoList = alarmWarnDao.getDeviceAlarmWarn(projectId, systemId);
        Set<Integer> integerSet = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            integerSet.add(i);
        }
        if (!CollectionUtils.isEmpty(alarmWarnInfoVoList)) {
            List<AlarmWarnPower> alarmWarnPowerList = new ArrayList<>();
            for (AlarmWarnPower alarmWarnPower : alarmWarnInfoVoList) {
                if (integerSet.contains(alarmWarnPower.getBindingType())) {
                    alarmWarnPowerList.add(alarmWarnPower);
                }
            }
            return alarmWarnPowerList.size();
        }
        return 0;
    }

    @Override
    public List<AlarmWarnPower> getAlaramWarnByDeviceId(Long deviceId, Long systemId) {
        return alarmWarnDao.getAlaramWarnByDeviceId(deviceId, systemId);
    }

    @Override
    public List<Map<String, Object>> getPowerAlarmCount(List<PowerDeviceInfo> deviceList) {
        return alarmWarnDao.getPowerAlarmCount(deviceList);
    }

    @Override
    public List<AlarmWarnItemVo> getPowerHistory(List<PowerDeviceInfo> deviceList) {
        return alarmWarnDao.getPowerHistory(deviceList);
    }

    @Override
    public int removePowerAlarm(Long deviceId) {
        return alarmWarnDao.removePowerAlarm(deviceId);
    }

    @Override
    public List<Map<String, Object>> getDayAndMonthAlarmCount(Long projectId, String year, String month, String day) {
        return alarmWarnDao.getEnergyDayAndMonthAlarmCount(projectId,year,month,day);
    }

    @Override
    public List<Map<String, Object>> getMonthConsumeCount(Long projectId, String year, String month) {
        return alarmWarnDao.getEnergyDayAndMonthAlarmCount(projectId,year,month,null);
    }

    @Override
    public List<Map<String, Object>> getQuarterCount(Long projectId, String year, List<String> monthList) {
        return alarmWarnDao.getQuarterCount(projectId,year,monthList);
    }

    @Override
    public List<AlarmWarn> getByProjectId(long projectId) {
        return alarmWarnDao.getByProjectId(projectId);
    }

    @Override
    public Integer updateAlarmValue(List<AlarmWarn> alarmWarnList) {
        return alarmWarnDao.updateAlarmValue(alarmWarnList);
    }

    @Override
    public AlarmWarnStatistic getSafePingCountStatistic(Long projectId) {
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime todayTime = LocalDateTime.now();
        LocalDateTime yesterdayTime = todayTime.plusDays(-1l);
        LocalDateTime weekTime = todayTime.plusDays(-6l);
        LocalDateTime monthTime = todayTime.plusDays(-29l);
        String today = dateTimeFormatter.format(todayTime);
        String yesterday = dateTimeFormatter.format(yesterdayTime);
        String week = dateTimeFormatter.format(weekTime);
        String month = dateTimeFormatter.format(monthTime);
        Integer todayCount=alarmWarnDao.getDayCount(projectId,today);
        Integer yesterdayCount = alarmWarnDao.getDayCount(projectId,yesterday);
        Integer weekCount=alarmWarnDao.getBetweenCount(projectId,week,today);
        Integer monthCount=alarmWarnDao.getBetweenCount(projectId,month,today);
        //查询未处理
        Integer inHandlerCount=alarmWarnDao.getInHandlerCount(projectId);
        //查询已处理
        Integer handlerCount=alarmWarnDao.getHandlerCount(projectId);
        //总数
        if(handlerCount==null){
            handlerCount=0;
        }
        if(inHandlerCount==null){
            inHandlerCount=0;
        }
        Integer totalCount=inHandlerCount+handlerCount;
        AlarmWarnStatistic alarmWarnStatistic=new AlarmWarnStatistic();
        alarmWarnStatistic.setDayCount(todayCount==null?0:todayCount);
        alarmWarnStatistic.setYesterdayCount(yesterdayCount==null?0:yesterdayCount);
        alarmWarnStatistic.setWeekCount(weekCount==null?0:weekCount);
        alarmWarnStatistic.setMonthCount(monthCount==null?0:monthCount);
        alarmWarnStatistic.setInHandlerCount(inHandlerCount==null?0:inHandlerCount);
        alarmWarnStatistic.setHandlerCount(handlerCount==null?0:handlerCount);
        alarmWarnStatistic.setTotalCount(totalCount==null?0:totalCount);
        if(handlerCount.equals(totalCount)){
            alarmWarnStatistic.setPercent("100");
        }else if(handlerCount.equals(0)){
            alarmWarnStatistic.setPercent("0");
        }else{
            handlerCount=handlerCount*100;
            BigDecimal totalBig=new BigDecimal(totalCount);
            BigDecimal handlerBig=new BigDecimal(handlerCount);
            BigDecimal divide = handlerBig.divide(totalBig, 1, BigDecimal.ROUND_HALF_UP);
            alarmWarnStatistic.setPercent(divide.toString());
        }
        return alarmWarnStatistic;
    }

    @Override
    public Map<String, Object> getSafeAlarmRingData(Long projectId) throws Exception {
        LocalDate dateNow = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lastData = dateNow.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate lastMonth = dateNow.plusMonths(-1);
        LocalDate begDate = lastMonth.with(TemporalAdjusters.firstDayOfMonth());
        String beg = dateTimeFormatter.format(begDate);
        String[] split = beg.split("-");
        String begYearMonth=split[0]+"-"+split[1];

        String end = dateTimeFormatter.format(lastData);
        String[] splits = end.split("-");
        String endYearMonth=splits[0]+"-"+splits[1];

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date begDateS = simpleDateFormat.parse(beg);
        Date endDateS = simpleDateFormat.parse(end);

        List<Map<String,Object>> dataMap=alarmWarnDao.getAlarmCountByDate(projectId,1000l,begDateS,endDateS);
        Map<String,Integer> begDataMap=new LinkedHashMap<>();
        Map<String,Integer> endDataMap=new LinkedHashMap<>();
        for (int i = 1; i <32 ; i++) {
            begDataMap.put(String.valueOf(i),null);
            endDataMap.put(String.valueOf(i),null);
        }
        if(!CollectionUtils.isEmpty(dataMap)){
            for (Map<String, Object> stringObjectMap : dataMap) {
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                String yearMonthDay = stringObjectMap.get("createTime").toString();
                int index = yearMonthDay.lastIndexOf("-");
                String yearMonth = yearMonthDay.substring(0, index);
                String day = yearMonthDay.substring(index + 1);
                Integer dayInt= Integer.parseInt(day);
                day=String.valueOf(dayInt);
                if(begYearMonth.equals(yearMonth)){
                    begDataMap.put(day,count);
                }else{
                    endDataMap.put(day,count);
                }


            }
        }
        Map<String,Object> dataFinal=new LinkedHashMap<>();
        dataFinal.put(begYearMonth,begDataMap);
        dataFinal.put(endYearMonth,endDataMap);
        return dataFinal ;
    }

    @Override
    public Map<String, Object> getAlarmRecentData(Long projectId, Integer type) throws Exception {
        LocalDate endTimeDate = LocalDate.now();
        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate begTimeDate=null;
        String[] dateArr=null;
        Date date=new Date();
        if(type.equals(1)){
            begTimeDate = endTimeDate.plusDays(-6);
            dateArr = DateUtils.getPastDateStrArr(date, 6);
        }else if(type.equals(2)){
            begTimeDate = endTimeDate.plusDays(-14);
            dateArr = DateUtils.getPastDateStrArr(date, 14);
        }else if(type.equals(3)){
            begTimeDate = endTimeDate.plusDays(-29);
            dateArr = DateUtils.getPastDateStrArr(date, 29);
        }
        String begTime = dateTimeFormatter.format(begTimeDate);
        String endTime = dateTimeFormatter.format(endTimeDate);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Date begDate = simpleDateFormat.parse(begTime);
        Date endDate = simpleDateFormat.parse(endTime);
        List<Map<String, Object>> alarmCountByDate = alarmWarnDao.getAlarmCountByDate(projectId, 1000l, begDate, endDate);
        Map<String,Integer> dataMap=new HashMap<>();
        if(!CollectionUtils.isEmpty(alarmCountByDate)){
            for (Map<String, Object> stringObjectMap : alarmCountByDate) {
                String yearMonthDay = stringObjectMap.get("createTime").toString();
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                dataMap.put(yearMonthDay,count);
            }
        }
        List<String> xList=new ArrayList<>();
        List<Integer> yList=new ArrayList<>();
        for (int i = 0; i < dateArr.length; i++) {
            String yearMonthDay = dateArr[i];
            xList.add(yearMonthDay);
            if(dataMap.containsKey(yearMonthDay)){
                yList.add(dataMap.get(yearMonthDay));
            }else{
                yList.add(0);
            }
        }
        Map<String, Object> finalDataMap=new LinkedHashMap<>();
        finalDataMap.put("xData",xList);
        finalDataMap.put("yData",yList);
        return finalDataMap;
    }

    @Override
    public List<DeviceTypeAlarmStatistic> getAlarmTypeStatistic(Long projectId) {
        List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics=new ArrayList<>();
        List<Map<String, Object>> alarmItemCountData =alarmWarnDao.getAlarmItemData(projectId);
        if(!CollectionUtils.isEmpty(alarmItemCountData)){
            for (Map<String, Object> alarmItemCountDatum : alarmItemCountData) {
                Integer count = Integer.parseInt(alarmItemCountDatum.get("count").toString());
                String alarmItem = alarmItemCountDatum.get("alarmItemParentName").toString();
                deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic(alarmItem,count));
            }
        }
        return deviceTypeAlarmStatistics;
    }

    @Override
    public List<AlarmWarnDetailInfo> getInHandlerWarnInfo(Long projectId) {
        List<Map<String,Object>> dataList=alarmWarnDao.getInHandlerAlarmWarnInfo(projectId);
        if(!CollectionUtils.isEmpty(dataList)){
            List<AlarmWarnDetailInfo> alarmWarnDetailInfos=new ArrayList<>();
            Map<Long, String> areaMap = regionAreaService.getAreaMap(projectId);
            Map<Long, String> buildingMap = regionBuildingService.getBuildingMap(projectId);
            Map<Long, String> storeyMap = regionStoreyService.getStoreyMap(projectId);
            Map<Long, String> roomMap = regionRoomService.getRoomMap(projectId);
            for (Map<String, Object> stringObjectMap : dataList) {
                Long alarmWarnId = Long.parseLong(stringObjectMap.get("alarmWarnId").toString());
                Long deviceId=Long.parseLong(stringObjectMap.get("deviceId").toString());
                String deviceName=stringObjectMap.get("deviceName").toString();
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                String alarmItemName=stringObjectMap.get("alarmItemName").toString();
                String dateTime=stringObjectMap.get("dateTime").toString();
                AlarmWarnDetailInfo alarmWarnDetailInfo=new AlarmWarnDetailInfo();
                alarmWarnDetailInfo.setAlarmWarnId(alarmWarnId);
                alarmWarnDetailInfo.setAlarmItemName(alarmItemName);
                alarmWarnDetailInfo.setDateTime(dateTime);
                alarmWarnDetailInfo.setDeviceId(deviceId);
                alarmWarnDetailInfo.setDeviceName(deviceName);
                alarmWarnDetailInfo.setHandleFlag(0);
                alarmWarnDetailInfo.setProjectId(projectId);
                alarmWarnDetailInfo.setStatus(status);
                Object location = stringObjectMap.get("location");
                if(location==null){
                    StringBuffer stringBuffer=new StringBuffer();
                    Object areaIdObj = stringObjectMap.get("areaId");
                    if(areaIdObj!=null){
                        Long areaId=Long.parseLong(areaIdObj.toString());
                        String areaName = areaMap.get(areaId);
                        if(!StringUtils.isEmpty(areaName)){
                            stringBuffer.append(areaName);
                        }
                    }
                    Long buildingId = Long.parseLong(stringObjectMap.get("buildingId").toString());
                    if(buildingMap.containsKey(buildingId)){
                        stringBuffer.append(buildingMap.get(buildingId));
                    }
                    Long storeyId = Long.parseLong(stringObjectMap.get("storeyId").toString());
                    if(storeyMap.containsKey(storeyId)){
                        stringBuffer.append(storeyMap.get(storeyId));
                    }
                    Long roomId = Long.parseLong(stringObjectMap.get("roomId").toString());
                    if(roomMap.containsKey(roomId)){
                        stringBuffer.append(roomMap.get(roomId));
                    }
                    alarmWarnDetailInfo.setLocation(stringBuffer.toString());
                }else{
                    alarmWarnDetailInfo.setLocation(location.toString());
                }
                alarmWarnDetailInfos.add(alarmWarnDetailInfo);
            }
            return alarmWarnDetailInfos;

        }
        return null;
    }

    @Override
    public List<DeviceTypeAlarmStatistic> getAreaTypeAlarm(Long projectId) {
        List<Map<String,Object>> dataMap= alarmWarnDao.getAreaLocationInfo(projectId);
        Map<Long, String> areaMap = regionAreaService.getAreaMap(projectId);
        if(!CollectionUtils.isEmpty(dataMap)){
            Integer unknown=0;
            Integer custom=0;
            Map<Long,Integer> areaIntMap=new LinkedHashMap<>();
            for (Map<String, Object> stringObjectMap : dataMap) {
                Long areaId = Long.parseLong(stringObjectMap.get("areaId").toString());
                Object location = stringObjectMap.get("location");
                if(location!=null){
                    custom++;
                }else{
                    if(areaId.equals(0l)){
                        unknown++;
                    }else{
                        if(areaIntMap.containsKey(areaId)){
                            Integer count = areaIntMap.get(areaId);
                            count=count+1;
                            areaIntMap.put(areaId,count);
                        }else{
                            areaIntMap.put(areaId,1);
                        }
                    }
                }
            }
            List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics=new ArrayList<>();
            for (Map.Entry<Long, Integer> longIntegerEntry : areaIntMap.entrySet()) {
                Long areaId = longIntegerEntry.getKey();
                Integer value = longIntegerEntry.getValue();
                String areaName=areaMap.get(areaId);
                deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic(areaName,value));
            }
            if(!unknown.equals(0)){
                deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("未知区域",unknown));
            }
            if(!custom.equals(0)){
                deviceTypeAlarmStatistics.add(new DeviceTypeAlarmStatistic("自定义区域",custom));
            }
            return deviceTypeAlarmStatistics;
        }
        return null;
    }

    @Override
    public int save(AlarmWarn alarmWarn) {
        return alarmWarnDao.insert(alarmWarn);
    }

    @Override
    public List<Map<String, Object>> getDeviceTypeStatistic(Set<Long> deviceSet) {
        return alarmWarnDao.getDeviceTypeStatistic(deviceSet);
    }
}
