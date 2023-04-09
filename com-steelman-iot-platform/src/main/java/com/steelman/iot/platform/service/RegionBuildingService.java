package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface RegionBuildingService {

    void insert(Building area);
    void update(Building area);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params, Long areaId);
    void deleteByAreaId(Long areaId);

    /**
     * 根据项目Id 获取所有Building信息
     * @param projectId
     * @return
     */
    List<Building> selectByProjectId(Long projectId);
    /**
     * 根据项目Id和区域Id 获取所有Building信息
     * @param projectId
     * @param areaId
     * @return
     */
    List<Building> selectByProjectIdAndAreaId(Long projectId,Long areaId);

    /**
     * 根据主键 获取建筑信息
     * @param buildingId
     * @return
     */
    Building selectByPrimaryKey(Long buildingId);

    /**
     * 根据主键删除建筑信息
     * @param projectId
     * @param buildingId
     * @return
     */
    Boolean deleteByBuildingId(Long projectId, Long buildingId);

    /**
     * 判断是否有没有区域的建筑楼信息
     * @param projectId
     * @return
     */
    Boolean getAreaFlag(Long projectId);

    /**
     * 获取所有建筑楼信息
     * @param projectId
     * @return
     */
    List<EntityDto> getBuildingfInfoList(Long projectId);

    Building findByNameProject(String buildingName, Long projectId);

    Map<Long, String> getBuildingMap(Long projectId);
}
