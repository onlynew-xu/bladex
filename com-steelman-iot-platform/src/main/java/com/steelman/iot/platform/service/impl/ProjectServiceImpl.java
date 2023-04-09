package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.ProjectDao;
import com.steelman.iot.platform.entity.Pager;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.UserProjectRole;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.ProjectDto;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import com.steelman.iot.platform.service.ProjectService;
import com.steelman.iot.platform.service.UserProjectRoleService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author tang
 * date 2020-11-23
 */
@Service("projectService")
public class ProjectServiceImpl extends BaseService implements ProjectService  {
    @Resource
    private ProjectDao projectDao;
    @Resource
    private UserProjectRoleService userProjectRoleService;
    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        return projectDao.getProjectsByUserId(userId);
    }

    @Override
    public List<ProjectSimInfo> getProjectSimInfoByUserId(Long userId) {
        return projectDao.getProjectSimInfoByUserId(userId);
    }

    @Override
    public void deleteProjectPicture(Project record) {
        projectDao.deletePictureByid(record);
    }

    @Override
    public void updateElectricRoomPicture(Project record) {
        projectDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Project> getProjectsPictureById(Long id) {
        return projectDao.getProjectsPictureById(id);
    }

    @Override
    public Pager<T> selectByAll(Map<String, Integer> params) {
        List<T> list = projectDao.selectByAll((params.get("page")-1)*params.get("size"),params.get("size"));
        Pager<T> pager = pagerText(params,list);
        return pager;
    }

    @Override
    public void addProjectPicture(Project projectPicture) {
        projectDao.addProjectPicture(projectPicture);
    }

    @Override
    public Project findById(Long projectId) {
        return projectDao.selectByPrimaryKey(projectId);
    }

    @Override
    public int update(Project project) {
        return projectDao.updateByPrimaryKeySelective(project);
    }

    @Override
    public List<Project> findAll() {
        return projectDao.findAll();
    }

    @Override
    public List<EntityDto> getSelectInfo(Long userId) {
        List<Project> all = projectDao.findAll();
        Map<Long,String> dataMap=new LinkedHashMap<>();
        for (Project project : all) {
            dataMap.put(project.getId(),project.getName());
        }
        List<UserProjectRole> userProjectRoleList = userProjectRoleService.findByUserId(userId);
        if(!CollectionUtils.isEmpty(userProjectRoleList)){
            for (UserProjectRole userProjectRole : userProjectRoleList) {
                dataMap.remove(userProjectRole.getProjectId());
            }
        }
        List<EntityDto> entityDtoList=new ArrayList<>();
        for (Map.Entry<Long, String> longStringEntry : dataMap.entrySet()) {
            Long key = longStringEntry.getKey();
            String value = longStringEntry.getValue();
            entityDtoList.add(new EntityDto(key,value,key));
        }
        return entityDtoList;
    }

    @Override
    public int saveProject(Map<String, Object> paramMap) {
        Project project=new Project();
        project.setActive(1);
        project.setName(paramMap.get("name").toString());
        project.setComment(paramMap.get("comment").toString());
        project.setJd(Double.parseDouble(paramMap.get("jd").toString()));
        project.setWd(Double.parseDouble(paramMap.get("wd").toString()));
        project.setDone(Integer.parseInt(paramMap.get("done").toString()));
        project.setType(Integer.parseInt(paramMap.get("type").toString()));
        project.setProvince(paramMap.get("province").toString());
        project.setCity(paramMap.get("city").toString());
        project.setDistrict(paramMap.get("district").toString());
        project.setLocation(paramMap.get("location").toString());
        project.setCreateTime(new Date());
        project.setUpdateTime(project.getCreateTime());
        return projectDao.insertSelective(project);
    }

    @Override
    public Project findByProjectName(String name) {
        return projectDao.findByProjectName(name);
    }

    @Override
    public Boolean updateProject(ProjectDto projectDto) {
        Project project=new Project();
        Long id = projectDto.getId();
        Boolean flag=false;
        project.setId(id);
        Project projectNew=new Project();
        Integer active = projectDto.getActive();
        Integer done = projectDto.getDone();
        Integer type = projectDto.getType();
        String city = projectDto.getCity();
        String province = projectDto.getProvince();
        Double jd  = projectDto.getJd();
        Double wd = projectDto.getWd();
        String comment = projectDto.getComment();
        String district = projectDto.getDistrict();
        String location = projectDto.getLocation();
        if(active!=null){
            flag=true;
            projectNew.setActive(active);
        }
        if(done!=null){
            flag=true;
            projectNew.setDone(active);
        }
        if(type!=null){
            flag=true;
            projectNew.setType(type);
        }
        if(city!=null){
            flag=true;
            projectNew.setCity(city);
        }
        if(province!=null){
            flag=true;
            projectNew.setProvince(province);
        }
        if(jd!=null){
            flag=true;
            projectNew.setJd(jd);
        }
        if(wd!=null){
            flag=true;
            projectNew.setWd(wd);
        }
        if(comment!=null){
            flag=true;
            projectNew.setComment(comment);
        }
        if(district!=null){
            flag=true;
            projectNew.setDistrict(district);
        }
        if(location!=null){
            flag=true;
            projectNew.setLocation(location);
        }
        if(flag){
            projectNew.setUpdateTime(new Date());
        }
        projectDao.updateByPrimaryKeySelective(projectNew);
        return true;
    }

    @Override
    public int remove(Long projectId) {
        return projectDao.deleteByPrimaryKey(projectId);
    }
}
