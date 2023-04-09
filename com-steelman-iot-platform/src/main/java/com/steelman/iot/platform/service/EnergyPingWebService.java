package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.vo.EnergyConsumeStatistic;
import com.steelman.iot.platform.entity.vo.EnergyTotalSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.EnergyTotal;
import com.steelman.iot.platform.largescreen.vo.EquipmentStatistic;

import java.util.List;

public interface EnergyPingWebService {

    /**
     * 获取设备统计数据
     * @param projectId
     * @return
     */
    EquipmentStatistic getEquipmentStatusStatistic(Long projectId);

    /**
     * 获取总电度
     * @param projectId
     * @return
     */
    EnergyTotal getTotalEnergy(Long projectId);

    /**
     * 获取能耗类型统计
     * @param projectId
     * @return
     */
    List<EnergyConsumeStatistic> getEquipmentConsumeType(Long projectId);

    /**
     * 获取能耗排行榜 总
     * @param projectId
     * @return
     */
    List<EnergyTotalSimpleInfo> getMeasureRank(Long projectId);
}
