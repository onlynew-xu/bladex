package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.StoreyDao;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.RegionStoreyService;
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
@Service("regionStoreyService")
public class RegionStoreyServiceImpl extends BaseService implements RegionStoreyService {
    @Resource
    private StoreyDao storeyDao;

    public void insert(Storey area) {
        storeyDao.insert(area);
    }
    public void update(Storey area) {
        storeyDao.updateByPrimaryKeySelective(area);
    }
    public void deleteById(Long id) {
        storeyDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params, Long buildingId) {
        List<T> list = storeyDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"),buildingId);
        Pager<T> pager = pagerText(params,list);
        return pager;
    }
    public void deleteByStorey(Storey area) {
        storeyDao.deleteByStorey(area);
    }

    @Override
    public List<Storey> selectByProjectId(Long projectId) {
        return storeyDao.selectByProjectId(projectId);
    }

    @Override
    public Storey selectByPrimaryKey(Long storyId) {
        return storeyDao.selectByPrimaryKey(storyId);
    }

    @Override
    public Storey getByBuildingAndStorey(Long buildingId, Long storeyId) {
        return storeyDao.getByBuildingAndStorey(buildingId,storeyId);
    }

    @Override
    public List<Storey> getByProjectAndBuildind(Long projectId, Long buildingId) {
        return storeyDao.getByProjectAndBuildind(projectId,buildingId);
    }

    @Override
    public Boolean deleteByStoreyId(Long storeyId, Long projectId) {
        //todo
        storeyDao.deleteByPrimaryKey(storeyId);
        return true;
    }

    @Override
    public List<EntityDto> getStoreyInfoList(Long projectId) {
        return storeyDao.getStoreyInfoList(projectId);
    }

    @Override
    public Map<Long, String> getStoreyMap(Long projectId) {
        List<Storey> storeyList=storeyDao.getByProjectId(projectId);
        Map<Long,String> storeyMap=new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(storeyList)){
            for (Storey storey : storeyList) {
                storeyMap.put(storey.getId(),storey.getName());
            }
        }
        return storeyMap;
    }
}
