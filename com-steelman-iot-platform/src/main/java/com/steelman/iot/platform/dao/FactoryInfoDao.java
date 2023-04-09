package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.FactoryInfo;
import org.apache.ibatis.annotations.Param;

public interface FactoryInfoDao {
    int deleteByPrimaryKey(Long id);

    int insert(FactoryInfo record);

    int insertSelective(FactoryInfo record);

    FactoryInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FactoryInfo record);

    int updateByPrimaryKey(FactoryInfo record);

    FactoryInfo findByProjectId(@Param("projectId") Long projectId);
}