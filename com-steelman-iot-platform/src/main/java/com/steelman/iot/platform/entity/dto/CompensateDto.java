package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompensateDto implements Serializable {
    private Long id;
    private String name;
    private Integer status;
    private String location;
    private Long deviceId;
    private Long deviceTypeId;
    private String pictureUrl;
    private String videoUrl;
    private Integer powerFactory;

    private DeviceDataSmartElecFloatDto smartData;

    private List<PowerAlarmWarnVo> powerAlarmWarnVoList;

    private Integer dataFlag;


    private Map<String,Object> voltDataMap;

    private Map<String,Object> ampDataMap;

    private Map<String,Object> activePowerMap;

    private Map<String,Object> reactivePowerMap;


}
