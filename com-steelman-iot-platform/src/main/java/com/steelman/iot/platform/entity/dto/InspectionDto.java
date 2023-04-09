package com.steelman.iot.platform.entity.dto;

import lombok.Data;

/**
 * @Author lsj
 * @DATE 2021/4/1 0001 14:45
 * @Description:
 */
@Data
public class InspectionDto {
    private Long id;
    private String content;
    private String title;
    private Integer evenType;
    private String evenWeek;
    private String evenMonth;
    private Long userId;
    private String beginTime;
    private String endTime;
    private Integer status;
    private String username;

}
