package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_alarm_info
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class AlarmInfo implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 设备Id
     */
    @ApiModelProperty(value = "设备Id")
    private Long deviceId;

    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String serialNum;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    /**
     * 设备所在区域
     */
    @ApiModelProperty(value = "设备所在区域")
    private Long areaId;

    /**
     * 设备所在楼信息
     */
    @ApiModelProperty(value = "设备所在楼信息")
    private Long buildingId;

    /**
     * 设备所在层信息
     */
    @ApiModelProperty(value = "设备所在层信息")
    private Long storeyId;

    /**
     * 设备所在房间信息
     */
    @ApiModelProperty(value = "设备所在房间信息")
    private Long roomId;

    /**
     * 自定义位置
     */
    @ApiModelProperty(value = "自定义位置")
    private String location;



    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型")
    private Long alarmTypeId;

    /**
     * 告警项
     */
    @ApiModelProperty(value = "告警项")
    private Long alarmItemId;

    /**
     * 告警等级
     */
    @ApiModelProperty(value = "告警等级")
    private Long alarmLevelId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 处理状态 0 未处理 1 已处理
     */
    @ApiModelProperty(value = "处理状态 0 未处理 1 已处理")
    private Integer status;

    /**
     * 关联的告警单
     */
    @ApiModelProperty(value = "关联的告警单")
    private Long alarmWarnId;

    /**
     * 告警值
     */
    @ApiModelProperty(value = "告警值")
    private String alarmValue;

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