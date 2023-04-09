package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentSecond;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentSecondDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentSecond record);

    int insertSelective(EnergyEquipmentSecond record);

    EnergyEquipmentSecond selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentSecond record);

    int updateByPrimaryKey(EnergyEquipmentSecond record);

    List<EnergyStatus> getEnergyStatistic(@Param("projectId") Long projectId);

    /**
     * 获取二级能耗类型统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> consumeStatistic(@Param("projectId") Long projectId);

    /**
     * 获取二级能耗设备的总能耗
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getEquipmentTotal(@Param("projectId") Long projectId);
}