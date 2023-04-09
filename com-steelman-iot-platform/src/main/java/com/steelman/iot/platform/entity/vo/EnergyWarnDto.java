package com.steelman.iot.platform.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tang
 * @date 2022/5/31 下午3:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "能源告警信息")
public class EnergyWarnDto {

    private Long equipmentId;

    private String equipmentName;

    private Long deviceId;

    private String deviceName;

    private Integer level;

    private Long alarmItemId;

    private String alarmItemName;

    private String value;

    private Integer handleFlag;

    private String dateTime;

}
