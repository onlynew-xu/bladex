package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EnergyTypeDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyType record);

    int insertSelective(EnergyType record);

    EnergyType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyType record);

    int updateByPrimaryKey(EnergyType record);

    List<EnergyType> selectByProjectSystem(@Param("projectId") Long projectId,@Param("systemId") Long systemId);

    List<EnergyType> selectAll(Long projectId);

    EnergyType selectByName(@Param("energyTypeName") String energyTypeName, @Param("projectId") Long projectId);
}