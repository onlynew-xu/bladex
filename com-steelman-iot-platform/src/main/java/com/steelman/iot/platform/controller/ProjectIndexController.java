package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.ProjectIndex;
import com.steelman.iot.platform.service.ProjectIndexService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/project/index")
public class ProjectIndexController extends BaseController {
    @Resource
    private ProjectIndexService projectIndexService;

    @RequestMapping(value = "save",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String saveProjectIndex(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object loginIndexObj = paramMap.get("loginIndex");
            if(projectIdObj==null||loginIndexObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Integer loginIndex=Integer.parseInt(loginIndexObj.toString());
                ProjectIndex projectIndex=new ProjectIndex();
                projectIndex.setProjectId(projectId);
                projectIndex.setLoginIndex(loginIndex);
                projectIndex.setCreateTime(new Date());
                projectIndex.setUpdateTime(projectIndex.getCreateTime());
                projectIndexService.saveProjectIndex(projectIndex);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/index/save"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @RequestMapping(value = "update",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String updateProjectIndex(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            Object loginIndexObj = paramMap.get("loginIndex");
            if(idObj==null||loginIndexObj==null){
                result= Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                Integer loginIndex=Integer.parseInt(loginIndexObj.toString());
                ProjectIndex projectIndex=new ProjectIndex();
                projectIndex.setId(id);
                projectIndex.setLoginIndex(loginIndex);
                projectIndex.setUpdateTime(new Date());
                projectIndexService.updateIndex(projectIndex);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/index/update"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
