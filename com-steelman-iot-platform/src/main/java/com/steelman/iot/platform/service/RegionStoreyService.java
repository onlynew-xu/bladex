package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface RegionStoreyService {

    void insert(Storey area);
    void update(Storey area);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String, Integer> params, Long buildingId);
    void deleteByStorey(Storey area);

    /**
     * 根据项目Id 获取楼层信息
     * @param projectId
     * @return
     */
    List<Storey> selectByProjectId(Long projectId);

    /**
     * 根据Id 获取楼层信息
     * @param storyId
     * @return
     */
    Storey selectByPrimaryKey(Long storyId);

    /**
     * 获取层信息
     * @param buildingId
     * @param storeyId
     * @return
     */
    Storey getByBuildingAndStorey(Long buildingId, Long storeyId);

    /**
     * 根据楼号获取所有层信息
     * @param projectId
     * @param buildingId
     * @return
     */
    List<Storey> getByProjectAndBuildind(Long projectId, Long buildingId);

    /**
     * 删除楼层信息
     * @param storeyId
     * @param projectId
     * @return
     */
    Boolean deleteByStoreyId(Long storeyId, Long projectId);

    /**
     * 获取所有层信息列表
     * @param projectId
     * @return
     */
    List<EntityDto> getStoreyInfoList(Long projectId);

    Map<Long, String> getStoreyMap(Long projectId);
}
