package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.DeviceMeasurement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceLastMeasureMentDao {

    List<DeviceMeasurement> getMeasureData(@Param("pastDateStrArr") String[] pastDateStrArr, @Param("deviceId") Long deviceId);
}
