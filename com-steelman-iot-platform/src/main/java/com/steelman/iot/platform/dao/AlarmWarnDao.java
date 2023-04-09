package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.AlarmWarn;
import com.steelman.iot.platform.entity.dto.AlarmWarnDto;
import com.steelman.iot.platform.entity.dto.AlarmWarnPower;
import com.steelman.iot.platform.entity.vo.AlarmWarnItemVo;
import com.steelman.iot.platform.entity.vo.AlarmWarnVo;
import com.steelman.iot.platform.entity.vo.PowerDeviceInfo;
import com.steelman.iot.platform.largescreen.vo.AlarmParentItemVo;
import com.steelman.iot.platform.largescreen.vo.AlarmWarnInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AlarmWarnDao {
    int deleteByPrimaryKey(Long id);

    int insert(AlarmWarn record);

    int insertSelective(AlarmWarn record);

    AlarmWarn selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmWarn record);

    int updateByPrimaryKey(AlarmWarn record);

    /**
     * 获取系统报警处理和未处理的次数
     * @param projectId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getHandleStatus(@Param("projectId") Long projectId,@Param("systemId") Long systemId,@Param("areaId") Long areaId,@Param("buildingId") Long buildingId);

    /**
     * 获取最近日期的报警次数
     * @param projectId
     * @param systemId
     * @param pastDate
     * @param currentDate
     * @return
     */
    List<Map<String, Object>> getAlarmCountByDate(@Param("projectId") Long projectId,@Param("systemId") Long systemId, @Param("strDate") Date pastDate,@Param("endDate") Date currentDate);

    /**
     * 获取所有未处理的报警信息
     * @param projectId
     * @param systemId
     * @return
     */
    List<AlarmWarnDto> getInHandlerAlarmWarn(@Param("projectId") Long projectId, @Param("systemId") Long systemId);

    /**
     * 获取所有报警项目 父项
     * @param projectId
     * @return
     */
    List<AlarmParentItemVo> getAlarmParentItem(@Param("projectId") Long projectId);

    /**
     * 获取所有年份信息(预警信息)
     * @return
     */
    List<String> getYear(@Param("projectId") Long projectId);

    /**
     * 获取月份(预警信息)
     * @param projectId
     * @param year
     * @return
     */
    List<String> getMonth(@Param("projectId") Long projectId, @Param("year") String year);

    /**
     * 获取预警信息
     * @param projectId
     * @param areaId
     * @param buildingId
     * @param alarmParentItemId
     * @param year
     * @param month
     * @return
     */
    List<AlarmWarnInfoVo> getAlarmInfoVo(@Param("projectId") Long projectId,@Param("areaId") Long areaId, @Param("buildingId") Long buildingId,@Param("alarmParentItemId") Long alarmParentItemId, @Param("year") String year, @Param("month") String month);

    /**
     * 根据位置信息和时间信息 统计报警次数
     * @param projectId
     * @param areaId
     * @param buildingId
     * @param year
     * @param month
     * @return
     */
    List<Map<String, Object>> getAlarmCountInfo(@Param("projectId") Long projectId,@Param("areaId") Long areaId, @Param("buildingId") Long buildingId, @Param("year") String year, @Param("month") String month);

    /**
     * 按照日期统计报警类型对应的次数
     * @param projectId
     * @param systemId
     * @param weekDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> getAlarmTypeCountStatistic(@Param("projectId") Long projectId, @Param("systemId") Long systemId, @Param("strDate") Date weekDate,@Param("endDate") Date endDate);

    /**
     * 按照日期统计设备类型对应的报警次数
     * @param projectId
     * @param systemId
     * @param weekDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> getDeviceTypeCountStatistic(@Param("projectId") Long projectId, @Param("systemId") Long systemId, @Param("strDate") Date weekDate, @Param("endDate") Date endDate);

    /**
     * 获取设备的离线历史记录
     * @param projectId
     * @param systemId
     * @param flag
     * @return
     */
    List<Map<String, Object>> getOfflineInfo(@Param("projectId") Long projectId,@Param("systemId") Long systemId, @Param("flag") String flag);

    /**
     * 获取设备风险信息
     * @param projectId
     * @param systemId
     * @param flag
     * @return
     */
    List<Map<String, Object>> getAlarmRiskByDate(@Param("projectId") Long projectId,@Param("systemId") Long systemId, @Param("flag") String flag,@Param("strDate") Date strDate,@Param("endDate") Date endDate);

    /**
     * 统计设备报警项的报警次数
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> statisticByAlarmItem(@Param("projectId") Long projectId,@Param("deviceId") Long deviceId, @Param("systemId") Long systemId);

    /**
     * 获取设备的历史报警数据
     * @param deviceId
     * @param projectId
     * @param systemId
     * @return
     */
    List<Map<String, Object>> getAlarmInfoByDeviceId(Long deviceId, Long projectId, Long systemId);

    /**
     * 移除对应系统的预警信息
     * @param projectId
     * @param deviceId
     * @param systemId
     * @return
     */
    int removeByDeviceSystem(@Param("projectId") Long projectId,@Param("deviceId") Long deviceId,@Param("systemId") Long systemId);

    List<AlarmWarnVo> selectAlarmByProjectId(@Param("projectId") Long projectId,@Param("systemId") Long systemId);

    List<Map<String, Object>> selectAlarmScale(@Param("projectId") Long projectId,@Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectProjectAlarm(@Param("projectId") Long projectId,@Param("systemId") Long systemId,@Param("part") String part);

    List<Map<String,Object>> selectDeviceAlarmCount(@Param("projectId") Long projectId,@Param("deviceId") Long deviceId,@Param("systemId") Long systemId);

    List<Map<String, Object>> selectIncomingDeviceAlarmCount(@Param("incomingId") Long incomingId, @Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectIncomingDeviceAlarm(Long incomingId, Long systemId);

    List<Map<String, Object>> selectCompensateDeviceAlarmCount(@Param("compensateId")Long compensateId,@Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectCompensateDeviceAlarm(@Param("compensateId")Long compensateId,@Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectWaveDeviceAlarm(@Param("waveId") Long waveId, @Param("systemId") Long systemId);

    List<Map<String, Object>> selectWaveDeviceAlarmCount(@Param("waveId") Long waveId, @Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectFeederLoopDeviceAlarm(@Param("loopId") Long loopId,@Param("systemId") Long systemId);

    List<Map<String, Object>> selectFeederLoopDeviceAlarmCount(@Param("loopId") Long loopId,@Param("systemId") Long systemId);

    List<AlarmWarnItemVo> selectDeviceAlarmInfo(@Param("projectId") Long projectId,@Param("deviceId") Long deviceId,@Param("systemId") Long systemId);

    /**
     * 获取还有未处理预警信息的设备
     * @param projectId
     * @return
     */
    List<Long> getInHandlerSafeDevice(@Param("projectId") Long projectId);

    List<AlarmWarn> getAlarmData(@Param("deviceId") Long deviceId, @Param("systemId") Long systemId, @Param("strDate") Date strDate,@Param("endDate") Date endDate);

    /**
     * 删除设备的预警信息
     * @param deviceId
     * @return
     */
    int removeByDeviceId(@Param("deviceId") Long deviceId);

    List<AlarmWarnPower> getDeviceAlarmWarn(@Param("projectId") Long projectId,@Param("systemId") Long systemId);

    List<AlarmWarnPower> getAlaramWarnByDeviceId(@Param("deviceId") Long deviceId,  @Param("systemId") Long systemId);

    List<Map<String, Object>> getPowerAlarmCount(@Param("deviceList") List<PowerDeviceInfo> deviceList);

    List<AlarmWarnItemVo> getPowerHistory(@Param("deviceList") List<PowerDeviceInfo> deviceList);

    int removePowerAlarm(@Param("deviceId") Long deviceId);

    List<Map<String, Object>> getEnergyDayAndMonthAlarmCount(@Param("projectId") Long projectId, @Param("year") String year, @Param("month") String month, @Param("day") String day);

    List<Map<String, Object>> getQuarterCount(@Param("projectId") Long projectId,@Param("year") String year,@Param("monthList") List<String> monthList);

    List<AlarmWarn> getByProjectId(@Param("projectId") long projectId);

    Integer updateAlarmValue(@Param("alarmWarnList") List<AlarmWarn> alarmWarnList);

    /**
     * 获取指定天的安全预警的告警数量
     * @param projectId
     * @param yearMonthDay
     * @return
     */
    Integer getDayCount(@Param("projectId") Long projectId,@Param("yearMonthDay") String yearMonthDay);

    /**
     * 获取某个时期内安全预警的告警数量
     * @param projectId
     * @param strDate
     * @param endDate
     * @return
     */
    Integer getBetweenCount(@Param("projectId") Long projectId, @Param("strDate") String strDate, @Param("endDate") String endDate);

    /**
     * 获取未处理的数量
     * @param projectId
     * @return
     */
    Integer getInHandlerCount(@Param("projectId") Long projectId);

    /**
     * 获取已处理的数量
     * @param projectId
     * @return
     */
    Integer getHandlerCount(@Param("projectId") Long projectId);

    /**
     * 获取预警类型的占比
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getAlarmItemData(@Param("projectId") Long projectId);

    /**
     * 获取安全预警未处理的预警信息
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getInHandlerAlarmWarnInfo(Long projectId);

    /**
     * 获取安全预警 区域告警数量
     * @param projectId
     * @return
     */
    List<Map<String, Object>> getAreaLocationInfo(Long projectId);


    List<Map<String, Object>> getDeviceTypeStatistic(@Param("deviceSet") Set<Long> deviceSet);
}