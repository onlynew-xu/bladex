package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.EnergyEquipment;
import com.steelman.iot.platform.entity.EnergyEquipmentDevice;
import com.steelman.iot.platform.entity.EnergyEquipmentPower;
import com.steelman.iot.platform.entity.vo.EnergyDeviceInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author lsj
 * @DATE 2021/3/31 0031 15:57
 * @Description:
 */
public interface EnergyEquipmentDeviceService {
    void save(EnergyEquipmentDevice equipmentDevice);

    EnergyEquipmentDevice getInfo(Long equipmentId);

    void update(EnergyEquipmentDevice equipmentDevice);


    List<EnergyDeviceInfo> deviceEnergyConsumeRank(Long projectId, Integer type);

    List<EnergyDeviceInfo> getCountCenter(Long projectId, Long consumeTypeId, Long energyTypeId, String beginTime, String endTime, Integer order,Integer type);

    List<EnergyDeviceInfo> countCenterReading(Long projectId, Long consumeTypeId, Long energyTypeId, Integer type, Integer order);

    List<Map<String, Object>> selectDeviceCountMeasurement(Long equipmentId);

    List<Map<String, Object>> selectEnergyTrend(Long id, String... dateStrArr);

    EnergyEquipmentPower selectByDeviceId(Long deviceId);

    void deleteByDeviceId(Long deviceId);

    EnergyEquipmentDevice selectByEquipmentId(Long id);

    /**
     * 获取设备的耗能排行(部分)
     * @param projectId
     * @param typeId
     * @return
     */
    List<EnergyDeviceInfo> deviceEnergyConsumePartRank(Long projectId, Integer typeId,Object part);


    /**
     * 获取设备的耗能排行信息用于导出
     * @param projectId
     * @return
     */
    Map<String, String[][]> getExportEnergyConsumeRank(Long projectId);

    void removeDevice(Long deviceId);

    /**
     * 删除能源设备
     * @param id
     */
    void removeByEquipmentId(Long id);

    EnergyEquipment findEnergyEquipment(Long id);

    List<EnergyEquipmentDevice> findByProjectId(Long projectId);
}
