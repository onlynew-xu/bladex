package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PowerEntityDto {
    private Long id;
    private String name;
    private Integer status;
    private Long transformerId;
    private Long powerId;
    private Long projectId;
}
