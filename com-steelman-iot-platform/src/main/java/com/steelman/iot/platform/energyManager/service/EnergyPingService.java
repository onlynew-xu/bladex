package com.steelman.iot.platform.energyManager.service;

import com.steelman.iot.platform.energyManager.dto.*;
import com.steelman.iot.platform.energyManager.entity.MonthMeasureStatistic;
import com.steelman.iot.platform.entity.dto.EnergyReportDataDetail;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.vo.EnergyConsumeStatisticDetail;
import com.steelman.iot.platform.entity.vo.EnergyStatisticYearDetail;
import com.steelman.iot.platform.entity.vo.EquipmentRankReport;

import java.util.List;
import java.util.Map;

public interface EnergyPingService {
    /**
     * 统计设备在线和离线数量
     * @param projectId
     * @return
     */
    CountStatistic countStatistic(Long projectId);

    /**
     * 获取日能耗 昨日能耗 月能耗数据
     * @param projectId
     * @return
     */
    MeasureDayMonthStatistic dayStatistic(Long projectId);

    /**
     * 获取日电度趋势图
     * @param projectId
     * @return
     */
    Map<String, Object> getHourMeasureStatistic(Long projectId);

    /**
     * 获取月环比数据
     * @param projectId
     * @return
     */
    List<EnergyConsumeStatisticDetail> monthCompareStatistic(Long projectId);

    /**
     * 获取电度统计 年 1 季 2 月 3 日 4
     * @param projectId
     * @param type
     * @return
     */
    TotalMeasure totalStatistic(Long projectId, Integer type);

    /**
     * 获取设备能源类型列表
     * @param projectId
     * @return
     */
    List<EntityDto> getConsumeList(Long projectId);

    /**
     * 获取设备当天的能源质量数据
     * @param projectId
     * @param consumeTypeId
     * @param dataType "activePower":"有用功率","reactivePower":"无功功率",
     *                 "apparentPower":"视在功率","powerFactor":"功率因数",
     *                 "thdv":"电压谐波"
     * @return
     */
    Map<String, Object> getHourDeviceData(Long projectId, Long consumeTypeId, String dataType);

    /**
     * 获取指定时间间隔的电度数据
     * @param projectId
     * @param consumeTypeId
     * @param begTime
     * @param endTime
     * @param order
     * @return
     */
    List<EnergyMeasureDateTime> measureDateTime(Long projectId, Long consumeTypeId, String begTime, String endTime, String order);

    /**
     * 获取用户的项目
     * @param userId
     * @return
     */
    List<EntityDto> getProjectList(Long userId);

    /**
     * 获取今年月份统计
     * @param projectId
     * @return
     */
    List<MonthMeasureStatistic> getMonthMeasureStatistic(Long projectId,String year);

    /**
     * 获取某个月每台设备的月电度数据
     * @param projectId
     * @param year
     * @param month
     * @return
     */
    List<MonthDetailStatistic> getMonthDetail(Long projectId, String year, String month);

    /**
     * 查看设备实时数据
     * @param projectId
     * @return
     */
    List<EquipmentDetailNow> getEquipmentDetail(Long projectId);

    /**
     * 获取设备报表数据 1天  2月  3季度  4年
     * @param projectId
     * @param type
     * @return
     */
    List<EquipmentReportDetail> getEquipmentReport(Long projectId, Integer type);

    /**
     * 获取最近半年的环比数据
     * @param projectId
     * @return
     */
    Map<String, Object> getRingRatioMeasure(Long projectId);

    /**
     * 获取类比数据
     * @param projectId
     * @return
     */
    Map<String, Object> getMonthAnalogyDetail(Long projectId);

    EnergyReportDataDetail getReportData(Long projectId, Integer typeId);

    List<EnergyStatisticYearDetail> getReportYearData(Long projectId);

    /**
     * 获取报表的耗能排行数据
     * @param projectId
     * @param typeId
     * @return
     */
    List<EquipmentRankReport> getReportRankData(Long projectId, Integer typeId);



}
