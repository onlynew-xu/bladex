package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.PowerComponents;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PowerComponentsDao {
    int deleteByPrimaryKey(Long id);

    int insert(PowerComponents record);

    int insertSelective(PowerComponents record);

    PowerComponents selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PowerComponents record);

    int updateByPrimaryKey(PowerComponents record);

    List<PowerComponents> selectDeviceComponents(@Param("powerDeviceId") Long id, @Param("type") Integer type, @Param("powerDeviceType") Integer powerDeviceType);

    List<PowerComponents> selectComponentsByPowerType(@Param("powerType") Integer powerType, @Param("powerDeviceId") Long powerDeviceId, @Param("componentType") Integer componentType);

    int updateInstallationNull(Long id);

    int updateUseCompanyNull(Long id);

    int updateMaintenanceNull(Long id);
}