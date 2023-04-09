package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectionTaskDto {

    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String username;
    private Integer status;
    private Date doingTime;
    private Date createTime;
    private Date updateTime;


}
