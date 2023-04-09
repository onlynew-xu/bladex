package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_contact
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="表描述", description="")
public class Contact implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 联系人姓名
     */
    @ApiModelProperty(value = "联系人姓名")
    private String name;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String tel;

    /**
     * 联系人地址
     */
    @ApiModelProperty(value = "联系人地址")
    private String address;

    /**
     * email
     */
    @ApiModelProperty(value = "email")
    private String email;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String mark;

    /**
     * 开启通知 0 关闭 1开启
     */
    @ApiModelProperty(value = "开启通知 0 关闭 1开启")
    private Integer advice;

    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String company;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String dept;

    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String offer;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

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