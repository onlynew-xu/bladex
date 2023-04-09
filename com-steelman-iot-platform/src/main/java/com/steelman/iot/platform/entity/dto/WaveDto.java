package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaveDto {
    private Long id;
    private String name;
    private Integer status;
    private String location;
    private Long deviceId;
    private Long deviceTypeId;
    private String pictureUrl; //图片
    private String videoUrl;  //监控地址

    private String thdi; //电流畸变率
    private String thdv; //电压畸变率
    private String waveAmp; //补偿斜波电流数据
    private String waveVolt; //补偿谐波电压数据

    private List<PowerAlarmWarnVo> powerAlarmWarnVoList; //滚动告警信息

    private Integer dataFlag;

    private DeviceDataSmartElecFloatDto smartData;

    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> activePowerMap;

    private Map<String,Object> reactivePowerMap;
}
