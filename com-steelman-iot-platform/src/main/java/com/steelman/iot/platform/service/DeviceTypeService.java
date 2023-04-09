package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.DeviceType;
import com.steelman.iot.platform.entity.DeviceTypePicture;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;
import java.util.Map;

public interface DeviceTypeService {
    /**
     * 获取所有设备类型
      * @return
     */
    List<DeviceType> listAll();

    void insert(DeviceType record);
    void update(DeviceType record);
    void deleteById(Long id);
    Pager<T> selectByAll(Map<String,Integer> params);
    DeviceType findByid(Long id);

    /**
     * 获取设备类型的图片
     * @return
     */
    List<DeviceTypePicture> listDeviceTypePicture();

    /**
     * 根据设备类型获取设备的图片
     * @param deviceTypeId
     * @return
     */
    DeviceTypePicture findByDeviceTypeId(Long deviceTypeId);

    List<EntityDto> getSystemList(Long projectId, Long systemId,Integer type);
}
