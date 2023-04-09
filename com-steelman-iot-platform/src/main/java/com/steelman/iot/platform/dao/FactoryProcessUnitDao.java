package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.FactoryProcessUnit;
import org.apache.ibatis.annotations.Param;

public interface FactoryProcessUnitDao {
    int deleteByPrimaryKey(Long id);

    int insert(FactoryProcessUnit record);

    int insertSelective(FactoryProcessUnit record);

    FactoryProcessUnit selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FactoryProcessUnit record);

    int updateByPrimaryKey(FactoryProcessUnit record);

    FactoryProcessUnit getByFactoryId(@Param("factoryInfoId") Long factoryInfoId);
}