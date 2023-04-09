package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyConsumeType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnergyConsumeTypeDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyConsumeType record);

    int insertSelective(EnergyConsumeType record);

    EnergyConsumeType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyConsumeType record);

    int updateByPrimaryKey(EnergyConsumeType record);

    List<EnergyConsumeType> selectByProjectId(Long projectId);

    /**
     * 根据名称获取能耗类型
     * @param projectId
     * @param energyConsumeName
     * @return
     */
    EnergyConsumeType findByName(@Param("projectId") Long projectId, @Param("energyConsumeName") String energyConsumeName);
}