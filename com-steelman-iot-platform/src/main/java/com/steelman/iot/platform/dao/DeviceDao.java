package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.Device;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.MeasureDataStr;
import com.steelman.iot.platform.entity.dto.MeasureDateData;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(Device record);

    int insertSelective(Device record);

    Device selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Device record);

    int updateByPrimaryKey(Device record);

    /**
     * 获取设备的统计信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getDeviceStatistic(@Param("projectId") Long projectId, @Param("systemId") Long systemId);

    List<T> selectByAll(int page, int size, Long deviceTypeId, Long projectId);

    /**
     * 获取设备的统计信息大屏使用接口
     * @param projectId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getDeviceStatisticDaPing(@Param("projectId") Long projectId, @Param("systemId") Long systemId);

    /**
     * 获取设备状态对应的设备数(大屏接口使用)
     * @param projectId
     * @param areaId
     * @param buildingId
     * @return
     */
    List<Map<String, Object>> deviceStatusStatistic(@Param("projectId") Long projectId,@Param("areaId") Long areaId, @Param("buildingId") Long buildingId);

    /**
     * 获取智慧用电设备信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getDeviceSimpleInfo(@Param("projectId") Long projectId,@Param("systemId") Long systemId);

    /**
     * 根据sn 获取设备信息
     * @param serialNum
     * @return  Device
     */
    Device selectBySerialNum(@Param("serialNum") String serialNum);

    /**
     * 获取设备列表  根据设备类型
     * @param projectId
     * @param deviceTypeId
     * @return
     */
    List<Device> selectByDeviceTypeId(@Param("projectId") Long projectId, @Param("deviceTypeId") Long deviceTypeId);

    /**
     * 根据区域获取设备信息
     * @param areaId
     * @param projectId
     * @return
     */
    List<Device> getAreaDevice(@Param("areaId") Long areaId, @Param("projectId") Long projectId);

    /**
     * 获取设备总的电度数据
     * @param deviceId
     * @return
     */
    MeasureDataStr getMeasureTotalData(@Param("deviceId") Long deviceId);

    List<MeasureDateData> getPastSixDayMeasure(@Param("deviceId") Long deviceId, @Param("projectId") Long projectId, @Param("begDate") Date begDate, @Param("endDate") Date endDate);

    MeasureDateData getTodayMeasureData(@Param("deviceId") Long deviceId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取过去的设备的电度数据
     * @param projectId
     * @param deviceId
     * @param lastBegDate
     * @param lastEndDate
     * @return
     */
    List<MeasureDateData> getPastDateDate(Long projectId, Long deviceId, Date lastBegDate, Date lastEndDate);

    MeasureDateData getDayMeasureData(@Param("deviceId") Long deviceId, @Param("yearMonthDay") String yearMonthDay);

    List<MeasureDateData> getLastMonthDayMeasureData(@Param("deviceId") Long deviceId, @Param("year") String year, @Param("month") String month);

    MeasureDateData getTodayMonthMeasure(@Param("deviceId") Long deviceId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 设备areaId buildingId storeyId roomId 为null
     * @param deviceId
     * @return
     */
    int updateAreaAllNull(@Param("deviceId") Long deviceId);

    int updateLocationNull(@Param("deviceId") Long deviceId);

    int updateAreaNull(@Param("deviceId") Long deviceId);

    int updateVideoUrlNull(@Param("deviceId") Long deviceId);

    List<EntityDto> getSystemDeviceList(@Param("projectId") Long projectId, @Param("deviceTypeId") Long deviceTypeId,@Param("systemId") Long systemId);

    Device findSystemNameDevice(@Param("deviceId") Long deviceId,@Param("systemId") Long systemId);

    List<Map<String, Object>> getPowerDeviceList(@Param("projectId") Long projectId);

    Integer getDeviceCount(@Param("projectId") Long projectId);

    List<Device> findByProjectId(@Param("projectId") Long projectId);

    List<Map<String, Object>> getSafeDeviceStatus(@Param("projectId") Long projectId);

    List<Map<String, Object>> getPowerDeviceStatus(@Param("projectId") Long projectId);
}