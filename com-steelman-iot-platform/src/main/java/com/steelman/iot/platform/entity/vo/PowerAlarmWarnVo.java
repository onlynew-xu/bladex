package com.steelman.iot.platform.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("电房告警信息")
public class PowerAlarmWarnVo {

    @ApiModelProperty(value = "告警Id")
    private Long alarmWarnId;

    @ApiModelProperty(value = "电房名称")
    private String powerName;

    @ApiModelProperty(value = "电房Id")
    private Long powerId;

    @ApiModelProperty(value = "电房设备名称 例如 1号进线柜")
    private String powerDeviceName;

    @ApiModelProperty(value = "电房设备Id")
    private Long powerDeviceId;

    @ApiModelProperty(value = "回路Id")
    private Long loopId;

    @ApiModelProperty(value = "回路名称")
    private String loopName;

    @ApiModelProperty(value = "位置信息")
    private String location;

    @ApiModelProperty(value = "告警项目Id")
    private Long alarmItemId;

    @ApiModelProperty(value = "告警项目名称")
    private String alarmItemName;

    @ApiModelProperty(value = "告警时间")
    private Date createTime;


}
