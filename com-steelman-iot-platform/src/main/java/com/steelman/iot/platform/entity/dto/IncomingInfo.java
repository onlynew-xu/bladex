package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncomingInfo {
    private Long id;
    private String name;
    private Integer status;
    private String location;
    private Long deviceId;
    private Long deviceTypeId;
    private String picture;
    private String videoUrl;
    private Integer dataFlag;
    private List<PowerAlarmWarnVo> powerAlarmWarnVoList;
    private DeviceDataSmartElecFloatDto deviceDataSmartElecFloatDto;
}
