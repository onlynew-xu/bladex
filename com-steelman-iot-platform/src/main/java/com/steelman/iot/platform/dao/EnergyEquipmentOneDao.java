package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipmentOne;
import com.steelman.iot.platform.largescreen.vo.EnergyStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentOneDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentOne record);

    int insertSelective(EnergyEquipmentOne record);

    /**
     * 根据id查询整条数据
     * @param id
     * @return
     */
    EnergyEquipmentOne selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentOne record);

    int updateByPrimaryKey(EnergyEquipmentOne record);





    /**
     * 获取能源status
     * * @param projectId
     * @return
     */
    List<EnergyStatus> getEnergyStatistic(@Param("projectId") Long projectId);


    /**
     * 获取一级能耗类型统计
     * @param projectId
     * @return
     */
    List<Map<String, Object>> consumeStatistic(@Param("projectId") Long projectId);

    /**
     * 获取一级能耗设备的总能耗
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getEquipmentTotal(@Param("projectId") Long projectId);

}