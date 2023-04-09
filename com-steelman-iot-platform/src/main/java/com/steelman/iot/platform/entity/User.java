package com.steelman.iot.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * sys_user
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class User implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 状态 0:禁用，1:正常
     */
    @ApiModelProperty(value = "状态 0:禁用，1:正常")
    private Integer status;

    /**
     * 超级管理员: 0:不是超级管理员  1:是超级管理员
     */
    @ApiModelProperty(value = "超级管理员: 0:不是超级管理员  1:是超级管理员")
    private Integer admin;

    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id")
    private Long creatorId;

    /**
     * 性别 0:男 1:女
     */
    @ApiModelProperty(value = "性别 0:男 1:女")
    private Integer sex;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    private Date birth;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String picture;

    /**
     * 头像地址
     */
    @ApiModelProperty(value = "头像地址")
    private String pictureUrl;

    /**
     * 居住地
     */
    @ApiModelProperty(value = "居住地")
    private String address;

    /**
     * 爱好
     */
    @ApiModelProperty(value = "爱好")
    private String hobby;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市")
    private String city;

    /**
     * 所在地区
     */
    @ApiModelProperty(value = "所在地区")
    private String district;
    /**
     * 公司Id
     */
    @ApiModelProperty(value = "公司Id")
    private Long companyId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String dept;

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