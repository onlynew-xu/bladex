package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Project;
import com.steelman.iot.platform.entity.dto.ProjectDto;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.DeviceService;
import com.steelman.iot.platform.service.ProjectService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
@Api(tags = "测试")
@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController {
    @Resource
    private ProjectService projectService;
    @Resource
    private DeviceService deviceService;
//    @Secured({"ROLE_super_admin"})
    @ApiOperation("获取用户的项目")
    @Log("获取用户的所有项目")
    @PostMapping(value = "/user",produces = CommonUtils.MediaTypeJSON)
    public String  getAllProject(@RequestBody Map<String,Object> paramMap){
        Result<List<Project>> result=new Result<>();
        Object userObj=paramMap.get("userId");
        try {
            if(userObj==null){
                result=Result.paramError(result);
            }else{
                Long userId = Long.parseLong(userObj.toString());
                List<Project> projectList=projectService.getProjectsByUserId(userId);
                if(!CollectionUtils.isEmpty(projectList)){
                    result.setCode(1);
                    result.setData(projectList);
                }else{
                    result=result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/user"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<Project>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/all",produces = CommonUtils.MediaTypeJSON)
    public String  getSuperProject(){
        Result<List<Project>> result=new Result<>();
        result.setCode(0);
        try {
            List<Project> projectList=projectService.findAll();
            result.setCode(1);
            result.setData(projectList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/all"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<Project>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/save",produces = CommonUtils.MediaTypeJSON)
    public String  save(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        try {
            Object nameObj = paramMap.get("name");
            Object commentObj = paramMap.get("comment");
            Object jdObj = paramMap.get("jd");
            Object wdObj = paramMap.get("wd");
            Object doneObj = paramMap.get("done");
            Object typeObj = paramMap.get("type");
            Object provinceObj = paramMap.get("province");
            Object cityObj = paramMap.get("city");
            Object districtObj = paramMap.get("district");
            Object locationObj = paramMap.get("location");
            if(nameObj==null||commentObj==null||jdObj==null||wdObj==null||doneObj==null||typeObj==null||
                    provinceObj==null||cityObj==null||districtObj==null||locationObj==null){
                result=Result.paramError(result);
            }else{
                Project projectOld=projectService.findByProjectName(nameObj.toString());
                if(projectOld!=null){
                    result.setMsg("项目名称已存在");
                }else{
                    projectService.saveProject(paramMap);
                    result.setCode(1);
                    result.setData(1);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/save"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @PostMapping(value = "/update/info",produces = CommonUtils.MediaTypeJSON)
    public String  updateInfo(@RequestBody Map<String,Object> paramMap){
        Result<ProjectDto> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Project project = projectService.findById(projectId);
                ProjectDto projectDto=new ProjectDto(project);
                result.setData(projectDto);
                result.setCode(1);
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/update/info"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<ProjectDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/update",produces = CommonUtils.MediaTypeJSON)
    public String  update(@RequestBody ProjectDto projectDto){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            if(projectDto.getId()==null){
                Result.paramError(result);
            }else{
               Boolean flag=projectService.updateProject(projectDto);
               if(flag){
                   result.setCode(1);
                   result.setData(1);
               }
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/update"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String  remove(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Boolean flag=deviceService.hasDevice(projectId);
                if(flag){
                    result.setMsg("删除失败,项目中还有设备");
                }else{
                    projectService.remove(projectId);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/remove"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

}
