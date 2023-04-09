package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentFourth;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentFourthDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentFourth record);

    int insertSelective(EnergyEquipmentFourth record);

    EnergyEquipmentFourth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentFourth record);

    int updateByPrimaryKey(EnergyEquipmentFourth record);

    List<EnergyStatus> getEnergyStatistic(@Param("projectId") Long projectId);

    List<Map<String, Object>> consumeStatistic(@Param("projectId") Long projectId);

    List<Map<String, Object>> getEquipmentTotal(@Param("projectId")Long projectId);
}