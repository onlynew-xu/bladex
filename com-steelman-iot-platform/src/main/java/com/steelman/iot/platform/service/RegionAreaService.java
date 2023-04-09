package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Area;
import com.steelman.iot.platform.entity.Pager;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface RegionAreaService {

    void insert(Area area);
    void update(Area area);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params);

    /**
     * 获取项目的所有区域信息
     * @param projectId
     * @return
     */
    List<Area> selectByProjectId(Long projectId);

    Area selectByPrimaryKey(Long areaId);

    /**
     * 根据区域名称获取区域信息
     * @param areaName
     * @return
     */
    Area selectByAreaName(String areaName,Long projectId);

    Boolean removeByProjectAndAreaId(Long projectId, Long areaId);

    /**
     * 删除区域信息
     * @param projectId
     * @param areaId
     * @return
     */
    Boolean deleteArea(Long projectId, Long areaId);

    Map<Long, String> getAreaMap(Long projectId);
}
