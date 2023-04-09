package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_alarm_config
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmConfig implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long deviceId;

    /**
     * 设备sn
     */
    @ApiModelProperty(value = "设备sn")
    private String serialNum;

    /**
     * 告警参数Id
     */
    @ApiModelProperty(value = "告警参数Id")
    private Long alarmItemId;

    /**
     * 告警参数名称
     */
    @ApiModelProperty(value = "告警参数名称")
    private String alarmItemName;

    /**
     * 告警次数
     */
    @ApiModelProperty(value = "告警次数")
    private Integer alarmCount;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}