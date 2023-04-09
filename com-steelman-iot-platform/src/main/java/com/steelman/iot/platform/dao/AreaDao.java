package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Area;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface AreaDao {
    int deleteByPrimaryKey(Long id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    List<T> selectByAll(int page, int size);

    /**
     * 根据项目获取所有区域信息
     * @param projectId
     * @return
     */
    List<Area> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据区域名称查询
     * @param areaName
     * @param projectId
     * @return
     */
    Area selectByAreaName(@Param("areaName") String areaName, @Param("projectId") Long projectId);
}