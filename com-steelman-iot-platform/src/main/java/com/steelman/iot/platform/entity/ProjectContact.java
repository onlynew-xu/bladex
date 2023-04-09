package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_project_contact
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class ProjectContact implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 联系人Id
     */
    @ApiModelProperty(value = "联系人Id")
    private Long contactId;

    /**
     * 告警通知状态 0:开启通知 1:关闭通知
     */
    @ApiModelProperty(value = "告警通知状态 0:开启通知 1:关闭通知")
    private Integer notice;

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