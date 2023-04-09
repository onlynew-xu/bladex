package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.AlarmWarn;
import com.steelman.iot.platform.entity.dto.AlarmWarnPower;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AlarmWarnService {
    /**
     * 获取告警处理状态
     * @param projectId
     * @param systemId
     * @return
     */
    AlarmHandleStatus getHandleStatus(Long projectId, Long systemId);

    /**
     * 告警次数数据统计
     * @param projectObj
     * @param systemObj
     * @return
     */
    List<AlarmCountStatistics> getAlarmCountByDate(Long projectObj, Long systemObj);

    /**
     * 获取未处理预警信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<InHandlerAlarm> inHandlerAlarm(Long projectId, long systemId);

    /**
     * 获取预警的处理率(智慧大屏使用)
     * @param projectId
     * @param systemId
     * @param areaId
     * @param buildingId
     * @return
     */
    String getHandlerStatus(Long projectId, Long systemId, Long areaId, Long buildingId);


    List<AlarmParentItemVo> getAlarmParentItem(Long projectId);
    /**
     * 获取预警信息的年份
     * @param projectId
     * @return
     */
    List<String> getYear(Long projectId);

    /**
     * 获取月份
     * @param projectId
     * @param year
     * @return
     */
    List<String> getMonth(Long projectId, String year);

    /**
     * 获取预警信息
     * @param projectId
     * @param areaId
     * @param buildingId
     * @param alarmParentItemId
     * @param year
     * @param month
     * @return
     */
    List<AlarmWarnInfoVo> getAlarmInfoVo(Long projectId, Long areaId, Long buildingId, Long alarmParentItemId, String year, String month);

    /**
     * 获取报警次数信息
     * @param projectId
     * @param areaId
     * @param buildingId
     * @param count
     * @param year
     * @param month
     * @return
     */
    List<AlarmCountInfo> getAlarmCountInfo(Long projectId, Long areaId, Long buildingId, Integer count, String year, String month);

    /**
     * 统计报警类型对应的数量信息
     * @param projectId
     * @param systemId
     * @return
     */
    Map<String, List<AlarmTypeStatistic>> statisticAlarmType(Long projectId, Long systemId);

    /**
     * 统计设备类型对应的报警数量
     * @param projectId
     * @param systemId
     * @return
     */
    Map<String, List<DeviceTypeAlarmStatistic>> deviceTypeStatistic(Long projectId, Long  systemId);

    /**
     * 获取设备的离线记录
     * @param projectId
     * @param systemId
     * @param flag
     * @return
     */
    List<DeviceOfflineInfo> getOfflineInfo(Long projectId, Long systemId, String flag);

    /**
     * 获取有风险的设备
     * @param projectId
     * @param systemId
     * @param part
     * @return
     */
    List<DeviceRiskInfo> getRiskDevice(Long projectId, Long systemId, String part);

    /**
     * 获取电气火灾设备的预警统计信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    List<DeviceTypeAlarmStatistic> getSafeAlarmTypeStatistic(Long projectId, Long deviceId, Long systemId);

    /**
     *安全预警 获取设备报警的历史记录信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    List<AlarmSimpleInfo> getDeviceAlarmHistory(Long projectId, Long deviceId, Long systemId);

    /**
     * 移除对应系统的预警信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    int removeByDeviceSystem(Long projectId, Long deviceId, Long systemId);

    List<AlarmWarnVo> getAlarmCount(Long projectId,Long systemId);

    List<Map<String, Object>> alarmCount(Long projectId,Long systemId);

    List<AlarmWarnItemVo> getAlarmList(Long projectId,Long systemId,String part);

    List<AlarmWarnItemVo> getDeviceAlarmInfo(Long projectId, Long deviceId, Long systemId);

    List<Map<String, Object>> getDeviceAlarmCount(Long projectId, Long deviceId, Long systemId);

    /**
     * 获取项目中有未处理预警信息的设备
     * @param projectId
     * @return
     */
    List<Long> getInHandlerSafeDevice(Long projectId);

    List<AlarmWarn> getByDeviceId(Long deviceId, Long l, String s, Date date);

    /**
     * 删除设备的报警信息
     * @param deviceId
     * @return
     */
    int removeByDeviceId(Long deviceId);

    /**
     * 获取电房未处理预警信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<PowerAlarmWarnVo> getPowerAlarmWarnVo(Long projectId, Long systemId);

    Integer getPowerAlarmWarnCount(Long projectId, Long systemId);

    List<AlarmWarnPower> getAlaramWarnByDeviceId(Long deviceId, Long systemId);

    List<Map<String, Object>> getPowerAlarmCount(List<PowerDeviceInfo> deviceList);

    List<AlarmWarnItemVo> getPowerHistory(List<PowerDeviceInfo> deviceList);

    int removePowerAlarm(Long deviceId);


    List<Map<String, Object>> getDayAndMonthAlarmCount(Long projectId, String year, String month, String day);

    /**
     * 根据能耗类型 统计当前月产生了多少条告警
     * @param projectId
     * @param yearStr
     * @param valueOf
     * @return
     */
    List<Map<String, Object>> getMonthConsumeCount(Long projectId, String yearStr, String valueOf);

    /**
     * 根据能耗类型 统计当前季度产生了多少条告警
     * @param projectId
     * @param year
     * @param monthListNow
     * @return
     */
    List<Map<String, Object>> getQuarterCount(Long projectId, String year, List<String> monthListNow);

    /**
     * 获取项目的所有报警信息
     * @param l
     * @return
     */
    List<AlarmWarn> getByProjectId(long l);

    /**
     * 更新报警值
     * @param alarmWarnList
     * @return
     */
    Integer updateAlarmValue(List<AlarmWarn> alarmWarnList);

    /**
     * web用电安全大屏告警数量及处理率统计
     * @param projectId
     * @return
     */
    AlarmWarnStatistic getSafePingCountStatistic(Long projectId);

    /**
     * web用电安全大屏告警数量月环比
     * @param projectId
     * @return
     * @throws Exception
     */
    Map<String, Object> getSafeAlarmRingData(Long projectId) throws Exception;

    /**
     * web用电安全大屏告警最近的数量 1:近七天 2:近15日 3:近30天
     * @param projectId
     * @param type
     * @return
     */
    Map<String, Object> getAlarmRecentData(Long projectId, Integer type) throws Exception;

    /**
     * web用电安全 报警类型统计
     * @param projectId
     * @return
     */
    List<DeviceTypeAlarmStatistic> getAlarmTypeStatistic(Long projectId);

    /**
     * web用电安全 未处理告警信息
     * @param projectId
     * @return
     */
    List<AlarmWarnDetailInfo> getInHandlerWarnInfo(Long projectId);

    /**
     * web用电安全 区域报警数量图
     * @param projectId
     * @return
     */
   List<DeviceTypeAlarmStatistic> getAreaTypeAlarm(Long projectId);

    int save(AlarmWarn alarmWarn);

    List<Map<String, Object>> getDeviceTypeStatistic(Set<Long> deviceSet);
}
