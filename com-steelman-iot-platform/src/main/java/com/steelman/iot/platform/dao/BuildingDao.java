package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface BuildingDao {
    int deleteByPrimaryKey(Long id);

    int insert(Building record);

    int insertSelective(Building record);

    Building selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Building record);

    int updateByPrimaryKey(Building record);

    List<T> selectByAll(int page, int size, Long areaId);


    List<Building> selectByProjectId(@Param("projectId") Long projectId);

    List<Building> selectByProjectIdAndAreaId(@Param("projectId")Long projectId,@Param("areaId")Long areaId);

    int deleteByAreaId(@Param("areaId") Long areaId);

    /**
     * 获取区域为空的建筑楼信息
     * @param projectId
     * @return
     */
    List<Building> getAreaIsNull(@Param("projectId") Long projectId);

    /**
     * 获取建筑楼信息
     * @param projectId
     * @return
     */
    List<EntityDto> getBuildingfInfoList(@Param("projectId") Long projectId);

    /**
     * 根据建筑名获取建筑楼信息
     * @param buildingName
     * @param projectId
     * @return
     */
    Building fingByNameProject(@Param("buildingName") String buildingName,@Param("projectId") Long projectId);


    List<Building> getByProjectId(@Param("projectId") Long projectId);
}