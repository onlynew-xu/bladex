package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.energyManager.dto.EnergyMeasureDataEntity;
import com.steelman.iot.platform.energyManager.entity.EquipmentDateTotalMeasure;
import com.steelman.iot.platform.entity.DeviceMeasurement;
import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyHourDifferData;
import com.steelman.iot.platform.entity.dto.*;
import com.steelman.iot.platform.entity.vo.EquipmentInfoVo;
import com.steelman.iot.platform.entity.vo.EquipmentItemVo;
import com.steelman.iot.platform.entity.vo.EquipmentRankReport;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EnergyEquipmentDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipment record);

    int insertSelective(EnergyEquipment record);

    EnergyEquipment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipment record);

    int updateByPrimaryKey(EnergyEquipment record);

    List<EnergyEquipment> selectByLoop( @Param("boxLoopId") Long boxLoopId, @Param("energyTypeId") Long energyTypeId);

    List<Map<String, Object>> selectCountGroupEnergyType(Long projectId);

    List<Map<String, Object>> selectCountGroupConsumeType(Long projectId);

    List<EquipmentItemVo> selectProjectEnergyType(@Param("projectId") Long projectId, @Param("energyTypeId") Long energyTypeId);

    List<EquipmentItemVo> selectProjectConsumeType(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId);

    EquipmentInfoVo selectCenterInfo(Long id);

    /**
     * 获取能源设备列表信息
     * @param projectId
     * @param energyTypeId
     * @return
     */
    List<EnergyEquipment> getByEnergyTypeId(@Param("projectId") Long projectId, @Param("energyTypeId") Long energyTypeId);

    /**
     * 获取所有能源设备当前的能耗
     * @param projectId
     * @return
     */
    List<DeviceMeasurement> getAllEnergyEquitmentMeausure(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取每个能源类型对应的数据
     * @param projectId
     * @return
     */
    List<Map<String, Object>> countEnergyTypeNum(Long projectId);

    /**
     * 获取每个能耗类型对应的数据
     * @param projectId
     * @return
     */
    List<Map<String, Object>> consumeStatistic(Long projectId);

    /**
     * 获取最近6天的日能耗数据
     * @param projectId
     * @return
     */
    List<MeasureIntegerDate> getWeekCousume(@Param("projectId") Long projectId, @Param("begDate") Date begDate,@Param("endDate") Date endDate);

    /**
     * 获取今日能耗数据
     * @param projectId
     * @return
     */
    List<MeasureIntegerDate> getTodayMeasure(@Param("projectId") Long projectId,@Param("todayDate") String date);

    /**
     * 获取设备中心数据
     * @param projectId
     * @return
     */
    List<EquipmentSimpleInfo> getEquipmentSimpleInfo(@Param("projectId") Long projectId);

    List<EnergyEquipment> getAreaEquipment(@Param("areaId") Long areaId,@Param("projectId") Long projectId);

    /**
     * 获取能耗设备
     * @param energyTypeId
     * @param projectId
     * @return
     */
    List<EnergyEquipment> selectByEnergyTypeId(@Param("energyTypeId") Long energyTypeId, @Param("projectId") Long projectId);

    /**
     * 获取指定日期的总耗能数据
     * @param projectId
     * @param consumeTypeId
     * @param energyTypeId
     * @param beginTime
     * @return
     */
    List<EnergyMeasureDateData> getEnergyDateStatistic(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("energyTypeId") Long energyTypeId, @Param("beginTime") String beginTime);

    List<MeasureIntegerDate> getYearMonthData(@Param("projectId") Long projectId, @Param("endDate") LocalDate localDate, @Param("strDate") LocalDate localDate2);

    MeasureIntegerDate getMonthData(@Param("projectId") Long projectId, @Param("yearMonthDay") String format);

    /**
     * 获取每个设备当日总电度数据
     * @param projectId
     * @param consumeTypeId
     * @param energyTypeId
     * @return
     */
    List<EnergyMeasureDateData> getTodayMeasureDate(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId, @Param("energyTypeId") Long energyTypeId,@Param("yearMonthDay") String yearMonthDay);

    EnergyEquipment findByName(@Param("name") String name, @Param("projectId") Long projectId);

    EquipmentVariable getVariableParam(@Param("equipmentId") Long equipmentId, @Param("projectId") Long projectId);

    /**
     * 更新能源设备的区域信息未null
     * @param id
     * @return
     */
    int updateAreaNull(@Param("id") Long id);

    /**
     * 更新使用公司为null
     * @param id
     * @return
     */
    int updateCompanyIdNull(@Param("id") Long id);


    List<EnergyEquipment> getByEnergyConsumeTypeId(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId);

    List<EnergyEquipment> getByProjectId(@Param("projectId") Long projectId);

    /**
     * 获取当日和当月的能耗统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getDayMonthStatistic(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取指定日期的总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    Integer getPastStatistic(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取日电度时柱状图
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getDayHourMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取本月  每天已记录 所有设备月总电度数据(根据天算和)
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getLastMonthMeasure(@Param("projectId") Long projectId, @Param("year") String year, @Param("month") String month);


    /**
     * 获取本月  每天已记录 所有设备月总电度数据(根据天算和)
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getMonthMeasure(@Param("projectId") Long projectId, @Param("year") String year,@Param("month") String month);

    /**
     * 获取今天  所有设备月总电度信息
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getMonthTodayMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取今天   每台设备的总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getTotalTotalMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取指定日期  每台设备的总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getLastMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取今天  每台设备的月总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getMonthTotalMeasure(@Param("projectId") Long projectId,  @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取今天 每台设备日总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EquipmentDateTotalMeasure> getTodayTotalMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取设备时有功功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSmartActivePowerData(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取设备时有功功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSuperActivePowerData(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取设备时有功功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSmartReactivePowerData(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取设备时有功功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @return
     */
    List<Map<String, Object>> getSuperReactivePowerData(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取设备时视在功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getSmartApparentPowerData(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取设备时视在功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getSuperApparentPowerData(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取能源设备的有功功率和无功功率(1128)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getPowerAndApparentPowerList(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId,  @Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取能源设备的有功功率和无功功率(1129)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getPowerAndApparentPowerSuperList(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取电压畸变率
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getThdvSmartList(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取过去指定日期的  每台的电度数据(日 月 总)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<EnergyMeasureDataEntity> getEnergyPastDateStatistic(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取今日的  每台设备最新电度数据(日  月 总)
     * @param projectId
     * @param consumeTypeId
     * @param yearMonthDay
     * @return
     */
    List<EnergyMeasureDataEntity> getEnergyPastTodayDateStatistic(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId,@Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取指定年月的  所有设备的月电度和
     * @param projectId
     * @param year
     * @return
     */
    List<Map<String, Object>> getYearMonthMeasure(@Param("projectId") Long projectId,@Param("year") String year);

    /**
     * 获取今天     所有设备的月总电度和
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getMonthTotal(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取设备数量
     * @param projectId
     * @return
     */
    Long getCount(@Param("projectId") Long projectId);


    /**
     * 获取今天 每台设备 日总电度 月总电度和 总电度
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getMonthDetail(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取过去时间段  每台设备的月电度数据 (获取月最后一天)
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getLastMonthDetail(@Param("projectId") Long projectId,@Param("year") String year, @Param("month") String month);

    /**
     * 根据消耗类型 获取今天的日总消耗和月总消耗
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getDayAndMonthMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 根据能耗类型 获取过去某天的日总消耗和月总消耗
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getLastDayConsumeMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);


    /**
     * 根据能耗类型 获取指定日期每个类型 所有设备的月总电度数据
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getLastMonthTotalMeasure(@Param("projectId") Long projectId, @Param("year") String year,@Param("month") String month);

    /**
     * 根据能耗类型 获取指定月份集合 所有设备的月总电度数据
     * @param projectId
     * @param year
     * @param monthList
     * @return
     */
    List<Map<String, Object>> getDataMonthMapByList(@Param("projectId") Long projectId, @Param("year") String year,@Param("monthList") List<String> monthList);

    /**
     * 根据能耗类型 获取指定年 所有设备的月总电度数据
     * @param projectId
     * @param year
     * @return
     */
    List<Map<String, Object>> getDataYearMeasure(@Param("projectId") Long projectId, @Param("year") String year);

    List<Map<String, Object>> getDataYearTotalMeasure(@Param("projectId") Long projectId,@Param("year") String year);

    List<Map<String, Object>> getMonthTotalLastMeasure(@Param("projectId") Long projectId, @Param("year") String year, @Param("month") String month);

    List<Map<String, Object>> getThdvSuperList(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId, @Param("yearMonthDay") String format);

    List<Map<String, Object>> getThdiSmartList(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId,@Param("yearMonthDay") String format);

    List<Map<String, Object>> getThdiSuperList(@Param("projectId") Long projectId, @Param("consumeTypeId") Long consumeTypeId,@Param("yearMonthDay") String format);

    /**
     * 获取所有设备 指定日的 时消耗总量
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<EnergyHourDifferData> getHourDiffData(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取所有设备 过去某日的日总消耗
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    EnergyDayData getDayTotal(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取所有设备 过去某月的月总消耗
     * @param year
     * @param month
     * @param projectId
     * @return
     */
    EnergyMonthData getMonthLastMeasure(@Param("year") String year,@Param("month") String month, @Param("projectId") Long projectId);


    /**
     * 获取所有设备当天日消耗和月消耗
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String,Object>> getDayMeasure(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);


    /**
     * 获取指定年月的总消耗 多月(IN)
     * @param monthList
     * @param projectId
     * @param year
     * @return
     */
    Long getPastMonth(@Param("monthList") List<String> monthList, Long projectId, String year);


    /**
     * 获取每台设备 已统计的年总消耗
     * @param projectId
     * @param year
     * @return
     */
    List<EquipmentDateTotalMeasure> getPastYearMonth(@Param("projectId") Long projectId, @Param("year") String year);

    /**
     * 获取所有设备 已统计的年总消耗
     * @param projectId
     * @param year
     * @return
     */
    List<Map<String, Object>> getLastYearMonth(@Param("projectId") Long projectId, @Param("year") String year);


    /**
     * 获取每台设备本月的消耗
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<EquipmentRankReport> getRankReportMonthData(@Param("projectId") Long projectId, @Param("year") String year ,@Param("month") String month);

    /**
     * 获取每台设备指定年份 已统计月份的能耗总计
     * @param projectId
     * @param year
     * @return
     */
    List<EquipmentRankReport> getRankReportLast(@Param("projectId") Long projectId, @Param("year") String year);

    /**
     * 获取每台设备当前月的数据 优化掉线的情况
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getMonthDataDetail(@Param("projectId") Long projectId, @Param("year") String year, @Param("month") String month);


    /**
     * 获取设备和状态
     * @param projectId
     * @return
     */
    List<EnergyStatus> getEnergyStatistic(@Param("projectId") Long projectId);

    /**
     * 获取设备总消耗
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getEquipmentTotal(@Param("projectId") Long projectId);
}