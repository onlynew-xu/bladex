package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceType;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

public interface DeviceTypeDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceType record);

    int insertSelective(DeviceType record);

    DeviceType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceType record);

    int updateByPrimaryKey(DeviceType record);

    /**
     * 获取所有设备类型
     * @return
     */
    List<DeviceType> listAll();

    List<T> selectByAll(int page, int size);

    List<EntityDto> getSystemList(Long projectId, Long systemId);
}