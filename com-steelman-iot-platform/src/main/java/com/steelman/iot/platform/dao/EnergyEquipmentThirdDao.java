package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentThird;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentThirdDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentThird record);

    int insertSelective(EnergyEquipmentThird record);

    EnergyEquipmentThird selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentThird record);

    int updateByPrimaryKey(EnergyEquipmentThird record);

    List<EnergyStatus> getEnergyStatistic(@Param("projectId") Long projectId);

    /**
     * 三级能耗类型统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> consumeStatistic(@Param("projectId") Long projectId);

    /**
     * 三级设备总能耗统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getEquipmentTotal(@Param("projectId") Long projectId);
}