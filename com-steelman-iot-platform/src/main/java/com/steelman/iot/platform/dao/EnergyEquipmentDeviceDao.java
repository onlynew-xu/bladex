package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentPower;
import com.steelman.iot.platform.entity.vo.EnergyDeviceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EnergyEquipmentDeviceDao {
    int deleteByPrimaryKey(Long id);

    int insert(EnergyEquipmentDevice record);

    int insertSelective(EnergyEquipmentDevice record);

    EnergyEquipmentDevice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EnergyEquipmentDevice record);

    int updateByPrimaryKey(EnergyEquipmentDevice record);

    EnergyEquipmentDevice selectByEquipmentId(Long equipmentId);


    List<EnergyDeviceInfo> selectMonthYearConsumeRank(@Param("projectId") Long projectId, @Param("month") int month, @Param("year") int year, @Param("type") Integer type);

    List<EnergyDeviceInfo> selectQuarterConsumeRank(@Param("projectId") Long projectId, @Param("year") int year, @Param("startMonth") int startMonth, @Param("endMonth") int endMonth);

    List<EnergyDeviceInfo> selectCountCenter(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId, @Param("energyTypeId") Long energyTypeId,@Param("beginTime") String beginTime,@Param("endTime") String endTime, @Param("order") Integer order,@Param("type") Integer type);

    List<EnergyDeviceInfo> selectCountCenterReading(@Param("projectId") Long projectId,@Param("consumeTypeId") Long consumeTypeId,@Param("energyTypeId") Long energyTypeId, @Param("type") Integer type, @Param("order") Integer order);

    List<Map<String, Object>> selectDeviceCountMeasurement(Long equipmentId);

    String selectDeviceMeasurementDay(@Param("equipmentId") Long equipmentId, @Param("dateStr") String dateStr,@Param("type") int type);

    EnergyEquipmentPower selectByDeviceId(Long deviceId);

    int deleteByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 获取月度排行榜数据
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getMonthMeasure(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取能源设备当日最新的数据
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    List<Map<String, Object>> getTodayMeasure(@Param("projectId") Long projectId, @Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取某日当天的是最后数据
     * @param projectId
     * @param dateYear
     * @return
     */
    List<Map<String, Object>> getJiDuMeasure(@Param("projectId") Long projectId, @Param("dateYear") String  dateYear);

    /**
     * 删除绑定的能源设备
     * @param equipmentId
     */
    void deleteByEquipmentId(@Param("equipmentId") Long equipmentId);

    EnergyEquipment selectEnergyEquipmentByDeviceId(Long id);

    /**
     * 根据项目id获取绑定关系
     * @param projectId
     * @return
     */
    List<EnergyEquipmentDevice> findByProjectId(@Param("projectId") Long projectId);
}