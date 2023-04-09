package com.steelman.iot.platform.largescreen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用电安全告警信息类")
public class AlarmWarnDetailInfo {

    @ApiModelProperty(value = "告警Id")
    private Long alarmWarnId;

    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "设备状态")
    private Integer status;

    @ApiModelProperty(value = "设备位置")
    private String location;

    @ApiModelProperty(value = "处理标志")
    private Integer handleFlag;

    @ApiModelProperty(value = "产生时间")
    private String dateTime;

    @ApiModelProperty(value = "告警类型名称")
    private String alarmItemName;

    @ApiModelProperty(value = "项目Id")
    private Long projectId;

}
