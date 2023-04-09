package com.steelman.iot.platform.entity.dto;

import com.steelman.iot.platform.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String comment;
    private Double jd;
    private Double wd;
    private Integer done;
    private Integer type;
    private Integer active;
    private String province;
    private String city;
    private String district;
    private String location;

    public ProjectDto(Project project){
        this.id=project.getId();
        this.name=project.getName();
        this.comment=project.getComment();
        this.jd=project.getJd();
        this.wd=project.getWd();
        this.done=project.getDone();
        this.type=project.getType();
        this.active=project.getActive();
        this.province=project.getProvince();
        this.city=project.getCity();
        this.district=project.getDistrict();
        this.location=project.getLocation();
    }
}
