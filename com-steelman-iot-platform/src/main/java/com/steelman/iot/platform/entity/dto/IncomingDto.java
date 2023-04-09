package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import com.steelman.iot.platform.entity.vo.WeekEnergyConsumeStatistic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingDto {
    private Long id;
    private String name;
    private Integer status;
    private String location;
    private Long deviceId;
    private Long deviceTypeId;
    private String picture;
    private String videoUrl;

    private List<PowerAlarmWarnVo> powerAlarmWarnVoList;

    private Integer dataFlag;

    private DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto;

    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> activePowerMap;

    private Map<String,Object> reactivePowerMap;

    private Map<String,Object> factorDataMap;
    //最近七天
    private Map<String,List<WeekEnergyConsumeStatistic>> measureData;




}
