package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransformLoadDto {
    private Long transformId;
    private String transformName;
    private String percent;
    private Integer defaultFlag;
}
