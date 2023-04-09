package com.steelman.iot.platform.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasureData {
    private Float total;
    private Float spike;
    private Float peak;
    private Float normal;
    private Float valley;
}
