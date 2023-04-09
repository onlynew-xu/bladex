package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransformerInfoDto {
    private Long id;
    private String name;
    private String brand;
    private String capacity;
    private Float load;

}
