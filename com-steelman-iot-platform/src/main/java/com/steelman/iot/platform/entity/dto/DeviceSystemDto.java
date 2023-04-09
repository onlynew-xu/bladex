package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tang
 * date 2020-12-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceSystemDto implements Serializable {
    /**
     * CREATE TABLE `iot_device_system` (
     *   `id` bigint(32) NOT NULL,
     *   `device_id` bigint(32) DEFAULT NULL COMMENT '设备Id',
     *   `serial_num` varchar(50) DEFAULT NULL COMMENT '设备sn',
     *   `name` varchar(20) DEFAULT NULL COMMENT '系统中的设备名称',
     *   `system_id` bigint(32) DEFAULT NULL COMMENT '系统Id',
     *   `project_id` bigint(32) DEFAULT NULL COMMENT '项目Id',
     *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
     *   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
     *   PRIMARY KEY (`id`) USING BTREE
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='设备系统表';
     */
    private Long id;
    private Long deviceId;
    private String serialNum;
    private String name;
    private Long systemId;
    private Long projectId;
    private Date createTime;
    private Date updateTime;
    private Long deviceTypeId;
    private Integer status;
}
