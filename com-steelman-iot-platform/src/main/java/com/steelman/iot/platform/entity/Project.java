package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot_project
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="表描述", description="")
public class Project implements Serializable {

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Long id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double jd;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double wd;

    /**
     * 是否结项(0:否;1:是)
     */
    @ApiModelProperty(value = "是否结项(0:否;1:是)")
    private Integer done;

    /**
     * 项目类型(0商业，1交通，2公建，3工业 4 医院改造)
     */
    @ApiModelProperty(value = "项目类型(0商业，1交通，2公建，3工业 4 医院改造)")
    private Integer type;

    /**
     * 是否可用  0 不可用 1可用
     */
    @ApiModelProperty(value = "是否可用  0 不可用 1可用")
    private Integer active;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String district;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String location;

    /**
     * BIM图片地址
     */
    @ApiModelProperty(value = "BIM图片地址")
    private String bim;

    /**
     * 项目图片地址
     */
    @ApiModelProperty(value = "项目图片地址")
    private String pic;

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