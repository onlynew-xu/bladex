package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.AreaDao;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.service.DeviceService;
import com.steelman.iot.platform.service.EnergyEquipmentService;
import com.steelman.iot.platform.service.RegionAreaService;
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
@Service("regionAreaService")
public class RegionAreaServiceImpl extends BaseService implements RegionAreaService {
    @Resource
    private AreaDao areaDao;
    @Resource
    private DeviceService deviceService;
    @Resource
    private EnergyEquipmentService energyEquipmentService;

    public void insert(Area area) {
        areaDao.insert(area);
    }
    public void update(Area area) {
        areaDao.updateByPrimaryKeySelective(area);
    }
    public void deleteById(Long id) {
        areaDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list = areaDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public List<Area> selectByProjectId(Long projectId) {
        List<Area> areaList=areaDao.selectByProjectId(projectId);
        return areaList;
    }

    @Override
    public Area selectByPrimaryKey(Long areaId) {
        return areaDao.selectByPrimaryKey(areaId);
    }

    @Override
    public Area selectByAreaName(String areaName, Long projectId) {
        return areaDao.selectByAreaName(areaName,projectId);
    }

    @Override
    public Boolean removeByProjectAndAreaId(Long projectId, Long areaId) {
        return null;
    }

    @Override
    public Boolean deleteArea(Long projectId, Long areaId) {
        //检查是否有设备绑定信息
        List<Device> deviceList=deviceService.getAreaDevice(areaId,projectId);
        //检查是否有能源设备绑定信息
        List<EnergyEquipment> energyEquipmentList=energyEquipmentService.getAreaEquipment(areaId,projectId);
        //检查是否有告警信息进行绑定
        List<AlarmWarn> alarmWarnList=null;
        if(CollectionUtils.isEmpty(deviceList)&&CollectionUtils.isEmpty(energyEquipmentList) &&CollectionUtils.isEmpty(alarmWarnList)){
            areaDao.deleteByPrimaryKey(areaId);
        }
        return true;
    }

    @Override
    public Map<Long, String> getAreaMap(Long projectId) {
        List<Area> areaList=areaDao.selectByProjectId(projectId);
        Map<Long,String> areaMap=new LinkedHashMap<>();
        if(!CollectionUtils.isEmpty(areaList)){
            for (Area area : areaList) {
                areaMap.put(area.getId(),area.getName());
            }
        }
        return areaMap;
    }
}
