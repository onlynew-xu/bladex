package com.steelman.iot.platform.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * iot_sys_content
 * @author 
 */
@Data
public class SystemContent implements Serializable {
    private Long id;

    /**
     * 主页内容
     */
    private String content;

    /**
     * 备注
     */
    private String mark;

    /**
     * 系统Id
     */
    private Long systemId;

    /**
     * 项目Id
     */
    private Long projectId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}