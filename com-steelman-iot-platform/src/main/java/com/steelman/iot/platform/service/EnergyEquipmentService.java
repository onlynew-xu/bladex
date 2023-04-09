package com.steelman.iot.platform.service;

import com.steelman.iot.platform.energyManager.dto.EnergyMeasureDataEntity;
import com.steelman.iot.platform.energyManager.entity.EquipmentDateTotalMeasure;
import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyHourDifferData;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.*;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/29 0029 10:48
 * @Description:
 */
public interface EnergyEquipmentService {
    void save(EnergyEquipment equipment);

    void delete(Long id);

    List<EnergyEquipment> getList( Long boxLoopId, Long energyTypeId);

    EnergyEquipment getInfo(Long id);

    void update(EnergyEquipment equipment);

    List<Map<String, Object>> countEnergy(Long projectId);

    List<Map<String, Object>> countConsume(Long projectId);

    List<Map<String,Object>> getEnergyEquipment(Long projectId);

    List<Map<String, Object>> getConsumeEquipment(Long projectId);

    EquipmentInfoVo getCenterInfo(Long id);

    Boolean saveEnergyDevice(EnergyEquipment equipment, Map<String, Object> paramMap,Boolean bindingStatus);

    /**
     * 删除能源设备
     * @param id
     * @return
     */
    Boolean deleteEquitment(Long id);

    /**
     * 根据能源类型获取能源设备信息
     * @param projectId
     * @param energyTypeId
     * @return
     */
    List<EnergyEquipment> getByEnergyTypeId(Long projectId, Long energyTypeId);


    List<EnergyEquipment> getByEnergyConsumeTypeId(Long projectId, Long consumeTypeId);



    /**
     * 获取总能耗数据
     * @param projectId
     * @return
     */
    MeasureData getTotalMeasurement(Long projectId);

    /**
     * 获取能源设备类型数据
     * @param projectId
     * @return
     */
    List<EnergyEquipmentStatistic> statisticEnergyType(Long projectId);

    /**
     * 获取能耗类型统计数据
     * @param projectId
     * @return
     */
    List<EnergyConsumeStatistic> consumeStatistic(Long projectId);

    /**
     * 获取近一周 每日能耗
     * @param projectId
     * @return
     */
    Map<String, List<WeekEnergyConsumeStatistic>> getWeekEnergyStatistic(Long projectId);

    /**
     * 获取管理中心返回数据
     * @param projectId
     * @return
     */
    EquipmentCenterInfo getCenterInfoData(Long projectId);

    /**
     * 获取所有区域能源设备
     * @param areaId
     * @param projectId
     * @return
     */
    List<EnergyEquipment> getAreaEquipment(Long areaId, Long projectId);

    /**
     * 能源设备详情页数据
     * @param equipmentId
     * @param projectId
     * @return
     */
    EquipmentInfoDto getEquipmentInfo(Long equipmentId, Long projectId);

    /**
     * 获取能源设备
     * @param energyTypeId
     * @param projectId
     * @return
     */
    List<EnergyEquipment> selectByEnergyTypeId(Long energyTypeId, Long projectId);

    /**
     * 获取设备近期能耗
     * @param equipmentId
     * @param projectId
     * @return
     */
    Map<String, List<WeekEnergyConsumeStatistic>> getEquipmentWeekInfo(Long equipmentId, Long projectId);

    /**
     * 获取能耗统计数据
     * @param projectId
     * @param consumeTypeId
     * @param energyTypeId
     * @param beginTime
     * @param endTime
     * @param order
     * @return
     */
    Map<String,List<EnergyDeviceInfo>> getEnergyDateStatistic(Long projectId, Long consumeTypeId, Long energyTypeId, String beginTime, String endTime, String order,String part);

    /**
     * 获取设备的类比数据
     * @param projectId
     * @param equipmentId
     * @return
     */
    Map<String, List<EnergyConsumeStatisticDetail>> getEquipmentContrastData(Long projectId, Long equipmentId);

    /**
     * 获取近半年的月电度数据
     * @param projectId
     * @return
     */
    Map<String, List<EnergyYearMonthConsumeDate>> getYearMonthConsumeData(Long projectId);

    /**
     * 自动抄表数据
     * @param projectId
     * @param consumeTypeId
     * @param energyTypeId
     * @param order
     * @param part
     * @return
     */
    Map<String, List<EnergyDeviceInfo>> getEnergyReading(Long projectId, Long consumeTypeId, Long energyTypeId, String order, String part);

    /**
     * 根据名称获取能源设备
     * @param name
     * @param projectId
     * @return
     */
    EnergyEquipment findByName(String name, Long projectId);

    /**
     * 获取设备详情页数据
     * @param projectId
     * @param equipmentId
     * @return
     */
    EquipmentInfo getEquipmentManagerInfo(Long projectId, Long equipmentId);

    /**
     * 修改能源设备参数返回数据
     * @param projectId
     * @param equipmentId
     * @return
     */
    EquipmentVariable getVariableParam(Long projectId, Long equipmentId);

    /**
     * 更新能源设备参数
     * @param equipmentVariable
     * @return
     */
    String updateParam(EquipmentVariable equipmentVariable);

    /**
     * 获取项目所有设备
     * @param projectId
     * @return
     */
    List<EnergyEquipment> getByProjectId(Long projectId);

    /**
     * 获取当日和当月能耗统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getDayMonthStatistic(Long projectId);

    /**
     * 获取昨日能耗数据
     * @param projectId
     * @return
     */
    Integer getYesterDayStatistic(Long projectId);

    /**
     * 获取日电度柱状图
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getTodayMeasure(Long projectId);

    /**
     * 获取上个月每日电度数据
     * @param projectId
     * @param yearMonth
     * @return
     */
    List<Map<String, Object>> getLastMonthMeasure(Long projectId,String yearMonth);

    /**
     * 获取本月已记录的数据
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getMonthMeasure(Long projectId,String yearMonth);

    /**
     * 获取当天的月总电度
     * @param projectId
     * @param format
     * @return
     */
    List<Map<String, Object>> getMonthTodayMeasure(Long projectId, String format);

    /**
     * 获取当日的总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getTotalTotalMeasure(Long projectId, String yearMonthDay);

    /**
     * 获取某天的总电度
     * @param projectId
     * @param yearLast
     * @return
     */
    List<EquipmentDateTotalMeasure> getLastMeasure(Long projectId, String yearLast);

    /**
     * 查询某天的月总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getMonthTotalMeasure(Long projectId, String yearMonthDay);

    /**
     * 获取日总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getTodayTotalMeasure(Long projectId, String yearMonthDay);

    /**
     * 获取能源设备当天的时有功功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSmartActivePowerData(Long projectId, Long consumeTypeId);

    /**
     * 获取能源设备的时有功功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSuperActivePowerData(Long projectId, Long consumeTypeId);


    /**
     * 获取能源设备当天的时无功功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSmartReactivePowerData(Long projectId, Long consumeTypeId);


    /**
     * 获取能源设备当天的时无功功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSuperReactivePowerData(Long projectId, Long consumeTypeId);

    /**
     * 获取能源设备当天的时视在功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSmartApparentPowerData(Long projectId, Long consumeTypeId);

    /**
     * 获取能源设备当天的时视在功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSuperApparentPowerData(Long projectId, Long consumeTypeId);

    /**
     * 获取能源设备的有功功率和视在功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getPowerAndApparentPowerList(Long projectId, Long consumeTypeId);

    /**
     * 获取能源设备的有功功率和视在功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getPowerAndApparentPowerSuperList(Long projectId, Long consumeTypeId);

    /**
     * 获取电压畸变率
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getThdvSmartList(Long projectId, Long consumeTypeId);



    /**
     * 获取电流畸变率
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getThdiSmartList(Long projectId, Long consumeTypeId);


    /**
     * 获取过去某个指定日期的电度数据
     * @param projectId
     * @param consumeTypeId
     * @param begTime
     * @return
     */
    List<EnergyMeasureDataEntity> getEnergyPastDateStatistic(Long projectId, Long consumeTypeId, String begTime);

    /**
     * 获取今日电度数据
     * @param projectId
     * @param consumeTypeId
     * @param format
     * @return
     */
    List<EnergyMeasureDataEntity> getEnergyPastTodayDateStatistic(Long projectId, Long consumeTypeId, String format);

    /**
     * 获取指定年的电度数据
     * @param projectId
     * @param year
     * @return
     */
    List<Map<String, Object>> getYearMonthMeasure(Long projectId, String year);

    /**
     * 获取当月的总电度数
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getMonthTotal(Long projectId);

    /**
     * 获取设备总数
     * @param projectId
     * @return
     */
    Long getCount(Long projectId);

    /**
     * 获取每台设备当前月的电度数据
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getMonthDetail(Long projectId);


    /**
     * 获取过去指定月的每台设备的电度数据
     * @param projectId
     * @param valueOf
     * @param month
     * @return
     */
    List<Map<String, Object>> getLastMonthDetail(Long projectId, String valueOf, String month);

    /**
     * 获取本月和今天的电度数据统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getDayAndMonthMeasure(Long projectId,String yearMonthDay);


    /**
     * 获取过去某天的电度数据 (根据能耗类型进项统计)
     * @param projectId
     * @param tomorrow
     * @return
     */
    List<Map<String, Object>> getLastDayConsumeMeasure(Long projectId, String tomorrow);


    /**
     * 获取历史某个月的总能耗
     * @param projectId
     * @param yearStr
     * @param lastMonthStr
     * @return
     */
    List<Map<String, Object>> getLastMonthTotalMeasure(Long projectId, String yearStr, String lastMonthStr);

    /**
     * 根据能耗类型 获取月份集合的 月总耗能和
     * @param projectId
     * @param year
     * @param monthListNow
     * @return
     */
    List<Map<String, Object>> getDataMonthMapByList(Long projectId, String year, List<String> monthListNow);

    /**
     * 根据能耗类型 获取年的月份集合
     * @param projectId
     * @param valueOf
     * @return
     */
    List<Map<String, Object>> getDataYearMeasure(Long projectId, String valueOf);

    /**
     * 根据能耗类型 获取年的总数据集合
     * @param projectId
     * @param year
     * @return
     */
    List<Map<String, Object>> getDataYearTotalMeasure( Long projectId, String year);

    /**
     * 获取过去指定月的 总能耗
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getMonthTotalLastMeasure(Long projectId, String year, String month);

    /**
     * 获取历史的电压谐波数据
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getThdvSuperList(Long projectId, Long consumeTypeId);
    /**
     * 获取历史的电压谐波数据
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getThdiSuperList(Long projectId, Long consumeTypeId);

    /**
     * 获取过去指定日期的时电度数据
     * @param projectId
     * @param format
     * @return
     */
    List<EnergyHourDifferData> getHourDiffData(Long projectId, String format);

    /**
     * 获取过去指定日期的
     * @param projectId
     * @param format
     * @return
     */
    EnergyDayData getDayTotal(Long projectId, String format);

    /**
     * 获取上个月的月总电度
     * @param projectId
     * @return
     */
    EnergyMonthData getMonthLastMeasure(Long projectId);

    List<Map<String,Object>> getDayMeasure(Long projectId);

    Long getPastMonth(List<String> monthListNow, Long projectId, String year);
    //获取过去每台设备的年能耗
    List<EquipmentDateTotalMeasure> getPastYearMonth(Long projectId, String year);

    List<Map<String,Object>> getLastYearMonth(Long projectId, String valueOf);

    /**
     * 获取月电度数据
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<EquipmentRankReport> getRankReportMonthData(Long projectId, String year,String month);

    /**
     * 获取之前月的电度数据
     * @param projectId
     * @param valueOf
     * @return
     */
    List<EquipmentRankReport> getRankReportLast(Long projectId, String valueOf);

    /**
     * 获取设备当前月的电度数据(优化掉线的情况)
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getMonthDataDetail(Long projectId);

//------------------------------------------------------
    /**
     * 获取设备和状态(大屏接口)
     * @param projectId
     * @return
     */
    List<EnergyStatus> getEnergyStatistic(Long projectId);

    /**
     * 获取设备的总消耗
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getEquipmentTotal(Long projectId);
}
