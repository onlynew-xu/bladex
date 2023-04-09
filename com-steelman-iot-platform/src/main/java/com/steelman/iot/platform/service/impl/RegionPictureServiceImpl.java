package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.StoreyDao;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Storey;
import com.steelman.iot.platform.service.RegionPictureService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("regionPictureService")
public class RegionPictureServiceImpl extends BaseService implements RegionPictureService {

    @Resource
    private StoreyDao storeyDao;


    @Override
    public void addFloorCenterPicture(Storey storeypicture) {
        storeyDao.addFloorCenterPicture(storeypicture);
    }

    @Override
    public void updateFloorNamePicture(Storey record) {
        storeyDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public void deleteFloorPicture(Storey record) {
        storeyDao.deletePictureById(record);
    }

    @Override
    public void deleteFloorCadPicture(Storey record) {
        storeyDao.deletePictureCadById(record);
    }

    @Override
    public Pager<T> selectByAll(Map<String, Integer> params,Long buildingId) {
        List<T> list = storeyDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"),buildingId);
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public Storey getgetFloorById(Long id) {
        return storeyDao.selectByPrimaryKey(id);
    }
}
