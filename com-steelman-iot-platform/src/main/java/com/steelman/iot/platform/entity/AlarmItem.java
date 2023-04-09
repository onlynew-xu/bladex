package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_alarm_item
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class AlarmItem implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 告警项
     */
    @ApiModelProperty(value = "告警项")
    private String name;

    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型")
    private Long alarmTypeId;

    /**
     * 告警父项Id
     */
    @ApiModelProperty(value = "告警类型")
    private Long parentItemId;



    /**
     * 告警父项(温度1过高 属于过温项)
     */
    @ApiModelProperty(value = "告警父项(温度1过高 属于过温项)")
    private String parentItem;

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