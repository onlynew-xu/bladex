package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_alarm_warn
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class AlarmWarn implements Serializable {
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
     * 设备所在区域
     */
    @ApiModelProperty(value = "设备所在区域")
    private Long areaId;

    /**
     * 设备所在建筑
     */
    @ApiModelProperty(value = "设备所在建筑")
    private Long buildingId;

    /**
     * 设备所在层
     */
    @ApiModelProperty(value = "设备所在层")
    private Long storeyId;

    /**
     * 设备所在房间
     */
    @ApiModelProperty(value = "设备所在房间")
    private Long roomId;

    /**
     * 自定义位置
     */
    @ApiModelProperty(value = "自定义位置")
    private String location;

    /**
     * 系统Id
     */
    @ApiModelProperty(value = "系统Id")
    private Long systemId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 设备关联的告警项
     */
    @ApiModelProperty(value = "设备关联的告警项")
    private Long alarmItemId;

    /**
     * 设备的告警类型
     */
    @ApiModelProperty(value = "设备的告警类型")
    private Long alarmTypeId;

    /**
     * 设备的告警值
     */
    @ApiModelProperty(value = "设备的告警值")
    private String alarmValue;


    /**
     * 处理标志 0 未处理 1 已处理
     */
    @ApiModelProperty(value = "处理标志 0 未处理 1 已处理")
    private Integer handleFlag;

    /**
     * 是否生成维保单 0 未生成 1 已生成
     */
    @ApiModelProperty(value = "是否生成维保单 0 未生成 1 已生成")
    private Integer createTask;

    /**
     * 生成任务类型  0 未生成 1 终端设备维保单 2配电柜维保单
     */
    @ApiModelProperty(value = "生成任务类型  0 未生成 1 终端设备维保单 2配电柜维保单")
    private Integer taskType;

    /**
     * 关联的维保单id
     */
    @ApiModelProperty(value = "关联的维保单id")
    private Long taskId;


    /**
     * 年
     */
    @ApiModelProperty(value = "年")
    private String year;

    /**
     * 月
     */
    @ApiModelProperty(value = "月")
    private String month;

    /**
     * 月
     */
    @ApiModelProperty(value = "日")
    private String day;


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