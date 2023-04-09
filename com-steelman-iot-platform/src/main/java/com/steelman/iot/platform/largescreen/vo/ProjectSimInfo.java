package com.steelman.iot.platform.largescreen.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSimInfo implements Serializable {
    private Long projectId;
    private String projectName;
    private Double jd;
    private Double wd;
    private Integer defaultFlg; //flag值为0 非默认 1:默认中心点
}
