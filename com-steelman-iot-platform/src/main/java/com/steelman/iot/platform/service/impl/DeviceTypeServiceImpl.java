package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.DeviceTypeDao;
import com.steelman.iot.platform.entity.DeviceType;
import com.steelman.iot.platform.entity.DeviceTypePicture;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.DeviceTypePictureService;
import com.steelman.iot.platform.service.DeviceTypeService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-12-04
 */
@Service("deviceTypeService")
public class DeviceTypeServiceImpl extends BaseService implements DeviceTypeService {
    @Resource
    private DeviceTypeDao deviceTypeDao;
    @Resource
    private DeviceTypePictureService deviceTypePictureService;
    @Override
    public List<DeviceType> listAll() {
        return deviceTypeDao.listAll();
    }
    public void insert(DeviceType record) {
        deviceTypeDao.insert(record);
    }
    public void update(DeviceType record) {
        deviceTypeDao.updateByPrimaryKeySelective(record);
    }
    public void deleteById(Long id) {
        deviceTypeDao.deleteByPrimaryKey(id);
    }
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list =deviceTypeDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }
    public DeviceType findByid(Long id) {
        return deviceTypeDao.selectByPrimaryKey(id);
    }

    @Override
    public List<DeviceTypePicture> listDeviceTypePicture() {
        return deviceTypePictureService.listAll();
    }

    @Override
    public DeviceTypePicture findByDeviceTypeId(Long deviceTypeId) {
        return deviceTypePictureService.findByDeviceTypeId(deviceTypeId);
    }

    @Override
    public List<EntityDto> getSystemList(Long projectId, Long systemId,Integer type) {
        if(systemId.equals(2000l)){
            List<EntityDto> list=new ArrayList<>();
            if(type.equals(0)){
                DeviceType deviceTypeDoor = deviceTypeDao.selectByPrimaryKey(9l);
                DeviceType deviceTypeTemp= deviceTypeDao.selectByPrimaryKey(12l);
                list.add(new EntityDto(deviceTypeTemp.getId(),deviceTypeTemp.getName(),projectId));
                list.add(new EntityDto(deviceTypeDoor.getId(),deviceTypeDoor.getName(),projectId));
            }else if(type.equals(1)){
                DeviceType deviceType3000 = deviceTypeDao.selectByPrimaryKey(6l);
                DeviceType deviceType5000= deviceTypeDao.selectByPrimaryKey(13l);
                DeviceType deviceType4000 = deviceTypeDao.selectByPrimaryKey(5l);
                list.add(new EntityDto(deviceType3000.getId(),deviceType3000.getName(),projectId));
                list.add(new EntityDto(deviceType5000.getId(),deviceType5000.getName(),projectId));
                list.add(new EntityDto(deviceType4000.getId(),deviceType4000.getName(),projectId));
            }else if(type.equals(2)){
                DeviceType deviceType3000 = deviceTypeDao.selectByPrimaryKey(6l);
                DeviceType deviceType5000= deviceTypeDao.selectByPrimaryKey(13l);
                DeviceType deviceTypeCom= deviceTypeDao.selectByPrimaryKey(11l);
                DeviceType deviceType4000 = deviceTypeDao.selectByPrimaryKey(5l);
                list.add(new EntityDto(deviceType3000.getId(),deviceType3000.getName(),projectId));
                list.add(new EntityDto(deviceType5000.getId(),deviceType5000.getName(),projectId));
                list.add(new EntityDto(deviceTypeCom.getId(),deviceTypeCom.getName(),projectId));
                list.add(new EntityDto(deviceType4000.getId(),deviceType4000.getName(),projectId));
            }else if(type.equals(3)){
                DeviceType deviceType3000 = deviceTypeDao.selectByPrimaryKey(6l);
                DeviceType deviceType5000= deviceTypeDao.selectByPrimaryKey(13l);
                DeviceType deviceTypeWave= deviceTypeDao.selectByPrimaryKey(10l);
                DeviceType deviceType4000 = deviceTypeDao.selectByPrimaryKey(5l);
                list.add(new EntityDto(deviceType3000.getId(),deviceType3000.getName(),projectId));
                list.add(new EntityDto(deviceType5000.getId(),deviceType5000.getName(),projectId));
                list.add(new EntityDto(deviceTypeWave.getId(),deviceTypeWave.getName(),projectId));
                list.add(new EntityDto(deviceType4000.getId(),deviceType4000.getName(),projectId));
            }else if(type.equals(4)||type.equals(5)){
                DeviceType deviceType3000 = deviceTypeDao.selectByPrimaryKey(6l);
                DeviceType deviceType5000= deviceTypeDao.selectByPrimaryKey(13l);
                DeviceType deviceType4000 = deviceTypeDao.selectByPrimaryKey(5l);
                list.add(new EntityDto(deviceType3000.getId(),deviceType3000.getName(),projectId));
                list.add(new EntityDto(deviceType5000.getId(),deviceType5000.getName(),projectId));
                list.add(new EntityDto(deviceType4000.getId(),deviceType4000.getName(),projectId));
            }
            return list;
        }else if(systemId.equals(3000l)){
            //能源管理获取绑定的类型
            DeviceType deviceType625 = deviceTypeDao.selectByPrimaryKey(5l);
            DeviceType deviceType1128 = deviceTypeDao.selectByPrimaryKey(6l);
            DeviceType deviceType1129 = deviceTypeDao.selectByPrimaryKey(13l);
            List<EntityDto> entityDtoList=new ArrayList<>();
            entityDtoList.add(new EntityDto(deviceType1128.getId(),deviceType1128.getName(),projectId));
            entityDtoList.add(new EntityDto(deviceType1129.getId(),deviceType1129.getName(),projectId));
            entityDtoList.add(new EntityDto(deviceType625.getId(),deviceType625.getName(),projectId));
            return entityDtoList;
        }else {
            return deviceTypeDao.getSystemList(projectId,systemId);
        }
    }
}
