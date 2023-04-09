package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Storey;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface RegionPictureService {
    /*添加楼层平面图中心*/
    void addFloorCenterPicture(Storey storeypicture);

    void updateFloorNamePicture(Storey record);

    void deleteFloorPicture(Storey record);

    void deleteFloorCadPicture(Storey record);

    Pager<T> selectByAll(Map<String, Integer> params,Long buildingId);

    Storey getgetFloorById(Long id);
}
