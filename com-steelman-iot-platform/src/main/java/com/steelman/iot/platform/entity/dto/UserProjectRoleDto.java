package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tang
 * date 2020-11-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectRoleDto {
    private Long id;

    private Long userId;

    private Long projectId;

    private String projectName;

    private Long roleId;

    private String roleName;

    private String roleDefinition;

    private Date createTime;

    private Date updateTime;

}
