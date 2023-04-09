package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOperatorDto {
    private String username;
    private String operation;
    private Date operationDate;
    private Long operationProjectId;
    private String operationProject;
}
