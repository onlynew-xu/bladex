package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratorDto {
    private Long id;
    private String name;
    private String brand;
    private Integer status;
    private String powerGeneration;
    private Long transformerId;
    private String transformerName;
}
