package com.steelman.iot.platform.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * iot_project_index
 * @author 
 */
@Data
public class ProjectIndex implements Serializable {
    private Long id;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 登录页 1:登录页1  2:登录页2
     */
    private Integer loginIndex;

    /**
     * 公司logo
     */
    private String logoUri;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}