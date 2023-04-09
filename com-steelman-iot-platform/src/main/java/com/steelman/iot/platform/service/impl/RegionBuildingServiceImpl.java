package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.BuildingDao;
import com.steelman.iot.platform.entity.Building;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.RegionBuildingService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-25
 */
@Service("regionBuildingService")
public class RegionBuildingServiceImpl extends BaseService implements RegionBuildingService {
    @Resource
    private BuildingDao buildingDao;

    public void insert(Building area) {
        buildingDao.insert(area);
    }
    public void update(Building area) {
        buildingDao.updateByPrimaryKeySelective(area);
    }
    public void deleteById(Long id) {
        buildingDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params, Long areaId) {
        List<T> list = buildingDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"),areaId);
        Pager<T> pager = pagerText(params,list);
        return pager;
    }
    public void deleteByAreaId(Long areaId) {
        buildingDao.deleteByAreaId(areaId);
    }

    @Override
    public List<Building> selectByProjectId(Long projectId) {
        return buildingDao.selectByProjectId(projectId);
    }

    @Override
    public List<Building> selectByProjectIdAndAreaId(Long projectId, Long areaId) {
        return buildingDao.selectByProjectIdAndAreaId(projectId,areaId);
    }

    @Override
    public Building selectByPrimaryKey(Long buildingId) {
        return buildingDao.selectByPrimaryKey(buildingId);
    }

    @Override
    public Boolean deleteByBuildingId(Long projectId, Long buildingId) {
        //toDo
        buildingDao.deleteByPrimaryKey(buildingId);
        return true;
    }

    @Override
    public Boolean getAreaFlag(Long projectId) {
        List<Building> buildingList=buildingDao.getAreaIsNull(projectId);
        return ! CollectionUtils.isEmpty(buildingList);
    }

    @Override
    public List<EntityDto> getBuildingfInfoList(Long projectId) {
        return buildingDao.getBuildingfInfoList(projectId);
    }

    @Override
    public Building findByNameProject(String buildingName, Long projectId) {
        return buildingDao.fingByNameProject(buildingName,projectId);
    }

    @Override
    public Map<Long, String> getBuildingMap(Long projectId) {
        List<Building> buildingList=  buildingDao.getByProjectId(projectId);
        Map<Long,String> buildingMap=new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(buildingList)){
            for (Building building : buildingList) {
                buildingMap.put(building.getId(),building.getName());
            }
        }
        return buildingMap;
    }
}
