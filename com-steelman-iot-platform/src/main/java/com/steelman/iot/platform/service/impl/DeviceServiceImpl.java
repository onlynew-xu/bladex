package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceDao;
import com.steelman.iot.platform.device.MT300CDeviceRemoteService;
import com.steelman.iot.platform.device.MT300DeviceRemoteService;
import com.steelman.iot.platform.device.MT300SDeviceRemoteService;
import com.steelman.iot.platform.device.SafeDeviceRemoteService;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.DeviceStatusStatistic;
import com.steelman.iot.platform.largescreen.vo.SafeDeviceCount;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author tang
 * date 2020-12-03
 */
@Service("deviceService")
public class DeviceServiceImpl extends BaseService implements DeviceService {
    @Resource
    private DeviceDao deviceDao;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private AlarmInfoService alarmInfoService;
    @Resource
    private RegionAreaService regionAreaService;
    @Resource
    private RegionBuildingService regionBuildingService;
    @Resource
    private RegionStoreyService regionStoreyService;
    @Resource
    private RegionRoomService regionRoomService;
    @Resource
    private DevicePictureService devicePictureService;
    @Resource
    private DeviceDataSafeElecService deviceDataSafeElecService;
    @Resource
    private AlarmConfigService alarmConfigService;
    @Resource
    private DeviceTaskService deviceTaskService;
    @Resource
    private DeviceDataWaterElecService deviceDataWaterElecService;
    @Resource
    private DeviceSystemService deviceSystemService;
    @Resource
    private DeviceDataSmokeElecService deviceDataSmokeElecService;
    @Resource
    private DataTempatureHumidityService dataTempatureHumidityService;
    @Resource
    private DeviceDataSmartElecService deviceDataSmartElecService;
    @Resource
    private DeviceDataCompensateService deviceDataCompensateService;
    @Resource
    private DeviceDataWaveService deviceDataWaveService;
    @Resource
    private DeviceParamsSafeElecService deviceParamsSafeElecService;
    @Resource
    private DeviceParamsSmartElecService deviceParamsSmartElecService;
    @Resource
    private DeviceParamSmokeService deviceParamSmokeService;
    @Resource
    private DeviceParamsWaterService deviceParamsWaterService;
    @Resource
    private DeviceParamsDoorService deviceParamsDoorService;
    @Resource
    private ParamsTemperaturehumidityService paramsTemperaturehumidityService;
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
    private EnergyEquipmentDeviceService energyEquipmentDeviceService;
    @Resource
    private DeviceTypeAlarmService deviceTypeAlarmService;
    @Resource
    private AlarmItemService alarmItemService;
    @Resource
    private DeviceTypePictureService deviceTypePictureService;
    @Resource
    private SafeDeviceRemoteService safeDeviceRemoteService;
    @Resource
    private MT300DeviceRemoteService mt300DeviceRemoteService;
    @Resource
    private MT300CDeviceRemoteService mt300CDeviceRemoteService;
    @Resource
    private MonitorDeviceService monitorDeviceService;
    @Resource
    private MonitorService monitorService;
    @Resource
    private DeviceParamsWaveElecService deviceParamsWaveElecService;
    @Resource
    private DeviceParamsCompensateElecService deviceParamsCompensateElecService;
    @Resource
    private DeviceAlarmService deviceAlarmService;
    @Resource
    private DeviceMeasurementService deviceMeasurementService;
    @Resource
    private DeviceDataDoorElecService deviceDataDoorElecService;
    @Resource
    private DeviceParamSmartSuperService deviceParamSmartSuperService;
    @Resource
    private DeviceDataSmartSuperService deviceDataSmartSuperService;
    @Resource
    private MT300SDeviceRemoteService mt300SDeviceRemoteService;

    public void insertSelective(Device record) {
        deviceDao.insertSelective(record);
    }

    public void update(Device record) {
        deviceDao.updateByPrimaryKeySelective(record);
    }

    public void deleteById(Long id) {
        deviceDao.deleteByPrimaryKey(id);
    }

    public Pager<T> selectByAll(Map<String, Integer> params, Long deviceTypeId, Long projectId) {
        List<T> list = deviceDao.selectByAll((params.get("page") - 1) * params.get("size"), params.get("size"), deviceTypeId, projectId);
        Pager<T> pager = pagerText(params, list);
        return pager;
    }

    public Device findById(Long id) {
        return deviceDao.selectByPrimaryKey(id);
    }

    @Override
    public int insert(Device device) {
        return deviceDao.insert(device);
    }

    @Override
    public Device selectBySerialNum(String serialNum) {
        return deviceDao.selectBySerialNum(serialNum);
    }

    @Override
    public List<Device> selectByDeviceTypeId(Long projectId, Long deviceTypeId) {
        return deviceDao.selectByDeviceTypeId(projectId, deviceTypeId);
    }

    @Override
    public List<DeviceStatisticsInfo> getDeviceStatistic(Long projectId, Long systemId) {
        List<DeviceStatisticsInfo> finalData = new ArrayList<>();
        List<DeviceType> deviceTypes = deviceTypeService.listAll();
        Map<Long, String> deviceTypeMap = new LinkedHashMap<>();
        for (DeviceType deviceType : deviceTypes) {
            deviceTypeMap.put(deviceType.getId(), deviceType.getName());
        }
        List<Map<String, Object>> dataList = deviceDao.getDeviceStatistic(projectId, systemId);
        if (!CollectionUtils.isEmpty(dataList)) {
            Map<Long, DeviceStatisticsInfo> dataMap = new LinkedHashMap<>();
            for (Map<String, Object> stringObjectMap : dataList) {
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                Long deviceTypeId = Long.parseLong(stringObjectMap.get("deviceTypeId").toString());
                if (dataMap.containsKey(deviceTypeId)) {
                    DeviceStatisticsInfo deviceStatisticsInfo = dataMap.get(deviceTypeId);
                    deviceStatisticsInfo.setTotal(deviceStatisticsInfo.getTotal() + count);
                    if (status.equals(1) || status.equals(3)) {
                        deviceStatisticsInfo.setInlineCount(deviceStatisticsInfo.getInlineCount()+count);
                    } else {
                        deviceStatisticsInfo.setOutlineCount(deviceStatisticsInfo.getOutlineCount()+count);
                    }
                } else {
                    DeviceStatisticsInfo deviceStatisticsInfo = new DeviceStatisticsInfo();
                    deviceStatisticsInfo.setDeviceTypeId(deviceTypeId);
                    deviceStatisticsInfo.setTotal(count);
                    deviceStatisticsInfo.setInlineCount(0);
                    deviceStatisticsInfo.setOutlineCount(0);
                    deviceStatisticsInfo.setDeviceTypeName(deviceTypeMap.get(deviceTypeId));
                    if (status.equals(1) || status.equals(3)) {
                        deviceStatisticsInfo.setInlineCount(count);
                    } else {
                        deviceStatisticsInfo.setOutlineCount(count);
                    }
                    dataMap.put(deviceTypeId, deviceStatisticsInfo);
                }
            }
            if (!CollectionUtils.isEmpty(dataMap)) {
                for (Map.Entry<Long, DeviceStatisticsInfo> longDeviceStatisticsDtoEntry : dataMap.entrySet()) {
                    finalData.add(longDeviceStatisticsDtoEntry.getValue());
                }
            }
        }
        if (!CollectionUtils.isEmpty(finalData)) {
            for (DeviceStatisticsInfo finalDatum : finalData) {
                Integer total = finalDatum.getTotal();
                finalDatum.setDeviceTypeName(deviceTypeMap.get(finalDatum.getDeviceTypeId()));
                Integer inlineCount = finalDatum.getInlineCount();
                if (total.equals(inlineCount)) {
                    finalDatum.setPercent("100");
                } else if (inlineCount.equals(0)) {
                    finalDatum.setPercent("0");
                } else {
                    BigDecimal handlerCountDe = new BigDecimal(inlineCount.toString());
                    BigDecimal totalDe = new BigDecimal(total.toString());
                    Double value = handlerCountDe.divide(totalDe, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
                    NumberFormat num = NumberFormat.getPercentInstance();
                    num.setMinimumFractionDigits(2);
                    finalDatum.setPercent(num.format(value).replace("%", ""));
                }
            }
        }
        return finalData;
    }

    @Override
    public Map<String, Integer> staticDeviceDaPin(long projectId, Long system) {
        Map<String, Integer> finalMap = new LinkedHashMap<>();
        finalMap.put("total", 0);
        List<DeviceType> deviceTypes = deviceTypeService.listAll();
        Map<Long, String> deviceTypeMap = new LinkedHashMap<>();
        for (DeviceType deviceType : deviceTypes) {
            deviceTypeMap.put(deviceType.getId(), deviceType.getName());
        }
        List<Map<String, Object>> deviceStatisticDaPing = deviceDao.getDeviceStatisticDaPing(projectId, system);
        if (deviceStatisticDaPing != null) {
            Integer count = 0;
            for (Map<String, Object> stringObjectMap : deviceStatisticDaPing) {
                Integer num = Integer.parseInt(stringObjectMap.get("count").toString());
                Long deviceTypeId = Long.parseLong(stringObjectMap.get("deviceTypeId").toString());
                if (deviceTypeMap.containsKey(deviceTypeId)) {
                    String deviceTypeName = deviceTypeMap.get(deviceTypeId);
                    finalMap.put(deviceTypeName, num);
                    count = count + num;
                }
            }
            finalMap.put("total", count);
            finalMap.put("消防电源", 0);
            finalMap.put("末端试水", 0);
            finalMap.put("烟雾探测", 0);
            finalMap.put("防火门", 0);
            finalMap.put("故障电弧", 0);
        }
        return finalMap;

    }

    @Override
    public DeviceStatusStatistic deviceStatusAndAlarmPer(Long projectId, Long areaId, Long buildingId) {
        DeviceStatusStatistic deviceStatusStatistic = new DeviceStatusStatistic(0, 0, 0, 0, "100%");
        if (areaId.equals(0l)) {
            areaId = null;
        }
        if (buildingId.equals(0l)) {
            buildingId = null;
        }
        //处理设备状态和对应的数量
        List<Map<String, Object>> dataMap = deviceDao.deviceStatusStatistic(projectId, areaId, buildingId);
        if (!CollectionUtils.isEmpty(dataMap)) {
            Integer total = 0;
            Integer inlineCount = 0;
            Integer outLineCount = 0;
            Integer alarmCount = 0;
            for (Map<String, Object> stringObjectMap : dataMap) {
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                Integer count = Integer.parseInt(stringObjectMap.get("count").toString());
                total = total + count;
                if (status.equals(2) || status.equals(4)) {
                    outLineCount = outLineCount + count;
                } else if (status.equals(3)) {
                    inlineCount = inlineCount + count;
                } else {
                    alarmCount = alarmCount + count;
                }
            }
            deviceStatusStatistic.setTotalCount(total);
            deviceStatusStatistic.setInLineCount(inlineCount);
            deviceStatusStatistic.setOutLineCount(outLineCount);
            deviceStatusStatistic.setAlarmCount(alarmCount);
        }
        String percent = alarmWarnService.getHandlerStatus(projectId, 1000L, areaId, buildingId);
        deviceStatusStatistic.setHandlerPer(percent);
        return deviceStatusStatistic;
    }

    @Override
    public List<SafeDeviceCenter> safeDeviceCenter(Long projectId, Long systemId) {
        List<SafeDeviceCenter> finalData = new ArrayList<>();
        List<Map<String, Object>> dataList = deviceDao.getDeviceSimpleInfo(projectId, systemId);
        List<Long> alarmDeviceInfoMap=alarmWarnService.getInHandlerSafeDevice(projectId);
        if (!CollectionUtils.isEmpty(dataList)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            List<DeviceSimpleInfoVo> allDeviceInfo = new ArrayList<>();
            List<DeviceSimpleInfoVo> alarmDeviceInfo = new ArrayList<>();
            List<DeviceSimpleInfoVo> offlineDeviceInfo = new ArrayList<>();
            Map<Long, List<DeviceSimpleInfoVo>> deviceSimpleInfo = new LinkedHashMap<>();
            Map<Long, String> hashMap = new HashMap<>();
            hashMap.put(1L, "IOTDAE-1000");
            hashMap.put(2L, "IOTDAE-1500");
            hashMap.put(3L, "用电安全预警模块A设备");
            hashMap.put(4L, "用电安全预警模块B设备");
            hashMap.put(5L, "IOTDAE-4000");
            hashMap.put(6L, "IOTDAE-3000");
            hashMap.put(7L, "烟雾探测设备");
            hashMap.put(8L, "末端试水设备");
            hashMap.put(9L, "防火门设备");
            hashMap.put(10L, "滤波控制器");
            hashMap.put(11L, "补偿控制器");
            hashMap.put(12L, "互联网温湿度");
            hashMap.put(13L, "IOTDAE-5000");
            Set<Long> alarmSet=new HashSet<>();
            for (Map<String, Object> stringObjectMap : dataList) {
                Long deviceId = Long.parseLong(stringObjectMap.get("deviceId").toString());
                String deviceName = stringObjectMap.get("deviceName").toString();
                Long deviceTypeId = Long.parseLong(stringObjectMap.get("deviceTypeId").toString());
                String deviceTypeName = stringObjectMap.get("deviceTypeName").toString();
                Date createTimeDate = (Date) stringObjectMap.get("createTime");
                String createTime = simpleDateFormat.format(createTimeDate);
                Integer status = Integer.parseInt(stringObjectMap.get("status").toString());
                DeviceSimpleInfoVo deviceSimpleInfoVo = new DeviceSimpleInfoVo(deviceId, deviceName, deviceTypeId, deviceTypeName, createTime, status);
                allDeviceInfo.add(deviceSimpleInfoVo);
                if (status.equals(2) || status.equals(4)) {
                    offlineDeviceInfo.add(deviceSimpleInfoVo);
                } else if (status.equals(1)) {
                    alarmSet.add(deviceId);
                    alarmDeviceInfo.add(deviceSimpleInfoVo);
                }
                if(alarmDeviceInfoMap!=null){
                  if(alarmDeviceInfoMap.contains(deviceId)){
                      if(!alarmSet.contains(deviceId)){
                          alarmDeviceInfo.add(deviceSimpleInfoVo);
                      }
                  }
                }
                if (deviceSimpleInfo.containsKey(deviceTypeId)) {
                    List<DeviceSimpleInfoVo> deviceSimpleInfoVoList = deviceSimpleInfo.get(deviceTypeId);
                    deviceSimpleInfoVoList.add(deviceSimpleInfoVo);
                } else {
                    List<DeviceSimpleInfoVo> deviceSimpleInfoVoList = new ArrayList<>();
                    deviceSimpleInfoVoList.add(deviceSimpleInfoVo);
                    deviceSimpleInfo.put(deviceTypeId, deviceSimpleInfoVoList);
                }
            }
            if (!CollectionUtils.isEmpty(alarmDeviceInfo)) {
                SafeDeviceCenter safeDeviceCenter = new SafeDeviceCenter("预警设备中心", alarmDeviceInfo);
                finalData.add(safeDeviceCenter);
            }
            if (!CollectionUtils.isEmpty(offlineDeviceInfo)) {
                SafeDeviceCenter safeDeviceCenter = new SafeDeviceCenter("离线设备中心", offlineDeviceInfo);
                finalData.add(safeDeviceCenter);
            }
            if (!CollectionUtils.isEmpty(allDeviceInfo)) {
                SafeDeviceCenter safeDeviceCenter = new SafeDeviceCenter("所有设备中心", allDeviceInfo);
                finalData.add(safeDeviceCenter);
            }
            for (Map.Entry<Long, List<DeviceSimpleInfoVo>> dataListEntry : deviceSimpleInfo.entrySet()) {
                Long key = dataListEntry.getKey();
                List<DeviceSimpleInfoVo> value = dataListEntry.getValue();
                String typeName = hashMap.get(key);
                if (!CollectionUtils.isEmpty(value)) {
                    SafeDeviceCenter safeDeviceCenter = new SafeDeviceCenter(typeName, value);
                    finalData.add(safeDeviceCenter);
                }
            }
        }
        return finalData;
    }

    @Override
    public DianQiDeviceCenterInfo getSafeDeviceCenterInfo(Long projectId, Long deviceId, Long systemId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //处理设备图片
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            DianQiDeviceCenterInfo dianQiDeviceCenterInfo = new DianQiDeviceCenterInfo();
            //处理位置信息
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    dianQiDeviceCenterInfo.setLocation(locationBuf.toString());
                }
            } else {
                dianQiDeviceCenterInfo.setLocation(location);
            }
            //处理基础信息
            dianQiDeviceCenterInfo.setDeviceId(device.getId());
            dianQiDeviceCenterInfo.setDeviceName(device.getName());
            dianQiDeviceCenterInfo.setDeviceTypeId(device.getDeviceTypeId());
            dianQiDeviceCenterInfo.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            dianQiDeviceCenterInfo.setStatus(device.getStatus());
            dianQiDeviceCenterInfo.setErasure(device.getErasure());
            if(deviceTypePicture!=null){
                dianQiDeviceCenterInfo.setPictureUrl(deviceTypePicture.getUrl());
            }
            dianQiDeviceCenterInfo.setProjectId(device.getProjectId());
            dianQiDeviceCenterInfo.setSerialNum(device.getSerialNum());
            dianQiDeviceCenterInfo.setVideoUrl(device.getVideoUrl());
            dianQiDeviceCenterInfo.setDataFlag(0);
            //处理设备数据
            List<DeviceDataSafeElec> deviceDataSafeElecList = deviceDataSafeElecService.selectBySbidLimit(deviceId);
            if (!CollectionUtils.isEmpty(deviceDataSafeElecList)) {
                dianQiDeviceCenterInfo.setDataFlag(1);
                List<Integer> xdata = new ArrayList<>();
                List<Float> dianya00 = new ArrayList<>();
                List<Float> dianya01 = new ArrayList<>();
                List<Float> dianya02 = new ArrayList<>();
                List<Float> dianliu01 = new ArrayList<>();
                List<Float> dianliu02 = new ArrayList<>();
                List<Float> dianliu03 = new ArrayList<>();
                List<Float> loudian = new ArrayList<>();
                List<Float> wendu01 = new ArrayList<>();
                List<Float> wendu02 = new ArrayList<>();
                List<Float> wendu03 = new ArrayList<>();
                List<Float> wendu04 = new ArrayList<>();
                long current = System.currentTimeMillis();
                BigDecimal shiBig=new BigDecimal("10");
                for (DeviceDataSafeElec deviceDataSafeElec : deviceDataSafeElecList) {
                    Date createTime = deviceDataSafeElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String volt00Str = deviceDataSafeElec.getVolt0();
                    if (volt00Str == null || volt00Str.equals("0")) {
                        //添加示例数据
                        dianya00.add(220f);
                    } else {
                        BigDecimal volt0Big=new BigDecimal(volt00Str);
                        BigDecimal divide = volt0Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianya00.add(divide.floatValue());
                    }
                    String volt1Str = deviceDataSafeElec.getVolt1();
                    if (volt1Str == null || volt1Str.equals("0")) {
                        dianya01.add(230f);
                    } else {
                        BigDecimal volt1Big=new BigDecimal(volt1Str);
                        BigDecimal divide = volt1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianya01.add(divide.floatValue());
                    }
                    String volt2Str = deviceDataSafeElec.getVolt2();
                    if (volt2Str == null || volt2Str.equals("0")) {
                        dianya02.add(240f);
                    } else {
                        BigDecimal volt2Big=new BigDecimal(volt2Str);
                        BigDecimal divide = volt2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianya02.add(divide.floatValue());
                    }
                    String temp1Str = deviceDataSafeElec.getTemp1();
                    if (temp1Str == null || temp1Str.equals("0")) {
                        wendu01.add(20f);
                    } else {
                        BigDecimal temp1Big=new BigDecimal(temp1Str);
                        BigDecimal divide = temp1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu01.add(divide.floatValue());
                    }
                    String temp2Str = deviceDataSafeElec.getTemp2();
                    if (temp2Str == null || temp2Str.equals("0")) {
                        wendu02.add(30f);
                    } else { ;
                        BigDecimal temp2Big=new BigDecimal(temp2Str);
                        BigDecimal divide = temp2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu02.add(divide.floatValue());
                    }
                    String temp3Str = deviceDataSafeElec.getTemp3();
                    if (temp3Str == null || temp3Str.equals("0")) {
                        wendu03.add(40f);
                    } else {
                        BigDecimal temp3Big=new BigDecimal(temp3Str);
                        BigDecimal divide = temp3Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu03.add(divide.floatValue());
                    }
                    String temp4Str = deviceDataSafeElec.getTemp4();
                    if (temp4Str == null || temp4Str.equals("0")) {
                        wendu04.add(50f);
                    } else {
                        BigDecimal temp4Big=new BigDecimal(temp4Str);
                        BigDecimal divide = temp4Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu04.add(divide.floatValue());
                    }
                    String elctr0Str = deviceDataSafeElec.getElctr0();
                    if (elctr0Str == null || elctr0Str.equals("0")) {
                        dianliu01.add(10f);
                    } else {
                        BigDecimal elctr0Big=new BigDecimal(elctr0Str);
                        BigDecimal divide = elctr0Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu01.add(divide.floatValue());

                    }
                    String elctr1Str = deviceDataSafeElec.getElctr1();
                    if (elctr1Str == null || elctr1Str.equals("0")) {
                        dianliu02.add(20f);
                    } else {
                        BigDecimal elctr1Big=new BigDecimal(elctr1Str);
                        BigDecimal divide = elctr1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu02.add(divide.floatValue());
                    }
                    String elctr2Str = deviceDataSafeElec.getElctr2();
                    if (elctr2Str == null || elctr2Str.equals("0")) {
                        dianliu03.add(30f);
                    } else {
                        BigDecimal elctr2Big=new BigDecimal(elctr2Str);
                        BigDecimal divide = elctr2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu03.add(divide.floatValue());
                    }
                    String elctr3Str = deviceDataSafeElec.getElctr3();
                    if (elctr3Str == null || elctr3Str.equals("0")) {
                        loudian.add(100f);
                    } else {
                        BigDecimal elctr3Big=new BigDecimal(elctr3Str);
                        BigDecimal divide = elctr3Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        loudian.add(divide.floatValue());
                    }
                }
                Map<String, Object> dianYa = dianQiDeviceCenterInfo.getDianYa();
                if (!CollectionUtils.isEmpty(dianya00)) {
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String,Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYa1", dianya00);
                    dianYaData.put("dianYa2", dianya01);
                    dianYaData.put("dianYa3", dianya02);
                    dianYa.put("dianya",dianYaData);
                }
                Map<String, Object> wenDu = dianQiDeviceCenterInfo.getWenDu();
                if (!CollectionUtils.isEmpty(wendu01)) {
                    wenDu.put("xDanWei", "秒前");
                    wenDu.put("yDanWei", "℃");
                    wenDu.put("xData", xdata);
                    Map<String,Object> wenDuData=new LinkedHashMap<>();
                    wenDuData.put("wenDu1", wendu01);
                    wenDuData.put("wenDu2", wendu02);
                    wenDuData.put("wenDu3", wendu03);
                    wenDuData.put("wenDu4", wendu04);
                    wenDu.put("wenDu",wenDuData);

                }
                Map<String, Object> dianLiu = dianQiDeviceCenterInfo.getDianLiu();
                if (!CollectionUtils.isEmpty(dianliu01)) {
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String,Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiu1", dianliu01);
                    dianLiuData.put("dianLiu2", dianliu02);
                    dianLiuData.put("dianLiu3", dianliu03);
                    dianLiu.put("dianLiu",dianLiuData);
                }
                Map<String, Object> louDianLiu = dianQiDeviceCenterInfo.getLouDianLiu();
                if (!CollectionUtils.isEmpty(loudian)) {
                    louDianLiu.put("xDanWei", "秒前");
                    louDianLiu.put("yDanWei", "mA");
                    louDianLiu.put("xData", xdata);
                    louDianLiu.put("louDianLiu", loudian);
                }
            } else {
                dianQiDeviceCenterInfo.setDataFlag(0);
            }
            return dianQiDeviceCenterInfo;
        }
        return null;
    }

    @Override
    public XiaoFangDeviceCenterInfo getXiaoFangCenterInfo(Long projectId, Long deviceId, Long systemId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //处理设备图片信息
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            XiaoFangDeviceCenterInfo xiaoFangDeviceCenterInfo = new XiaoFangDeviceCenterInfo();
            String location = device.getLocation();
            //处理位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    xiaoFangDeviceCenterInfo.setLocation(locationBuf.toString());
                }
            } else {
                xiaoFangDeviceCenterInfo.setLocation(location);
            }
            //处理基础信息
            xiaoFangDeviceCenterInfo.setDeviceId(device.getId());
            xiaoFangDeviceCenterInfo.setDeviceName(device.getName());
            xiaoFangDeviceCenterInfo.setDeviceTypeId(device.getDeviceTypeId());
            xiaoFangDeviceCenterInfo.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            xiaoFangDeviceCenterInfo.setStatus(device.getStatus());
            xiaoFangDeviceCenterInfo.setErasure(device.getErasure());
            if(deviceTypePicture!=null){
                xiaoFangDeviceCenterInfo.setPictureUrl(deviceTypePicture.getUrl());
            }
            xiaoFangDeviceCenterInfo.setProjectId(device.getProjectId());
            xiaoFangDeviceCenterInfo.setSerialNum(device.getSerialNum());
            xiaoFangDeviceCenterInfo.setVideoUrl(device.getVideoUrl());
            //处理设备数据
            List<DeviceDataSafeElec> deviceDataSafeElecList = deviceDataSafeElecService.selectBySbidLimit(deviceId);
            xiaoFangDeviceCenterInfo.setDataFlag(0);
            if (!CollectionUtils.isEmpty(deviceDataSafeElecList)) {
                xiaoFangDeviceCenterInfo.setDataFlag(1);
                List<Integer> xdata = new ArrayList<>();
                List<Float> dianyaOne00 = new ArrayList<>();
                List<Float> dianyaOne01 = new ArrayList<>();
                List<Float> dianyaOne02 = new ArrayList<>();
                List<Float> dianyaTwo00 = new ArrayList<>();
                List<Float> dianyaTwo01 = new ArrayList<>();
                List<Float> dianyaTwo02 = new ArrayList<>();
                List<Float> dianliu01 = new ArrayList<>();
                List<Float> dianliu02 = new ArrayList<>();
                List<Float> dianliu03 = new ArrayList<>();
                long current = System.currentTimeMillis();
                BigDecimal shiBig=new BigDecimal("10");
                for (DeviceDataSafeElec deviceDataSafeElec : deviceDataSafeElecList) {
                    Date createTime = deviceDataSafeElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String volt00Str = deviceDataSafeElec.getVolt0();
                    if (StringUtils.isBlank(volt00Str) || volt00Str.equals("0")) {
                        //添加示例数据
                        dianyaOne00.add(220f);
                    } else {
                        BigDecimal volt0Big=new BigDecimal(volt00Str);
                        BigDecimal divide = volt0Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaOne00.add(divide.floatValue());
                    }
                    String volt1Str = deviceDataSafeElec.getVolt1();
                    if (StringUtils.isBlank(volt1Str) || volt1Str.equals("0")) {
                        dianyaOne01.add(230f);
                    } else {
                        BigDecimal volt1Big=new BigDecimal(volt1Str);
                        BigDecimal divide = volt1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaOne01.add(divide.floatValue());
                    }
                    String volt2Str = deviceDataSafeElec.getVolt2();
                    if (StringUtils.isBlank(volt2Str) || volt2Str.equals("0")) {
                        dianyaOne02.add(240f);
                    } else {
                        BigDecimal volt2Big=new BigDecimal(volt2Str);
                        BigDecimal divide = volt2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaOne02.add(divide.floatValue());
                    }
                    String volt3Str = deviceDataSafeElec.getVolt3();
                    if (StringUtils.isBlank(volt3Str) || volt3Str.equals("0")) {
                        dianyaTwo00.add(220f);
                    } else {
                        BigDecimal volt3Big=new BigDecimal(volt3Str);
                        BigDecimal divide = volt3Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaTwo00.add(divide.floatValue());
                    }
                    String volt4Str = deviceDataSafeElec.getVolt4();
                    if (StringUtils.isBlank(volt4Str) || volt4Str.equals("0")) {
                        dianyaTwo01.add(230f);
                    } else {
                        BigDecimal volt4Big=new BigDecimal(volt4Str);
                        BigDecimal divide = volt4Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaTwo01.add(divide.floatValue());
                    }
                    String volt5Str = deviceDataSafeElec.getVolt5();
                    if (StringUtils.isBlank(volt5Str) || volt5Str.equals("0")) {
                        dianyaTwo02.add(240f);
                    } else {
                        BigDecimal volt5Big=new BigDecimal(volt5Str);
                        BigDecimal divide = volt5Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaTwo02.add(divide.floatValue());
                    }
                    String elctr0Str = deviceDataSafeElec.getElctr0();
                    if (elctr0Str == null || elctr0Str.equals("0")) {
                        dianliu01.add(10f);
                    } else {
                        BigDecimal elctr0Big=new BigDecimal(elctr0Str);
                        BigDecimal divide = elctr0Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu01.add(divide.floatValue());
                    }
                    String elctr1Str = deviceDataSafeElec.getElctr1();
                    if (elctr1Str == null || elctr1Str.equals("0")) {
                        dianliu02.add(20f);
                    } else {
                        BigDecimal elctr1Big=new BigDecimal(elctr1Str);
                        BigDecimal divide = elctr1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu02.add(divide.floatValue());
                    }
                    String elctr2Str = deviceDataSafeElec.getElctr2();
                    if (elctr2Str == null || elctr2Str.equals("0")) {
                        dianliu03.add(30f);
                    } else {
                        BigDecimal elctr2Big=new BigDecimal(elctr2Str);
                        BigDecimal divide = elctr2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianliu03.add(divide.floatValue());
                    }

                }
                Map<String, Object> dianYaOne = xiaoFangDeviceCenterInfo.getDianYaOne();
                if (!CollectionUtils.isEmpty(dianyaOne00)) {
                    dianYaOne.put("xDanWei", "秒前");
                    dianYaOne.put("yDanWei", "V");
                    dianYaOne.put("xData", xdata);
                    Map<String,Object> dianYaOneData=new LinkedHashMap<>();
                    dianYaOneData.put("dianYaA", dianyaOne00);
                    dianYaOneData.put("dianYaB", dianyaOne01);
                    dianYaOneData.put("dianYaC", dianyaOne02);
                    dianYaOne.put("dianYa",dianYaOneData);
                }
                Map<String, Object> dianYaTwo = xiaoFangDeviceCenterInfo.getDianYaTwo();
                if (!CollectionUtils.isEmpty(dianyaTwo00)) {
                    dianYaTwo.put("xDanWei", "秒前");
                    dianYaTwo.put("yDanWei", "V");
                    dianYaTwo.put("xData", xdata);
                    Map<String,Object> dianYaTwoData=new LinkedHashMap<>();
                    dianYaTwoData.put("dianYaA", dianyaTwo00);
                    dianYaTwoData.put("dianYaB", dianyaTwo01);
                    dianYaTwoData.put("dianYaC", dianyaTwo02);
                    dianYaTwo.put("dianYa",dianYaTwoData);
                }
                Map<String, Object> dianLiu = xiaoFangDeviceCenterInfo.getDianLiu();
                if (!CollectionUtils.isEmpty(dianliu01)) {
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String,Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliu01);
                    dianLiuData.put("dianLiuB", dianliu02);
                    dianLiuData.put("dianLiuC", dianliu03);
                    dianLiu.put("dianLiu",dianLiuData);
                }
            } else {
                //处理假数据用于渲染
                if(deviceId.equals(17l)){
                    xiaoFangDeviceCenterInfo.setDataFlag(1);
                    List<Integer> xdata = new ArrayList<>();
                    List<Float> dianyaOne00 = new ArrayList<>();
                    List<Float> dianyaOne01 = new ArrayList<>();
                    List<Float> dianyaOne02 = new ArrayList<>();
                    List<Float> dianyaTwo00 = new ArrayList<>();
                    List<Float> dianyaTwo01 = new ArrayList<>();
                    List<Float> dianyaTwo02 = new ArrayList<>();
                    List<Float> dianliu01 = new ArrayList<>();
                    List<Float> dianliu02 = new ArrayList<>();
                    List<Float> dianliu03 = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaOne00,220f);
                    handlerExamplesData(dianyaOne01,240f);
                    handlerExamplesData(dianyaOne02,260f);
                    handlerExamplesData(dianyaTwo00,220f);
                    handlerExamplesData(dianyaTwo01,250f);
                    handlerExamplesData(dianyaTwo02,280f);
                    handlerExamplesData(dianliu01,100f);
                    handlerExamplesData(dianliu02,120f);
                    handlerExamplesData(dianliu03,140f);
                    Map<String, Object> dianYaOne = xiaoFangDeviceCenterInfo.getDianYaOne();
                    dianYaOne.put("xDanWei", "秒前");
                    dianYaOne.put("yDanWei", "V");
                    dianYaOne.put("xData", xdata);
                    Map<String,Object> dianYaOneData=new LinkedHashMap<>();
                    dianYaOneData.put("dianYaA", dianyaOne00);
                    dianYaOneData.put("dianYaB", dianyaOne01);
                    dianYaOneData.put("dianYaC", dianyaOne02);
                    dianYaOne.put("dianYa",dianYaOneData);

                    Map<String, Object> dianYaTwo = xiaoFangDeviceCenterInfo.getDianYaTwo();
                    dianYaTwo.put("xDanWei", "秒前");
                    dianYaTwo.put("yDanWei", "V");
                    dianYaTwo.put("xData", xdata);

                    Map<String,Object> dianYaTwoData=new LinkedHashMap<>();
                    dianYaTwoData.put("dianYaA", dianyaTwo00);
                    dianYaTwoData.put("dianYaB", dianyaTwo01);
                    dianYaTwoData.put("dianYaC", dianyaTwo02);
                    dianYaTwo.put("dianYa",dianYaTwoData);

                    Map<String, Object> dianLiu = xiaoFangDeviceCenterInfo.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A ");
                    dianLiu.put("xData", xdata);
                    Map<String,Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliu01);
                    dianLiuData.put("dianLiuB", dianliu02);
                    dianLiuData.put("dianLiuC", dianliu03);
                    dianLiu.put("dianLiu",dianLiuData);
                }
            }
            return xiaoFangDeviceCenterInfo;
        }
        return null;
    }

    @Override
    public SafeWaterDeviceInfo waterSafeInfo(Long deviceId, Long projectId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //处理设备图片
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeWaterDeviceInfo safeWaterDeviceInfo = new SafeWaterDeviceInfo();
            String location = device.getLocation();
            //处理位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeWaterDeviceInfo.setLocation(locationBuf.toString());
                }
            } else {
                safeWaterDeviceInfo.setLocation(location);
            }
            //处理基本信息
            DeviceDataWaterElec deviceDataWaterElec = deviceDataWaterElecService.findRecentData(device.getId());
            safeWaterDeviceInfo.setDeviceId(device.getId());
            safeWaterDeviceInfo.setDeviceName(device.getName());
            safeWaterDeviceInfo.setDeviceTypeId(device.getDeviceTypeId());
            safeWaterDeviceInfo.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeWaterDeviceInfo.setErasure(device.getErasure());
            safeWaterDeviceInfo.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeWaterDeviceInfo.setSwitchStatus(device.getSwitchStatus());
            safeWaterDeviceInfo.setStatus(device.getStatus());
            safeWaterDeviceInfo.setWaterPress(deviceDataWaterElec == null ? null : deviceDataWaterElec.getWaterPressure());
            safeWaterDeviceInfo.setVideoUrl(device.getVideoUrl());
            return safeWaterDeviceInfo;
        }
        return null;
    }

    @Override
    public SafeSmokeDeviceInfo smokeInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //设备图片
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeSmokeDeviceInfo safeSmokeDeviceInfo = new SafeSmokeDeviceInfo();
            String location = device.getLocation();
            //处理位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeSmokeDeviceInfo.setLocation(locationBuf.toString());
                }
            } else {
                safeSmokeDeviceInfo.setLocation(location);
            }
            //处理基础信息
            safeSmokeDeviceInfo.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            DeviceDataSmokeElec deviceDataSmokeElec = deviceDataSmokeElecService.selectByLastedData(deviceId);
            safeSmokeDeviceInfo.setDeviceId(device.getId());
            safeSmokeDeviceInfo.setSwitchStatus(device.getSwitchStatus());
            safeSmokeDeviceInfo.setSerialNum(device.getSerialNum());
            safeSmokeDeviceInfo.setDeviceName(device.getName());
            safeSmokeDeviceInfo.setDeviceTypeId(device.getDeviceTypeId());
            safeSmokeDeviceInfo.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeSmokeDeviceInfo.setErasure(device.getErasure());
            safeSmokeDeviceInfo.setTemperature(deviceDataSmokeElec == null ? null : deviceDataSmokeElec.getTemperature());
            safeSmokeDeviceInfo.setVideoUrl(device.getVideoUrl());
            safeSmokeDeviceInfo.setStatus(device.getStatus());
            //使用假数据
            if(deviceId.equals(23l)){
                safeSmokeDeviceInfo.setTemperature("27");
            }
            return safeSmokeDeviceInfo;
        }
        return null;
    }

    @Override
    public SafeDoorDeviceInfo doorInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeDoorDeviceInfo safeDoorDeviceInfo = new SafeDoorDeviceInfo();
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeDoorDeviceInfo.setLocation(locationBuf.toString());
                }
            } else {
                safeDoorDeviceInfo.setLocation(location);
            }
            safeDoorDeviceInfo.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeDoorDeviceInfo.setDeviceId(device.getId());
            safeDoorDeviceInfo.setSwitchStatus(device.getSwitchStatus());
            safeDoorDeviceInfo.setSerialNum(device.getSerialNum());
            safeDoorDeviceInfo.setDeviceName(device.getName());
            safeDoorDeviceInfo.setDeviceTypeId(device.getDeviceTypeId());
            safeDoorDeviceInfo.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeDoorDeviceInfo.setErasure(device.getErasure());
            safeDoorDeviceInfo.setStatus(device.getStatus());
            safeDoorDeviceInfo.setVideoUrl(device.getVideoUrl());
            return safeDoorDeviceInfo;
        }
        return null;
    }

    @Override
    public SafeTempatureHumidity tempatureHumidityInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeTempatureHumidity safeTempatureHumidity = new SafeTempatureHumidity();
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeTempatureHumidity.setLocation(locationBuf.toString());
                }
            } else {
                safeTempatureHumidity.setLocation(location);
            }
            safeTempatureHumidity.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeTempatureHumidity.setDeviceId(device.getId());
            safeTempatureHumidity.setStatus(device.getStatus());
            safeTempatureHumidity.setSerialNum(device.getSerialNum());
            safeTempatureHumidity.setDeviceName(device.getName());
            safeTempatureHumidity.setDeviceTypeId(device.getDeviceTypeId());
            safeTempatureHumidity.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeTempatureHumidity.setErasure(device.getErasure());
            safeTempatureHumidity.setVideoUrl(device.getVideoUrl());
            DataTemperaturehumidity lastedData = dataTempatureHumidityService.getLastedData(device.getId());
            if (lastedData != null) {
                safeTempatureHumidity.setTemperature(lastedData.getTemperature());
                safeTempatureHumidity.setHumidity(lastedData.getHumidity());
            }
            return safeTempatureHumidity;
        }
        return null;

    }

    @Override
    public SafeMT300DeviceCenter safeMT300DeviceInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //处理设备图片信息
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeMT300DeviceCenter safeMT300DeviceCenter = new SafeMT300DeviceCenter();
            String location = device.getLocation();
            //获取位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(roomId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeMT300DeviceCenter.setLocation(locationBuf.toString());
                }
            } else {
                safeMT300DeviceCenter.setLocation(location);
            }
            //处理基础信息
            safeMT300DeviceCenter.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeMT300DeviceCenter.setDeviceId(device.getId());
            safeMT300DeviceCenter.setStatus(device.getStatus());
            safeMT300DeviceCenter.setSerialNum(device.getSerialNum());
            safeMT300DeviceCenter.setDeviceName(device.getName());
            safeMT300DeviceCenter.setDeviceTypeId(device.getDeviceTypeId());
            safeMT300DeviceCenter.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeMT300DeviceCenter.setErasure(device.getErasure());
            safeMT300DeviceCenter.setVideoUrl(device.getVideoUrl());
            safeMT300DeviceCenter.setProjectId(projectId);
            safeMT300DeviceCenter.setDataFlag(0);
            //处理数据
            List<DeviceDataSmartElec> deviceDataSmartElecList = deviceDataSmartElecService.getLastedTenData(device.getId());
            if (!CollectionUtils.isEmpty(deviceDataSmartElecList)) {
                safeMT300DeviceCenter.setDataFlag(1);
                List<Integer> xdata = new ArrayList<>();
                List<Float> dianyaA = new ArrayList<>();
                List<Float> dianyaB = new ArrayList<>();
                List<Float> dianyaC = new ArrayList<>();
                List<Float> dianliuA = new ArrayList<>();
                List<Float> dianliuB = new ArrayList<>();
                List<Float> dianliuC = new ArrayList<>();
                List<Float> loudian = new ArrayList<>();
                List<Float> wendu01 = new ArrayList<>();
                List<Float> wendu02 = new ArrayList<>();
                List<Float> wendu03 = new ArrayList<>();
                List<Float> wendu04 = new ArrayList<>();
                List<Float> activePowerA = new ArrayList<>();
                List<Float> activePowerB = new ArrayList<>();
                List<Float> activePowerC = new ArrayList<>();
                List<Float> inActivePowerA = new ArrayList<>();
                List<Float> inActivePowerB = new ArrayList<>();
                List<Float> inActivePowerC = new ArrayList<>();
                List<Float> powerFactorA = new ArrayList<>();
                List<Float> powerFactorB = new ArrayList<>();
                List<Float> powerFactorC = new ArrayList<>();
                BigDecimal shiBig=new BigDecimal("10");
                BigDecimal baiBig=new BigDecimal("100");
                BigDecimal qianBig=new BigDecimal("1000");
                long current = System.currentTimeMillis();
                for (DeviceDataSmartElec deviceDataSmartElec : deviceDataSmartElecList) {
                    Date createTime = deviceDataSmartElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = deviceDataSmartElec.getVoltRmsA();
                    if (voltRmsA == null || voltRmsA.equals("0")) {
                        //添加示例数据
                        dianyaA.add(220f);
                    } else {
                        BigDecimal voltABig=new BigDecimal(voltRmsA);
                        BigDecimal divide = voltABig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaA.add(divide.floatValue());
                    }
                    String voltRmsB = deviceDataSmartElec.getVoltRmsB();
                    if (voltRmsB == null || voltRmsB.equals("0")) {
                        //添加示例数据
                        dianyaB.add(230f);
                    } else {
                        BigDecimal voltBBig=new BigDecimal(voltRmsB);
                        BigDecimal divide = voltBBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaB.add(divide.floatValue());
                    }
                    String voltRmsC = deviceDataSmartElec.getVoltRmsC();
                    if (voltRmsC == null || voltRmsC.equals("0")) {
                        //添加示例数据
                        dianyaC.add(240f);
                    } else {
                        BigDecimal voltCBig=new BigDecimal(voltRmsC);
                        BigDecimal divide = voltCBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaC.add(divide.floatValue());
                    }
                    //电流
                    String ampRmsA = deviceDataSmartElec.getAmpRmsA();
                    if (StringUtils.isBlank(ampRmsA) || ampRmsA.equals("0")) {
                        dianliuA.add(100f);
                    } else {
                        BigDecimal ampRmsABig=new BigDecimal(ampRmsA);
                        BigDecimal divide = ampRmsABig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuA.add(divide.floatValue());
                    }
                    String ampRmsB = deviceDataSmartElec.getAmpRmsB();
                    if (StringUtils.isBlank(ampRmsB) || ampRmsB.equals("0")) {
                        dianliuB.add(120f);
                    } else {
                        BigDecimal ampRmsBBig=new BigDecimal(ampRmsB);
                        BigDecimal divide = ampRmsBBig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuB.add(divide.floatValue());
                    }
                    String ampRmsC = deviceDataSmartElec.getAmpRmsC();
                    if (StringUtils.isBlank(ampRmsC) || ampRmsC.equals("0")) {
                        dianliuC.add(140f);
                    } else {
                        BigDecimal ampRmsCBig=new BigDecimal(ampRmsC);
                        BigDecimal divide = ampRmsCBig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuC.add(divide.floatValue());
                    }
                    String leaked = deviceDataSmartElec.getLeaked();
                    if (StringUtils.isBlank(leaked) || leaked.equals("0")) {
                        loudian.add(100f);
                    } else {
                        loudian.add(Float.parseFloat(leaked));
                    }
                    String temp1 = deviceDataSmartElec.getTemp1();
                    if (StringUtils.isBlank(temp1) || temp1.equals("0")) {
                        wendu01.add(20f);
                    } else {
                        BigDecimal temp1Big=new BigDecimal(temp1);
                        BigDecimal divide = temp1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu01.add(divide.floatValue());
                    }
                    String temp2 = deviceDataSmartElec.getTemp2();
                    if (StringUtils.isBlank(temp2) || temp2.equals("0")) {
                        wendu02.add(30f);
                    } else {
                        BigDecimal temp2Big=new BigDecimal(temp2);
                        BigDecimal divide = temp2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu02.add(divide.floatValue());
                    }
                    String temp3 = deviceDataSmartElec.getTemp3();
                    if (StringUtils.isBlank(temp3) || temp3.equals("0")) {
                        wendu03.add(40f);
                    } else {
                        BigDecimal temp3Big=new BigDecimal(temp3);
                        BigDecimal divide = temp3Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu03.add(divide.floatValue());
                    }
                    String temp4 = deviceDataSmartElec.getTemp4();
                    if (StringUtils.isBlank(temp4) || temp4.equals("0")) {
                        wendu04.add(40f);
                    } else {
                        BigDecimal temp4Big=new BigDecimal(temp4);
                        BigDecimal divide = temp4Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu04.add(divide.floatValue());
                    }
                    String activePowerA1 = deviceDataSmartElec.getActivePowerA();
                    if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(100f);
                    } else {
                        BigDecimal activePowerA1Big=new BigDecimal(activePowerA1);
                        BigDecimal divide = activePowerA1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerA.add(divide.floatValue());

                    }
                    String activePowerB1 = deviceDataSmartElec.getActivePowerB();
                    if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(200f);
                    } else {
                        BigDecimal activePowerB1Big=new BigDecimal(activePowerB1);
                        BigDecimal divide = activePowerB1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerB.add(divide.floatValue());

                    }
                    String activePowerC1 = deviceDataSmartElec.getActivePowerC();
                    if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(300f);
                    } else {
                        BigDecimal activePowerC1Big=new BigDecimal(activePowerC1);
                        BigDecimal divide = activePowerC1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerC.add(divide.floatValue());
                    }

                    String reactivePowerA = deviceDataSmartElec.getReactivePowerA();
                    if (StringUtils.isBlank(reactivePowerA) || reactivePowerA.equals("0")) {
                        inActivePowerA.add(10f);
                    } else {
                        BigDecimal reactivePowerABig=new BigDecimal(reactivePowerA);
                        BigDecimal divide = reactivePowerABig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerA.add(divide.floatValue());
                    }
                    String reactivePowerB = deviceDataSmartElec.getReactivePowerB();
                    if (StringUtils.isBlank(reactivePowerB) || reactivePowerB.equals("0")) {
                        inActivePowerB.add(20f);
                    } else {
                        BigDecimal reactivePowerBBig=new BigDecimal(reactivePowerB);
                        BigDecimal divide = reactivePowerBBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerB.add(divide.floatValue());
                    }
                    String reactivePowerC = deviceDataSmartElec.getReactivePowerC();
                    if (StringUtils.isBlank(reactivePowerC) || reactivePowerC.equals("0")) {
                        inActivePowerC.add(30f);
                    } else {
                        BigDecimal reactivePowerCBig=new BigDecimal(reactivePowerC);
                        BigDecimal divide = reactivePowerCBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerC.add(divide.floatValue());
                    }
                    String powerFactorA1 = deviceDataSmartElec.getPowerFactorA();
                    if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                        powerFactorA.add(100f);
                    } else {
                        powerFactorA.add(Float.parseFloat(powerFactorA1));
                    }
                    String powerFactorB1 = deviceDataSmartElec.getPowerFactorB();
                    if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                        powerFactorB.add(200f);
                    } else {
                        powerFactorB.add(Float.parseFloat(powerFactorB1));
                    }
                    String powerFactorC1 = deviceDataSmartElec.getPowerFactorC();
                    if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                        powerFactorC.add(100f);
                    } else {
                        powerFactorC.add(Float.parseFloat(powerFactorC1));
                    }
                }
                Map<String, Object> dianYa = safeMT300DeviceCenter.getDianYa();
                if (!CollectionUtils.isEmpty(dianyaA)) {
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String, Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYaA", dianyaA);
                    dianYaData.put("dianYaB", dianyaB);
                    dianYaData.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaData);
                }
                Map<String, Object> wenDu = safeMT300DeviceCenter.getWenDu();
                if (!CollectionUtils.isEmpty(wendu01)) {
                    wenDu.put("xDanWei", "秒前");
                    wenDu.put("yDanWei", "℃");
                    wenDu.put("xData", xdata);
                    Map<String, Object> wenDuData=new LinkedHashMap<>();
                    wenDuData.put("wenDu1", wendu01);
                    wenDuData.put("wenDu2", wendu02);
                    wenDuData.put("wenDu3", wendu03);
                    wenDuData.put("wenDu4", wendu04);
                    wenDu.put("wenDu",wenDuData);
                }
                Map<String, Object> dianLiu = safeMT300DeviceCenter.getDianLiu();
                if (!CollectionUtils.isEmpty(dianliuA)) {
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A(安)");
                    dianLiu.put("xData", xdata);
                    Map<String, Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);
                }
                Map<String, Object> louDianLiu = safeMT300DeviceCenter.getLouDianLiu();
                if (!CollectionUtils.isEmpty(loudian)) {
                    louDianLiu.put("xDanWei", "秒前");
                    louDianLiu.put("yDanWei", "mA");
                    louDianLiu.put("xData", xdata);
                    louDianLiu.put("louDianLiu", loudian);
                }
                Map<String, Object> activePower = safeMT300DeviceCenter.getActivePower();
                if (!CollectionUtils.isEmpty(activePowerA)) {
                    activePower.put("xDanWei", "秒前");
                    activePower.put("yDanWei", "w");
                    activePower.put("xData", xdata);
                    Map<String, Object> activePowerData=new LinkedHashMap<>();
                    activePowerData.put("activePowerA", activePowerA);
                    activePowerData.put("activePowerB", activePowerB);
                    activePowerData.put("activePowerC", activePowerC);
                    activePower.put("activePower",activePowerData);
                }
                Map<String, Object> inActivePower = safeMT300DeviceCenter.getInActivePower();
                if (!CollectionUtils.isEmpty(inActivePowerA)) {
                    inActivePower.put("xDanWei", "s前");
                    inActivePower.put("yDanWei", "var");
                    inActivePower.put("xData", xdata);
                    Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                    inActivePowerData.put("inActivePowerA", inActivePowerA);
                    inActivePowerData.put("inActivePowerB", inActivePowerB);
                    inActivePowerData.put("inActivePowerC", inActivePowerC);
                    inActivePower.put("inActivePower",inActivePowerData);
                }
                Map<String, Object> powerFactor = safeMT300DeviceCenter.getPowerFactor();
                if (!CollectionUtils.isEmpty(powerFactorA)) {
                    powerFactor.put("xDanWei", "秒前");
                    powerFactor.put("yDanWei", "0.001");
                    powerFactor.put("xData", xdata);
                    Map<String, Object> powerFactorData =new LinkedHashMap<>();
                    powerFactorData.put("powerFactorA", powerFactorA);
                    powerFactorData.put("powerFactorB", powerFactorB);
                    powerFactorData.put("powerFactorC", powerFactorC);
                    powerFactor.put("powerFactor",powerFactorData);
                }
            }else{
                //使用测试数据
                if(deviceId.equals(22l)){
                    safeMT300DeviceCenter.setDataFlag(1);
                    List<Integer> xdata = new ArrayList<>();
                    List<Float> dianyaA = new ArrayList<>();
                    List<Float> dianyaB = new ArrayList<>();
                    List<Float> dianyaC = new ArrayList<>();
                    List<Float> dianliuA = new ArrayList<>();
                    List<Float> dianliuB = new ArrayList<>();
                    List<Float> dianliuC = new ArrayList<>();
                    List<Float> loudian = new ArrayList<>();
                    List<Float> wendu01 = new ArrayList<>();
                    List<Float> wendu02 = new ArrayList<>();
                    List<Float> wendu03 = new ArrayList<>();
                    List<Float> wendu04 = new ArrayList<>();
                    List<Float> activePowerA = new ArrayList<>();
                    List<Float> activePowerB = new ArrayList<>();
                    List<Float> activePowerC = new ArrayList<>();
                    List<Float> inActivePowerA = new ArrayList<>();
                    List<Float> inActivePowerB = new ArrayList<>();
                    List<Float> inActivePowerC = new ArrayList<>();
                    List<Float> powerFactorA = new ArrayList<>();
                    List<Float> powerFactorB = new ArrayList<>();
                    List<Float> powerFactorC = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaA,220f);
                    handlerExamplesData(dianyaB,240f);
                    handlerExamplesData(dianyaC,260f);
                    handlerExamplesData(dianliuA,100f);
                    handlerExamplesData(dianliuB,140f);
                    handlerExamplesData(dianliuC,180f);
                    handlerExamplesData(loudian,100f);
                    handlerExamplesData(wendu01,35f);
                    handlerExamplesData(wendu02,45f);
                    handlerExamplesData(wendu03,50f);
                    handlerExamplesData(wendu04,55f);
                    handlerExamplesData(activePowerA,1000f);
                    handlerExamplesData(activePowerB,2000f);
                    handlerExamplesData(activePowerC,3000f);
                    handlerExamplesData(inActivePowerA,20f);
                    handlerExamplesData(inActivePowerB,60f);
                    handlerExamplesData(inActivePowerC,100f);
                    handlerExamplesData(powerFactorA,500f);
                    handlerExamplesData(powerFactorB,700f);
                    handlerExamplesData(powerFactorC,800f);
                    Map<String, Object> dianYa = safeMT300DeviceCenter.getDianYa();
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String, Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYaA", dianyaA);
                    dianYaData.put("dianYaB", dianyaB);
                    dianYaData.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaData);
                    Map<String, Object> wenDu = safeMT300DeviceCenter.getWenDu();
                    wenDu.put("xDanWei", "秒前");
                    wenDu.put("yDanWei", "℃");
                    wenDu.put("xData", xdata);
                    Map<String, Object> wenDuData=new LinkedHashMap<>();
                    wenDuData.put("wenDu1", wendu01);
                    wenDuData.put("wenDu2", wendu02);
                    wenDuData.put("wenDu3", wendu03);
                    wenDuData.put("wenDu4", wendu04);
                    wenDu.put("wenDu",wenDuData);
                    Map<String, Object> dianLiu = safeMT300DeviceCenter.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String, Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);
                    Map<String, Object> louDianLiu = safeMT300DeviceCenter.getLouDianLiu();
                    louDianLiu.put("xDanWei", "秒前");
                    louDianLiu.put("yDanWei", "mA");
                    louDianLiu.put("xData", xdata);
                    louDianLiu.put("louDianLiu", loudian);
                    Map<String, Object> activePower = safeMT300DeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String, Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeMT300DeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(inActivePowerA)) {
                        inActivePower.put("xDanWei", "s前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeMT300DeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(powerFactorA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String, Object> powerFactorData =new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                }
            }
            return safeMT300DeviceCenter;
        }
        return null;
    }



    @Override
    public SafeMT300CDeviceCenter safeMT300CDeviceInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //处理设备图片
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeMT300CDeviceCenter safeMT300CDeviceCenter = new SafeMT300CDeviceCenter();
            String location = device.getLocation();
            //处理位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeMT300CDeviceCenter.setLocation(locationBuf.toString());
                }
            } else {
                safeMT300CDeviceCenter.setLocation(location);
            }
            //处理基本信息
            safeMT300CDeviceCenter.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeMT300CDeviceCenter.setDeviceId(device.getId());
            safeMT300CDeviceCenter.setStatus(device.getStatus());
            safeMT300CDeviceCenter.setSerialNum(device.getSerialNum());
            safeMT300CDeviceCenter.setDeviceName(device.getName());
            safeMT300CDeviceCenter.setDeviceTypeId(device.getDeviceTypeId());
            safeMT300CDeviceCenter.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeMT300CDeviceCenter.setErasure(device.getErasure());
            safeMT300CDeviceCenter.setVideoUrl(device.getVideoUrl());
            safeMT300CDeviceCenter.setProjectId(projectId);
            safeMT300CDeviceCenter.setDataFlag(0);
            //处理设备数据
            List<DeviceDataSmartElec> deviceDataSmartElecList = deviceDataSmartElecService.getLastedTenData(device.getId());
            if (!CollectionUtils.isEmpty(deviceDataSmartElecList)) {
                safeMT300CDeviceCenter.setDataFlag(1);
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
                List<Float> inActivePowerA = new ArrayList<>();
                List<Float> inActivePowerB = new ArrayList<>();
                List<Float> inActivePowerC = new ArrayList<>();
                List<Float> powerFactorA = new ArrayList<>();
                List<Float> powerFactorB = new ArrayList<>();
                List<Float> powerFactorC = new ArrayList<>();
                long current = System.currentTimeMillis();
                BigDecimal shiBig=new BigDecimal("10");
                BigDecimal baiBig=new BigDecimal("100");
                BigDecimal qianBig=new BigDecimal("1000");
                for (DeviceDataSmartElec deviceDataSmartElec : deviceDataSmartElecList) {
                    Date createTime = deviceDataSmartElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = deviceDataSmartElec.getVoltRmsA();
                    if (voltRmsA == null || voltRmsA.equals("0")) {
                        //添加示例数据
                        dianyaA.add(220f);
                    } else {
                        BigDecimal voltRmsABig=new BigDecimal(voltRmsA);
                        BigDecimal divide = voltRmsABig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaA.add(divide.floatValue());
                    }
                    String voltRmsB = deviceDataSmartElec.getVoltRmsB();
                    if (voltRmsB == null || voltRmsB.equals("0")) {
                        //添加示例数据
                        dianyaB.add(230f);
                    } else {
                        BigDecimal voltRmsBBig=new BigDecimal(voltRmsB);
                        BigDecimal divide = voltRmsBBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaB.add(divide.floatValue());
                    }
                    String voltRmsC = deviceDataSmartElec.getVoltRmsC();
                    if (voltRmsC == null || voltRmsC.equals("0")) {
                        //添加示例数据
                        dianyaC.add(240f);
                    } else {
                        BigDecimal voltRmsCBig=new BigDecimal(voltRmsC);
                        BigDecimal divide = voltRmsCBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianyaC.add(divide.floatValue());
                    }
                    String ampRmsA = deviceDataSmartElec.getAmpRmsA();
                    if (StringUtils.isBlank(ampRmsA) || ampRmsA.equals("0")) {
                        dianliuA.add(100f);
                    } else {
                        BigDecimal ampRmsABig=new BigDecimal(ampRmsA);
                        BigDecimal divide = ampRmsABig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuA.add(divide.floatValue());
                    }
                    String ampRmsB = deviceDataSmartElec.getAmpRmsB();
                    if (StringUtils.isBlank(ampRmsB) || ampRmsB.equals("0")) {
                        dianliuB.add(120f);
                    } else {
                        BigDecimal ampRmsBBig=new BigDecimal(ampRmsB);
                        BigDecimal divide = ampRmsBBig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuB.add(divide.floatValue());
                    }
                    String ampRmsC = deviceDataSmartElec.getAmpRmsC();
                    if (StringUtils.isBlank(ampRmsC) || ampRmsC.equals("0")) {
                        dianliuC.add(140f);
                    } else {
                        BigDecimal ampRmsCBig=new BigDecimal(ampRmsC);
                        BigDecimal divide = ampRmsCBig.divide(qianBig, 3, BigDecimal.ROUND_HALF_UP);
                        dianliuC.add(divide.floatValue());
                    }
                    String activePowerA1 = deviceDataSmartElec.getActivePowerA();
                    if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(100f);
                    } else {
                        BigDecimal activePowerA1Big=new BigDecimal(activePowerA1);
                        BigDecimal divide = activePowerA1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerA.add(divide.floatValue());
                    }
                    String activePowerB1 = deviceDataSmartElec.getActivePowerB();
                    if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(200f);
                    } else {
                        BigDecimal activePowerB1Big=new BigDecimal(activePowerB1);
                        BigDecimal divide = activePowerB1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerB.add(divide.floatValue());
                    }
                    String activePowerC1 = deviceDataSmartElec.getActivePowerC();
                    if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(300f);
                    } else {
                        BigDecimal activePowerC1Big=new BigDecimal(activePowerC1);
                        BigDecimal divide = activePowerC1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        activePowerC.add(divide.floatValue());
                    }

                    String reactivePowerA = deviceDataSmartElec.getReactivePowerA();
                    if (StringUtils.isBlank(reactivePowerA) || reactivePowerA.equals("0")) {
                        inActivePowerA.add(10f);
                    } else {
                        BigDecimal reactivePowerABig=new BigDecimal(reactivePowerA);
                        BigDecimal divide = reactivePowerABig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerA.add(divide.floatValue());
                    }
                    String reactivePowerB = deviceDataSmartElec.getReactivePowerB();
                    if (StringUtils.isBlank(reactivePowerB) || reactivePowerB.equals("0")) {
                        inActivePowerB.add(20f);
                    } else {
                        BigDecimal reactivePowerBBig=new BigDecimal(reactivePowerB);
                        BigDecimal divide = reactivePowerBBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerB.add(divide.floatValue());
                    }
                    String reactivePowerC = deviceDataSmartElec.getReactivePowerC();
                    if (StringUtils.isBlank(reactivePowerC) || reactivePowerC.equals("0")) {
                        inActivePowerC.add(30f);
                    } else {
                        BigDecimal reactivePowerCBig=new BigDecimal(reactivePowerC);
                        BigDecimal divide = reactivePowerCBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        inActivePowerC.add(divide.floatValue());
                    }
                    String powerFactorA1 = deviceDataSmartElec.getPowerFactorA();
                    if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                        powerFactorA.add(100f);
                    } else {
                        powerFactorA.add(Float.parseFloat(powerFactorA1));
                    }
                    String powerFactorB1 = deviceDataSmartElec.getPowerFactorB();
                    if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                        powerFactorB.add(200f);
                    } else {
                        powerFactorB.add(Float.parseFloat(powerFactorB1));
                    }
                    String powerFactorC1 = deviceDataSmartElec.getPowerFactorC();
                    if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                        powerFactorC.add(100f);
                    } else {
                        powerFactorC.add(Float.parseFloat(powerFactorC1));
                    }

                    Map<String, Object> dianYa = safeMT300CDeviceCenter.getDianYa();
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        dianYa.put("xDanWei", "秒前");
                        dianYa.put("yDanWei", "V");
                        dianYa.put("xData", xdata);
                        Map<String,Object> dianYaData=new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        dianYa.put("dianYa",dianYaData);
                    }

                    Map<String, Object> dianLiu = safeMT300CDeviceCenter.getDianLiu();
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        dianLiu.put("xDanWei", "秒前");
                        dianLiu.put("yDanWei", "A");
                        dianLiu.put("xData", xdata);
                        Map<String,Object> dianLiuData=new LinkedHashMap<>();
                        dianLiuData.put("dianLiuA", dianliuA);
                        dianLiuData.put("dianLiuB", dianliuB);
                        dianLiuData.put("dianLiuC", dianliuC);
                        dianLiu.put("dianLiu",dianLiuData);
                    }
                    Map<String, Object> activePower = safeMT300CDeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String, Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeMT300CDeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(inActivePowerA)) {
                        inActivePower.put("xDanWei", "s前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeMT300CDeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(powerFactorA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String, Object> powerFactorData =new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                }
            }else{
                //处理间数据
                if(deviceId.equals(22l)){
                    safeMT300CDeviceCenter.setDataFlag(1);
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
                    List<Float> inActivePowerA = new ArrayList<>();
                    List<Float> inActivePowerB = new ArrayList<>();
                    List<Float> inActivePowerC = new ArrayList<>();
                    List<Float> powerFactorA = new ArrayList<>();
                    List<Float> powerFactorB = new ArrayList<>();
                    List<Float> powerFactorC = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaA,220f);
                    handlerExamplesData(dianyaB,260f);
                    handlerExamplesData(dianyaC,280f);
                    handlerExamplesData(dianliuA,50f);
                    handlerExamplesData(dianliuB,80f);
                    handlerExamplesData(dianliuC,100f);
                    handlerExamplesData(activePowerA,1000f);
                    handlerExamplesData(activePowerB,2000f);
                    handlerExamplesData(activePowerC,3000f);
                    handlerExamplesData(inActivePowerA,20f);
                    handlerExamplesData(inActivePowerB,60f);
                    handlerExamplesData(inActivePowerC,100f);
                    handlerExamplesData(powerFactorA,500f);
                    handlerExamplesData(powerFactorB,700f);
                    handlerExamplesData(powerFactorC,800f);
                    Map<String, Object> dianYa = safeMT300CDeviceCenter.getDianYa();
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String, Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYaA", dianyaA);
                    dianYaData.put("dianYaB", dianyaB);
                    dianYaData.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaData);

                    Map<String, Object> dianLiu = safeMT300CDeviceCenter.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String, Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);

                    Map<String, Object> activePower = safeMT300CDeviceCenter.getActivePower();
                    activePower.put("xDanWei", "秒前");
                    activePower.put("yDanWei", "w");
                    activePower.put("xData", xdata);
                    Map<String, Object> activePowerData=new LinkedHashMap<>();
                    activePowerData.put("activePowerA", activePowerA);
                    activePowerData.put("activePowerB", activePowerB);
                    activePowerData.put("activePowerC", activePowerC);
                    activePower.put("activePower",activePowerData);

                    Map<String, Object> inActivePower = safeMT300CDeviceCenter.getInActivePower();
                    inActivePower.put("xDanWei", "秒前");
                    inActivePower.put("yDanWei", "var");
                    inActivePower.put("xData", xdata);
                    Map<String,Object> inActivePowerData=new LinkedHashMap<>();
                    inActivePowerData.put("inActivePowerA", inActivePowerA);
                    inActivePowerData.put("inActivePowerB", inActivePowerB);
                    inActivePowerData.put("inActivePowerC", inActivePowerC);
                    inActivePower.put("inActivePower",inActivePowerData);

                    Map<String, Object> powerFactor = safeMT300CDeviceCenter.getPowerFactor();
                    powerFactor.put("xDanWei", "秒前");
                    powerFactor.put("yDanWei", "0.001");
                    powerFactor.put("xData", xdata);
                    Map<String,Object> powerFactorData=new LinkedHashMap<>();

                    powerFactorData.put("powerFactorA", powerFactorA);
                    powerFactorData.put("powerFactorB", powerFactorB);
                    powerFactorData.put("powerFactorC", powerFactorC);
                    powerFactor.put("powerFactor",powerFactorData);
                }
            }
            return safeMT300CDeviceCenter;
        }
        return null;
    }

    @Override
    public SafeMT300SDeviceCenter safeMT300SDeviceInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            //设备图片
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeMT300SDeviceCenter safeMT300SDeviceCenter = new SafeMT300SDeviceCenter();
            String location = device.getLocation();
            //设备位置信息
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeMT300SDeviceCenter.setLocation(locationBuf.toString());
                }
            } else {
                safeMT300SDeviceCenter.setLocation(location);
            }
            //基础信息
            safeMT300SDeviceCenter.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeMT300SDeviceCenter.setDeviceId(device.getId());
            safeMT300SDeviceCenter.setStatus(device.getStatus());
            safeMT300SDeviceCenter.setSerialNum(device.getSerialNum());
            safeMT300SDeviceCenter.setDeviceName(device.getName());
            safeMT300SDeviceCenter.setDeviceTypeId(device.getDeviceTypeId());
            safeMT300SDeviceCenter.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeMT300SDeviceCenter.setErasure(device.getErasure());
            safeMT300SDeviceCenter.setVideoUrl(device.getVideoUrl());
            safeMT300SDeviceCenter.setProjectId(projectId);
            safeMT300SDeviceCenter.setDataFlag(0);
            //设备数据
            List<DeviceDataSmartSuper> deviceDataSmartSuperList = deviceDataSmartSuperService.getLastedTenData(device.getId());
            if (!CollectionUtils.isEmpty(deviceDataSmartSuperList)) {
                safeMT300SDeviceCenter.setDataFlag(1);
                List<Integer> xdata = new ArrayList<>();
                List<Float> dianyaA = new ArrayList<>();
                List<Float> dianyaB = new ArrayList<>();
                List<Float> dianyaC = new ArrayList<>();
                List<Float> dianliuA = new ArrayList<>();
                List<Float> dianliuB = new ArrayList<>();
                List<Float> dianliuC = new ArrayList<>();
                List<Float> loudian = new ArrayList<>();
                List<Float> wendu01 = new ArrayList<>();
                List<Float> wendu02 = new ArrayList<>();
                List<Float> wendu03 = new ArrayList<>();
                List<Float> wendu04 = new ArrayList<>();
                List<Float> activePowerA = new ArrayList<>();
                List<Float> activePowerB = new ArrayList<>();
                List<Float> activePowerC = new ArrayList<>();
                List<Float> inActivePowerA = new ArrayList<>();
                List<Float> inActivePowerB = new ArrayList<>();
                List<Float> inActivePowerC = new ArrayList<>();
                List<Float> powerFactorA = new ArrayList<>();
                List<Float> powerFactorB = new ArrayList<>();
                List<Float> powerFactorC = new ArrayList<>();
                long current = System.currentTimeMillis();
                BigDecimal shiBig=new BigDecimal("10");
                BigDecimal baiBig=new BigDecimal("100");
                for (DeviceDataSmartSuper deviceDataSmartSuper : deviceDataSmartSuperList) {
                    Date createTime = deviceDataSmartSuper.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = deviceDataSmartSuper.getVoltRmsA();
                    if (voltRmsA == null || voltRmsA.equals("0")) {
                        //添加示例数据
                        dianyaA.add(220f);
                    } else {
                        BigDecimal voltRmsABig=new BigDecimal(voltRmsA);
                        BigDecimal divide = voltRmsABig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaA.add(divide.floatValue());
                    }
                    String voltRmsB = deviceDataSmartSuper.getVoltRmsB();
                    if (voltRmsB == null || voltRmsB.equals("0")) {
                        //添加示例数据
                        dianyaB.add(230f);
                    } else {
                        BigDecimal voltRmsBBig=new BigDecimal(voltRmsB);
                        BigDecimal divide = voltRmsBBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaB.add(divide.floatValue());
                    }
                    String voltRmsC = deviceDataSmartSuper.getVoltRmsC();
                    if (voltRmsC == null || voltRmsC.equals("0")) {
                        //添加示例数据
                        dianyaC.add(240f);
                    } else {
                        BigDecimal voltRmsCBig=new BigDecimal(voltRmsC);
                        BigDecimal divide = voltRmsCBig.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        dianyaC.add(divide.floatValue());
                    }
                    String ampRmsA = deviceDataSmartSuper.getAmpRmsA();
                    if (StringUtils.isBlank(ampRmsA) || ampRmsA.equals("0")) {
                        dianliuA.add(100f);
                    } else {
                        BigDecimal ampRmsABig=new BigDecimal(ampRmsA);
                        BigDecimal divide = ampRmsABig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianliuA.add(divide.floatValue());
                    }
                    String ampRmsB = deviceDataSmartSuper.getAmpRmsB();
                    if (StringUtils.isBlank(ampRmsB) || ampRmsB.equals("0")) {
                        dianliuB.add(120f);
                    } else {
                        BigDecimal ampRmsBBig=new BigDecimal(ampRmsB);
                        BigDecimal divide = ampRmsBBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianliuB.add(divide.floatValue());
                    }
                    String ampRmsC = deviceDataSmartSuper.getAmpRmsC();
                    if (StringUtils.isBlank(ampRmsC) || ampRmsC.equals("0")) {
                        dianliuC.add(140f);
                    } else {
                        BigDecimal ampRmsCBig=new BigDecimal(ampRmsC);
                        BigDecimal divide = ampRmsCBig.divide(baiBig, 2, BigDecimal.ROUND_HALF_UP);
                        dianliuC.add(divide.floatValue());
                    }
                    String leaked = deviceDataSmartSuper.getLeaked();
                    if (StringUtils.isBlank(leaked) || leaked.equals("0")) {
                        loudian.add(100f);
                    } else {
                        loudian.add(Float.parseFloat(leaked));
                    }
                    String temp1 = deviceDataSmartSuper.getTemp1();
                    if (StringUtils.isBlank(temp1) || temp1.equals("0")) {
                        wendu01.add(20f);
                    } else {
                        BigDecimal temp1Big=new BigDecimal(temp1);
                        BigDecimal divide = temp1Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu01.add(divide.floatValue());
                    }
                    String temp2 = deviceDataSmartSuper.getTemp2();
                    if (StringUtils.isBlank(temp2) || temp2.equals("0")) {
                        wendu02.add(30f);
                    } else {
                        BigDecimal temp2Big=new BigDecimal(temp2);
                        BigDecimal divide = temp2Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu02.add(divide.floatValue());
                    }
                    String temp3 = deviceDataSmartSuper.getTemp3();
                    if (StringUtils.isBlank(temp3) || temp3.equals("0")) {
                        wendu03.add(40f);
                    } else {
                        BigDecimal temp3Big=new BigDecimal(temp3);
                        BigDecimal divide = temp3Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu03.add(divide.floatValue());
                    }
                    String temp4 = deviceDataSmartSuper.getTemp4();
                    if (StringUtils.isBlank(temp4) || temp4.equals("0")) {
                        wendu04.add(40f);
                    } else {
                        BigDecimal temp4Big=new BigDecimal(temp4);
                        BigDecimal divide = temp4Big.divide(shiBig, 1, BigDecimal.ROUND_HALF_UP);
                        wendu04.add(divide.floatValue());
                    }
                    String activePowerA1 = deviceDataSmartSuper.getActivePowerA();
                    if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(100f);
                    } else {
                        activePowerA.add(Float.parseFloat(activePowerA1));
                    }
                    String activePowerB1 = deviceDataSmartSuper.getActivePowerB();
                    if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(200f);
                    } else {
                        activePowerB.add(Float.parseFloat(activePowerB1));
                    }
                    String activePowerC1 = deviceDataSmartSuper.getActivePowerC();
                    if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(300f);
                    } else {
                        activePowerC.add(Float.parseFloat(activePowerC1));
                    }

                    String reactivePowerA = deviceDataSmartSuper.getReactivePowerA();
                    if (StringUtils.isBlank(reactivePowerA) || reactivePowerA.equals("0")) {
                        inActivePowerA.add(10f);
                    } else {
                        inActivePowerA.add(Float.parseFloat(reactivePowerA));
                    }
                    String reactivePowerB = deviceDataSmartSuper.getReactivePowerB();
                    if (StringUtils.isBlank(reactivePowerB) || reactivePowerB.equals("0")) {
                        inActivePowerB.add(20f);
                    } else {
                        inActivePowerB.add(Float.parseFloat(reactivePowerB));
                    }
                    String reactivePowerC = deviceDataSmartSuper.getReactivePowerC();
                    if (StringUtils.isBlank(reactivePowerC) || reactivePowerC.equals("0")) {
                        inActivePowerC.add(30f);
                    } else {
                        inActivePowerC.add(Float.parseFloat(reactivePowerC));
                    }
                    String powerFactorA1 = deviceDataSmartSuper.getPowerFactorA();
                    if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                        powerFactorA.add(100f);
                    } else {
                        powerFactorA.add(Float.parseFloat(powerFactorA1));
                    }
                    String powerFactorB1 = deviceDataSmartSuper.getPowerFactorB();
                    if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                        powerFactorB.add(200f);
                    } else {
                        powerFactorB.add(Float.parseFloat(powerFactorB1));
                    }
                    String powerFactorC1 = deviceDataSmartSuper.getPowerFactorC();
                    if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                        powerFactorC.add(100f);
                    } else {
                        powerFactorC.add(Float.parseFloat(powerFactorC1));
                    }

                    Map<String, Object> dianYa = safeMT300SDeviceCenter.getDianYa();
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        dianYa.put("xDanWei", "秒前");
                        dianYa.put("yDanWei", "V");
                        dianYa.put("xData", xdata);
                        Map<String,Object> dianYaData=new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        dianYa.put("dianYa",dianYaData);
                    }

                    Map<String, Object> dianLiu = safeMT300SDeviceCenter.getDianLiu();
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        dianLiu.put("xDanWei", "秒前");
                        dianLiu.put("yDanWei", "A");
                        dianLiu.put("xData", xdata);
                        Map<String,Object> dianLiuData=new LinkedHashMap<>();

                        dianLiuData.put("dianLiuA", dianliuA);
                        dianLiuData.put("dianLiuB", dianliuB);
                        dianLiuData.put("dianLiuC", dianliuC);
                        dianLiu.put("dianLiu",dianLiuData);
                    }
                    Map<String, Object> activePower = safeMT300SDeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String, Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeMT300SDeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(inActivePowerA)) {
                        inActivePower.put("xDanWei", "s前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeMT300SDeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(powerFactorA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String, Object> powerFactorData =new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                    Map<String, Object> wenDu = safeMT300SDeviceCenter.getWenDu();
                    if (!CollectionUtils.isEmpty(wendu01)) {
                        wenDu.put("xDanWei", "秒前");
                        wenDu.put("yDanWei", "℃");
                        wenDu.put("xData", xdata);
                        Map<String, Object> wenDuData=new LinkedHashMap<>();
                        wenDuData.put("wenDu1", wendu01);
                        wenDuData.put("wenDu2", wendu02);
                        wenDuData.put("wenDu3", wendu03);
                        wenDuData.put("wenDu4", wendu04);
                        wenDu.put("wenDu",wenDuData);
                    }
                    Map<String, Object> louDianLiu = safeMT300SDeviceCenter.getLouDianLiu();
                    if (!CollectionUtils.isEmpty(loudian)) {
                        louDianLiu.put("xDanWei", "秒前");
                        louDianLiu.put("yDanWei", "mA");
                        louDianLiu.put("xData", xdata);
                        louDianLiu.put("louDianLiu", loudian);
                    }
                }
            }else{
                if(deviceId.equals(124l)){
                    safeMT300SDeviceCenter.setDataFlag(1);
                    device.setStatus(3);
                    List<Integer> xdata = new ArrayList<>();
                    List<Float> dianyaA = new ArrayList<>();
                    List<Float> dianyaB = new ArrayList<>();
                    List<Float> dianyaC = new ArrayList<>();
                    List<Float> dianliuA = new ArrayList<>();
                    List<Float> dianliuB = new ArrayList<>();
                    List<Float> dianliuC = new ArrayList<>();
                    List<Float> loudian = new ArrayList<>();
                    List<Float> wendu01 = new ArrayList<>();
                    List<Float> wendu02 = new ArrayList<>();
                    List<Float> wendu03 = new ArrayList<>();
                    List<Float> wendu04 = new ArrayList<>();
                    List<Float> activePowerA = new ArrayList<>();
                    List<Float> activePowerB = new ArrayList<>();
                    List<Float> activePowerC = new ArrayList<>();
                    List<Float> inActivePowerA = new ArrayList<>();
                    List<Float> inActivePowerB = new ArrayList<>();
                    List<Float> inActivePowerC = new ArrayList<>();
                    List<Float> powerFactorA = new ArrayList<>();
                    List<Float> powerFactorB = new ArrayList<>();
                    List<Float> powerFactorC = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaA,220f);
                    handlerExamplesData(dianyaB,240f);
                    handlerExamplesData(dianyaC,260f);
                    handlerExamplesData(dianliuA,100f);
                    handlerExamplesData(dianliuB,140f);
                    handlerExamplesData(dianliuC,180f);
                    handlerExamplesData(loudian,100f);
                    handlerExamplesData(wendu01,35f);
                    handlerExamplesData(wendu02,45f);
                    handlerExamplesData(wendu03,50f);
                    handlerExamplesData(wendu04,55f);
                    handlerExamplesData(activePowerA,1000f);
                    handlerExamplesData(activePowerB,2000f);
                    handlerExamplesData(activePowerC,3000f);
                    handlerExamplesData(inActivePowerA,20f);
                    handlerExamplesData(inActivePowerB,60f);
                    handlerExamplesData(inActivePowerC,100f);
                    handlerExamplesData(powerFactorA,500f);
                    handlerExamplesData(powerFactorB,700f);
                    handlerExamplesData(powerFactorC,800f);
                    Map<String, Object> dianYa = safeMT300SDeviceCenter.getDianYa();
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String, Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYaA", dianyaA);
                    dianYaData.put("dianYaB", dianyaB);
                    dianYaData.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaData);
                    Map<String, Object> wenDu = safeMT300SDeviceCenter.getWenDu();
                    wenDu.put("xDanWei", "秒前");
                    wenDu.put("yDanWei", "℃");
                    wenDu.put("xData", xdata);
                    Map<String, Object> wenDuData=new LinkedHashMap<>();
                    wenDuData.put("wenDu1", wendu01);
                    wenDuData.put("wenDu2", wendu02);
                    wenDuData.put("wenDu3", wendu03);
                    wenDuData.put("wenDu4", wendu04);
                    wenDu.put("wenDu",wenDuData);
                    Map<String, Object> dianLiu = safeMT300SDeviceCenter.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String, Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);
                    Map<String, Object> louDianLiu = safeMT300SDeviceCenter.getLouDianLiu();
                    louDianLiu.put("xDanWei", "秒前");
                    louDianLiu.put("yDanWei", "mA");
                    louDianLiu.put("xData", xdata);
                    louDianLiu.put("louDianLiu", loudian);
                    Map<String, Object> activePower = safeMT300SDeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String, Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeMT300SDeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(inActivePowerA)) {
                        inActivePower.put("xDanWei", "s前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeMT300SDeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(powerFactorA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String, Object> powerFactorData =new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                }
            }
            return safeMT300SDeviceCenter;
        }
        return null;
    }




    @Override
    public SafeCompensateDeviceCenter safeCompensateDeviceInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeCompensateDeviceCenter safeCompensateDeviceCenter = new SafeCompensateDeviceCenter();
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeCompensateDeviceCenter.setLocation(locationBuf.toString());
                }
            } else {
                safeCompensateDeviceCenter.setLocation(location);
            }
            safeCompensateDeviceCenter.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeCompensateDeviceCenter.setDeviceId(device.getId());
            safeCompensateDeviceCenter.setStatus(device.getStatus());
            safeCompensateDeviceCenter.setSerialNum(device.getSerialNum());
            safeCompensateDeviceCenter.setDeviceName(device.getName());
            safeCompensateDeviceCenter.setDeviceTypeId(device.getDeviceTypeId());
            safeCompensateDeviceCenter.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeCompensateDeviceCenter.setErasure(device.getErasure());
            safeCompensateDeviceCenter.setVideoUrl(device.getVideoUrl());
            safeCompensateDeviceCenter.setProjectId(projectId);
            safeCompensateDeviceCenter.setDataFlag(0);
            List<DeviceDataCompensateElec> dataCompensateElecList = deviceDataCompensateService.getLastedTenData(device.getId());
            if (!CollectionUtils.isEmpty(dataCompensateElecList)) {
                safeCompensateDeviceCenter.setDataFlag(1);
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
                List<Float> inActivePowerA = new ArrayList<>();
                List<Float> inActivePowerB = new ArrayList<>();
                List<Float> inActivePowerC = new ArrayList<>();
                List<Float> powerFactorA = new ArrayList<>();
                List<Float> powerFactorB = new ArrayList<>();
                List<Float> powerFactorC = new ArrayList<>();
                long current = System.currentTimeMillis();
                for (DeviceDataCompensateElec dataCompensateElec : dataCompensateElecList) {
                    Date createTime = dataCompensateElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = dataCompensateElec.getVoltRmsA();
                    if (voltRmsA == null || voltRmsA.equals("0")) {
                        //添加示例数据
                        dianyaA.add(220f);
                    } else {
                        Float voltRmsAF = Float.parseFloat(voltRmsA);
                        dianyaA.add(voltRmsAF / 100);
                    }
                    String voltRmsB = dataCompensateElec.getVoltRmsB();
                    if (voltRmsB == null || voltRmsB.equals("0")) {
                        //添加示例数据
                        dianyaB.add(230f);
                    } else {
                        Float voltRmsBF = Float.parseFloat(voltRmsB);
                        dianyaB.add(voltRmsBF / 100);
                    }
                    String voltRmsC = dataCompensateElec.getVoltRmsC();
                    if (voltRmsC == null || voltRmsC.equals("0")) {
                        //添加示例数据
                        dianyaC.add(240f);
                    } else {
                        Float voltRmsCF = Float.parseFloat(voltRmsC);
                        dianyaC.add(voltRmsCF / 100);
                    }
                    String ampRmsA = dataCompensateElec.getAmpRmsA();
                    if (StringUtils.isBlank(ampRmsA) || ampRmsA.equals("0")) {
                        dianliuA.add(100f);
                    } else {
                        Float ampRmsAF = Float.parseFloat(ampRmsA);
                        dianliuA.add(ampRmsAF / 1000);
                    }
                    String ampRmsB = dataCompensateElec.getAmpRmsB();
                    if (StringUtils.isBlank(ampRmsB) || ampRmsB.equals("0")) {
                        dianliuB.add(120f);
                    } else {
                        Float ampRmsBF = Float.parseFloat(ampRmsB);
                        dianliuB.add(ampRmsBF / 1000);
                    }
                    String ampRmsC = dataCompensateElec.getAmpRmsC();
                    if (StringUtils.isBlank(ampRmsC) || ampRmsC.equals("0")) {
                        dianliuC.add(140f);
                    } else {
                        Float ampRmsCF = Float.parseFloat(ampRmsC);
                        dianliuC.add(ampRmsCF / 1000);
                    }
                    String activePowerA1 = dataCompensateElec.getActivePowerA();
                    if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(100f);
                    } else {
                        Float activePowerA1F = Float.parseFloat(activePowerA1);
                        activePowerA.add(activePowerA1F / 10);
                    }
                    String activePowerB1 = dataCompensateElec.getActivePowerB();
                    if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(200f);
                    } else {
                        Float activePowerB1F = Float.parseFloat(activePowerB1);
                        activePowerB.add(activePowerB1F / 10);
                    }
                    String activePowerC1 = dataCompensateElec.getActivePowerC();
                    if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(300f);
                    } else {
                        Float activePowerC1F = Float.parseFloat(activePowerC1);
                        activePowerC.add(activePowerC1F / 10);
                    }

                    String reactivePowerA = dataCompensateElec.getReactivePowerA();
                    if (StringUtils.isBlank(reactivePowerA) || reactivePowerA.equals("0")) {
                        inActivePowerA.add(10f);
                    } else {
                        Float reactivePowerAF = Float.parseFloat(reactivePowerA);
                        inActivePowerA.add(reactivePowerAF / 10);
                    }
                    String reactivePowerB = dataCompensateElec.getReactivePowerB();
                    if (StringUtils.isBlank(reactivePowerB) || reactivePowerB.equals("0")) {
                        inActivePowerB.add(20f);
                    } else {
                        Float reactivePowerBF = Float.parseFloat(reactivePowerB);
                        inActivePowerB.add(reactivePowerBF / 10);
                    }
                    String reactivePowerC = dataCompensateElec.getReactivePowerC();
                    if (StringUtils.isBlank(reactivePowerC) || reactivePowerC.equals("0")) {
                        inActivePowerC.add(30f);
                    } else {
                        Float reactivePowerCF = Float.parseFloat(reactivePowerC);
                        inActivePowerC.add(reactivePowerCF / 10);
                    }
                    String powerFactorA1 = dataCompensateElec.getPowerFactorA();
                    if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                        powerFactorA.add(100f);
                    } else {
                        powerFactorA.add(Float.parseFloat(powerFactorA1));
                    }
                    String powerFactorB1 = dataCompensateElec.getPowerFactorB();
                    if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                        powerFactorB.add(200f);
                    } else {
                        powerFactorB.add(Float.parseFloat(powerFactorB1));
                    }
                    String powerFactorC1 = dataCompensateElec.getPowerFactorC();
                    if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                        powerFactorC.add(100f);
                    } else {
                        powerFactorC.add(Float.parseFloat(powerFactorC1));
                    }

                    Map<String, Object> dianYa = safeCompensateDeviceCenter.getDianYa();
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        dianYa.put("xDanWei", "秒前");
                        dianYa.put("yDanWei", "V");
                        dianYa.put("xData", xdata);
                        Map<String, Object> dianYaData=new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        dianYa.put("dianYa",dianYaData);
                    }

                    Map<String, Object> dianLiu = safeCompensateDeviceCenter.getDianLiu();
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        dianLiu.put("xDanWei", "秒前");
                        dianLiu.put("yDanWei", "A");
                        dianLiu.put("xData", xdata);
                        Map<String, Object> dianLiuData=new LinkedHashMap<>();
                        dianLiuData.put("dianLiuA", dianliuA);
                        dianLiuData.put("dianLiuB", dianliuB);
                        dianLiuData.put("dianLiuC", dianliuC);
                        dianLiu.put("dianLiu",dianLiuData);

                    }
                    Map<String, Object> activePower = safeCompensateDeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String, Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeCompensateDeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        inActivePower.put("xDanWei", "秒前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String, Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeCompensateDeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String, Object> powerFactorData=new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                }
            }else{
                if(deviceId.equals(26l)){
                    safeCompensateDeviceCenter.setDataFlag(1);
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
                    List<Float> inActivePowerA = new ArrayList<>();
                    List<Float> inActivePowerB = new ArrayList<>();
                    List<Float> inActivePowerC = new ArrayList<>();
                    List<Float> powerFactorA = new ArrayList<>();
                    List<Float> powerFactorB = new ArrayList<>();
                    List<Float> powerFactorC = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaA,220f);
                    handlerExamplesData(dianyaB,260f);
                    handlerExamplesData(dianyaC,280f);
                    handlerExamplesData(dianliuA,50f);
                    handlerExamplesData(dianliuB,80f);
                    handlerExamplesData(dianliuC,100f);
                    handlerExamplesData(activePowerA,1000f);
                    handlerExamplesData(activePowerB,2000f);
                    handlerExamplesData(activePowerC,3000f);
                    handlerExamplesData(inActivePowerA,20f);
                    handlerExamplesData(inActivePowerB,60f);
                    handlerExamplesData(inActivePowerC,100f);
                    handlerExamplesData(powerFactorA,500f);
                    handlerExamplesData(powerFactorB,700f);
                    handlerExamplesData(powerFactorC,800f);
                    Map<String, Object> dianYa = safeCompensateDeviceCenter.getDianYa();
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String,Object> dianYaDataMap=new LinkedHashMap<>();
                    dianYaDataMap.put("dianYaA", dianyaA);
                    dianYaDataMap.put("dianYaB", dianyaB);
                    dianYaDataMap.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaDataMap);

                    Map<String, Object> dianLiu = safeCompensateDeviceCenter.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String,Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);

                    Map<String, Object> activePower = safeCompensateDeviceCenter.getActivePower();
                    activePower.put("xDanWei", "秒前");
                    activePower.put("yDanWei", "w(瓦)");
                    activePower.put("xData", xdata);
                    Map<String,Object> activePowerData=new LinkedHashMap<>();
                    activePowerData.put("activePowerA", activePowerA);
                    activePowerData.put("activePowerB", activePowerB);
                    activePowerData.put("activePowerC", activePowerC);
                    activePower.put("activePower",activePowerData);
                    Map<String, Object> inActivePower = safeCompensateDeviceCenter.getInActivePower();
                    inActivePower.put("xDanWei", "秒前");
                    inActivePower.put("yDanWei", "var");
                    inActivePower.put("xData", xdata);
                    Map<String,Object> inActivePowerData=new LinkedHashMap<>();
                    inActivePowerData.put("inActivePowerA", inActivePowerA);
                    inActivePowerData.put("inActivePowerB", inActivePowerB);
                    inActivePowerData.put("inActivePowerC", inActivePowerC);
                    inActivePower.put("inActivePower",inActivePowerData);

                    Map<String, Object> powerFactor = safeCompensateDeviceCenter.getPowerFactor();
                    powerFactor.put("xDanWei", "秒前");
                    powerFactor.put("yDanWei", "0.001");
                    powerFactor.put("xData", xdata);
                    Map<String,Object> powerFactorData=new LinkedHashMap<>();
                    powerFactorData.put("powerFactorA", powerFactorA);
                    powerFactorData.put("powerFactorB", powerFactorB);
                    powerFactorData.put("powerFactorC", powerFactorC);
                    powerFactor.put("powerFactor",powerFactorData);
                }

            }
            return safeCompensateDeviceCenter;
        }
        return null;
    }

    @Override
    public SafeWaveDeviceCenter safeWaveDeviceInfo(Long projectId, Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if (device != null) {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            Map<Long, String> deviceTypeMap = new HashMap<>();
            for (DeviceType deviceType : deviceTypeList) {
                deviceTypeMap.put(deviceType.getId(), deviceType.getName());
            }
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            SafeWaveDeviceCenter safeWaveDeviceCenter = new SafeWaveDeviceCenter();
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                StringBuffer locationBuf = new StringBuffer();
                Long areaId = device.getAreaId();
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (areaId != null && (!areaId.equals(0l))) {
                    Area area = regionAreaService.selectByPrimaryKey(areaId);
                    if (area != null) {
                        locationBuf.append(area.getName());
                    }
                }
                if (buildingId != null && (!buildingId.equals(0l))) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        locationBuf.append(building.getName());
                    }
                }
                if (storeyId != null && (!storeyId.equals(0l))) {
                    Storey storey = regionStoreyService.selectByPrimaryKey(buildingId);
                    if (storey != null) {
                        locationBuf.append(storey.getName());
                    }
                }
                if (roomId != null && (!roomId.equals(0l))) {
                    Room room = regionRoomService.selectByPrimaryKey(buildingId);
                    if (room != null) {
                        locationBuf.append(room.getName());
                    }
                }
                if (!StringUtils.isBlank(locationBuf.toString())) {
                    safeWaveDeviceCenter.setLocation(locationBuf.toString());
                }
            } else {
                safeWaveDeviceCenter.setLocation(location);
            }
            safeWaveDeviceCenter.setPictureUrl(deviceTypePicture == null ? null : deviceTypePicture.getUrl());
            safeWaveDeviceCenter.setDeviceId(device.getId());
            safeWaveDeviceCenter.setStatus(device.getStatus());
            safeWaveDeviceCenter.setSerialNum(device.getSerialNum());
            safeWaveDeviceCenter.setDeviceName(device.getName());
            safeWaveDeviceCenter.setDeviceTypeId(device.getDeviceTypeId());
            safeWaveDeviceCenter.setDeviceTypeName(deviceTypeMap.get(device.getDeviceTypeId()));
            safeWaveDeviceCenter.setErasure(device.getErasure());
            safeWaveDeviceCenter.setVideoUrl(device.getVideoUrl());
            safeWaveDeviceCenter.setProjectId(projectId);
            safeWaveDeviceCenter.setDataFlag(0);
            List<DeviceDataWaveElec> deviceDataWaveElecList = deviceDataWaveService.getLastedTenData(device.getId());
            if (!CollectionUtils.isEmpty(deviceDataWaveElecList)) {
                safeWaveDeviceCenter.setDataFlag(1);
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
                List<Float> inActivePowerA = new ArrayList<>();
                List<Float> inActivePowerB = new ArrayList<>();
                List<Float> inActivePowerC = new ArrayList<>();
                List<Float> powerFactorA = new ArrayList<>();
                List<Float> powerFactorB = new ArrayList<>();
                List<Float> powerFactorC = new ArrayList<>();
                long current = System.currentTimeMillis();
                for (DeviceDataWaveElec deviceDataWaveElec : deviceDataWaveElecList) {
                    Date createTime = deviceDataWaveElec.getCreateTime();
                    long time = createTime.getTime();
                    int sec = (int) (current - time) / 1000;
                    xdata.add(sec);
                    String voltRmsA = deviceDataWaveElec.getVoltRmsA();
                    if (voltRmsA == null || voltRmsA.equals("0")) {
                        //添加示例数据
                        dianyaA.add(220f);
                    } else {
                        Float voltRmsAF = Float.parseFloat(voltRmsA);
                        dianyaA.add(voltRmsAF / 100);
                    }
                    String voltRmsB = deviceDataWaveElec.getVoltRmsB();
                    if (voltRmsB == null || voltRmsB.equals("0")) {
                        //添加示例数据
                        dianyaB.add(230f);
                    } else {
                        Float voltRmsBF = Float.parseFloat(voltRmsB);
                        dianyaB.add(voltRmsBF / 100);
                    }
                    String voltRmsC = deviceDataWaveElec.getVoltRmsC();
                    if (voltRmsC == null || voltRmsC.equals("0")) {
                        //添加示例数据
                        dianyaC.add(240f);
                    } else {
                        Float voltRmsCF = Float.parseFloat(voltRmsC);
                        dianyaC.add(voltRmsCF / 100);
                    }
                    String ampRmsA = deviceDataWaveElec.getAmpRmsA();
                    if (StringUtils.isBlank(ampRmsA) || ampRmsA.equals("0")) {
                        dianliuA.add(100f);
                    } else {
                        Float ampRmsAF = Float.parseFloat(ampRmsA);
                        dianliuA.add(ampRmsAF / 1000);
                    }
                    String ampRmsB = deviceDataWaveElec.getAmpRmsB();
                    if (StringUtils.isBlank(ampRmsB) || ampRmsB.equals("0")) {
                        dianliuB.add(120f);
                    } else {
                        Float ampRmsBF = Float.parseFloat(ampRmsB);
                        dianliuB.add(ampRmsBF / 1000);
                    }
                    String ampRmsC = deviceDataWaveElec.getAmpRmsC();
                    if (StringUtils.isBlank(ampRmsC) || ampRmsC.equals("0")) {
                        dianliuC.add(140f);
                    } else {
                        Float ampRmsCF = Float.parseFloat(ampRmsC);
                        dianliuC.add(ampRmsCF / 1000);
                    }
                    String activePowerA1 = deviceDataWaveElec.getActivePowerA();
                    if (StringUtils.isBlank(activePowerA1) || activePowerA1.equals("0")) {
                        activePowerA.add(100f);
                    } else {
                        Float activePowerA1F = Float.parseFloat(activePowerA1);
                        activePowerA.add(activePowerA1F / 10);
                    }
                    String activePowerB1 = deviceDataWaveElec.getActivePowerB();
                    if (StringUtils.isBlank(activePowerB1) || activePowerB1.equals("0")) {
                        activePowerB.add(200f);
                    } else {
                        Float activePowerB1F = Float.parseFloat(activePowerB1);
                        activePowerB.add(activePowerB1F / 10);
                    }
                    String activePowerC1 = deviceDataWaveElec.getActivePowerC();
                    if (StringUtils.isBlank(activePowerC1) || activePowerC1.equals("0")) {
                        activePowerC.add(300f);
                    } else {
                        Float activePowerC1F = Float.parseFloat(activePowerC1);
                        activePowerC.add(activePowerC1F / 10);
                    }

                    String reactivePowerA = deviceDataWaveElec.getReactivePowerA();
                    if (StringUtils.isBlank(reactivePowerA) || reactivePowerA.equals("0")) {
                        inActivePowerA.add(10f);
                    } else {
                        Float reactivePowerAF = Float.parseFloat(reactivePowerA);
                        inActivePowerA.add(reactivePowerAF / 10);
                    }
                    String reactivePowerB = deviceDataWaveElec.getReactivePowerB();
                    if (StringUtils.isBlank(reactivePowerB) || reactivePowerB.equals("0")) {
                        inActivePowerB.add(20f);
                    } else {
                        Float reactivePowerBF = Float.parseFloat(reactivePowerB);
                        inActivePowerB.add(reactivePowerBF / 10);
                    }
                    String reactivePowerC = deviceDataWaveElec.getReactivePowerC();
                    if (StringUtils.isBlank(reactivePowerC) || reactivePowerC.equals("0")) {
                        inActivePowerC.add(30f);
                    } else {
                        Float reactivePowerCF = Float.parseFloat(reactivePowerC);
                        inActivePowerC.add(reactivePowerCF / 10);
                    }
                    String powerFactorA1 = deviceDataWaveElec.getPowerFactorA();
                    if (StringUtils.isBlank(powerFactorA1) || powerFactorA1.equals("0")) {
                        powerFactorA.add(100f);
                    } else {
                        powerFactorA.add(Float.parseFloat(powerFactorA1));
                    }
                    String powerFactorB1 = deviceDataWaveElec.getPowerFactorB();
                    if (StringUtils.isBlank(powerFactorB1) || powerFactorB1.equals("0")) {
                        powerFactorB.add(200f);
                    } else {
                        powerFactorB.add(Float.parseFloat(powerFactorB1));
                    }
                    String powerFactorC1 = deviceDataWaveElec.getPowerFactorC();
                    if (StringUtils.isBlank(powerFactorC1) || powerFactorC1.equals("0")) {
                        powerFactorC.add(100f);
                    } else {
                        powerFactorC.add(Float.parseFloat(powerFactorC1));
                    }

                    Map<String, Object> dianYa = safeWaveDeviceCenter.getDianYa();
                    if (!CollectionUtils.isEmpty(dianyaA)) {
                        dianYa.put("xDanWei", "秒前");
                        dianYa.put("yDanWei", "V");
                        dianYa.put("xData", xdata);
                        Map<String,Object> dianYaData=new LinkedHashMap<>();
                        dianYaData.put("dianYaA", dianyaA);
                        dianYaData.put("dianYaB", dianyaB);
                        dianYaData.put("dianYaC", dianyaC);
                        dianYa.put("dianYa",dianYaData);
                    }

                    Map<String, Object> dianLiu = safeWaveDeviceCenter.getDianLiu();
                    if (!CollectionUtils.isEmpty(dianliuA)) {
                        dianLiu.put("xDanWei", "秒前");
                        dianLiu.put("yDanWei", "A");
                        dianLiu.put("xData", xdata);
                        Map<String,Object> dianLiuData=new LinkedHashMap<>();
                        dianLiuData.put("dianLiuA", dianliuA);
                        dianLiuData.put("dianLiuB", dianliuB);
                        dianLiuData.put("dianLiuC", dianliuC);
                        dianLiu.put("dianLiu",dianLiuData);
                    }
                    Map<String, Object> activePower = safeWaveDeviceCenter.getActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        activePower.put("xDanWei", "秒前");
                        activePower.put("yDanWei", "w");
                        activePower.put("xData", xdata);
                        Map<String,Object> activePowerData=new LinkedHashMap<>();
                        activePowerData.put("activePowerA", activePowerA);
                        activePowerData.put("activePowerB", activePowerB);
                        activePowerData.put("activePowerC", activePowerC);
                        activePower.put("activePower",activePowerData);
                    }
                    Map<String, Object> inActivePower = safeWaveDeviceCenter.getInActivePower();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        inActivePower.put("xDanWei", "秒前");
                        inActivePower.put("yDanWei", "var");
                        inActivePower.put("xData", xdata);
                        Map<String,Object> inActivePowerData=new LinkedHashMap<>();
                        inActivePowerData.put("inActivePowerA", inActivePowerA);
                        inActivePowerData.put("inActivePowerB", inActivePowerB);
                        inActivePowerData.put("inActivePowerC", inActivePowerC);
                        inActivePower.put("inActivePower",inActivePowerData);
                    }
                    Map<String, Object> powerFactor = safeWaveDeviceCenter.getPowerFactor();
                    if (!CollectionUtils.isEmpty(activePowerA)) {
                        powerFactor.put("xDanWei", "秒前");
                        powerFactor.put("yDanWei", "0.001");
                        powerFactor.put("xData", xdata);
                        Map<String,Object> powerFactorData=new LinkedHashMap<>();
                        powerFactorData.put("powerFactorA", powerFactorA);
                        powerFactorData.put("powerFactorB", powerFactorB);
                        powerFactorData.put("powerFactorC", powerFactorC);
                        powerFactor.put("powerFactor",powerFactorData);
                    }
                }
            }else {
                if(deviceId.equals(13l)){
                    safeWaveDeviceCenter.setDataFlag(1);
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
                    List<Float> inActivePowerA = new ArrayList<>();
                    List<Float> inActivePowerB = new ArrayList<>();
                    List<Float> inActivePowerC = new ArrayList<>();
                    List<Float> powerFactorA = new ArrayList<>();
                    List<Float> powerFactorB = new ArrayList<>();
                    List<Float> powerFactorC = new ArrayList<>();
                    handlerExamplesTimeData(xdata);
                    handlerExamplesData(dianyaA,220f);
                    handlerExamplesData(dianyaB,260f);
                    handlerExamplesData(dianyaC,280f);
                    handlerExamplesData(dianliuA,50f);
                    handlerExamplesData(dianliuB,80f);
                    handlerExamplesData(dianliuC,100f);
                    handlerExamplesData(activePowerA,1000f);
                    handlerExamplesData(activePowerB,2000f);
                    handlerExamplesData(activePowerC,3000f);
                    handlerExamplesData(inActivePowerA,20f);
                    handlerExamplesData(inActivePowerB,60f);
                    handlerExamplesData(inActivePowerC,100f);
                    handlerExamplesData(powerFactorA,500f);
                    handlerExamplesData(powerFactorB,700f);
                    handlerExamplesData(powerFactorC,800f);
                    Map<String, Object> dianYa = safeWaveDeviceCenter.getDianYa();
                    dianYa.put("xDanWei", "秒前");
                    dianYa.put("yDanWei", "V");
                    dianYa.put("xData", xdata);
                    Map<String,Object> dianYaData=new LinkedHashMap<>();
                    dianYaData.put("dianYaA", dianyaA);
                    dianYaData.put("dianYaB", dianyaB);
                    dianYaData.put("dianYaC", dianyaC);
                    dianYa.put("dianYa",dianYaData);

                    Map<String, Object> dianLiu = safeWaveDeviceCenter.getDianLiu();
                    dianLiu.put("xDanWei", "秒前");
                    dianLiu.put("yDanWei", "A");
                    dianLiu.put("xData", xdata);
                    Map<String,Object> dianLiuData=new LinkedHashMap<>();
                    dianLiuData.put("dianLiuA", dianliuA);
                    dianLiuData.put("dianLiuB", dianliuB);
                    dianLiuData.put("dianLiuC", dianliuC);
                    dianLiu.put("dianLiu",dianLiuData);

                    Map<String, Object> activePower = safeWaveDeviceCenter.getActivePower();
                    activePower.put("xDanWei", "秒前");
                    activePower.put("yDanWei", "w");
                    activePower.put("xData", xdata);
                    Map<String,Object> activePowerData=new LinkedHashMap<>();
                    activePowerData.put("activePowerA", activePowerA);
                    activePowerData.put("activePowerB", activePowerB);
                    activePowerData.put("activePowerC", activePowerC);
                    activePower.put("activePower",activePowerData);

                    Map<String, Object> inActivePower = safeWaveDeviceCenter.getInActivePower();
                    inActivePower.put("xDanWei", "秒前");
                    inActivePower.put("yDanWei", "var");
                    inActivePower.put("xData", xdata);
                    Map<String,Object> inActivePowerData=new LinkedHashMap<>();
                    inActivePowerData.put("inActivePowerA", inActivePowerA);
                    inActivePowerData.put("inActivePowerB", inActivePowerB);
                    inActivePowerData.put("inActivePowerC", inActivePowerC);
                    inActivePower.put("inActivePower",inActivePowerData);

                    Map<String, Object> powerFactor = safeWaveDeviceCenter.getPowerFactor();
                    powerFactor.put("xDanWei", "秒前");
                    powerFactor.put("yDanWei", "0.001");
                    powerFactor.put("xData", xdata);
                    Map<String,Object> powerFactorData=new LinkedHashMap<>();
                    powerFactorData.put("powerFactorA", powerFactorA);
                    powerFactorData.put("powerFactorB", powerFactorB);
                    powerFactorData.put("powerFactorC", powerFactorC);
                    powerFactor.put("powerFactor",powerFactorData);
                }
            }
            return safeWaveDeviceCenter;
        }
        return null;
    }

    @Override
    public SafeDeviceVariableParam getSafeDeviceVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        SafeDeviceVariableParam safeDeviceVariableParam = null;
        if (device != null) {
            safeDeviceVariableParam = new SafeDeviceVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                safeDeviceVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                safeDeviceVariableParam.setSystemIds(systemIds);
                safeDeviceVariableParam.setSystemList(deviceSystemList);
            }
            safeDeviceVariableParam.setDeviceId(device.getId());
            safeDeviceVariableParam.setSerialNum(device.getSerialNum());
            safeDeviceVariableParam.setAreaId(device.getAreaId());
            safeDeviceVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            //处理位置信息
            if (StringUtils.isBlank(location)) {
                safeDeviceVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        safeDeviceVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        safeDeviceVariableParam.setBuildingId(buildingId);
                        safeDeviceVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        safeDeviceVariableParam.setStoreyName(storey.getName());
                        safeDeviceVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        safeDeviceVariableParam.setRoomId(roomId);
                        safeDeviceVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                safeDeviceVariableParam.setLocationFlag(1);
                safeDeviceVariableParam.setLocation(device.getLocation());
            }
            //处理监控
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                safeDeviceVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                safeDeviceVariableParam.setMonitorName(monitor.getName());
            }
            //处理基础信息
            safeDeviceVariableParam.setDeviceId(device.getId());
            safeDeviceVariableParam.setDeviceName(device.getName());
            safeDeviceVariableParam.setJd(device.getJd());
            safeDeviceVariableParam.setWd(device.getWd());
            safeDeviceVariableParam.setVideoUrl(device.getVideoUrl());
            safeDeviceVariableParam.setManufacturer(device.getManufacturer());
            safeDeviceVariableParam.setUsefulLife(device.getUsefulLife());
            //处理设备图片
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                safeDeviceVariableParam.setPictureUrl(devicePicture.getUrl());
            }
            //处理设备参数
            DeviceParamsSafeElec deviceParamsSafeElec = deviceParamsSafeElecService.findByDeviceId(deviceId);
            if (deviceParamsSafeElec != null) {
                safeDeviceVariableParam.setUpInterval(deviceParamsSafeElec.getUpInterval());
                String tempHigh = deviceParamsSafeElec.getTempHigh();
                String currHigh = deviceParamsSafeElec.getCurrHigh();
                String currLeak = deviceParamsSafeElec.getCurrLeak();
                String voltHigh = deviceParamsSafeElec.getVoltHigh();
                String voltLow = deviceParamsSafeElec.getVoltLow();
                if(StringUtils.isNotBlank(tempHigh)){
                    Integer tempHighInt = Integer.parseInt(tempHigh);
                    safeDeviceVariableParam.setTempHigh(String.valueOf(tempHighInt/10));
                }
                if(StringUtils.isNotBlank(currLeak)){
                    Integer currLeakInt = Integer.parseInt(currLeak);
                    safeDeviceVariableParam.setCurrLeak(String.valueOf(currLeakInt/10));
                }
                if(StringUtils.isNotBlank(currHigh)){
                    Integer currHighInt = Integer.parseInt(currHigh);
                    safeDeviceVariableParam.setCurrHigh(String.valueOf(currHighInt/10));
                }
                if(StringUtils.isNotBlank(voltHigh)){
                    Integer voltHighInt = Integer.parseInt(voltHigh);
                    safeDeviceVariableParam.setVoltHigh(String.valueOf(voltHighInt/10));
                }
                if(StringUtils.isNotBlank(voltLow)){
                    Integer voltLowInt = Integer.parseInt(voltLow);
                    safeDeviceVariableParam.setVoltLow(String.valueOf(voltLowInt/10));
                }
            }

        }
        return safeDeviceVariableParam;
    }

    @Override
    public SmartDeviceVariableParam getSmartDeviceVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        SmartDeviceVariableParam smartDeviceVariableParam = null;
        if (device != null) {
            smartDeviceVariableParam = new SmartDeviceVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                smartDeviceVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                smartDeviceVariableParam.setSystemIds(systemIds);
                smartDeviceVariableParam.setSystemList(deviceSystemList);
            }
            smartDeviceVariableParam.setDeviceId(device.getId());
            smartDeviceVariableParam.setSerialNum(device.getSerialNum());
            smartDeviceVariableParam.setAreaId(device.getAreaId());
            smartDeviceVariableParam.setStatus(device.getStatus());
            //处理位置信息
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                smartDeviceVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        smartDeviceVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        smartDeviceVariableParam.setBuildingId(buildingId);
                        smartDeviceVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        smartDeviceVariableParam.setStoreyName(storey.getName());
                        smartDeviceVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        smartDeviceVariableParam.setRoomId(roomId);
                        smartDeviceVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                smartDeviceVariableParam.setLocationFlag(1);
                smartDeviceVariableParam.setLocation(device.getLocation());
            }
            //处理监控信息
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                smartDeviceVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                smartDeviceVariableParam.setMonitorName(monitor.getName());
            }
            //处理基础信息
            smartDeviceVariableParam.setDeviceId(device.getId());
            smartDeviceVariableParam.setDeviceName(device.getName());
            smartDeviceVariableParam.setJd(device.getJd());
            smartDeviceVariableParam.setWd(device.getWd());
            smartDeviceVariableParam.setVideoUrl(device.getVideoUrl());
            smartDeviceVariableParam.setManufacturer(device.getManufacturer());
            smartDeviceVariableParam.setUsefulLife(device.getUsefulLife());
            //处理设备图片
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                smartDeviceVariableParam.setPictureUrl(devicePicture.getUrl());
            }
            //处理设备参数
            DeviceParamsSmartElec deviceParamsSmartElec = deviceParamsSmartElecService.findByDeviceId(deviceId);
            smartDeviceVariableParam.setCurrCT(deviceParamsSmartElec.getCtRatioA());
            smartDeviceVariableParam.setLeakCT(deviceParamsSmartElec.getCtRatioD());
            smartDeviceVariableParam.setCurrentThdA(deviceParamsSmartElec.getCurrentThdA());
            smartDeviceVariableParam.setCurrentThdB(deviceParamsSmartElec.getCurrentThdB());
            smartDeviceVariableParam.setCurrentThdC(deviceParamsSmartElec.getCurrentThdC());
            smartDeviceVariableParam.setLackPhaseA(deviceParamsSmartElec.getLackPhaseA());
            smartDeviceVariableParam.setLackPhaseB(deviceParamsSmartElec.getLackPhaseB());
            smartDeviceVariableParam.setLackPhaseC(deviceParamsSmartElec.getLackPhaseC());
            smartDeviceVariableParam.setOverCurrentA(deviceParamsSmartElec.getOverCurrentA());
            smartDeviceVariableParam.setOverCurrentB(deviceParamsSmartElec.getOverCurrentB());
            smartDeviceVariableParam.setOverCurrentC(deviceParamsSmartElec.getOverCurrentC());
            smartDeviceVariableParam.setOverLeaked(deviceParamsSmartElec.getOverLeaked());
            String overTemper = deviceParamsSmartElec.getOverTemper();
            //温度参数除以10
            if(StringUtils.isNotBlank(overTemper)){
                Integer overTemperInt = Integer.parseInt(overTemper);
                smartDeviceVariableParam.setOverTemper(String.valueOf(overTemperInt/10));
            }
            smartDeviceVariableParam.setOverVoltageA(deviceParamsSmartElec.getOverVoltageA());
            smartDeviceVariableParam.setOverVoltageB(deviceParamsSmartElec.getOverVoltageA());
            smartDeviceVariableParam.setOverVoltageC(deviceParamsSmartElec.getOverVoltageA());
            smartDeviceVariableParam.setPowerFactorA(deviceParamsSmartElec.getPowerFactorA());
            smartDeviceVariableParam.setPowerFactorB(deviceParamsSmartElec.getPowerFactorB());
            smartDeviceVariableParam.setPowerFactorC(deviceParamsSmartElec.getPowerFactorA());
            smartDeviceVariableParam.setReportInterval(deviceParamsSmartElec.getReportInterval());
            smartDeviceVariableParam.setTransCapacity(deviceParamsSmartElec.getTransCapacity());
            smartDeviceVariableParam.setTransLoad(deviceParamsSmartElec.getTransLoad());
            smartDeviceVariableParam.setUnderVoltageA(deviceParamsSmartElec.getUnderVoltageA());
            smartDeviceVariableParam.setUnderVoltageB(deviceParamsSmartElec.getUnderVoltageB());
            smartDeviceVariableParam.setUnderVoltageC(deviceParamsSmartElec.getUnderVoltageC());
            smartDeviceVariableParam.setVoltageThdA(deviceParamsSmartElec.getVoltageThdA());
            smartDeviceVariableParam.setVoltageThdB(deviceParamsSmartElec.getVoltageThdB());
            smartDeviceVariableParam.setVoltageThdC(deviceParamsSmartElec.getVoltageThdC());
        }
        return smartDeviceVariableParam;
    }


    @Override
    public SmokeDeviceVariableParam getSmokeDeviceVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        SmokeDeviceVariableParam smokeDeviceVariableParam = null;
        if (device != null) {
            smokeDeviceVariableParam = new SmokeDeviceVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                smokeDeviceVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                smokeDeviceVariableParam.setSystemIds(systemIds);
                smokeDeviceVariableParam.setSystemList(deviceSystemList);
            }
            smokeDeviceVariableParam.setDeviceId(device.getId());
            smokeDeviceVariableParam.setSerialNum(device.getSerialNum());
            smokeDeviceVariableParam.setAreaId(device.getAreaId());
            smokeDeviceVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                smokeDeviceVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        smokeDeviceVariableParam.setAreaName(area.getName());
                        smokeDeviceVariableParam.setAreaId(area.getId());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        smokeDeviceVariableParam.setBuildingId(buildingId);
                        smokeDeviceVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        smokeDeviceVariableParam.setStoreyName(storey.getName());
                        smokeDeviceVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        smokeDeviceVariableParam.setRoomId(roomId);
                        smokeDeviceVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                smokeDeviceVariableParam.setLocationFlag(1);
                smokeDeviceVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                smokeDeviceVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                smokeDeviceVariableParam.setMonitorName(monitor.getName());
            }
            smokeDeviceVariableParam.setDeviceId(device.getId());
            smokeDeviceVariableParam.setDeviceName(device.getName());
            smokeDeviceVariableParam.setJd(device.getJd());
            smokeDeviceVariableParam.setWd(device.getWd());
            smokeDeviceVariableParam.setVideoUrl(device.getVideoUrl());
            smokeDeviceVariableParam.setManufacturer(device.getManufacturer());
            smokeDeviceVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                smokeDeviceVariableParam.setPictureUrl(devicePicture.getUrl());
            }
            DeviceParamsSmoke deviceParamsSmoke = deviceParamSmokeService.findByDeviceId(deviceId);
            if (deviceParamsSmoke != null) {
                smokeDeviceVariableParam.setInterval(deviceParamsSmoke.getInterval());
                smokeDeviceVariableParam.setElectricityQuantity(deviceParamsSmoke.getElectricityQuantity());
                smokeDeviceVariableParam.setSmokeConcentration(deviceParamsSmoke.getSmokeConcentration());
            }

        }
        return smokeDeviceVariableParam;
    }

    @Override
    public WaterDeviceVariableParam getWaterDeviceVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        WaterDeviceVariableParam waterDeviceVariableParam = null;
        if (device != null) {
            waterDeviceVariableParam = new WaterDeviceVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                waterDeviceVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                waterDeviceVariableParam.setSystemIds(systemIds);
                waterDeviceVariableParam.setSystemList(deviceSystemList);
            }
            waterDeviceVariableParam.setDeviceId(device.getId());
            waterDeviceVariableParam.setSerialNum(device.getSerialNum());
            waterDeviceVariableParam.setAreaId(device.getAreaId());
            waterDeviceVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                waterDeviceVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        waterDeviceVariableParam.setAreaName(area.getName());
                        waterDeviceVariableParam.setAreaId(area.getId());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        waterDeviceVariableParam.setBuildingId(buildingId);
                        waterDeviceVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        waterDeviceVariableParam.setStoreyName(storey.getName());
                        waterDeviceVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        waterDeviceVariableParam.setRoomId(roomId);
                        waterDeviceVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                waterDeviceVariableParam.setLocationFlag(1);
                waterDeviceVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                waterDeviceVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor=monitorService.findById(deviceMonitorHls.getMonitorId());
                waterDeviceVariableParam.setMonitorName(monitor.getName());
            }
            waterDeviceVariableParam.setDeviceId(device.getId());
            waterDeviceVariableParam.setDeviceName(device.getName());
            waterDeviceVariableParam.setJd(device.getJd());
            waterDeviceVariableParam.setWd(device.getWd());
            waterDeviceVariableParam.setVideoUrl(device.getVideoUrl());
            waterDeviceVariableParam.setManufacturer(device.getManufacturer());
            waterDeviceVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                waterDeviceVariableParam.setPictureUrl(devicePicture.getUrl());
            }
            DeviceParamsWater deviceParamsWater = deviceParamsWaterService.findByDeviceId(deviceId);
            if(deviceParamsWater!=null){
                waterDeviceVariableParam.setInterval(deviceParamsWater.getInterval());
                waterDeviceVariableParam.setPressure(deviceParamsWater.getPressure());
                waterDeviceVariableParam.setDischarge(deviceParamsWater.getDischarge());
            }
        }
        return waterDeviceVariableParam;
    }

    @Override
    public DoorDeviceVariableParams getDoorWaterVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        DoorDeviceVariableParams doorDeviceVariableParams = null;
        if (device != null) {
            doorDeviceVariableParams = new DoorDeviceVariableParams();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                doorDeviceVariableParams.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                doorDeviceVariableParams.setSystemIds(systemIds);
                doorDeviceVariableParams.setSystemList(deviceSystemList);
            }
            doorDeviceVariableParams.setDeviceId(device.getId());
            doorDeviceVariableParams.setSerialNum(device.getSerialNum());
            doorDeviceVariableParams.setAreaId(device.getAreaId());
            doorDeviceVariableParams.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                doorDeviceVariableParams.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        doorDeviceVariableParams.setAreaName(area.getName());
                        doorDeviceVariableParams.setAreaId(area.getId());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        doorDeviceVariableParams.setBuildingId(buildingId);
                        doorDeviceVariableParams.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        doorDeviceVariableParams.setStoreyName(storey.getName());
                        doorDeviceVariableParams.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        doorDeviceVariableParams.setRoomId(roomId);
                        doorDeviceVariableParams.setRoomName(room.getName());
                    }
                }

            } else {
                doorDeviceVariableParams.setLocationFlag(1);
                doorDeviceVariableParams.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                doorDeviceVariableParams.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                doorDeviceVariableParams.setMonitorName(monitor.getName());
            }
            doorDeviceVariableParams.setDeviceId(device.getId());
            doorDeviceVariableParams.setDeviceName(device.getName());
            doorDeviceVariableParams.setJd(device.getJd());
            doorDeviceVariableParams.setWd(device.getWd());
            doorDeviceVariableParams.setVideoUrl(device.getVideoUrl());
            doorDeviceVariableParams.setManufacturer(device.getManufacturer());
            doorDeviceVariableParams.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                doorDeviceVariableParams.setPictureUrl(devicePicture.getUrl());
            }
            DeviceParamsDoor deviceParamsDoor =  deviceParamsDoorService.findByDeviceId(deviceId);
            if(deviceParamsDoor!=null){
                doorDeviceVariableParams.setInterval(deviceParamsDoor.getInterval());
                doorDeviceVariableParams.setCallSwitch(deviceParamsDoor.getCallSwitch());
            }
        }
        return doorDeviceVariableParams;
    }

    @Override
    public WaveVariableParam getWaveVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        WaveVariableParam WaveVariableParam = null;
        if (device != null) {
            WaveVariableParam = new WaveVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                WaveVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                WaveVariableParam.setSystemIds(systemIds);
                WaveVariableParam.setSystemList(deviceSystemList);
            }
            WaveVariableParam.setDeviceId(device.getId());
            WaveVariableParam.setSerialNum(device.getSerialNum());
            WaveVariableParam.setAreaId(device.getAreaId());
            WaveVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                WaveVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        WaveVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        WaveVariableParam.setBuildingId(buildingId);
                        WaveVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        WaveVariableParam.setStoreyName(storey.getName());
                        WaveVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        WaveVariableParam.setRoomId(roomId);
                        WaveVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                WaveVariableParam.setLocationFlag(1);
                WaveVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                WaveVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                WaveVariableParam.setMonitorName(monitor.getName());
            }
            WaveVariableParam.setDeviceId(device.getId());
            WaveVariableParam.setDeviceName(device.getName());
            WaveVariableParam.setJd(device.getJd());
            WaveVariableParam.setWd(device.getWd());
            WaveVariableParam.setVideoUrl(device.getVideoUrl());
            WaveVariableParam.setManufacturer(device.getManufacturer());
            WaveVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (deviceTypePicture != null) {
                WaveVariableParam.setPictureUrl(deviceTypePicture.getUrl());
            }
            DeviceParamsWaveElec deviceParamsWaveElec = deviceParamsWaveElecService.findByDeviceId(deviceId);
            WaveVariableParam.setCurrCT(deviceParamsWaveElec.getCtRatioA());
            WaveVariableParam.setCurrentThdA(deviceParamsWaveElec.getCurrentThdA());
            WaveVariableParam.setCurrentThdB(deviceParamsWaveElec.getCurrentThdB());
            WaveVariableParam.setCurrentThdC(deviceParamsWaveElec.getCurrentThdC());
            WaveVariableParam.setLackPhaseA(deviceParamsWaveElec.getLackPhaseA());
            WaveVariableParam.setLackPhaseB(deviceParamsWaveElec.getLackPhaseB());
            WaveVariableParam.setLackPhaseC(deviceParamsWaveElec.getLackPhaseC());
            WaveVariableParam.setOverCurrentA(deviceParamsWaveElec.getOverCurrentA());
            WaveVariableParam.setOverCurrentB(deviceParamsWaveElec.getOverCurrentB());
            WaveVariableParam.setOverCurrentC(deviceParamsWaveElec.getOverCurrentC());
            WaveVariableParam.setOverVoltageA(deviceParamsWaveElec.getOverVoltageA());
            WaveVariableParam.setOverVoltageB(deviceParamsWaveElec.getOverVoltageA());
            WaveVariableParam.setOverVoltageC(deviceParamsWaveElec.getOverVoltageA());
            WaveVariableParam.setPowerFactorA(deviceParamsWaveElec.getPowerFactorA());
            WaveVariableParam.setPowerFactorB(deviceParamsWaveElec.getPowerFactorB());
            WaveVariableParam.setPowerFactorC(deviceParamsWaveElec.getPowerFactorA());
            WaveVariableParam.setReportInterval(deviceParamsWaveElec.getReportInterval());
            WaveVariableParam.setTransCapacity(deviceParamsWaveElec.getTransCapacity());
            WaveVariableParam.setTransLoad(deviceParamsWaveElec.getTransLoad());
            WaveVariableParam.setUnderVoltageA(deviceParamsWaveElec.getUnderVoltageA());
            WaveVariableParam.setUnderVoltageB(deviceParamsWaveElec.getUnderVoltageB());
            WaveVariableParam.setUnderVoltageC(deviceParamsWaveElec.getUnderVoltageC());
            WaveVariableParam.setVoltageThdA(deviceParamsWaveElec.getVoltageThdA());
            WaveVariableParam.setVoltageThdB(deviceParamsWaveElec.getVoltageThdB());
            WaveVariableParam.setVoltageThdC(deviceParamsWaveElec.getVoltageThdC());
            return WaveVariableParam;
        }else{
            return null;
        }
    }

    @Override
    public CompensateVariableParam getCompensateVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        CompensateVariableParam compensateVariableParam = null;
        if (device != null) {
            compensateVariableParam = new CompensateVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                compensateVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                compensateVariableParam.setSystemIds(systemIds);
                compensateVariableParam.setSystemList(deviceSystemList);
            }
            compensateVariableParam.setDeviceId(device.getId());
            compensateVariableParam.setSerialNum(device.getSerialNum());
            compensateVariableParam.setAreaId(device.getAreaId());
            compensateVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                compensateVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        compensateVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        compensateVariableParam.setBuildingId(buildingId);
                        compensateVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        compensateVariableParam.setStoreyName(storey.getName());
                        compensateVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        compensateVariableParam.setRoomId(roomId);
                        compensateVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                compensateVariableParam.setLocationFlag(1);
                compensateVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice monitorDevice=monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(monitorDevice!=null){
                compensateVariableParam.setMonitorId(monitorDevice.getMonitorId());
                Monitor monitor=monitorService.findById(monitorDevice.getMonitorId());
                compensateVariableParam.setMonitorName(monitor.getName());
            }
            compensateVariableParam.setDeviceId(device.getId());
            compensateVariableParam.setDeviceName(device.getName());
            compensateVariableParam.setJd(device.getJd());
            compensateVariableParam.setWd(device.getWd());
            compensateVariableParam.setVideoUrl(device.getVideoUrl());
            compensateVariableParam.setManufacturer(device.getManufacturer());
            compensateVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (deviceTypePicture != null) {
                compensateVariableParam.setPictureUrl(deviceTypePicture.getUrl());
            }
            DeviceParamsCompensateElec deviceParamsWaveElec = deviceParamsCompensateElecService.findByDeviceId(deviceId);
            compensateVariableParam.setCurrCT(deviceParamsWaveElec.getCtRatioA());
            compensateVariableParam.setCurrentThdA(deviceParamsWaveElec.getCurrentThdA());
            compensateVariableParam.setCurrentThdB(deviceParamsWaveElec.getCurrentThdB());
            compensateVariableParam.setCurrentThdC(deviceParamsWaveElec.getCurrentThdC());
            compensateVariableParam.setLackPhaseA(deviceParamsWaveElec.getLackPhaseA());
            compensateVariableParam.setLackPhaseB(deviceParamsWaveElec.getLackPhaseB());
            compensateVariableParam.setLackPhaseC(deviceParamsWaveElec.getLackPhaseC());
            compensateVariableParam.setOverCurrentA(deviceParamsWaveElec.getOverCurrentA());
            compensateVariableParam.setOverCurrentB(deviceParamsWaveElec.getOverCurrentB());
            compensateVariableParam.setOverCurrentC(deviceParamsWaveElec.getOverCurrentC());
            compensateVariableParam.setOverVoltageA(deviceParamsWaveElec.getOverVoltageA());
            compensateVariableParam.setOverVoltageB(deviceParamsWaveElec.getOverVoltageA());
            compensateVariableParam.setOverVoltageC(deviceParamsWaveElec.getOverVoltageA());
            compensateVariableParam.setPowerFactorA(deviceParamsWaveElec.getPowerFactorA());
            compensateVariableParam.setPowerFactorB(deviceParamsWaveElec.getPowerFactorB());
            compensateVariableParam.setPowerFactorC(deviceParamsWaveElec.getPowerFactorA());
            compensateVariableParam.setReportInterval(deviceParamsWaveElec.getReportInterval());
            compensateVariableParam.setTransCapacity(deviceParamsWaveElec.getTransCapacity());
            compensateVariableParam.setTransLoad(deviceParamsWaveElec.getTransLoad());
            compensateVariableParam.setUnderVoltageA(deviceParamsWaveElec.getUnderVoltageA());
            compensateVariableParam.setUnderVoltageB(deviceParamsWaveElec.getUnderVoltageB());
            compensateVariableParam.setUnderVoltageC(deviceParamsWaveElec.getUnderVoltageC());
            compensateVariableParam.setVoltageThdA(deviceParamsWaveElec.getVoltageThdA());
            compensateVariableParam.setVoltageThdB(deviceParamsWaveElec.getVoltageThdB());
            compensateVariableParam.setVoltageThdC(deviceParamsWaveElec.getVoltageThdC());
            return compensateVariableParam;
        }else{
            return null;
        }
    }

    @Override
    public TempatureVariableParam getTempatureVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        TempatureVariableParam tempatureVariableParam = null;
        if (device != null) {
            tempatureVariableParam = new TempatureVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                tempatureVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                tempatureVariableParam.setSystemId(systemIds);
                tempatureVariableParam.setSystemList(deviceSystemList);
            }
            tempatureVariableParam.setDeviceId(device.getId());
            tempatureVariableParam.setSerialNum(device.getSerialNum());
            tempatureVariableParam.setAreaId(device.getAreaId());
            tempatureVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                tempatureVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        tempatureVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        tempatureVariableParam.setBuildingId(buildingId);
                        tempatureVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        tempatureVariableParam.setStoreyName(storey.getName());
                        tempatureVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        tempatureVariableParam.setRoomId(roomId);
                        tempatureVariableParam.setRoomName(room.getName());
                    }
                }

            } else {
                tempatureVariableParam.setLocationFlag(1);
                tempatureVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                tempatureVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                tempatureVariableParam.setMonitorName(monitor.getName());
            }
            tempatureVariableParam.setDeviceId(device.getId());
            tempatureVariableParam.setDeviceName(device.getName());
            tempatureVariableParam.setJd(device.getJd());
            tempatureVariableParam.setWd(device.getWd());
            tempatureVariableParam.setVideoUrl(device.getVideoUrl());
            tempatureVariableParam.setManufacturer(device.getManufacturer());
            tempatureVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture deviceTypePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (deviceTypePicture != null) {
                tempatureVariableParam.setPictureUrl(deviceTypePicture.getUrl());
            }
            DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity = paramsTemperaturehumidityService.findByDeviceId(deviceId);
            tempatureVariableParam.setInterval(deviceParamsTemperaturehumidity.getInterval());
            return tempatureVariableParam;
        }else{
            return null;
        }
    }

    @Override
    public SuperDeviceVariableParam getSuperDeviceVariableParam(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        SuperDeviceVariableParam superDeviceVariableParam = null;
        if (device != null) {
            superDeviceVariableParam = new SuperDeviceVariableParam();
            List<DeviceSystem> deviceSystemList = deviceSystemService.selectByDeviceId(deviceId);
            if (CollectionUtils.isEmpty(deviceSystemList)) {
                superDeviceVariableParam.setSystemList(null);
            } else {
                String systemIds="";
                for (DeviceSystem deviceSystem : deviceSystemList) {
                    if(StringUtils.isNotBlank(systemIds)){
                        systemIds=systemIds+"," ;
                    }
                    systemIds=systemIds+deviceSystem.getSystemId();
                }
                superDeviceVariableParam.setSystemIds(systemIds);
                superDeviceVariableParam.setSystemList(deviceSystemList);
            }
            superDeviceVariableParam.setDeviceId(device.getId());
            superDeviceVariableParam.setSerialNum(device.getSerialNum());
            superDeviceVariableParam.setAreaId(device.getAreaId());
            superDeviceVariableParam.setStatus(device.getStatus());
            String location = device.getLocation();
            if (StringUtils.isBlank(location)) {
                superDeviceVariableParam.setLocationFlag(0);
                if (device.getAreaId() != null) {
                    Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                    if (area != null) {
                        superDeviceVariableParam.setAreaName(area.getName());
                    }
                }
                Long buildingId = device.getBuildingId();
                Long storeyId = device.getStoreyId();
                Long roomId = device.getRoomId();
                if (buildingId != null && storeyId != null && roomId != null) {
                    Building building = regionBuildingService.selectByPrimaryKey(buildingId);
                    if (building != null) {
                        superDeviceVariableParam.setBuildingId(buildingId);
                        superDeviceVariableParam.setBuildingName(building.getName());
                    }
                    Storey storey = regionStoreyService.getByBuildingAndStorey(buildingId, storeyId);
                    if (storey != null) {
                        superDeviceVariableParam.setStoreyName(storey.getName());
                        superDeviceVariableParam.setStoreyId(storeyId);
                    }
                    Room room = regionRoomService.getByStoreyAndRoom(storeyId, roomId);
                    if (room != null) {
                        superDeviceVariableParam.setRoomId(roomId);
                        superDeviceVariableParam.setRoomName(room.getName());
                    }
                }
            } else {
                superDeviceVariableParam.setLocationFlag(1);
                superDeviceVariableParam.setLocation(device.getLocation());
            }
            MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
            if(deviceMonitorHls!=null){
                superDeviceVariableParam.setMonitorId(deviceMonitorHls.getMonitorId());
                Monitor monitor = monitorService.findById(deviceMonitorHls.getMonitorId());
                superDeviceVariableParam.setMonitorName(monitor.getName());
            }
            superDeviceVariableParam.setDeviceId(device.getId());
            superDeviceVariableParam.setDeviceName(device.getName());
            superDeviceVariableParam.setJd(device.getJd());
            superDeviceVariableParam.setWd(device.getWd());
            superDeviceVariableParam.setVideoUrl(device.getVideoUrl());
            superDeviceVariableParam.setManufacturer(device.getManufacturer());
            superDeviceVariableParam.setUsefulLife(device.getUsefulLife());
            DeviceTypePicture devicePicture = deviceTypePictureService.findByDeviceTypeId(device.getDeviceTypeId());
            if (devicePicture != null) {
                superDeviceVariableParam.setPictureUrl(devicePicture.getUrl());
            }
            DeviceParamSmartSuper deviceParamSmartSuper = deviceParamSmartSuperService.findByDeviceId(device.getId());
            superDeviceVariableParam.setCurrCT(deviceParamSmartSuper.getCtRatioA());
            superDeviceVariableParam.setLeakCT(deviceParamSmartSuper.getCtRatioD());
            superDeviceVariableParam.setCurrentThdA(deviceParamSmartSuper.getCurrentThdA());
            superDeviceVariableParam.setCurrentThdB(deviceParamSmartSuper.getCurrentThdB());
            superDeviceVariableParam.setCurrentThdC(deviceParamSmartSuper.getCurrentThdC());
            superDeviceVariableParam.setLackPhaseA(deviceParamSmartSuper.getLackPhaseA());
            superDeviceVariableParam.setLackPhaseB(deviceParamSmartSuper.getLackPhaseB());
            superDeviceVariableParam.setLackPhaseC(deviceParamSmartSuper.getLackPhaseC());
            superDeviceVariableParam.setOverCurrentA(deviceParamSmartSuper.getOverCurrentA());
            superDeviceVariableParam.setOverCurrentB(deviceParamSmartSuper.getOverCurrentB());
            superDeviceVariableParam.setOverCurrentC(deviceParamSmartSuper.getOverCurrentC());
            superDeviceVariableParam.setOverLeaked(deviceParamSmartSuper.getOverLeaked());
            String overTemper = deviceParamSmartSuper.getOverTemper();
            //温度处理除以10
            if(StringUtils.isNotBlank(overTemper)){
                Integer overTemperInt=Integer.parseInt(overTemper);
                superDeviceVariableParam.setOverTemper(String.valueOf(overTemperInt/10));
            }
            superDeviceVariableParam.setOverVoltageA(deviceParamSmartSuper.getOverVoltageA());
            superDeviceVariableParam.setOverVoltageB(deviceParamSmartSuper.getOverVoltageA());
            superDeviceVariableParam.setOverVoltageC(deviceParamSmartSuper.getOverVoltageA());
            superDeviceVariableParam.setPowerFactorA(deviceParamSmartSuper.getPowerFactorA());
            superDeviceVariableParam.setPowerFactorB(deviceParamSmartSuper.getPowerFactorB());
            superDeviceVariableParam.setPowerFactorC(deviceParamSmartSuper.getPowerFactorA());
            superDeviceVariableParam.setReportTime(deviceParamSmartSuper.getReportTime());
            superDeviceVariableParam.setUnderVoltageA(deviceParamSmartSuper.getUnderVoltageA());
            superDeviceVariableParam.setUnderVoltageB(deviceParamSmartSuper.getUnderVoltageB());
            superDeviceVariableParam.setUnderVoltageC(deviceParamSmartSuper.getUnderVoltageC());
            superDeviceVariableParam.setVoltageThdA(deviceParamSmartSuper.getVoltageThdA());
            superDeviceVariableParam.setVoltageThdB(deviceParamSmartSuper.getVoltageThdB());
            superDeviceVariableParam.setVoltageThdC(deviceParamSmartSuper.getVoltageThdC());

            superDeviceVariableParam.setCtRatioHighVolt(deviceParamSmartSuper.getCtRatioHighVolt());
            superDeviceVariableParam.setCurrentUnbalance(deviceParamSmartSuper.getCurrentUnbalance());
            superDeviceVariableParam.setVoltageUnbalance(deviceParamSmartSuper.getVoltageUnbalance());
        }
        return superDeviceVariableParam;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeviceParam(Map<String, Object> paramMap) throws Exception {
        Long  deviceId = Long.parseLong(paramMap.get("deviceId").toString());
        Long projectId = Long.parseLong(paramMap.get("projectId").toString());
        Long deviceTypeId=Long.parseLong(paramMap.get("deviceTypeId").toString());
        Device deviceSys = deviceDao.selectByPrimaryKey(deviceId);
        Date date=new Date();
        Boolean deviceChangeFlag=false;
        Boolean nameFlag=false;
        //处理设备的基本信息
        if(deviceSys!=null){
            Device device=new Device();
            device.setId(deviceId);
            Object jdObj = paramMap.get("jd");
            Object wdObj = paramMap.get("wd");
            if(jdObj!=null){
                device.setJd(Double.parseDouble(jdObj.toString()));
                deviceChangeFlag=true;
            }
            if(wdObj!=null){
                device.setWd(Double.parseDouble(wdObj.toString()));
                deviceChangeFlag=true;
            }
            Object deviceNameObj = paramMap.get("deviceName");
            if(deviceNameObj!=null&&StringUtils.isNotBlank(deviceNameObj.toString())){
                device.setName(deviceNameObj.toString());
                deviceChangeFlag=true;
                nameFlag=true;

            }
            //处理监控信息
            Object monitorIdObj = paramMap.get("monitorId");
            if(monitorIdObj!=null&&StringUtils.isNotBlank(monitorIdObj.toString())){
                String monitorIdStr=monitorIdObj.toString();
                if(monitorIdStr.equals("0")){
                    monitorDeviceService.deleteByDeviceId(deviceId);
                    deviceDao.updateVideoUrlNull(deviceId);
                }else{
                    Long monitorId=Long.parseLong(monitorIdStr);
                    Monitor monitor = monitorService.findById(monitorId);
                    String hlsHd = monitor.getHlsHd();
                    MonitorDevice deviceMonitorHls = monitorDeviceService.getDeviceMonitorHls(deviceId);
                    if(deviceMonitorHls==null) {
                        device.setVideoUrl(hlsHd);
                        MonitorDevice monitorDevice=new MonitorDevice();
                        monitorDevice.setDeviceId(deviceId);
                        monitorDevice.setMonitorId(monitorId);
                        monitorDevice.setHlsHd(hlsHd);
                        monitorDevice.setCreateTime(date);
                        monitorDevice.setUpdateTime(date);
                        monitorDeviceService.insert(monitorDevice);
                    }else{
                        if(!deviceMonitorHls.getMonitorId().equals(monitorId)){
                            monitorDeviceService.deleteByDeviceId(deviceId);
                            device.setVideoUrl(hlsHd);
                            MonitorDevice monitorDevice=new MonitorDevice();
                            monitorDevice.setDeviceId(deviceId);
                            monitorDevice.setMonitorId(monitorId);
                            monitorDevice.setHlsHd(hlsHd);
                            monitorDevice.setCreateTime(date);
                            monitorDevice.setUpdateTime(date);
                        }
                    }

                }
                deviceChangeFlag=true;
            }
            //处理位置信息
            Object locationFlagObj = paramMap.get("locationFlag");
            Boolean areaAllFlag=false;
            Boolean areaFlag=false;
            Boolean locationFlagF=false;
            if(locationFlagObj!=null){
                deviceChangeFlag=true;
                if(locationFlagObj.toString().equals("1")){
                    Object locationObj = paramMap.get("location");
                    device.setLocation(locationObj.toString());
                    areaAllFlag=true;
                    device.setAreaId(0L);
                    device.setBuildingId(0L);
                    device.setStoreyId(0L);
                    device.setRoomId(0L);
                }else{
                    device.setLocation(null);
                    locationFlagF=true;
                    Object areaObj = paramMap.get("areaId");
                    if(areaObj!=null&&StringUtils.isNotBlank(areaObj.toString())){
                        device.setAreaId(Long.parseLong(areaObj.toString()));
                        if(areaObj.toString().equals("0")){
                            areaFlag=true;
                        }
                    }
                    Object buildingObj = paramMap.get("buildingId");
                    if(buildingObj!=null&&StringUtils.isNotBlank(buildingObj.toString())){
                        device.setBuildingId(Long.parseLong(buildingObj.toString()));
                    }
                    Object storeyObj = paramMap.get("storeyId");
                    if(storeyObj!=null&&StringUtils.isNotBlank(storeyObj.toString())){
                        device.setStoreyId(Long.parseLong(storeyObj.toString()));
                    }
                    Object roomObj = paramMap.get("roomId");
                    if(roomObj!=null&&StringUtils.isNotBlank(roomObj.toString())){
                        device.setRoomId(Long.parseLong(roomObj.toString()));
                    }
                }
            }
            //更新设备基本信息
            if(deviceChangeFlag){
                device.setUpdateTime(new Date());
                //更新设备的参数
                deviceDao.updateByPrimaryKeySelective(device);
                if(areaAllFlag){
                    deviceDao.updateAreaAllNull(deviceId);
                }
                if(locationFlagF){
                    deviceDao.updateLocationNull(deviceId);
                }
                if(areaFlag){
                    deviceDao.updateAreaNull(deviceId);
                }

            }
            //处理设备系统信息
            Object systemObj = paramMap.get("systemIds");
            Set<Long> deviceSystemNewList=new HashSet<>();
            if(systemObj!=null){
                String[] split = systemObj.toString().split(",");
                for (String systemNewId : split) {
                    deviceSystemNewList.add(Long.parseLong(systemNewId));
                }
                List<AlarmItem> alarmItemList=alarmItemService.selectAll();
                Map<Long,String> alarmItemIdNameMap=new HashMap<>();
                for (AlarmItem alarmItem : alarmItemList) {
                    alarmItemIdNameMap.put(alarmItem.getId(),alarmItem.getName());
                }
                List<DeviceSystem> deviceSystems = deviceSystemService.selectByDeviceId(deviceId);
                if(!CollectionUtils.isEmpty(deviceSystems)){
                    List<DeviceSystem> deviceSystemList=new ArrayList<>();
                    Set<Long> deviceSystemIdList=new HashSet<>();
                    for (DeviceSystem deviceSystem : deviceSystems) {
                        deviceSystemIdList.add(deviceSystem.getSystemId());
                    }
                    List<Long> newIdList=new ArrayList<>();
                    if(deviceSystemNewList.size()==0){
                        deviceSystemService.deleteByDeviceId(deviceId);
                        //移除旧的设备系统关系
                        removeDeviceSystem(deviceId, projectId, deviceSystemIdList);
                    }else{
                        getDeviceSystemList(deviceSystemNewList,deviceSystemIdList,deviceSys,deviceSystemList,deviceNameObj==null?deviceSys.getName():deviceNameObj.toString(),newIdList);
                        //插入新的设备系统关系和相关预警配置项
                        if(!CollectionUtils.isEmpty(deviceSystemList)){
                            deviceSystemService.insertList(deviceSystemList);
                            List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceSys.getDeviceTypeId(), newIdList);
                            if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                                List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(deviceTypeAlarmList,deviceSys,date,alarmItemIdNameMap);
                                if(!CollectionUtils.isEmpty(alarmConfigList)){
                                    alarmConfigService.insertListRecord(alarmConfigList);
                                }
                            }
                        }
                        removeDeviceSystem(deviceId, projectId, deviceSystemIdList);
                    }
                    if(nameFlag){
                        deviceSystemService.updateDeviceNameByDeviceId(deviceId,deviceNameObj.toString());
                    }
                }else{
                    if(!CollectionUtils.isEmpty(deviceSystemNewList)){
                        List<DeviceSystem> deviceSystemList=new ArrayList<>();
                        List<Long> newIdList=new ArrayList<>();
                        newIdList.addAll(deviceSystemNewList);
                        for (Long systemId : deviceSystemNewList) {
                            DeviceSystem deviceSystem=new DeviceSystem();
                            deviceSystem.setCreateTime(date);
                            deviceSystem.setUpdateTime(date);
                            deviceSystem.setSystemId(systemId);
                            deviceSystem.setSerialNum(deviceSys.getSerialNum());
                            deviceSystem.setProjectId(deviceSys.getProjectId());
                            if(nameFlag){
                                deviceSystem.setName(deviceNameObj.toString());
                            }else{
                                deviceSystem.setName(deviceSys.getName());
                            }
                            deviceSystem.setDeviceId(deviceId);
                            deviceSystemList.add(deviceSystem);
                        }
                        deviceSystemService.insertList(deviceSystemList);
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceSys.getDeviceTypeId(), newIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(deviceTypeAlarmList,deviceSys,date,alarmItemIdNameMap);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                }
            }

            //更新设备参数信息
            if(deviceTypeId.equals(1L)||deviceTypeId.equals(3L)||deviceTypeId.equals(2L)||deviceTypeId.equals(4L)){
                DeviceParamsSafeElec variableSafeParams = getVariableSafeParams(paramMap,date);
                if(variableSafeParams!=null){
                    variableSafeParams.setDeviceId(deviceId);
                    deviceParamsSafeElecService.updateVariableParams(variableSafeParams);
                    variableSafeParams.setSerialNum(deviceSys.getSerialNum());
                    Boolean flag=safeDeviceRemoteService.sendDeviceParam(variableSafeParams);
                    if(!flag){
                        throw  new Exception("更新设备参数失败");
                    }
                }
            }else if(deviceTypeId.equals(5L)||deviceTypeId.equals(6L)){
                DeviceParamsSmartElec variableSmartParams = getVariableSmartParams(paramMap,date);
                if(variableSmartParams!=null){
                    variableSmartParams.setDeviceId(deviceId);
                    deviceParamsSmartElecService.updateVariableParams(variableSmartParams);
                    if(deviceTypeId.equals(5l)){
                        mt300CDeviceRemoteService.setDeviceParameter(deviceSys.getSerialNum(),variableSmartParams);
                    }else{
                        mt300DeviceRemoteService.setDeviceParameter(deviceSys.getSerialNum(),variableSmartParams);
                    }
                }
            }else if(deviceTypeId.equals(7L)){
                DeviceParamsSmoke variableParamsSmoke=getVariableSmokeParams(paramMap,date);
                if(variableParamsSmoke!=null){
                    variableParamsSmoke.setDeviceId(deviceId);
                    deviceParamSmokeService.updateVariableParams(variableParamsSmoke);
                }
            }else if(deviceTypeId.equals(8l)){
                DeviceParamsWater variableParamsWater=getVariableWaterParams(paramMap,date);
                if(variableParamsWater!=null){
                    variableParamsWater.setDeviceId(deviceId);
                    deviceParamsWaterService.updateVariableParams(variableParamsWater);
                }
            }else if(deviceTypeId.equals(9l)){
                DeviceParamsDoor deviceParamsDoor=getVariableDoorParam(paramMap,date);
                if(deviceParamsDoor!=null){
                    deviceParamsDoor.setDeviceId(deviceId);
                    deviceParamsDoorService.updateVariableParams(deviceParamsDoor);
                }
            }else if(deviceTypeId.equals(10l)){
                DeviceParamsWaveElec deviceParamsWaveElec=getVariableWaveElec(paramMap,date);
                if(deviceParamsWaveElec!=null){
                    deviceParamsWaveElec.setDeviceId(deviceId);
                    deviceParamsWaveElecService.updateVariableParams(deviceParamsWaveElec);
                }
            }else if(deviceTypeId.equals(11l)){
                DeviceParamsCompensateElec deviceParamsCompensateElec=getVariableCompensateElec(paramMap,date);
                if(deviceParamsCompensateElec!=null){
                    deviceParamsCompensateElec.setDeviceId(deviceId);
                    deviceParamsCompensateElecService.updateVariableParams(deviceParamsCompensateElec);
                }
            }else if(deviceTypeId.equals(12l)){
                DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity=getVariableTemperatureElec(paramMap,date);
                if(deviceParamsTemperaturehumidity!=null){
                    deviceParamsTemperaturehumidity.setDeviceId(deviceId);
                    paramsTemperaturehumidityService.updateVariableParams(deviceParamsTemperaturehumidity);
                }
            }else if(deviceTypeId.equals(13l)){
                DeviceParamSmartSuper deviceParamSmartSuper=getVariableSmartSuper(paramMap,date);
                if(deviceParamSmartSuper!=null){
                    deviceParamSmartSuper.setDeviceId(deviceId);
                    deviceParamSmartSuperService.updateVariableParams(deviceParamSmartSuper);
                    //发送远程命令
                    mt300SDeviceRemoteService.setDeviceParameter(deviceSys.getSerialNum(),deviceParamSmartSuper);
                }
            }
            return true;
        }else{
            return  false;
        }
    }

    private DeviceParamSmartSuper getVariableSmartSuper(Map<String, Object> paramMap, Date date) {
        Boolean flag = false;
        Object reportTime = paramMap.get("reportTime");
        Object currCT = paramMap.get("currCT");
        Object ctRatioHighVolt = paramMap.get("ctRatioHighVolt");

        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");


        Object leakCT = paramMap.get("leakCT");
        Object overLeaked = paramMap.get("overLeaked");
        Object overTemper = paramMap.get("overTemper");
        Object currentUnbalance = paramMap.get("currentUnbalance");
        Object voltageUnbalance = paramMap.get("voltageUnbalance");


        DeviceParamSmartSuper deviceParamSmartSuper = new DeviceParamSmartSuper();

        if(reportTime!=null&&StringUtils.isNotBlank(reportTime.toString())){
            deviceParamSmartSuper.setReportTime(reportTime.toString());
            flag=true;
        }
        if (currCT != null && StringUtils.isNotBlank(currCT.toString())) {
            deviceParamSmartSuper.setCtRatioA(currCT.toString());
            deviceParamSmartSuper.setCtRatioB(currCT.toString());
            deviceParamSmartSuper.setCtRatioC(currCT.toString());
            flag = true;
        }
        if (powerFactorA != null && StringUtils.isNotBlank(powerFactorA.toString())) {
            deviceParamSmartSuper.setPowerFactorA(powerFactorA.toString());
            flag = true;
        }
        if (powerFactorB != null && StringUtils.isNotBlank(powerFactorB.toString())) {
            deviceParamSmartSuper.setPowerFactorB(powerFactorB.toString());
            flag = true;
        }
        if (powerFactorC != null && StringUtils.isNotBlank(powerFactorC.toString())) {
            deviceParamSmartSuper.setPowerFactorC(powerFactorC.toString());
            flag = true;
        }
        if (overVoltageA != null && StringUtils.isNotBlank(overVoltageA.toString())) {
            deviceParamSmartSuper.setOverVoltageA(overVoltageA.toString());
            flag = true;
        }
        if (overVoltageB != null && StringUtils.isNotBlank(overVoltageB.toString())) {
            deviceParamSmartSuper.setOverVoltageB(overVoltageB.toString());
            flag = true;
        }
        if (overVoltageC != null && StringUtils.isNotBlank(overVoltageC.toString())) {
            deviceParamSmartSuper.setOverVoltageC(overVoltageC.toString());
            flag = true;
        }
        if (underVoltageA != null && StringUtils.isNotBlank(underVoltageA.toString())) {
            deviceParamSmartSuper.setUnderVoltageA(underVoltageA.toString());
            flag = true;
        }
        if (underVoltageB != null && StringUtils.isNotBlank(underVoltageB.toString())) {
            deviceParamSmartSuper.setUnderVoltageB(underVoltageB.toString());
            flag = true;
        }
        if (underVoltageC != null && StringUtils.isNotBlank(underVoltageC.toString())) {
            deviceParamSmartSuper.setUnderVoltageC(underVoltageC.toString());
            flag = true;
        }

        if (lackPhaseA != null && StringUtils.isNotBlank(lackPhaseA.toString())) {
            deviceParamSmartSuper.setLackPhaseA(lackPhaseA.toString());
            flag = true;
        }
        if (lackPhaseB != null && StringUtils.isNotBlank(lackPhaseB.toString())) {
            deviceParamSmartSuper.setLackPhaseB(lackPhaseB.toString());
            flag = true;
        }
        if (lackPhaseC != null && StringUtils.isNotBlank(lackPhaseC.toString())) {
            deviceParamSmartSuper.setLackPhaseC(lackPhaseC.toString());
            flag = true;
        }
        if (overCurrentA != null && StringUtils.isNotBlank(overCurrentA.toString())) {
            deviceParamSmartSuper.setOverCurrentA(overCurrentA.toString());
            flag = true;
        }
        if (overCurrentB != null && StringUtils.isNotBlank(overCurrentB.toString())) {
            deviceParamSmartSuper.setOverCurrentB(overCurrentB.toString());
            flag = true;
        }
        if (overCurrentC != null && StringUtils.isNotBlank(overCurrentC.toString())) {
            deviceParamSmartSuper.setOverCurrentC(overCurrentC.toString());
            flag = true;
        }
        if (currentThdA != null && StringUtils.isNotBlank(currentThdA.toString())) {
            deviceParamSmartSuper.setCurrentThdA(currentThdA.toString());
            flag = true;
        }
        if (currentThdB != null && StringUtils.isNotBlank(currentThdB.toString())) {
            deviceParamSmartSuper.setCurrentThdB(currentThdB.toString());
            flag = true;
        }
        if (currentThdC != null && StringUtils.isNotBlank(currentThdC.toString())) {
            deviceParamSmartSuper.setCurrentThdC(currentThdC.toString());
            flag = true;
        }
        if (voltageThdA != null && StringUtils.isNotBlank(voltageThdA.toString())) {
            deviceParamSmartSuper.setVoltageThdA(voltageThdA.toString());
            flag = true;
        }
        if (voltageThdB != null && StringUtils.isNotBlank(voltageThdB.toString())) {
            deviceParamSmartSuper.setVoltageThdB(voltageThdB.toString());
            flag = true;
        }
        if (voltageThdC != null && StringUtils.isNotBlank(voltageThdC.toString())) {
            deviceParamSmartSuper.setVoltageThdC(voltageThdC.toString());
            flag = true;
        }
        if (leakCT != null && StringUtils.isNotBlank(leakCT.toString())) {
            deviceParamSmartSuper.setCtRatioD(leakCT.toString());
            flag = true;
        }
        if (overLeaked != null && StringUtils.isNotBlank(overLeaked.toString())) {
            deviceParamSmartSuper.setOverLeaked(overLeaked.toString());
            flag = true;
        }
        if (overTemper != null && StringUtils.isNotBlank(overTemper.toString())) {
            deviceParamSmartSuper.setOverTemper(overTemper.toString());
            flag = true;
        }
        if(ctRatioHighVolt!=null && StringUtils.isNotBlank(ctRatioHighVolt.toString())){
            deviceParamSmartSuper.setCtRatioHighVolt(ctRatioHighVolt.toString());
            flag=true;
        }
        if(currentUnbalance!=null && StringUtils.isNotBlank(currentUnbalance.toString())){
            deviceParamSmartSuper.setCurrentUnbalance(currentUnbalance.toString());
            flag=true;
        }
        if(voltageUnbalance!=null && StringUtils.isNotBlank(voltageUnbalance.toString())){
            deviceParamSmartSuper.setVoltageUnbalance(voltageUnbalance.toString());
            flag=true;
        }

        if(flag){
            deviceParamSmartSuper.setUpdateTime(date);
            return deviceParamSmartSuper;
        }
        return null;
    }

    private DeviceParamsDoor getVariableDoorParam(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsDoor deviceParamsDoor=new DeviceParamsDoor();
        Object interval = paramMap.get("interval");
        if(interval!=null && StringUtils.isNotBlank(interval.toString())){
            deviceParamsDoor.setInterval(interval.toString());
            flag=true;
        }
        Object callSwitch  = paramMap.get("callSwitch");
        if(callSwitch!=null && StringUtils.isNotBlank(callSwitch.toString())){
            deviceParamsDoor.setCallSwitch(callSwitch.toString());
            flag=true;
        }
        if(flag){
            deviceParamsDoor.setUpdateTime(date);
            return deviceParamsDoor;
        }else{
            return null;
        }

    }

    private DeviceParamsTemperaturehumidity getVariableTemperatureElec(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsTemperaturehumidity deviceParamsTemperaturehumidity=new DeviceParamsTemperaturehumidity();
        Object interval = paramMap.get("interval");
        if(interval!=null&&StringUtils.isNotBlank(interval.toString())){
            deviceParamsTemperaturehumidity.setInterval(interval.toString());
            flag=true;
        }
        if(flag){
            deviceParamsTemperaturehumidity.setUpdateTime(date);
            return deviceParamsTemperaturehumidity;
        }else{
            return null;
        }

    }

    private DeviceParamsCompensateElec getVariableCompensateElec(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsCompensateElec deviceParamsCompensateElec=new DeviceParamsCompensateElec();
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object reportInterval = paramMap.get("reportInterval");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");
        if(transCapacity!=null&&StringUtils.isNotBlank(transCapacity.toString())){
            deviceParamsCompensateElec.setTransCapacity(transCapacity.toString());
            flag=true;
        }
        if(transLoad!=null&&StringUtils.isNotBlank(transLoad.toString())){
            deviceParamsCompensateElec.setTransLoad(transLoad.toString());
            flag=true;
        }
        if(currCT!=null&&StringUtils.isNotBlank(currCT.toString())){
            deviceParamsCompensateElec.setCtRatioA(currCT.toString());
            deviceParamsCompensateElec.setCtRatioB(currCT.toString());
            deviceParamsCompensateElec.setCtRatioC(currCT.toString());
            flag=true;
        }
        if(reportInterval!=null&&StringUtils.isNotBlank(reportInterval.toString())){
            deviceParamsCompensateElec.setReportInterval(reportInterval.toString());
            flag=true;
        }
        if(powerFactorA!=null&&StringUtils.isNotBlank(powerFactorA.toString())){
            deviceParamsCompensateElec.setPowerFactorA(powerFactorA.toString());
            flag=true;
        }
        if(powerFactorB!=null&&StringUtils.isNotBlank(powerFactorB.toString())){
            deviceParamsCompensateElec.setPowerFactorB(powerFactorB.toString());
            flag=true;
        }
        if(powerFactorC!=null&&StringUtils.isNotBlank(powerFactorC.toString())){
            deviceParamsCompensateElec.setPowerFactorC(powerFactorC.toString());
            flag=true;
        }
        if(overVoltageA!=null&&StringUtils.isNotBlank(overVoltageA.toString())){
            deviceParamsCompensateElec.setOverVoltageA(overVoltageA.toString());
            flag=true;
        }
        if(overVoltageB!=null&&StringUtils.isNotBlank(overVoltageB.toString())){
            deviceParamsCompensateElec.setOverVoltageB(overVoltageB.toString());
            flag=true;
        }
        if(overVoltageC!=null&&StringUtils.isNotBlank(overVoltageC.toString())){
            deviceParamsCompensateElec.setOverVoltageC(overVoltageC.toString());
            flag=true;
        }
        if(underVoltageA!=null&&StringUtils.isNotBlank(underVoltageA.toString())){
            deviceParamsCompensateElec.setUnderVoltageA(underVoltageA.toString());
            flag=true;
        }
        if(underVoltageB!=null&&StringUtils.isNotBlank(underVoltageB.toString())){
            deviceParamsCompensateElec.setUnderVoltageB(underVoltageB.toString());
            flag=true;
        }
        if(underVoltageC!=null&&StringUtils.isNotBlank(underVoltageC.toString())){
            deviceParamsCompensateElec.setUnderVoltageC(underVoltageC.toString());
            flag=true;
        }
        if(lackPhaseA!=null&&StringUtils.isNotBlank(lackPhaseA.toString())){
            deviceParamsCompensateElec.setLackPhaseA(lackPhaseA.toString());
            flag=true;
        }
        if(lackPhaseB!=null&&StringUtils.isNotBlank(lackPhaseB.toString())){
            deviceParamsCompensateElec.setLackPhaseB(lackPhaseB.toString());
            flag=true;
        }
        if(lackPhaseC!=null&&StringUtils.isNotBlank(lackPhaseC.toString())){
            deviceParamsCompensateElec.setLackPhaseC(lackPhaseC.toString());
            flag=true;
        }
        if(overCurrentA!=null&&StringUtils.isNotBlank(overCurrentA.toString())){
            deviceParamsCompensateElec.setOverCurrentA(overCurrentA.toString());
            flag=true;
        }
        if(overCurrentB!=null&&StringUtils.isNotBlank(overCurrentB.toString())){
            deviceParamsCompensateElec.setOverCurrentB(overCurrentB.toString());
            flag=true;
        }
        if(overCurrentC!=null&&StringUtils.isNotBlank(overCurrentC.toString())){
            deviceParamsCompensateElec.setOverCurrentC(overCurrentC.toString());
            flag=true;
        }
        if(currentThdA!=null&&StringUtils.isNotBlank(currentThdA.toString())){
            deviceParamsCompensateElec.setCurrentThdA(currentThdA.toString());
            flag=true;
        }
        if(currentThdB!=null&&StringUtils.isNotBlank(currentThdB.toString())){
            deviceParamsCompensateElec.setCurrentThdB(currentThdB.toString());
            flag=true;
        }
        if(currentThdC!=null&&StringUtils.isNotBlank(currentThdC.toString())){
            deviceParamsCompensateElec.setCurrentThdC(currentThdC.toString());
            flag=true;
        }
        if(voltageThdA!=null&&StringUtils.isNotBlank(voltageThdA.toString())){
            deviceParamsCompensateElec.setVoltageThdA(voltageThdA.toString());
            flag=true;
        }
        if(voltageThdB!=null&&StringUtils.isNotBlank(voltageThdB.toString())){
            deviceParamsCompensateElec.setVoltageThdB(voltageThdB.toString());
            flag=true;
        }
        if(voltageThdC!=null&&StringUtils.isNotBlank(voltageThdC.toString())){
            deviceParamsCompensateElec.setVoltageThdC(voltageThdC.toString());
            flag=true;
        }
        if(flag){
            deviceParamsCompensateElec.setUpdateTime(date);
            return deviceParamsCompensateElec;
        }else{
            return  null;
        }
    }

    private DeviceParamsWaveElec getVariableWaveElec(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsWaveElec deviceParamsWaveElec=new DeviceParamsWaveElec();
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object reportInterval = paramMap.get("reportInterval");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");
        if(transCapacity!=null&&StringUtils.isNotBlank(transCapacity.toString())){
            deviceParamsWaveElec.setTransCapacity(transCapacity.toString());
            flag=true;
        }
        if(transLoad!=null&&StringUtils.isNotBlank(transLoad.toString())){
            deviceParamsWaveElec.setTransLoad(transLoad.toString());
            flag=true;
        }
        if(currCT!=null&&StringUtils.isNotBlank(currCT.toString())){
            deviceParamsWaveElec.setCtRatioA(currCT.toString());
            deviceParamsWaveElec.setCtRatioB(currCT.toString());
            deviceParamsWaveElec.setCtRatioC(currCT.toString());
            flag=true;
        }
        if(reportInterval!=null&&StringUtils.isNotBlank(reportInterval.toString())){
            deviceParamsWaveElec.setReportInterval(reportInterval.toString());
            flag=true;
        }
        if(powerFactorA!=null&&StringUtils.isNotBlank(powerFactorA.toString())){
            deviceParamsWaveElec.setPowerFactorA(powerFactorA.toString());
            flag=true;
        }
        if(powerFactorB!=null&&StringUtils.isNotBlank(powerFactorB.toString())){
            deviceParamsWaveElec.setPowerFactorB(powerFactorB.toString());
            flag=true;
        }
        if(powerFactorC!=null&&StringUtils.isNotBlank(powerFactorC.toString())){
            deviceParamsWaveElec.setPowerFactorC(powerFactorC.toString());
            flag=true;
        }
        if(overVoltageA!=null&&StringUtils.isNotBlank(overVoltageA.toString())){
            deviceParamsWaveElec.setOverVoltageA(overVoltageA.toString());
            flag=true;
        }
        if(overVoltageB!=null&&StringUtils.isNotBlank(overVoltageB.toString())){
            deviceParamsWaveElec.setOverVoltageB(overVoltageB.toString());
            flag=true;
        }
        if(overVoltageC!=null&&StringUtils.isNotBlank(overVoltageC.toString())){
            deviceParamsWaveElec.setOverVoltageC(overVoltageC.toString());
            flag=true;
        }
        if(underVoltageA!=null&&StringUtils.isNotBlank(underVoltageA.toString())){
            deviceParamsWaveElec.setUnderVoltageA(underVoltageA.toString());
            flag=true;
        }
        if(underVoltageB!=null&&StringUtils.isNotBlank(underVoltageB.toString())){
            deviceParamsWaveElec.setUnderVoltageB(underVoltageB.toString());
            flag=true;
        }
        if(underVoltageC!=null&&StringUtils.isNotBlank(underVoltageC.toString())){
            deviceParamsWaveElec.setUnderVoltageC(underVoltageC.toString());
            flag=true;
        }
        if(lackPhaseA!=null&&StringUtils.isNotBlank(lackPhaseA.toString())){
            deviceParamsWaveElec.setLackPhaseA(lackPhaseA.toString());
            flag=true;
        }
        if(lackPhaseB!=null&&StringUtils.isNotBlank(lackPhaseB.toString())){
            deviceParamsWaveElec.setLackPhaseB(lackPhaseB.toString());
            flag=true;
        }
        if(lackPhaseC!=null&&StringUtils.isNotBlank(lackPhaseC.toString())){
            deviceParamsWaveElec.setLackPhaseC(lackPhaseC.toString());
            flag=true;
        }
        if(overCurrentA!=null&&StringUtils.isNotBlank(overCurrentA.toString())){
            deviceParamsWaveElec.setOverCurrentA(overCurrentA.toString());
            flag=true;
        }
        if(overCurrentB!=null&&StringUtils.isNotBlank(overCurrentB.toString())){
            deviceParamsWaveElec.setOverCurrentB(overCurrentB.toString());
            flag=true;
        }
        if(overCurrentC!=null&&StringUtils.isNotBlank(overCurrentC.toString())){
            deviceParamsWaveElec.setOverCurrentC(overCurrentC.toString());
            flag=true;
        }
        if(currentThdA!=null&&StringUtils.isNotBlank(currentThdA.toString())){
            deviceParamsWaveElec.setCurrentThdA(currentThdA.toString());
            flag=true;
        }
        if(currentThdB!=null&&StringUtils.isNotBlank(currentThdB.toString())){
            deviceParamsWaveElec.setCurrentThdB(currentThdB.toString());
            flag=true;
        }
        if(currentThdC!=null&&StringUtils.isNotBlank(currentThdC.toString())){
            deviceParamsWaveElec.setCurrentThdC(currentThdC.toString());
            flag=true;
        }
        if(voltageThdA!=null&&StringUtils.isNotBlank(voltageThdA.toString())){
            deviceParamsWaveElec.setVoltageThdA(voltageThdA.toString());
            flag=true;
        }
        if(voltageThdB!=null&&StringUtils.isNotBlank(voltageThdB.toString())){
            deviceParamsWaveElec.setVoltageThdB(voltageThdB.toString());
            flag=true;
        }
        if(voltageThdC!=null&&StringUtils.isNotBlank(voltageThdC.toString())){
            deviceParamsWaveElec.setVoltageThdC(voltageThdC.toString());
            flag=true;
        }
        if(flag){
            deviceParamsWaveElec.setUpdateTime(date);
            return deviceParamsWaveElec;
        }else{
            return  null;
        }

    }

    private DeviceParamsWater getVariableWaterParams(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsWater deviceParamsWater=new DeviceParamsWater();
        Object discharge = paramMap.get("discharge");
        Object pressure = paramMap.get("pressure");
        Object interval = paramMap.get("interval");
        if(discharge!=null&&StringUtils.isNotBlank(discharge.toString())){
            deviceParamsWater.setDischarge(discharge.toString());
            flag=true;
        }
        if(interval!=null&&StringUtils.isNotBlank(interval.toString())){
            deviceParamsWater.setInterval(interval.toString());
            flag=true;
        }
        if(pressure!=null&&StringUtils.isNotBlank(pressure.toString())){
            deviceParamsWater.setPressure(pressure.toString());
            flag=true;
        }
        if(flag){
            deviceParamsWater.setUpdateTime(date);
            return deviceParamsWater;
        }else{
            return null;
        }
    }

    private DeviceParamsSmoke getVariableSmokeParams(Map<String, Object> paramMap, Date date) {
        Boolean flag=false;
        DeviceParamsSmoke deviceParamsSmoke=new DeviceParamsSmoke();
        Object smokeConcentration = paramMap.get("smokeConcentration");
        if(smokeConcentration!=null && StringUtils.isNotBlank(smokeConcentration.toString())) {
            deviceParamsSmoke.setSmokeConcentration(smokeConcentration.toString());
            flag=true;
        }
        Object interval = paramMap.get("interval");
        if(interval!=null && StringUtils.isNotBlank(interval.toString())) {
            deviceParamsSmoke.setInterval(interval.toString());
            flag=true;
        }
        Object electricityQuantity = paramMap.get("electricityQuantity");
        if(electricityQuantity!=null && StringUtils.isNotBlank(electricityQuantity.toString())){
            deviceParamsSmoke.setElectricityQuantity(electricityQuantity.toString());
            flag=true;
        }
       if(flag){
           deviceParamsSmoke.setUpdateTime(date);
           return deviceParamsSmoke;
       }else{
           return null;
       }
    }

    private DeviceParamsSmartElec getVariableSmartParams(Map<String, Object> paramMap, Date date) {
        Boolean flag = false;
        Object reportInterval = paramMap.get("reportInterval");
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");


        Object leakCT = paramMap.get("leakCT");
        Object overLeaked = paramMap.get("overLeaked");
        Object overTemper = paramMap.get("overTemper");
        DeviceParamsSmartElec deviceParamsSmartElec = new DeviceParamsSmartElec();

        if(reportInterval!=null&&StringUtils.isNotBlank(reportInterval.toString())){
            deviceParamsSmartElec.setReportInterval(reportInterval.toString());
            flag=true;
        }

        if (transCapacity != null && StringUtils.isNotBlank(transCapacity.toString())) {
            deviceParamsSmartElec.setTransCapacity(transCapacity.toString());
            flag = true;
        }
        if (transLoad != null && StringUtils.isNotBlank(transLoad.toString())) {
            deviceParamsSmartElec.setTransLoad(transLoad.toString());
            flag = true;
        }
        if (currCT != null && StringUtils.isNotBlank(currCT.toString())) {
            deviceParamsSmartElec.setCtRatioA(currCT.toString());
            deviceParamsSmartElec.setCtRatioB(currCT.toString());
            deviceParamsSmartElec.setCtRatioC(currCT.toString());
            flag = true;
        }
        if (powerFactorA != null && StringUtils.isNotBlank(powerFactorA.toString())) {
            deviceParamsSmartElec.setPowerFactorA(powerFactorA.toString());
            flag = true;
        }
        if (powerFactorB != null && StringUtils.isNotBlank(powerFactorB.toString())) {
            deviceParamsSmartElec.setPowerFactorB(powerFactorB.toString());
            flag = true;
        }
        if (powerFactorC != null && StringUtils.isNotBlank(powerFactorC.toString())) {
            deviceParamsSmartElec.setPowerFactorC(powerFactorC.toString());
            flag = true;
        }
        if (overVoltageA != null && StringUtils.isNotBlank(overVoltageA.toString())) {
            deviceParamsSmartElec.setOverVoltageA(overVoltageA.toString());
            flag = true;
        }
        if (overVoltageB != null && StringUtils.isNotBlank(overVoltageB.toString())) {
            deviceParamsSmartElec.setOverVoltageB(overVoltageB.toString());
            flag = true;
        }
        if (overVoltageC != null && StringUtils.isNotBlank(overVoltageC.toString())) {
            deviceParamsSmartElec.setOverVoltageC(overVoltageC.toString());
            flag = true;
        }
        if (underVoltageA != null && StringUtils.isNotBlank(underVoltageA.toString())) {
            deviceParamsSmartElec.setUnderVoltageA(underVoltageA.toString());
            flag = true;
        }
        if (underVoltageB != null && StringUtils.isNotBlank(underVoltageB.toString())) {
            deviceParamsSmartElec.setUnderVoltageB(underVoltageB.toString());
            flag = true;
        }
        if (underVoltageC != null && StringUtils.isNotBlank(underVoltageC.toString())) {
            deviceParamsSmartElec.setUnderVoltageC(underVoltageC.toString());
            flag = true;
        }

        if (lackPhaseA != null && StringUtils.isNotBlank(lackPhaseA.toString())) {
            deviceParamsSmartElec.setLackPhaseA(lackPhaseA.toString());
            flag = true;
        }
        if (lackPhaseB != null && StringUtils.isNotBlank(lackPhaseB.toString())) {
            deviceParamsSmartElec.setLackPhaseB(lackPhaseB.toString());
            flag = true;
        }
        if (lackPhaseC != null && StringUtils.isNotBlank(lackPhaseC.toString())) {
            deviceParamsSmartElec.setLackPhaseC(lackPhaseC.toString());
            flag = true;
        }
        if (overCurrentA != null && StringUtils.isNotBlank(overCurrentA.toString())) {
            deviceParamsSmartElec.setOverCurrentA(overCurrentA.toString());
            flag = true;
        }
        if (overCurrentB != null && StringUtils.isNotBlank(overCurrentB.toString())) {
            deviceParamsSmartElec.setOverCurrentB(overCurrentB.toString());
            flag = true;
        }
        if (overCurrentC != null && StringUtils.isNotBlank(overCurrentC.toString())) {
            deviceParamsSmartElec.setOverCurrentC(overCurrentC.toString());
            flag = true;
        }
        if (currentThdA != null && StringUtils.isNotBlank(currentThdA.toString())) {
            deviceParamsSmartElec.setCurrentThdA(currentThdA.toString());
            flag = true;
        }
        if (currentThdB != null && StringUtils.isNotBlank(currentThdB.toString())) {
            deviceParamsSmartElec.setCurrentThdB(currentThdB.toString());
            flag = true;
        }
        if (currentThdC != null && StringUtils.isNotBlank(currentThdC.toString())) {
            deviceParamsSmartElec.setCurrentThdC(currentThdC.toString());
            flag = true;
        }
        if (voltageThdA != null && StringUtils.isNotBlank(voltageThdA.toString())) {
            deviceParamsSmartElec.setVoltageThdA(voltageThdA.toString());
            flag = true;
        }
        if (voltageThdB != null && StringUtils.isNotBlank(voltageThdB.toString())) {
            deviceParamsSmartElec.setVoltageThdB(voltageThdB.toString());
            flag = true;
        }
        if (voltageThdC != null && StringUtils.isNotBlank(voltageThdC.toString())) {
            deviceParamsSmartElec.setVoltageThdC(voltageThdC.toString());
            flag = true;
        }
        if (leakCT != null && StringUtils.isNotBlank(leakCT.toString())) {
            deviceParamsSmartElec.setCtRatioD(leakCT.toString());
            flag = true;
        }
        if (overLeaked != null && StringUtils.isNotBlank(overLeaked.toString())) {
            deviceParamsSmartElec.setOverLeaked(overLeaked.toString());
            flag = true;
        }
        if (overTemper != null && StringUtils.isNotBlank(overTemper.toString())) {
            deviceParamsSmartElec.setOverTemper(overTemper.toString());
            flag = true;
        }
        if(flag){
            deviceParamsSmartElec.setUpdateTime(date);
            return deviceParamsSmartElec;
        }
        return null;
    }

    private DeviceParamsSafeElec getVariableSafeParams(Map<String,Object> paramMap,Date date){
        Boolean flag=false;
        Object voltHighObj = paramMap.get("voltHigh");
        Object voltLowObj = paramMap.get("voltLow");
        Object currHighObj = paramMap.get("currHigh");
        Object currLeakObj = paramMap.get("currLeak");
        Object tempHighObj = paramMap.get("tempHigh");
        Object intervalObj = paramMap.get("upInterval");
        DeviceParamsSafeElec deviceParamsSafeElec=new DeviceParamsSafeElec();
        if(voltHighObj!=null&&StringUtils.isNotBlank(voltHighObj.toString())){
            deviceParamsSafeElec.setVoltHigh(voltHighObj.toString());
            flag=true;
        }
        if(voltLowObj!=null&&StringUtils.isNotBlank(voltLowObj.toString())){
            deviceParamsSafeElec.setVoltLow(voltLowObj.toString());
            flag=true;
        }
        if(currHighObj!=null&&StringUtils.isNotBlank(currHighObj.toString())){
            deviceParamsSafeElec.setCurrHigh(currHighObj.toString());
            flag=true;
        }
        if(currLeakObj!=null&&StringUtils.isNotBlank(currLeakObj.toString())){
            deviceParamsSafeElec.setCurrLeak(currLeakObj.toString());
            flag=true;
        }
        if(tempHighObj!=null&&StringUtils.isNotBlank(tempHighObj.toString())){
            deviceParamsSafeElec.setTempHigh(tempHighObj.toString());
            flag=true;
        }
        if(intervalObj!=null&&StringUtils.isNotBlank(intervalObj.toString())){
            deviceParamsSafeElec.setUpInterval(intervalObj.toString());
            flag=true;
        }
        if(flag){
            deviceParamsSafeElec.setUpdateTime(date);
           return  deviceParamsSafeElec;
        }else{
            return null;
        }
    }

    private void removeDeviceSystem(Long deviceId, Long projectId, Set<Long> deviceSystemIdList) throws Exception {
        if(!CollectionUtils.isEmpty(deviceSystemIdList)){
            if(deviceSystemIdList.contains(1000L)){
                removeByDeviceSafeSystem(projectId,deviceId);
            }
            if(deviceSystemIdList.contains(2000L)){
                removeByDeviceSmartSystem(projectId,deviceId);
            }
            if(deviceSystemIdList.contains(3000L)){
                removeByDeviceEnergySystem(projectId,deviceId);
            }
        }
    }

    /**
     * 处理设备系统预警配置
     * @param deviceTypeAlarmList
     * @param device
     * @param date
     * @return
     */
    private List<AlarmConfig> handlerAlarmConfigSystem(List<DeviceTypeAlarm> deviceTypeAlarmList, Device device, Date date,Map<Long,String> alarmItemMap) {
        List<AlarmConfig> alarmConfigList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
            for (DeviceTypeAlarm deviceTypeAlarm : deviceTypeAlarmList) {
                Long systemId = deviceTypeAlarm.getSystemId();
                String alarmItemIds = deviceTypeAlarm.getAlarmItemIds();
                String[] split = alarmItemIds.split(",");
                for (String alarmItemIdStr : split) {
                    AlarmConfig alarmConfig=new AlarmConfig();
                    alarmConfig.setAlarmItemId(Long.parseLong(alarmItemIdStr));
                    alarmConfig.setSystemId(systemId);
                    alarmConfig.setCreateTime(date);
                    alarmConfig.setUpdateTime(date);
                    alarmConfig.setSerialNum(device.getSerialNum());
                    alarmConfig.setDeviceId(device.getId());
                    alarmConfig.setAlarmItemName(alarmItemMap.get(Long.parseLong(alarmItemIdStr)));
                    alarmConfig.setAlarmCount(0);
                    alarmConfigList.add(alarmConfig);
                }
            }

        }
        return  alarmConfigList;
    }

    private void  getDeviceSystemList(Set<Long> deviceSystemNewList, Set<Long> deviceSystemIdList, Device device, List<DeviceSystem> deviceSystemList,String deviceName,List<Long> newIdList ){
        Date date =new Date();
        List<Long> systemAllList=new ArrayList<>();
        systemAllList.add(1000L);
        systemAllList.add(2000L);
        systemAllList.add(3000L);
        systemAllList.add(4000L);
        for (Long systemId : systemAllList) {
            if(deviceSystemNewList.contains(systemId)){
                if(deviceSystemIdList.contains(systemId)){
                    deviceSystemIdList.remove(systemId);
                }else{
                    DeviceSystem deviceSystem=new DeviceSystem();
                    deviceSystem.setSystemId(systemId);
                    deviceSystem.setDeviceId(device.getId());
                    deviceSystem.setName(deviceName);
                    deviceSystem.setProjectId(device.getProjectId());
                    deviceSystem.setSerialNum(device.getSerialNum());
                    deviceSystem.setCreateTime(date);
                    deviceSystem.setUpdateTime(date);
                    newIdList.add(systemId);
                    deviceSystemList.add(deviceSystem);
                }
            }
        }

    }

    private void handlerExamplesData( List<Float> dataList,Float data){
        for (int i = 0; i < 10; i++) {
            dataList.add(data);
        }
    }

    private void handlerExamplesTimeData( List<Integer> dataList){
        dataList.add(5);
        dataList.add(65);
        dataList.add(125);
        dataList.add(185);
        dataList.add(245);
        dataList.add(305);
        dataList.add(365);
        dataList.add(425);
        dataList.add(485);
        dataList.add(545);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByDeviceSafeSystem(Long projectId, Long deviceId) throws Exception {
        deviceSystemService.removeDeviceSystem(projectId, deviceId, 1000L);
        //删除设备该系统下的预警信息
        alarmInfoService.removeDeviceSystem(projectId, deviceId, 1000L);
        //移除iot_alarm_config
        alarmConfigService.removeByDeviceSystem(deviceId, 1000L);
        //移除iot_alarm_warn
        alarmWarnService.removeByDeviceSystem(projectId, deviceId, 1000L);
        //移除iot_device_task
        deviceTaskService.removeByDeviceSystem(projectId, deviceId, 1000L);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addDeviceSystem(String serialNum, Long systemId) throws Exception {
        Device device = deviceDao.selectBySerialNum(serialNum);
        if(device!=null){
            Date date=new Date();
            DeviceSystem deviceSystem=new DeviceSystem();
            deviceSystem.setDeviceId(device.getId());
            deviceSystem.setName(device.getName());
            deviceSystem.setProjectId(device.getProjectId());
            deviceSystem.setSerialNum(device.getSerialNum());
            deviceSystem.setSystemId(systemId);
            deviceSystem.setCreateTime(date);
            deviceSystem.setUpdateTime(date);
            deviceSystemService.insertSelective(deviceSystem);
            List<Long> systemList=new ArrayList<>();
            systemList.add(systemId);
            List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(device.getDeviceTypeId(),systemList);
            if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                List<AlarmItem> alarmItemList=alarmItemService.selectAll();
                Map<Long,String> alarmMap=new HashMap<>();
                for (AlarmItem alarmItem : alarmItemList) {
                    alarmMap.put(alarmItem.getId(),alarmItem.getName());
                }
                List<AlarmConfig> alarmConfigList=new ArrayList<>();
                for (DeviceTypeAlarm deviceTypeAlarm : deviceTypeAlarmList) {
                    String alarmItemIds = deviceTypeAlarm.getAlarmItemIds();
                    String[] alarmItemIdArr = alarmItemIds.split(",");
                    for (int i = 0; i < alarmItemIdArr.length; i++) {
                        Long alarmItemId = Long.parseLong(alarmItemIdArr[i]);
                        String  alarmItemName=alarmMap.get(alarmItemId);
                        AlarmConfig alarmConfig=new AlarmConfig();
                        alarmConfig.setAlarmCount(0);
                        alarmConfig.setAlarmItemId(alarmItemId);
                        alarmConfig.setAlarmItemName(alarmItemName);
                        alarmConfig.setDeviceId(device.getId());
                        alarmConfig.setSerialNum(device.getSerialNum());
                        alarmConfig.setSystemId(systemId);
                        alarmConfig.setCreateTime(date);
                        alarmConfig.setUpdateTime(date);
                        alarmConfigList.add(alarmConfig);
                    }
                }
                if(!CollectionUtils.isEmpty(alarmConfigList)){
                    alarmConfigService.insertListRecord(alarmConfigList);
                }
            }
        }
        return true;
    }


    @Override
    public DeviceMaintenanceDetail getDeviceMaintenanceDetail(Long deviceId) {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if(device!=null){
            DeviceMaintenanceDetail deviceMaintenanceDetail=new DeviceMaintenanceDetail();
            deviceMaintenanceDetail.setDeviceId(device.getId());
            deviceMaintenanceDetail.setDeviceName(device.getName());
            deviceMaintenanceDetail.setErasure(device.getErasure());
            deviceMaintenanceDetail.setStatus(device.getStatus());
            deviceMaintenanceDetail.setProjectId(device.getProjectId());
            String location = device.getLocation();
            if(StringUtils.isNotBlank(location)){
                deviceMaintenanceDetail.setLocation(location);
            }else{
                StringBuffer stringBuffer=new StringBuffer();
                Area area = regionAreaService.selectByPrimaryKey(device.getAreaId());
                if(area!=null){
                    stringBuffer.append(area.getName());
                }
                Building building = regionBuildingService.selectByPrimaryKey(device.getBuildingId());
                if(building!=null){
                    stringBuffer.append(building.getName());
                }
                Storey storey = regionStoreyService.selectByPrimaryKey(device.getStoreyId());
                if(storey!=null){
                    stringBuffer.append(storey.getName());
                }
                Room room = regionRoomService.selectByPrimaryKey(device.getRoomId());
                if(room!=null){
                    stringBuffer.append(room.getName());
                }
                deviceMaintenanceDetail.setLocation(stringBuffer.toString());
            }
            return deviceMaintenanceDetail;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDevice(Long projectId, Long deviceId) throws Exception {
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        if(device!=null){
            Long deviceTypeId = device.getDeviceTypeId();
            //删除设备信息
            deviceDao.deleteByPrimaryKey(device.getId());
            //删除设备参数和数据
            if(deviceTypeId.equals(1L)||deviceTypeId.equals(2L)||deviceTypeId.equals(3L)||deviceTypeId.equals(4L)){
                deviceParamsSafeElecService.deleteByDeviceId(deviceId);
                deviceDataSafeElecService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(5L)||deviceTypeId.equals(6L)){
                deviceParamsSmartElecService.deleteByDeviceId(deviceId);
                deviceDataSmartElecService.deleteByDeviceId(deviceId);
                deviceMeasurementService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(7l)){
                deviceParamSmokeService.deleteByDeviceId(deviceId);
                deviceDataSmokeElecService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(8l)){
                deviceParamsWaterService.deleteByDeviceId(deviceId);
                deviceDataWaterElecService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(9l)){
                deviceParamsDoorService.deleteByDeviceId(deviceId);
                deviceDataDoorElecService.deleteByDeviceId(deviceId);
            }else if (deviceTypeId.equals(10l)){
                deviceParamsWaveElecService.deleteByDeviceId(deviceId);
                deviceDataWaveService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(11l)){
                deviceParamsCompensateElecService.deleteByDeviceId(deviceId);
                deviceDataCompensateService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(12l)){
                paramsTemperaturehumidityService.deleteByDeviceId(deviceId);
                dataTempatureHumidityService.deleteByDeviceId(deviceId);
            }else if(deviceTypeId.equals(13l)){
                deviceParamSmartSuperService.deleteByDeviceId(deviceId);
                deviceDataSmartSuperService.deleteByDeviceId(deviceId);
                deviceMeasurementService.deleteByDeviceId(deviceId);
            }
            //删除电房设备和能源设备的绑定关系
            Integer bindingStatus = device.getBindingStatus();
            if(bindingStatus.equals(1)){
                Integer bindingType = device.getBindingType();
                if(bindingType.equals(0)){
                    powerDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(1)){
                    powerIncomingDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(2)){
                    powerCompensateDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(3)){
                    powerWaveDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(4)){
                    powerFeederLoopDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(5)){
                    powerBoxLoopDeviceService.removeDevice(deviceId);
                }else if(bindingType.equals(6l)){
                    energyEquipmentDeviceService.removeDevice(deviceId);
                }
            }
            //删除设备预警信息
            alarmConfigService.removeByDeviceId(deviceId);
            alarmWarnService.removeByDeviceId(deviceId);
            alarmInfoService.removeByDeviceId(deviceId);
            deviceAlarmService.removeByDeviceId(deviceId);
            //删除监控绑定设备信息
            monitorDeviceService.deleteByDeviceId(deviceId);
            //删除设备的维保任务
            deviceTaskService.deleteByDeviceId(deviceId);
            //删除设备系统信息
            deviceSystemService.deleteByDeviceId(deviceId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByDeviceSmartSystem(Long projectId, Long deviceId) throws Exception {
        deviceSystemService.removeDeviceSystem(projectId, deviceId, 2000L);
        //删除设备该系统下的预警信息
        alarmInfoService.removeDeviceSystem(projectId, deviceId, 2000L);
        //移除iot_alarm_config
        alarmConfigService.removeByDeviceSystem(deviceId, 2000L);
        //移除iot_alarm_warn
        alarmWarnService.removeByDeviceSystem(projectId, deviceId, 2000L);
        //移除iot_device_task
        deviceTaskService.removeByDeviceSystem(projectId, deviceId, 2000L);
        //移除电房绑定关系
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        Integer bindingStatus = device.getBindingStatus();
        if(bindingStatus.equals(1L)){
            Integer bindingType = device.getBindingType();
            if(bindingType.equals(0L)){
                powerDeviceService.deleteByDeviceId(deviceId);
            }else if(bindingType.equals(1L)){
                powerIncomingDeviceService.deleteByDeviceId(deviceId);
            }else if(bindingType.equals(2L)){
                powerCompensateDeviceService.deleteByDeviceId(deviceId);
            }else if(bindingType.equals(3L)){
                powerWaveDeviceService.deleteByDeviceId(deviceId);
            }else if(bindingType.equals(4L)){
                powerFeederLoopDeviceService.removeByDeviceId(deviceId);
            }else if(bindingType.equals(5L)){
                powerBoxLoopDeviceService.removeByDeviceId(deviceId);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByDeviceEnergySystem(Long projectId, Long deviceId) throws Exception {
        deviceSystemService.removeDeviceSystem(projectId, deviceId, 3000L);
        //删除设备该系统下的预警信息
        alarmInfoService.removeDeviceSystem(projectId, deviceId, 3000L);
        //移除iot_alarm_config
        alarmConfigService.removeByDeviceSystem(deviceId, 3000L);
        //移除iot_alarm_warn
        alarmWarnService.removeByDeviceSystem(projectId, deviceId, 3000L);
        //移除iot_device_task
        deviceTaskService.removeByDeviceSystem(projectId, deviceId, 3000L);
        Device device = deviceDao.selectByPrimaryKey(deviceId);
        Integer bindingStatus = device.getBindingStatus();
        Integer bindingType = device.getBindingType();
        if(bindingStatus.equals(1L)){
            if(bindingType.equals(6L)){
                energyEquipmentDeviceService.deleteByDeviceId(deviceId);
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> saveDevice(Map<String, Object> paramMap) throws Exception{
        Result<String> result=new Result<>();
        result.setCode(0);
        Device device=new Device();
        logger.info("添加设备参数"+paramMap.toString());
        //验证设备基本信息
        String paramError = checkSaveDeviceParam(paramMap,device);
        if(StringUtils.isBlank(paramError)){
            Long deviceTypeId = device.getDeviceTypeId();
            Date date=new Date();
            List<AlarmItem> alarmItemList=alarmItemService.selectAll();
            Map<Long,String> alarmMap=new HashMap<>();
            for (AlarmItem alarmItem : alarmItemList) {
                alarmMap.put(alarmItem.getId(),alarmItem.getName());
            }
            //处理电气火灾
            if(deviceTypeId.equals(1l)||deviceTypeId.equals(3l)){
                DeviceParamsSafeElec  elec=new DeviceParamsSafeElec();
                String safeParamError = checkSafeDeviceParam(paramMap,elec,1);
                if(StringUtils.isBlank(safeParamError)){
                    //处理监控信息
                    MonitorDevice monitorDevice = handlerMonitor(paramMap, device, date);
                    //保存设备信息
                    saveDeviceExtParam(device,date);
                    device.setTransType(1);
                    device.setDeviceModel("A");
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存设备图片信息
                    //保存设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && !systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    //保存设备参数
                    elec.setVoltInterval("3");
                    elec.setCurrInterval("3");
                    elec.setLeakInterval("3");
                    elec.setTempInterval("3");
                    //默认关闭电压开关
                    elec.setAlarmEn("0000001111111100");
                    elec.setSerialNum(device.getSerialNum());
                    elec.setCreateTime(date);
                    elec.setUpdateTime(date);
                    elec.setReturnCode("100");
                    elec.setDeviceId(device.getId());
                    //发送设备参数给设备
                    Boolean flag=safeDeviceRemoteService.sendDeviceParam(elec);
                    if(flag){
                        deviceParamsSafeElecService.insertSelective(elec);
                    }
                    //保存设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(1l, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                   result.setData(safeParamError);
                   return  result;
                }
            //处理消防电源
            }else if(deviceTypeId.equals(2L)||deviceTypeId.equals(4L)){
                //处理消防电源
                DeviceParamsSafeElec  elec=new DeviceParamsSafeElec();
                String safeDeviceParam = checkSafeDeviceParam(paramMap, elec, 2);
                if(StringUtils.isBlank(safeDeviceParam)){
                    MonitorDevice monitorDevice = handlerMonitor(paramMap, device, date);
                    //保存设备信息
                    saveDeviceExtParam(device,date);
                    device.setTransType(1);
                    device.setDeviceModel("B");
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && !systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    //保存设备参数
                    elec.setVoltInterval("3");
                    elec.setCurrInterval("3");
                    elec.setLeakInterval("3");
                    elec.setTempInterval("3");
                    elec.setAlarmEn("0000001110000000");
                    elec.setSerialNum(device.getSerialNum());
                    elec.setCreateTime(date);
                    elec.setUpdateTime(date);
                    elec.setReturnCode("100");
                    elec.setDeviceId(device.getId());
                    //发送设备参数给设备
                    Boolean flag=safeDeviceRemoteService.sendDeviceParam(elec);
                    if(flag){
                        deviceParamsSafeElecService.insertSelective(elec);
                    }
                    //保存设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                    result.setData(safeDeviceParam);
                    return  result;
                }
            //处理625
            }else if(deviceTypeId.equals(5l)){
                DeviceParamsSmartElec smartElec=new DeviceParamsSmartElec();
                String smartInfo=checkSmartDeviceParam(paramMap,smartElec,5);
                if(StringUtils.isBlank(smartInfo)){
                    //保存EA625设备信息
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    saveDeviceExtParam(device,date);
                    device.setTransType(1);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存EA625设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0&& !systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    smartElec.setDeviceId(device.getId());
                    smartElec.setSerialNum(device.getSerialNum());
                    smartElec.setModbusAddress("1");
                    smartElec.setModbusBaudrate("9600");
                    smartElec.setPeakReportTime("7:00");
                    smartElec.setNormalReportTime("18:00");
                    smartElec.setValleyReportTime("23:00");
                    smartElec.setDidoSet("111");
                    smartElec.setSystemSw("1011111");
                    smartElec.setAlarmSw("1111111111111");
                    smartElec.setPeak("7:01-18:00");
                    smartElec.setNormal("18:01-23:00");
                    smartElec.setValley("23:01-7:00");
                    smartElec.setPeakPrice("120");
                    smartElec.setNormalPrice("100");
                    smartElec.setValleyPrice("80");
                    smartElec.setReturnCode("100");
                    smartElec.setCreateTime(date);
                    smartElec.setUpdateTime(date);
                    Boolean flag=mt300CDeviceRemoteService.setDeviceParameter(device.getSerialNum(),smartElec);
                    if(flag){
                        deviceParamsSmartElecService.insertSelective(smartElec);
                    }
                    //保存EA625设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                    result.setData(smartInfo);
                    return result;
                }
            //处理1128
            }else if(deviceTypeId.equals(6l)){
                DeviceParamsSmartElec smartElec=new DeviceParamsSmartElec();
                String smartInfo=checkSmartDeviceParam(paramMap,smartElec,6);
                if(StringUtils.isBlank(smartInfo)){
                    //保存EA1128设备信息
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    saveDeviceExtParam(device,date);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存EA1128设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && ! systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    smartElec.setDeviceId(device.getId());
                    smartElec.setSerialNum(device.getSerialNum());
                    smartElec.setModbusAddress("1");
                    smartElec.setModbusBaudrate("9600");
                    smartElec.setPeakReportTime("7:00");
                    smartElec.setNormalReportTime("18:00");
                    smartElec.setValleyReportTime("23:00");
                    smartElec.setDidoSet("111");
                    smartElec.setSystemSw("1011111");
                    smartElec.setAlarmSw("111111111111111111");
                    smartElec.setPeak("7:01-18:00");
                    smartElec.setNormal("18:01-23:00");
                    smartElec.setValley("23:01-7:00");
                    smartElec.setPeakPrice("120");
                    smartElec.setNormalPrice("100");
                    smartElec.setValleyPrice("80");
                    smartElec.setReturnCode("100");
                    smartElec.setCreateTime(date);
                    smartElec.setUpdateTime(date);
                    Boolean flag=mt300DeviceRemoteService.setDeviceParameter(device.getSerialNum(),smartElec);
                    if(flag){
                        deviceParamsSmartElecService.insertSelective(smartElec);
                    }
                    //保存EA1128设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return  result;
                }else{
                    result.setData(smartInfo);
                    return result;
                }
            }else if(deviceTypeId.equals(7l)){
                DeviceParamsSmoke deviceParamsSmoke=new DeviceParamsSmoke();
                String smokeParam=checkSmokeDevice(paramMap,deviceParamsSmoke);
                if(StringUtils.isBlank(smokeParam)){
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    //保存烟感设备信息
                    saveDeviceExtParam(device,date);
                    device.setSwitchStatus(0);
                    device.setTransType(1);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存烟感设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && !systemIdStr.equals("0") ){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    deviceParamsSmoke.setDeviceId(device.getId());
                    deviceParamsSmoke.setSerialNum(device.getSerialNum());
                    deviceParamsSmoke.setUpdateTime(date);
                    deviceParamsSmoke.setUpdateTime(date);
                    //保存设备烟感 参数信息
                    deviceParamSmokeService.insertSelective(deviceParamsSmoke);
                    //保存设备系统信息
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return  result;
                }else{
                    result.setData(smokeParam);
                    return result;
                }
            }else if(deviceTypeId.equals(8l)){
                DeviceParamsWater  deviceParamsWater=new DeviceParamsWater();
                String waterInfo=checkDeviceWaterParams(paramMap,deviceParamsWater);
                if(StringUtils.isBlank(waterInfo)){
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    //保存末端试水设备信息
                    saveDeviceExtParam(device,date);
                    device.setSwitchStatus(0);
                    device.setTransType(1);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存末端试水设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && !systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    //保存末端试水参数系统信息
                    deviceParamsWater.setDeviceId(device.getId());
                    deviceParamsWater.setSerialNum(device.getSerialNum());
                    deviceParamsWater.setUpdateTime(date);
                    deviceParamsWater.setCreateTime(date);
                    deviceParamsWaterService.insertSelective(deviceParamsWater);
                    //保存末端试水设备系统信息
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                    result.setData(waterInfo);
                    return result;
                }
            }else if(deviceTypeId.equals(9l)){
                MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                //保存防火门设备信息
                saveDeviceExtParam(device,date);
                device.setSwitchStatus(0);
                device.setTransType(1);
                deviceDao.insertSelective(device);
                if(monitorDevice!=null){
                    monitorDevice.setDeviceId(device.getId());
                    monitorDeviceService.insert(monitorDevice);
                }
                //保存防火门设备系统信息
                Object systemIdsObj = paramMap.get("systemIds");
                String[] split = systemIdsObj.toString().split(",");
                List<Long> systemIdList=new ArrayList<>();
                String systemIdStr=systemIdsObj.toString();
                if(split.length>0 && ! systemIdStr.equals("0")){
                    systemIdList= saveDeviceSystem(device,split,date);
                }
                //保存防火门参数信息
                Object interval = paramMap.get("interval");
                Object callSwitch = paramMap.get("callSwitch");
                DeviceParamsDoor deviceParamsDoor=new DeviceParamsDoor();
                deviceParamsDoor.setCallSwitch(callSwitch.toString());
                deviceParamsDoor.setDeviceId(device.getId());
                deviceParamsDoor.setInterval(interval.toString());
                deviceParamsDoor.setSerialNum(device.getSerialNum());
                deviceParamsDoor.setCreateTime(date);
                deviceParamsDoor.setUpdateTime(date);
                deviceParamsDoorService.insertSelective(deviceParamsDoor);
                //保存防火门设备系统信息
                if(systemIdList.size()>0){
                    List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                    if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                        List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                        if(!CollectionUtils.isEmpty(alarmConfigList)){
                            alarmConfigService.insertListRecord(alarmConfigList);
                        }
                    }
                }
                result.setCode(1);
                return result;
            }else if(deviceTypeId.equals(10l)){
                DeviceParamsWaveElec deviceParamsWaveElec=new DeviceParamsWaveElec();
                String waveParam = checkDeviceParamsWaveElec(paramMap, deviceParamsWaveElec);
                if(StringUtils.isBlank(waveParam)){
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    //保存滤波控制器
                    saveDeviceExtParam(device,date);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存滤波控制器设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && !systemIdStr.equals("0") ){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    //保存滤波控制器的设备参数信息
                    deviceParamsWaveElec.setDeviceId(device.getId());
                    deviceParamsWaveElec.setSerialNum(device.getSerialNum());
                    deviceParamsWaveElec.setModbusAddress("1");
                    deviceParamsWaveElec.setModbusBaudrate("9600");
                    deviceParamsWaveElec.setPeakReportTime("7:00");
                    deviceParamsWaveElec.setNormalReportTime("18:00");
                    deviceParamsWaveElec.setValleyReportTime("23:00");
                    deviceParamsWaveElec.setDidoSet("111");
                    deviceParamsWaveElec.setSystemSw("1011111");
                    deviceParamsWaveElec.setAlarmSw("1111111111111");
                    deviceParamsWaveElec.setPeak("7:01-18:00");
                    deviceParamsWaveElec.setNormal("18：01-23：00");
                    deviceParamsWaveElec.setValley("23：01-7：00");
                    deviceParamsWaveElec.setPeakPrice("120");
                    deviceParamsWaveElec.setNormalPrice("100");
                    deviceParamsWaveElec.setValleyPrice("80");
                    deviceParamsWaveElec.setReturnCode("100");
                    deviceParamsWaveElec.setCreateTime(date);
                    deviceParamsWaveElec.setUpdateTime(date);
                    deviceParamsWaveElecService.insertRecord(deviceParamsWaveElec);
                    //保存滤波控制器设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                    result.setData(waveParam);
                    return result;
                }
            }else if(deviceTypeId.equals(11l)){
                DeviceParamsCompensateElec deviceParamsCompensateElec=new DeviceParamsCompensateElec();
                String compensateParam = checkDeviceParamsCompensateElec(paramMap, deviceParamsCompensateElec);
                if(StringUtils.isBlank(compensateParam)){
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    saveDeviceExtParam(device,date);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存滤波控制器设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && ! systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    deviceParamsCompensateElec.setDeviceId(device.getId());
                    deviceParamsCompensateElec.setSerialNum(device.getSerialNum());
                    deviceParamsCompensateElec.setModbusAddress("1");
                    deviceParamsCompensateElec.setModbusBaudrate("9600");
                    deviceParamsCompensateElec.setPeakReportTime("7:00");
                    deviceParamsCompensateElec.setNormalReportTime("18:00");
                    deviceParamsCompensateElec.setValleyReportTime("23:00");
                    deviceParamsCompensateElec.setDidoSet("111");
                    deviceParamsCompensateElec.setSystemSw("1011111");
                    deviceParamsCompensateElec.setAlarmSw("1111111111111");
                    deviceParamsCompensateElec.setPeak("7:01-18:00");
                    deviceParamsCompensateElec.setNormal("18：01-23：00");
                    deviceParamsCompensateElec.setValley("23：01-7：00");
                    deviceParamsCompensateElec.setPeakPrice("120");
                    deviceParamsCompensateElec.setNormalPrice("100");
                    deviceParamsCompensateElec.setValleyPrice("80");
                    deviceParamsCompensateElec.setReturnCode("100");
                    deviceParamsCompensateElec.setCreateTime(date);
                    deviceParamsCompensateElec.setUpdateTime(date);
                    deviceParamsCompensateElecService.insertRecored(deviceParamsCompensateElec);
                    //保存滤波控制器设备报警参数
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return  result;
                }else {
                    result.setData(compensateParam);
                    return result;
                }
            }else if(deviceTypeId.equals(12l)){
                DeviceParamsTemperaturehumidity paramsTemperaturehumidity=new DeviceParamsTemperaturehumidity();
                String paramsTempatureHumidity = checkDeviceParamsTempatureHumidity(paramMap, paramsTemperaturehumidity);
                if(StringUtils.isBlank(paramsTempatureHumidity)){
                    MonitorDevice monitorDevice=handlerMonitor(paramMap,device,date);
                    saveDeviceExtParam(device,date);
                    deviceDao.insertSelective(device);
                    if(monitorDevice!=null){
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList=new ArrayList<>();
                    String systemIdStr=systemIdsObj.toString();
                    if(split.length>0 && ! systemIdStr.equals("0")){
                        systemIdList= saveDeviceSystem(device,split,date);
                    }
                    paramsTemperaturehumidity.setCreateTime(date);
                    paramsTemperaturehumidity.setUpdateTime(date);
                    paramsTemperaturehumidity.setDeviceId(device.getId());
                    paramsTemperaturehumidity.setSerialNum(device.getSerialNum());
                    paramsTemperaturehumidityService.insertRecord(paramsTemperaturehumidity);
                    if(systemIdList.size()>0){
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if(!CollectionUtils.isEmpty(deviceTypeAlarmList)){
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if(!CollectionUtils.isEmpty(alarmConfigList)){
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    return result;
                }else{
                    result.setData(paramsTempatureHumidity);
                    return result;
                }
            //处理1129
            }else if(deviceTypeId.equals(13l)) {
                DeviceParamSmartSuper smartElec = new DeviceParamSmartSuper();
                String smartInfo = checkSuperDeviceParam(paramMap, smartElec);
                if (StringUtils.isBlank(smartInfo)) {
                    //保存EA1129设备信息
                    MonitorDevice monitorDevice = handlerMonitor(paramMap, device, date);
                    saveDeviceExtParam(device, date);
                    deviceDao.insertSelective(device);
                    if (monitorDevice != null) {
                        monitorDevice.setDeviceId(device.getId());
                        monitorDeviceService.insert(monitorDevice);
                    }
                    //保存EA1129设备系统信息
                    Object systemIdsObj = paramMap.get("systemIds");
                    String[] split = systemIdsObj.toString().split(",");
                    List<Long> systemIdList = new ArrayList<>();
                    String systemIdStr = systemIdsObj.toString();
                    if (split.length > 0 && !systemIdStr.equals("0")) {
                        systemIdList = saveDeviceSystem(device, split, date);
                    }
                    smartElec.setDeviceId(device.getId());
                    smartElec.setSerialNum(device.getSerialNum());
                    smartElec.setModbusAddress("1");
                    smartElec.setModbusBaudrate("9600");
                    smartElec.setSpikeReportTime("8:00");
                    smartElec.setPeakReportTime("12:00");
                    smartElec.setNormalReportTime("17:00");
                    smartElec.setValleyReportTime("00:00");

                    smartElec.setDidoSet("1111");
                    smartElec.setSystemSw("1111");
                    smartElec.setAlarmSw("0000000111000000000000");
                    smartElec.setFaultEn("00000");
                    smartElec.setSpike("8:00-11:59");
                    smartElec.setPeak("12:00-16:59");
                    smartElec.setNormal("17:00-23:59");
                    smartElec.setValley("0:00-7:59");
                    smartElec.setDelayAlarmAmp("3");
                    smartElec.setDelayAlarmLeaked("3");
                    smartElec.setDelayAlarmTemp("3");
                    smartElec.setDelayAlarmVolt("3");
                    smartElec.setCreateTime(date);
                    smartElec.setUpdateTime(date);
                    Boolean flag = false;
                    flag=mt300SDeviceRemoteService.setDeviceParameter(device.getSerialNum(), smartElec);
                    if (flag) {
                        deviceParamSmartSuperService.saveParam(smartElec);
                    }
                    //保存EA1129设备报警参数
                    if (systemIdList.size() > 0) {
                        List<DeviceTypeAlarm> deviceTypeAlarmList = deviceTypeAlarmService.selectByDeviceTypeAndSystem(deviceTypeId, systemIdList);
                        if (!CollectionUtils.isEmpty(deviceTypeAlarmList)) {
                            List<AlarmConfig> alarmConfigList = handlerAlarmConfigSystem(alarmMap, deviceTypeAlarmList, device, date);
                            if (!CollectionUtils.isEmpty(alarmConfigList)) {
                                alarmConfigService.insertListRecord(alarmConfigList);
                            }
                        }
                    }
                    result.setCode(1);
                    result.setData("");
                    return result;
                } else {
                    result.setMsg(smartInfo);
                    return result;
                }
            }else{
               result.setMsg("设备类型错误");
            }
        }else{
            result.setData(paramError);
        }
        return result;
    }

    private String checkSaveDeviceParam(Map<String,Object> paramMap,Device device){
        Object serialNumObj = paramMap.get("serialNum");
        List<String> msg=new ArrayList<>();
        if(serialNumObj==null){
            msg.add("sn码错误");
        }else{
            if(StringUtils.isBlank(serialNumObj.toString())){
                msg.add("sn码错误");
            }else{
                String sn = serialNumObj.toString();
                Device deviceD = deviceDao.selectBySerialNum(sn);
                if(deviceD!=null){
                    msg.add("sn的设备已存在");
                }else{
                    device.setSerialNum(sn);
                }
            }
        }
        Object deviceTypeObj = paramMap.get("deviceTypeId");
        if(deviceTypeObj==null){
            msg.add("设备类型信息错误");
        }else{
            Long deviceTypeId = Long.parseLong(deviceTypeObj.toString());
            if(deviceTypeId.equals(0L)){
                msg.add("设备类型信息错误");
            }else{
                device.setDeviceTypeId(deviceTypeId);
            }
        }
        Object jdObj = paramMap.get("jd");
        Object wdObj = paramMap.get("wd");
        if(jdObj==null||wdObj==null){
            msg.add("经纬度信息错误");
        }else{
            device.setJd(Double.parseDouble(jdObj.toString()));
            device.setWd(Double.parseDouble(wdObj.toString()));
        }
        Object deviceNameObj = paramMap.get("deviceName");
        if(deviceNameObj==null){
            msg.add("设备名称信息错误");
        }else{
            if(StringUtils.isBlank(deviceNameObj.toString())){
                msg.add("设备名称信息错误");
            }else{
                device.setName(deviceNameObj.toString());
            }
        }
        Object monitorId = paramMap.get("monitorId");
        if(monitorId==null){
            msg.add("远程视频信息错误");
        }
        Object locationFlagObj = paramMap.get("locationFlag");
        if(locationFlagObj==null){
            msg.add("自定义区域标志信息错误");
        }else{
            if(locationFlagObj.toString().equals("1")){
                Object locationObj = paramMap.get("location");
                if(locationObj==null||StringUtils.isBlank(locationObj.toString())){
                    msg.add("自定义区域信息错误");
                }else{
                    device.setLocation(locationObj.toString());
                }
            }else{
                Object areaObj = paramMap.get("areaId");
                Object buildingObj = paramMap.get("buildingId");
                Object storeyObj = paramMap.get("storeyId");
                Object roomObj = paramMap.get("roomId");
                if(areaObj==null){
                    msg.add("区信息错误");
                }else{
                    if(!areaObj.toString().equals("0")){
                        device.setAreaId(Long.parseLong(areaObj.toString()));
                    }
                }
                if(buildingObj==null||buildingObj.toString().equals("0")){
                    msg.add("楼信息错误");
                }else{
                    device.setBuildingId(Long.parseLong(buildingObj.toString()));
                }
                if(storeyObj==null||storeyObj.toString().equals("0")){
                    msg.add("层信息错误");
                }else{
                    device.setStoreyId(Long.parseLong(storeyObj.toString()));
                }
                if(roomObj==null||roomObj.toString().equals("0")){
                    msg.add("房间信息错误");
                }else{
                    device.setRoomId(Long.parseLong(roomObj.toString()));
                }
            }
        }
        Object systemIdsObj = paramMap.get("systemIds");
        if(systemIdsObj==null||StringUtils.isBlank(systemIdsObj.toString())){
            msg.add("系统信息错误");
        }
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            msg.add("项目Id参数信息错误");
        }else{
            device.setProjectId(Long.parseLong(projectIdObj.toString()));
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg, ",");
        }
    }

    private String checkSafeDeviceParam(Map<String,Object> paramMap, DeviceParamsSafeElec deviceParamsSafeElec, Integer type){
        Object voltHighObj = paramMap.get("voltHigh");
        Object voltLowObj = paramMap.get("voltLow");
        Object currHighObj = paramMap.get("currHigh");
        Object currLeakObj = paramMap.get("currLeak");
        Object tempHighObj = paramMap.get("tempHigh");
        Object upInterval = paramMap.get("upInterval");
        List<String> msg=new ArrayList<>();
        if(voltHighObj==null||StringUtils.isBlank(voltHighObj.toString())){
            msg.add("电压过高告警阈值信息错误");
        }else{
            Integer voltHighData= Integer.parseInt(voltHighObj.toString())*10;
            deviceParamsSafeElec.setVoltHigh(voltHighData.toString());
        }
        if(voltLowObj==null||StringUtils.isBlank(voltLowObj.toString())){
            msg.add("电压欠压告警阈值信息错误");
        }else{
            Integer voltLowData= Integer.parseInt(voltLowObj.toString())*10;
            deviceParamsSafeElec.setVoltLow(voltLowData.toString());
        }
        if(currHighObj==null||StringUtils.isBlank(currHighObj.toString())){
            msg.add("电流过流告警阈值信息错误");
        }else{
            Integer currHighData= Integer.parseInt(currHighObj.toString())*10;
            deviceParamsSafeElec.setCurrHigh(currHighData.toString());
        }
        if(type.equals(1)){

            if(currLeakObj==null||StringUtils.isBlank(currLeakObj.toString())){
                msg.add("漏电流过流告警阈值信息错误");
            }else{
                Integer currLeakData= Integer.parseInt(currLeakObj.toString())*10;
                deviceParamsSafeElec.setCurrLeak(currLeakData.toString());
            }
            if(tempHighObj==null||StringUtils.isBlank(tempHighObj.toString())){
                msg.add("过温告警阈值信息错误");
            }else{
                Integer tempHighData= Integer.parseInt(tempHighObj.toString())*10;
                deviceParamsSafeElec.setTempHigh(tempHighData.toString());
            }
        }
        if(upInterval==null||StringUtils.isBlank(upInterval.toString())){
            msg.add("上报时间间隔参数信息错误");
        }else{
            deviceParamsSafeElec.setUpInterval(upInterval.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return  StringUtils.join(msg,",");
        }
    }

    private String checkSmartDeviceParam(Map<String,Object> paramMap, DeviceParamsSmartElec deviceParamsSmartElec, Integer type){

        List<String> msg=new ArrayList<>();
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object reportInterval = paramMap.get("reportInterval");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");


        Object leakCT = paramMap.get("leakCT");
        Object overLeaked = paramMap.get("overLeaked");
        Object overTemper = paramMap.get("overTemper");
        if(transCapacity==null||StringUtils.isBlank(transCapacity.toString())){
            msg.add("变压器容量参数错误");
        }else{
            deviceParamsSmartElec.setTransCapacity(transCapacity.toString());
        }
        if(reportInterval==null||StringUtils.isBlank(reportInterval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamsSmartElec.setReportInterval(reportInterval.toString());
        }
        if(transLoad==null||StringUtils.isBlank(transLoad.toString())){
            msg.add("变压器负荷阈值参数错误");
        }else{
            deviceParamsSmartElec.setTransLoad(transLoad.toString());
        }
        if(currCT==null||StringUtils.isBlank(currCT.toString())){
            msg.add("电流互感器变比参数错误");
        }else{
            deviceParamsSmartElec.setCtRatioA(currCT.toString());
            deviceParamsSmartElec.setCtRatioB(currCT.toString());
            deviceParamsSmartElec.setCtRatioC(currCT.toString());
        }
        if(powerFactorA==null||StringUtils.isBlank(powerFactorA.toString())){
            msg.add("A相功率因数参数错误");
        }else{
            deviceParamsSmartElec.setPowerFactorA(powerFactorA.toString());
        }
        if(powerFactorB==null||StringUtils.isBlank(powerFactorB.toString())){
            msg.add("B相功率因数参数错误");
        }else{
            deviceParamsSmartElec.setPowerFactorB(powerFactorB.toString());
        }
        if(powerFactorC==null||StringUtils.isBlank(powerFactorC.toString())){
            msg.add("C相功率因数参数错误");
        }else{
            deviceParamsSmartElec.setPowerFactorC(powerFactorC.toString());
        }
        if(overVoltageA==null||StringUtils.isBlank(overVoltageA.toString())){
            msg.add("A相电压过压阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverVoltageA(overVoltageA.toString());
        }
        if(overVoltageB==null||StringUtils.isBlank(overVoltageB.toString())){
            msg.add("B相电压过压阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverVoltageB(overVoltageB.toString());
        }
        if(overVoltageC==null||StringUtils.isBlank(overVoltageC.toString())){
            msg.add("C相电压过压阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverVoltageC(overVoltageC.toString());
        }
        if(underVoltageA==null||StringUtils.isBlank(underVoltageA.toString())){
            msg.add("A相电压欠压阈值参数错误");
        }else{
            deviceParamsSmartElec.setUnderVoltageA(underVoltageA.toString());
        }
        if(underVoltageB==null||StringUtils.isBlank(underVoltageB.toString())){
            msg.add("B相电压欠压阈值参数错误");
        }else{
            deviceParamsSmartElec.setUnderVoltageB(underVoltageB.toString());
        }
        if(underVoltageC==null||StringUtils.isBlank(underVoltageC.toString())){
            msg.add("C相电压欠压阈值参数错误");
        }else{
            deviceParamsSmartElec.setUnderVoltageC(underVoltageC.toString());
        }
        if(lackPhaseA==null||StringUtils.isBlank(lackPhaseA.toString())){
            msg.add("A相电压缺项阈值参数错误");
        }else{
            deviceParamsSmartElec.setLackPhaseA(lackPhaseA.toString());
        }
        if(lackPhaseB==null||StringUtils.isBlank(lackPhaseB.toString())){
            msg.add("B相电压缺项阈值参数错误");
        }else{
            deviceParamsSmartElec.setLackPhaseB(lackPhaseB.toString());
        }
        if(lackPhaseC==null||StringUtils.isBlank(lackPhaseC.toString())){
            msg.add("C相电压缺项阈值参数错误");
        }else{
            deviceParamsSmartElec.setLackPhaseC(lackPhaseC.toString());
        }
        if(overCurrentA==null||StringUtils.isBlank(overCurrentA.toString())){
            msg.add("A相电流过流阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverCurrentA(overCurrentA.toString());
        }
        if(overCurrentB==null||StringUtils.isBlank(overCurrentB.toString())){
            msg.add("B相电流过流阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverCurrentB(overCurrentB.toString());
        }
        if(overCurrentC==null||StringUtils.isBlank(overCurrentC.toString())){
            msg.add("C相电流过流阈值参数错误");
        }else{
            deviceParamsSmartElec.setOverCurrentC(overCurrentC.toString());
        }
        if(currentThdA==null||StringUtils.isBlank(currentThdA.toString())){
            msg.add("A相电流畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setCurrentThdA(currentThdA.toString());
        }
        if(currentThdB==null||StringUtils.isBlank(currentThdB.toString())){
            msg.add("B相电流畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setCurrentThdB(currentThdB.toString());
        }
        if(currentThdC==null||StringUtils.isBlank(currentThdC.toString())){
            msg.add("C相电流畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setCurrentThdC(currentThdC.toString());
        }
        if(voltageThdA==null||StringUtils.isBlank(voltageThdA.toString())){
            msg.add("A相电压畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setVoltageThdA(voltageThdA.toString());
        }
        if(voltageThdB==null||StringUtils.isBlank(voltageThdB.toString())){
            msg.add("B相电压畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setVoltageThdB(voltageThdB.toString());
        }
        if(voltageThdC==null||StringUtils.isBlank(voltageThdC.toString())){
            msg.add("C相电压畸变率阈值参数错误");
        }else{
            deviceParamsSmartElec.setVoltageThdC(voltageThdC.toString());
        }
        if(type.equals(6)){
            if(leakCT==null||StringUtils.isBlank(leakCT.toString())){
                msg.add("漏电流互感器变比参数错误");
            }else{
                deviceParamsSmartElec.setCtRatioD(leakCT.toString());
            }
            if(overLeaked==null||StringUtils.isBlank(overLeaked.toString())){
                msg.add("漏电流过流阈值参数错误");
            }else{
                deviceParamsSmartElec.setOverLeaked(overLeaked.toString());
            }
            if(overTemper==null||StringUtils.isBlank(overTemper.toString())){
                msg.add("温度过温阈值参数错误");
            }else{
               Integer overTemperData=Integer.parseInt(overTemper.toString())*10;
                deviceParamsSmartElec.setOverTemper(overTemperData.toString());
            }

        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkSuperDeviceParam(Map<String,Object> paramMap,DeviceParamSmartSuper deviceParamSmartSuper){

        List<String> msg=new ArrayList<>();
        //CT转换比例
        Object currCT = paramMap.get("currCT");

        Object leakedCt = paramMap.get("leakedCt");

        //高压转换比例
        Object ctRatioHighVolt = paramMap.get("ctRatioHighVolt");
        //时间间隔
        Object reportInterval = paramMap.get("reportTime");
        //功率因数
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");
        //过压
        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");
        //欠压
        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");
        //缺相
        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");
        //过流
        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");
        //漏电
        Object overLeaked = paramMap.get("overLeaked");
        //过温
        Object overTemper = paramMap.get("overTemper");
        //电流畸变率
        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");
        //电压畸变率
        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");
        //电流不平衡度
        Object currentUnbalance = paramMap.get("currentUnbalance");
        //电压不平衡度
        Object voltageUnbalance = paramMap.get("voltageUnbalance");


        if(currCT==null||StringUtils.isBlank(currCT.toString())){
            msg.add("ct转换比例参数错误");
        }else{
            deviceParamSmartSuper.setCtRatioA(currCT.toString());
            deviceParamSmartSuper.setCtRatioB(currCT.toString());
            deviceParamSmartSuper.setCtRatioC(currCT.toString());
        }

        if(leakedCt==null||StringUtils.isBlank(leakedCt.toString())){
            msg.add("漏电ct转换比例参数错误");
        }else{
            deviceParamSmartSuper.setCtRatioD(leakedCt.toString());
        }
        if(ctRatioHighVolt==null||StringUtils.isBlank(ctRatioHighVolt.toString())){
            msg.add("高压ct转换比例参数错误");
        }else{
            deviceParamSmartSuper.setCtRatioHighVolt(ctRatioHighVolt.toString());
        }

        if(reportInterval==null||StringUtils.isBlank(reportInterval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamSmartSuper.setReportTime(reportInterval.toString());
        }

        if(powerFactorA==null||StringUtils.isBlank(powerFactorA.toString())){
            msg.add("A相功率因数参数错误");
        }else{
            deviceParamSmartSuper.setPowerFactorA(powerFactorA.toString());
        }
        if(powerFactorB==null||StringUtils.isBlank(powerFactorB.toString())){
            msg.add("B相功率因数参数错误");
        }else{
            deviceParamSmartSuper.setPowerFactorB(powerFactorB.toString());
        }
        if(powerFactorC==null||StringUtils.isBlank(powerFactorC.toString())){
            msg.add("C相功率因数参数错误");
        }else{
            deviceParamSmartSuper.setPowerFactorC(powerFactorC.toString());
        }

        if(overVoltageA==null||StringUtils.isBlank(overVoltageA.toString())){
            msg.add("A相电压过压阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverVoltageA(overVoltageA.toString());
        }
        if(overVoltageB==null||StringUtils.isBlank(overVoltageB.toString())){
            msg.add("B相电压过压阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverVoltageB(overVoltageB.toString());
        }
        if(overVoltageC==null||StringUtils.isBlank(overVoltageC.toString())){
            msg.add("C相电压过压阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverVoltageC(overVoltageC.toString());
        }
        if(underVoltageA==null||StringUtils.isBlank(underVoltageA.toString())){
            msg.add("A相电压欠压阈值参数错误");
        }else{
            deviceParamSmartSuper.setUnderVoltageA(underVoltageA.toString());
        }
        if(underVoltageB==null||StringUtils.isBlank(underVoltageB.toString())){
            msg.add("B相电压欠压阈值参数错误");
        }else{
            deviceParamSmartSuper.setUnderVoltageB(underVoltageB.toString());
        }
        if(underVoltageC==null||StringUtils.isBlank(underVoltageC.toString())){
            msg.add("C相电压欠压阈值参数错误");
        }else{
            deviceParamSmartSuper.setUnderVoltageC(underVoltageC.toString());
        }
        if(lackPhaseA==null||StringUtils.isBlank(lackPhaseA.toString())){
            msg.add("A相电压缺项阈值参数错误");
        }else{
            deviceParamSmartSuper.setLackPhaseA(lackPhaseA.toString());
        }
        if(lackPhaseB==null||StringUtils.isBlank(lackPhaseB.toString())){
            msg.add("B相电压缺项阈值参数错误");
        }else{
            deviceParamSmartSuper.setLackPhaseB(lackPhaseB.toString());
        }
        if(lackPhaseC==null||StringUtils.isBlank(lackPhaseC.toString())){
            msg.add("C相电压缺项阈值参数错误");
        }else{
            deviceParamSmartSuper.setLackPhaseC(lackPhaseC.toString());
        }
        if(overCurrentA==null||StringUtils.isBlank(overCurrentA.toString())){
            msg.add("A相电流过流阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverCurrentA(overCurrentA.toString());
        }
        if(overCurrentB==null||StringUtils.isBlank(overCurrentB.toString())){
            msg.add("B相电流过流阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverCurrentB(overCurrentB.toString());
        }
        if(overCurrentC==null||StringUtils.isBlank(overCurrentC.toString())){
            msg.add("C相电流过流阈值参数错误");
        }else{
            deviceParamSmartSuper.setOverCurrentC(overCurrentC.toString());
        }
        if(currentThdA==null||StringUtils.isBlank(currentThdA.toString())){
            msg.add("A相电流畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setCurrentThdA(currentThdA.toString());
        }
        if(currentThdB==null||StringUtils.isBlank(currentThdB.toString())){
            msg.add("B相电流畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setCurrentThdB(currentThdB.toString());
        }
        if(currentThdC==null||StringUtils.isBlank(currentThdC.toString())){
            msg.add("C相电流畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setCurrentThdC(currentThdC.toString());
        }
        if(voltageThdA==null||StringUtils.isBlank(voltageThdA.toString())){
            msg.add("A相电压畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setVoltageThdA(voltageThdA.toString());
        }
        if(voltageThdB==null||StringUtils.isBlank(voltageThdB.toString())){
            msg.add("B相电压畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setVoltageThdB(voltageThdB.toString());
        }
        if(voltageThdC==null||StringUtils.isBlank(voltageThdC.toString())){
            msg.add("C相电压畸变率阈值参数错误");
        }else{
            deviceParamSmartSuper.setVoltageThdC(voltageThdC.toString());
        }
        //电流不平衡度
        if(currentUnbalance==null||StringUtils.isBlank(currentUnbalance.toString())){
            msg.add("电流不平衡度参数错误");
        }else{
            deviceParamSmartSuper.setCurrentUnbalance(currentUnbalance.toString());
        }
        //电压不平衡度
        if(voltageUnbalance==null||StringUtils.isBlank(voltageUnbalance.toString())){
            msg.add("电压不平衡度参数错误");
        }else{
            deviceParamSmartSuper.setVoltageUnbalance(voltageUnbalance.toString());
        }
        //过温
        if(overTemper==null||StringUtils.isBlank(overTemper.toString())){
            msg.add("过温参数错误");
        }else{
            Integer temper=Integer.parseInt(overTemper.toString())*10;
            deviceParamSmartSuper.setOverTemper(String.valueOf(temper));
        }
        //漏电流
        if(overLeaked==null||StringUtils.isBlank(overLeaked.toString())){
            msg.add("漏电流参数错误");
        }else{
            deviceParamSmartSuper.setOverLeaked(overLeaked.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkSmokeDevice(Map<String, Object> paramMap,DeviceParamsSmoke deviceParamsSmoke) {
        List<String> msg=new ArrayList<>();
        Object smokeConcentration = paramMap.get("smokeConcentration");
        if(smokeConcentration==null||StringUtils.isBlank(smokeConcentration.toString())){
            msg.add("烟雾浓度告警阈值参数错误");
        }else{
            deviceParamsSmoke.setSmokeConcentration(smokeConcentration.toString());
        }
        Object electricityQuantity = paramMap.get("electricityQuantity");
        if(electricityQuantity==null||StringUtils.isBlank(smokeConcentration.toString())){
            msg.add("电池电量告警阈值参数错误");
        }else{
            deviceParamsSmoke.setElectricityQuantity(electricityQuantity.toString());
        }
        Object interval = paramMap.get("interval");
        if(interval==null||StringUtils.isBlank(interval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamsSmoke.setInterval(interval.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkDeviceWaterParams(Map<String, Object> paramMap, DeviceParamsWater deviceParamsWater) {
        List<String> msg=new ArrayList<>();
        Object discharge = paramMap.get("discharge");
        if(discharge==null||StringUtils.isBlank(discharge.toString())){
            msg.add("水流量告警阈值参数错误");
        }else{
            deviceParamsWater.setDischarge(discharge.toString());
        }
        Object pressure = paramMap.get("pressure");
        if(pressure==null||StringUtils.isBlank(pressure.toString())){
            msg.add("水压告警阈值参数错误");
        }else{
            deviceParamsWater.setPressure(pressure.toString());
        }
        Object interval = paramMap.get("interval");
        if(interval==null||StringUtils.isBlank(interval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamsWater.setInterval(interval.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkDeviceParamsWaveElec(Map<String,Object> paramMap, DeviceParamsWaveElec deviceParamsWaveElec){

        List<String> msg=new ArrayList<>();
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object reportInterval = paramMap.get("reportInterval");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");

        if(reportInterval==null||StringUtils.isBlank(reportInterval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamsWaveElec.setReportInterval(reportInterval.toString());
        }
        if(transCapacity==null||StringUtils.isBlank(transCapacity.toString())){
            msg.add("变压器容量参数错误");
        }else{
            deviceParamsWaveElec.setTransCapacity(transCapacity.toString());
        }
        if(transLoad==null||StringUtils.isBlank(transLoad.toString())){
            msg.add("变压器负荷阈值参数错误");
        }else{
            deviceParamsWaveElec.setTransLoad(transLoad.toString());
        }
        if(currCT==null||StringUtils.isBlank(currCT.toString())){
            msg.add("电流互感器变比参数错误");
        }else{
            deviceParamsWaveElec.setCtRatioA(currCT.toString());
            deviceParamsWaveElec.setCtRatioB(currCT.toString());
            deviceParamsWaveElec.setCtRatioC(currCT.toString());
        }
        if(powerFactorA==null||StringUtils.isBlank(powerFactorA.toString())){
            msg.add("A相功率因数参数错误");
        }else{
            deviceParamsWaveElec.setPowerFactorA(powerFactorA.toString());
        }
        if(powerFactorB==null||StringUtils.isBlank(powerFactorB.toString())){
            msg.add("B相功率因数参数错误");
        }else{
            deviceParamsWaveElec.setPowerFactorB(powerFactorB.toString());
        }
        if(powerFactorC==null||StringUtils.isBlank(powerFactorC.toString())){
            msg.add("C相功率因数参数错误");
        }else{
            deviceParamsWaveElec.setPowerFactorC(powerFactorC.toString());
        }
        if(overVoltageA==null||StringUtils.isBlank(overVoltageA.toString())){
            msg.add("A相电压过压阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverVoltageA(overVoltageA.toString());
        }
        if(overVoltageB==null||StringUtils.isBlank(overVoltageB.toString())){
            msg.add("B相电压过压阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverVoltageB(overVoltageB.toString());
        }
        if(overVoltageC==null||StringUtils.isBlank(overVoltageC.toString())){
            msg.add("C相电压过压阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverVoltageC(overVoltageC.toString());
        }
        if(underVoltageA==null||StringUtils.isBlank(underVoltageA.toString())){
            msg.add("A相电压欠压阈值参数错误");
        }else{
            deviceParamsWaveElec.setUnderVoltageA(underVoltageA.toString());
        }
        if(underVoltageB==null||StringUtils.isBlank(underVoltageB.toString())){
            msg.add("B相电压欠压阈值参数错误");
        }else{
            deviceParamsWaveElec.setUnderVoltageB(underVoltageB.toString());
        }
        if(underVoltageC==null||StringUtils.isBlank(underVoltageC.toString())){
            msg.add("C相电压欠压阈值参数错误");
        }else{
            deviceParamsWaveElec.setUnderVoltageC(underVoltageC.toString());
        }
        if(lackPhaseA==null||StringUtils.isBlank(lackPhaseA.toString())){
            msg.add("A相电压缺项阈值参数错误");
        }else{
            deviceParamsWaveElec.setLackPhaseA(lackPhaseA.toString());
        }
        if(lackPhaseB==null||StringUtils.isBlank(lackPhaseB.toString())){
            msg.add("B相电压缺项阈值参数错误");
        }else{
            deviceParamsWaveElec.setLackPhaseB(lackPhaseB.toString());
        }
        if(lackPhaseC==null||StringUtils.isBlank(lackPhaseC.toString())){
            msg.add("C相电压缺项阈值参数错误");
        }else{
            deviceParamsWaveElec.setLackPhaseC(lackPhaseC.toString());
        }
        if(overCurrentA==null||StringUtils.isBlank(overCurrentA.toString())){
            msg.add("A相电流过流阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverCurrentA(overCurrentA.toString());
        }
        if(overCurrentB==null||StringUtils.isBlank(overCurrentB.toString())){
            msg.add("B相电流过流阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverCurrentB(overCurrentB.toString());
        }
        if(overCurrentC==null||StringUtils.isBlank(overCurrentC.toString())){
            msg.add("C相电流过流阈值参数错误");
        }else{
            deviceParamsWaveElec.setOverCurrentC(overCurrentC.toString());
        }
        if(currentThdA==null||StringUtils.isBlank(currentThdA.toString())){
            msg.add("A相电流畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setCurrentThdA(currentThdA.toString());
        }
        if(currentThdB==null||StringUtils.isBlank(currentThdB.toString())){
            msg.add("B相电流畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setCurrentThdB(currentThdB.toString());
        }
        if(currentThdC==null||StringUtils.isBlank(currentThdC.toString())){
            msg.add("C相电流畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setCurrentThdC(currentThdC.toString());
        }
        if(voltageThdA==null||StringUtils.isBlank(voltageThdA.toString())){
            msg.add("A相电压畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setVoltageThdA(voltageThdA.toString());
        }
        if(voltageThdB==null||StringUtils.isBlank(voltageThdB.toString())){
            msg.add("B相电压畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setVoltageThdB(voltageThdB.toString());
        }
        if(voltageThdC==null||StringUtils.isBlank(voltageThdC.toString())){
            msg.add("C相电压畸变率阈值参数错误");
        }else{
            deviceParamsWaveElec.setVoltageThdC(voltageThdC.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkDeviceParamsCompensateElec(Map<String,Object> paramMap,DeviceParamsCompensateElec deviceParamsCompensateElec){

        List<String> msg=new ArrayList<>();
        Object transCapacity = paramMap.get("transCapacity");
        Object transLoad = paramMap.get("transLoad");
        Object currCT = paramMap.get("currCT");
        Object reportInterval = paramMap.get("reportInterval");
        Object powerFactorA = paramMap.get("powerFactorA");
        Object powerFactorB = paramMap.get("powerFactorB");
        Object powerFactorC = paramMap.get("powerFactorC");

        Object overVoltageA = paramMap.get("overVoltageA");
        Object overVoltageB = paramMap.get("overVoltageB");
        Object overVoltageC = paramMap.get("overVoltageC");

        Object underVoltageA = paramMap.get("underVoltageA");
        Object underVoltageB = paramMap.get("underVoltageB");
        Object underVoltageC = paramMap.get("underVoltageC");

        Object lackPhaseA = paramMap.get("lackPhaseA");
        Object lackPhaseB = paramMap.get("lackPhaseB");
        Object lackPhaseC = paramMap.get("lackPhaseC");

        Object overCurrentA = paramMap.get("overCurrentA");
        Object overCurrentB = paramMap.get("overCurrentB");
        Object overCurrentC = paramMap.get("overCurrentC");

        Object currentThdA = paramMap.get("currentThdA");
        Object currentThdB = paramMap.get("currentThdB");
        Object currentThdC = paramMap.get("currentThdC");

        Object voltageThdA = paramMap.get("voltageThdA");
        Object voltageThdB = paramMap.get("voltageThdB");
        Object voltageThdC = paramMap.get("voltageThdC");

        if(reportInterval==null||StringUtils.isBlank(reportInterval.toString())){
            msg.add("上报时间间隔参数错误");
        }else{
            deviceParamsCompensateElec.setReportInterval(reportInterval.toString());
        }
        if(transCapacity==null||StringUtils.isBlank(transCapacity.toString())){
            msg.add("变压器容量参数错误");
        }else{
            deviceParamsCompensateElec.setTransCapacity(transCapacity.toString());
        }
        if(transLoad==null||StringUtils.isBlank(transLoad.toString())){
            msg.add("变压器负荷阈值参数错误");
        }else{
            deviceParamsCompensateElec.setTransLoad(transLoad.toString());
        }
        if(currCT==null||StringUtils.isBlank(currCT.toString())){
            msg.add("电流互感器变比参数错误");
        }else{
            deviceParamsCompensateElec.setCtRatioA(currCT.toString());
            deviceParamsCompensateElec.setCtRatioB(currCT.toString());
            deviceParamsCompensateElec.setCtRatioC(currCT.toString());
        }
        if(powerFactorA==null||StringUtils.isBlank(powerFactorA.toString())){
            msg.add("A相功率因数参数错误");
        }else{
            deviceParamsCompensateElec.setPowerFactorA(powerFactorA.toString());
        }
        if(powerFactorB==null||StringUtils.isBlank(powerFactorB.toString())){
            msg.add("B相功率因数参数错误");
        }else{
            deviceParamsCompensateElec.setPowerFactorB(powerFactorB.toString());
        }
        if(powerFactorC==null||StringUtils.isBlank(powerFactorC.toString())){
            msg.add("C相功率因数参数错误");
        }else{
            deviceParamsCompensateElec.setPowerFactorC(powerFactorC.toString());
        }
        if(overVoltageA==null||StringUtils.isBlank(overVoltageA.toString())){
            msg.add("A相电压过压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverVoltageA(overVoltageA.toString());
        }
        if(overVoltageB==null||StringUtils.isBlank(overVoltageB.toString())){
            msg.add("B相电压过压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverVoltageB(overVoltageB.toString());
        }
        if(overVoltageC==null||StringUtils.isBlank(overVoltageC.toString())){
            msg.add("C相电压过压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverVoltageC(overVoltageC.toString());
        }
        if(underVoltageA==null||StringUtils.isBlank(underVoltageA.toString())){
            msg.add("A相电压欠压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setUnderVoltageA(underVoltageA.toString());
        }
        if(underVoltageB==null||StringUtils.isBlank(underVoltageB.toString())){
            msg.add("B相电压欠压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setUnderVoltageB(underVoltageB.toString());
        }
        if(underVoltageC==null||StringUtils.isBlank(underVoltageC.toString())){
            msg.add("C相电压欠压阈值参数错误");
        }else{
            deviceParamsCompensateElec.setUnderVoltageC(underVoltageC.toString());
        }
        if(lackPhaseA==null||StringUtils.isBlank(lackPhaseA.toString())){
            msg.add("A相电压缺项阈值参数错误");
        }else{
            deviceParamsCompensateElec.setLackPhaseA(lackPhaseA.toString());
        }
        if(lackPhaseB==null||StringUtils.isBlank(lackPhaseB.toString())){
            msg.add("B相电压缺项阈值参数错误");
        }else{
            deviceParamsCompensateElec.setLackPhaseB(lackPhaseB.toString());
        }
        if(lackPhaseC==null||StringUtils.isBlank(lackPhaseC.toString())){
            msg.add("C相电压缺项阈值参数错误");
        }else{
            deviceParamsCompensateElec.setLackPhaseC(lackPhaseC.toString());
        }
        if(overCurrentA==null||StringUtils.isBlank(overCurrentA.toString())){
            msg.add("A相电流过流阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverCurrentA(overCurrentA.toString());
        }
        if(overCurrentB==null||StringUtils.isBlank(overCurrentB.toString())){
            msg.add("B相电流过流阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverCurrentB(overCurrentB.toString());
        }
        if(overCurrentC==null||StringUtils.isBlank(overCurrentC.toString())){
            msg.add("C相电流过流阈值参数错误");
        }else{
            deviceParamsCompensateElec.setOverCurrentC(overCurrentC.toString());
        }
        if(currentThdA==null||StringUtils.isBlank(currentThdA.toString())){
            msg.add("A相电流畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setCurrentThdA(currentThdA.toString());
        }
        if(currentThdB==null||StringUtils.isBlank(currentThdB.toString())){
            msg.add("B相电流畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setCurrentThdB(currentThdB.toString());
        }
        if(currentThdC==null||StringUtils.isBlank(currentThdC.toString())){
            msg.add("C相电流畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setCurrentThdC(currentThdC.toString());
        }
        if(voltageThdA==null||StringUtils.isBlank(voltageThdA.toString())){
            msg.add("A相电压畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setVoltageThdA(voltageThdA.toString());
        }
        if(voltageThdB==null||StringUtils.isBlank(voltageThdB.toString())){
            msg.add("B相电压畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setVoltageThdB(voltageThdB.toString());
        }
        if(voltageThdC==null||StringUtils.isBlank(voltageThdC.toString())){
            msg.add("C相电压畸变率阈值参数错误");
        }else{
            deviceParamsCompensateElec.setVoltageThdC(voltageThdC.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private String checkDeviceParamsTempatureHumidity(Map<String, Object> paramMap, DeviceParamsTemperaturehumidity paramsTemperaturehumidity) {
        List<String> msg=new ArrayList<>();
        Object interval = paramMap.get("interval");
        if(interval==null||StringUtils.isBlank(interval.toString())){
            msg.add("设备时间间隔参数错误");
        }else{
            paramsTemperaturehumidity.setInterval(interval.toString());
        }
        if(CollectionUtils.isEmpty(msg)){
            return null;
        }else{
            return StringUtils.join(msg,",");
        }
    }

    private MonitorDevice handlerMonitor(Map<String, Object> paramMap, Device device, Date date) {
        Long monitorId = Long.parseLong(paramMap.get("monitorId").toString());
        if(!monitorId.equals(0l)){
            Monitor monitor = monitorService.findById(monitorId);
            if(monitor!=null){
                String hlsHd = monitor.getHlsHd();
                device.setVideoUrl(hlsHd);
                MonitorDevice monitorDevice=new MonitorDevice();
                monitorDevice.setMonitorId(monitorId);
                monitorDevice.setHlsHd(hlsHd);
                monitorDevice.setCreateTime(date);
                monitorDevice.setUpdateTime(date);
                return monitorDevice;
            }
        }
        return null;
    }

    private void  saveDeviceExtParam(Device device,Date date){
        device.setErasure(0);
        device.setStatus(4);
        device.setSwitchStatus(-1);
        device.setBindingStatus(0);
        device.setBindingType(-1);
        device.setExtFlag(0);
        device.setCreateTime(date);
        device.setUpdateTime(date);
    }

    private List<Long> saveDeviceSystem(Device device,String[] systemArr,Date date){
        List<DeviceSystem> deviceSystemList =new ArrayList<>();
        List<Long> systemIdList=new ArrayList<>();
        for (int i = 0; i < systemArr.length; i++) {
            String systemStr = systemArr[i];
            systemIdList.add(Long.parseLong(systemStr));
            DeviceSystem deviceSystem=new DeviceSystem();
            deviceSystem.setDeviceId(device.getId());
            deviceSystem.setName(device.getName());
            deviceSystem.setProjectId(device.getProjectId());
            deviceSystem.setSerialNum(device.getSerialNum());
            deviceSystem.setSystemId(Long.parseLong(systemStr));
            deviceSystem.setCreateTime(date);
            deviceSystem.setUpdateTime(date);
            deviceSystemList.add(deviceSystem);
        }
        if(!CollectionUtils.isEmpty(deviceSystemList)){
            deviceSystemService.insertList(deviceSystemList);
        }
        return systemIdList;
    }

    private List<AlarmConfig> handlerAlarmConfigSystem(Map<Long,String> alarmItemMap,List<DeviceTypeAlarm> deviceTypeAlarmList,Device device,Date date){
        List<AlarmConfig> alarmConfigList=new ArrayList<>();
        for (DeviceTypeAlarm deviceTypeAlarm : deviceTypeAlarmList) {
            String alarmItemIds = deviceTypeAlarm.getAlarmItemIds();
            Long systemId = deviceTypeAlarm.getSystemId();
            String[] alarmItemIdArr = alarmItemIds.split(",");
            for (int i = 0; i < alarmItemIdArr.length; i++) {
                Long alarmItemId = Long.parseLong(alarmItemIdArr[i]);
                String  alarmItemName=alarmItemMap.get(alarmItemId);
                AlarmConfig alarmConfig=new AlarmConfig();
                alarmConfig.setAlarmCount(0);
                alarmConfig.setAlarmItemId(alarmItemId);
                alarmConfig.setAlarmItemName(alarmItemName);
                alarmConfig.setDeviceId(device.getId());
                alarmConfig.setSerialNum(device.getSerialNum());
                alarmConfig.setSystemId(systemId);
                alarmConfig.setCreateTime(date);
                alarmConfig.setUpdateTime(date);
                alarmConfigList.add(alarmConfig);
            }
        }
        return alarmConfigList;
    }

    @Override
    public List<Device> getAreaDevice(Long areaId, Long projectId) {
        return deviceDao.getAreaDevice(areaId,projectId);
    }

    @Override
    public MeasureDataStr getMeasureTotalData(Long deviceId) {
        return deviceDao.getMeasureTotalData(deviceId);
    }

    @Override
    public List<MeasureDateData> getPastSixDayMeasure(Long deviceId, Long projectId,Date strDate,Date endDate) {
        return deviceDao.getPastSixDayMeasure(deviceId,projectId,strDate,endDate);
    }

    @Override
    public MeasureDateData getTodayMeasureData(Long deviceId,String yearMonthDay) {
        return deviceDao.getTodayMeasureData(deviceId,yearMonthDay);
    }

    @Override
    public List<MeasureDateData> getPastDateDate(Long deviceId, Long projectId, Date lastBegDate, Date lastEndDate) {
        return deviceDao.getPastDateDate(projectId,deviceId,lastBegDate,lastEndDate);
    }

    @Override
    public MeasureDateData getDayMeasureData(Long deviceId, String lastTimeStr) {
        return deviceDao.getDayMeasureData(deviceId,lastTimeStr);
    }

    @Override
    public List<MeasureDateData> getLastMonthDayMeasureData(Long deviceId, String year, String month) {
        return deviceDao.getLastMonthDayMeasureData(deviceId,year,month);
    }

    @Override
    public MeasureDateData getTodayMonthMeasure(Long deviceId, String yearMonthDay) {
        return deviceDao.getTodayMonthMeasure(deviceId,yearMonthDay);
    }

    @Override
    public List<EntityDto> getSystemDeviceList(Long projectId, Long deviceTypeId, Long systemId) {
        return deviceDao.getSystemDeviceList(projectId,deviceTypeId,systemId);
    }

    @Override
    public Device findSystemNameDevice(Long deviceId, Long systemId) {
        return deviceDao.findSystemNameDevice(deviceId,systemId);
    }

    @Override
    public List<Map<String,Object>> getPowerDeviceList(Long projectId) {
        return deviceDao.getPowerDeviceList(projectId);
    }

    @Override
    public Boolean hasDevice(Long projectId) {
        Integer num=deviceDao.getDeviceCount(projectId);
        if(num>0){
            return true;
        }
        return false;
    }

    @Override
    public List<Device> getByProjectId(Long projectId) {
        return deviceDao.findByProjectId(projectId);
    }

    @Override
    public SafeDeviceCount safeDeviceStatus(Long projectId) {
        List<Map<String,Object>> deviceStatusData=deviceDao.getSafeDeviceStatus(projectId);
        if(CollectionUtils.isEmpty(deviceStatusData)){
            SafeDeviceCount safeDeviceCount=new SafeDeviceCount(0,0,0);
            return safeDeviceCount;
        }else{
            Integer inline=0;
            Integer outline=0;
            Integer total=0;
            for (Map<String, Object> deviceStatusDatum : deviceStatusData) {
                Integer status = Integer.parseInt(deviceStatusDatum.get("status").toString());
                if(status.equals(1)||status.equals(3)){
                    inline++;
                }else{
                    outline++;
                }
                total++;
            }
            SafeDeviceCount safeDeviceCount =new SafeDeviceCount();
            safeDeviceCount.setInline(inline);
            safeDeviceCount.setOutline(outline);
            safeDeviceCount.setTotal(total);
            return safeDeviceCount;
        }
    }

    @Override
    public List<Map<String, Object>> getPowerDeviceStatus(Long projectId) {
        return deviceDao.getPowerDeviceStatus(projectId);
    }
}