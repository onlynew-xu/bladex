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
public class PowerFeederInfoDto {

    private Long id;
    private String name;
    private Integer status;
    private String location;
    private Long deviceId;
    private Long deviceTypeId;
    private Long loopId;
    private String loopName;
    private String pictureUrl; //图片
    private String videoUrl;  //监控地址


    private List<PowerAlarmWarnVo> powerAlarmWarnVoList; //滚动告警信息

    private Integer dataFlag;

    private DeviceDataSmartElecFloatDto smartData;

    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> activePowerMap;

    private Map<String,Object> reactivePowerMap;

    private Map<String,Object> powerFactorMap;

    private Map<String,List<WeekEnergyConsumeStatistic>> measureData;
}
