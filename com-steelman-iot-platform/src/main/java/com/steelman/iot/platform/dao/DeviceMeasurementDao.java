package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceMeasurement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeviceMeasurementDao {
    int deleteByPrimaryKey(Long id);

    int insert(DeviceMeasurement record);

    int insertSelective(DeviceMeasurement record);

    DeviceMeasurement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DeviceMeasurement record);

    int updateByPrimaryKey(DeviceMeasurement record);

    List<Map<String, Object>> selectNowDegreeByDeviceId(Long deviceId);

    List<Map<String,Object>> selectLastDegreeByDeviceId(Long deviceId);

    /**
     * 删除设备的电度数据
     * @param deviceId
     * @return
     */
    int deleteByDeviceId(@Param("deviceId") Long deviceId);

    List<DeviceMeasurement> getMeasureList(@Param("serialNum") String serialNum,@Param("dateFo") String dateFo);

    int updateData(@Param("deviceMeasurementList") List<DeviceMeasurement> deviceMeasurementList);

    DeviceMeasurement getLastDevice(@Param("deviceId") Long deviceId);

    List<DeviceMeasurement> getUpdateData(@Param("deviceId") Long deviceId, @Param("idMax") Integer idMax);

    int updateNormal(@Param("deviceMeasurementUpdate") List<DeviceMeasurement> deviceMeasurementUpdate);
}