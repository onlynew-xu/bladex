package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * iot_log_project_user
 * @author 
 */
@Data
@ApiModel(value="表描述", description="")
public class LogProjectUser implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long projectId;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private String projectName;

    /**
     * 角色Id
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 登陆时间
     */
    @ApiModelProperty(value = "登陆时间")
    private String loginTime;

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

    private static final long serialVersionUID = 1L;
}