package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.DeviceStatisticsInfo;
import com.steelman.iot.platform.entity.vo.SafeDeviceCenter;
import com.steelman.iot.platform.entity.vo.DianQiDeviceCenterInfo;
import com.steelman.iot.platform.entity.vo.XiaoFangDeviceCenterInfo;
import com.steelman.iot.platform.largescreen.vo.DeviceStatusStatistic;
import com.steelman.iot.platform.largescreen.vo.SafeDeviceCount;
import com.steelman.iot.platform.utils.Result;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeviceService {
    /**
     * 获取设备的统计信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<DeviceStatisticsInfo> getDeviceStatistic(Long projectId,Long systemId);

    void insertSelective(Device record);
    void update(Device record);
    void deleteById(Long id);

    Pager<T> selectByAll(Map<String, Integer> params, Long deviceTypeId, Long projectId);
    Device findById(Long id);

    /**
     * 添加设备
     * @param device
     * @return
     */
    int insert(Device device);

    /**
     * 智慧大屏统计设备信息使用的接口
     * @param projectId
     * @param systemId
     * @return
     */
    Map<String, Integer> staticDeviceDaPin(long projectId, Long systemId);


    /**
     * 智慧大屏使用的接口 统计设备各个状态的设备数量
     * @param projectId
     * @param areaId
     * @param buildingId
     * @return
     */
    DeviceStatusStatistic  deviceStatusAndAlarmPer(Long projectId,Long areaId,Long buildingId);

    /**
     * 安全预警设备中心信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<SafeDeviceCenter> safeDeviceCenter(Long projectId, Long systemId);

    /**
     * 获取电气火灾的设备信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    DianQiDeviceCenterInfo getSafeDeviceCenterInfo(Long projectId, Long deviceId, Long systemId);

    /**
     * 获取消防电源设备信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    XiaoFangDeviceCenterInfo getXiaoFangCenterInfo(Long projectId, Long deviceId, Long systemId);

    /**
     * 从安全预警系统中的移除设备信息
     * @param projectId
     * @param deviceId
     * @return
     */
    Boolean removeByDeviceSafeSystem(Long projectId, Long deviceId) throws Exception;

    /**
     * 从智慧用电系统中的移除设备信息
     * @param projectId
     * @param deviceId
     * @return
     */
    Boolean removeByDeviceSmartSystem(Long projectId, Long deviceId) throws Exception;

    /**
     * 从能源管理系统中的移除设备信息
     * @param projectId
     * @param deviceId
     * @return
     */
    Boolean removeByDeviceEnergySystem(Long projectId, Long deviceId) throws Exception;


    /**
     * 根据设备sn 获取设备信息
     * @param serialNum
     * @return
     */
    Device selectBySerialNum(String serialNum);

    /**
     * 根据设备类型 获取设备列表信息
     * @param projectId
     * @param deviceTypeId
     * @return
     */
    List<Device> selectByDeviceTypeId(Long projectId, Long deviceTypeId);

    /**
     * 获取末端试水详情页面数据
     * @param deviceId
     * @param projectId
     * @return
     */
    SafeWaterDeviceInfo waterSafeInfo(Long deviceId, Long projectId);

    /**
     * 获取烟雾探测详情页面数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeSmokeDeviceInfo smokeInfo(Long projectId, Long deviceId);

    /**
     * 获取防火门详情页面数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeDoorDeviceInfo doorInfo(Long projectId, Long deviceId);

    /**
     * 获取互联网温湿度设备的页面详情数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeTempatureHumidity tempatureHumidityInfo(Long projectId, Long deviceId);

    /**
     * 获取Mt300的数据 适用于安全预警系统的数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeMT300DeviceCenter safeMT300DeviceInfo(Long projectId, Long deviceId);
    /**
     * 获取Mt300c的数据 适用于安全预警系统的数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeMT300CDeviceCenter safeMT300CDeviceInfo(Long projectId, Long deviceId);

    /**
     * 获取Mt300S的数据 适用于安全预警系统的数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeMT300SDeviceCenter safeMT300SDeviceInfo(Long projectId, Long deviceId);
    /**
     * 获取补偿控制器的数据 适用于安全预警系统的数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeCompensateDeviceCenter safeCompensateDeviceInfo(Long projectId, Long deviceId);

    /**
     * 获取滤波控制器的数据 适用于安全预警系统的数据
     * @param projectId
     * @param deviceId
     * @return
     */
    SafeWaveDeviceCenter safeWaveDeviceInfo(Long projectId, Long deviceId);

    /**
     * 修改设备参数接口630s(页面详情部分)
     * @param deviceId
     * @return
     */
    SafeDeviceVariableParam getSafeDeviceVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  EA1128
     * @param deviceId
     * @return
     */
    SmartDeviceVariableParam getSmartDeviceVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  烟感设备
     * @param deviceId
     * @return
     */
    SmokeDeviceVariableParam getSmokeDeviceVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  末端试水
     * @param deviceId
     * @return
     */
    WaterDeviceVariableParam getWaterDeviceVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  防火门
     * @param deviceId
     * @return
     */
    DoorDeviceVariableParams getDoorWaterVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  滤波控制器
     * @param deviceId
     * @return
     */
    WaveVariableParam getWaveVariableParam(Long deviceId);
    /**
     * 修改设备参数接口(页面详情部分)  补偿控制器
     * @param deviceId
     * @return
     */
    CompensateVariableParam getCompensateVariableParam(Long deviceId);

    /**
     * 修改设备参数接口(页面详情部分) 互联网温湿度
     * @param deviceId
     * @return
     */
    TempatureVariableParam getTempatureVariableParam(Long deviceId);

    /**
     * 修改设备参数接口(页面详情部分)
     * @param deviceId
     * @return
     */
    SuperDeviceVariableParam getSuperDeviceVariableParam(Long deviceId);




    /**
     * 修改设备的参数
     * @param paramMap
     * @return
     */
    Boolean updateDeviceParam(Map<String, Object> paramMap) throws Exception;




    /**
     * 添加设备系统
     * @param serialNum
     * @param systemId
     * @return
     */
    Boolean addDeviceSystem(String serialNum, Long systemId) throws Exception;

    /**
     * 设备维保详情页数据
     * @param deviceId
     * @return
     */
    DeviceMaintenanceDetail getDeviceMaintenanceDetail(Long deviceId);

    /**
     * 删除设备
     * @param projectId
     * @param deviceId
     * @return
     */
    Boolean removeDevice(Long projectId, Long deviceId) throws Exception;

    /**
     * 保存设备数据
     * @param paramMap
     * @return
     */

    Result<String> saveDevice(Map<String, Object> paramMap) throws Exception;

    List<Device> getAreaDevice(Long areaId, Long projectId);

    /**
     * 获取设备总计的电度数据
     * @param deviceId
     * @return
     */
    MeasureDataStr getMeasureTotalData(Long deviceId);

    /**
     * 获取设备过去六天的日电度数据
     * @param deviceId
     * @param projectId
     * @return
     */
    List<MeasureDateData> getPastSixDayMeasure(Long deviceId, Long projectId, Date begDate,Date endDate);

    /**
     * 获取设备当日电度
     * @param deviceId
     * @return
     */
    MeasureDateData getTodayMeasureData(Long deviceId,String yearMonthDay);

    /**
     * 获取过去日期的电度数据
     * @param deviceId
     * @param projectId
     * @param lastBegDate
     * @param lastEndDate
     * @return
     */
    List<MeasureDateData> getPastDateDate(Long deviceId, Long projectId, Date lastBegDate, Date lastEndDate);

    /**
     * 获取特定一天的电度数据
     * @param deviceId
     * @param lastTimeStr
     * @return
     */
    MeasureDateData getDayMeasureData(Long deviceId, String lastTimeStr);

    /**
     * 获取上个月电度数据
     * @param deviceId
     * @param year
     * @param month
     * @return
     */
    List<MeasureDateData> getLastMonthDayMeasureData(Long deviceId, String year, String month);

    /**
     * 获取当日的月电度
     * @param deviceId
     * @param yearMonthDay
     * @return
     */
    MeasureDateData getTodayMonthMeasure(Long deviceId, String yearMonthDay);

    /**
     * 获取系统类型 对应的设备
     * @param projectId
     * @param deviceTypeId
     * @param systemId
     * @return
     */
    List<EntityDto> getSystemDeviceList(Long projectId, Long deviceTypeId, Long systemId);

    /**获取具有系统特定名称的设备信息
     * @param deviceId
     * @return
     */
    Device findSystemNameDevice(Long deviceId,Long systemId);

    List<Map<String, Object>> getPowerDeviceList(Long projectId);


    /**
     * 检验是否还要设备
     * @param projectId
     * @return
     */
    Boolean hasDevice(Long projectId);

    /**
     * 根据项目Id获取设备信息
     * @param projectId
     * @return
     */
    List<Device> getByProjectId(Long projectId);

    /**
     * 安全预警设备状态统计
     * @param projectId
     * @return
     */
    SafeDeviceCount safeDeviceStatus(Long projectId);

    /**
     * 获取电房设备状态
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getPowerDeviceStatus(Long projectId);
}
