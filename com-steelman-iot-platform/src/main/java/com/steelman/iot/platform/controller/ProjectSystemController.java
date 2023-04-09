package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.ProjectSystem;
import com.steelman.iot.platform.entity.dto.SystemSimpleInfo;
import com.steelman.iot.platform.entity.vo.ProjectSystemDetail;
import com.steelman.iot.platform.service.ProjectSystemService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projectSystem")
public class ProjectSystemController extends BaseController {
    @Resource
    private ProjectSystemService projectSystemService;

    @PostMapping(value = "getActiveSystem",produces = CommonUtils.MediaTypeJSON)
    public String getActiveSystem(@RequestBody Map<String,Object> param){
        Result<List<SystemSimpleInfo>> result= null;
        try {
            Object projectObj = param.get("projectId");
            result = new Result<>();
            if(projectObj!=null){
                Long projectId=Long.parseLong(projectObj.toString());
                List<ProjectSystemDetail> allActiveSystem = projectSystemService.getAllActiveSystem(projectId);
                if(CollectionUtils.isEmpty(allActiveSystem)){
                    result = Result.empty(result);
                }else{
                    List<SystemSimpleInfo> dataList=new ArrayList<>();
                    for (ProjectSystemDetail projectSystemDetail : allActiveSystem) {
                        dataList.add(new SystemSimpleInfo(projectSystemDetail.getSystemId(),projectSystemDetail.getSystemName(),projectSystemDetail.getStatus()));
                    }
                    result.setCode(1);
                    result.setData(dataList);
                    result.setMsg("success");
                }
            }else{
                    result = Result.paramError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/projectSystem/getActiveSystem"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<SystemSimpleInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "info",produces = CommonUtils.MediaTypeJSON)
    public String projectSystemInfo(@RequestBody Map<String,Object> param){
        Result<List<ProjectSystem>> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            if(projectObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<ProjectSystem> projectSystemList= projectSystemService.projectSystemInfo(projectId);
                if(!CollectionUtils.isEmpty(projectSystemList)){
                    result.setData(projectSystemList);
                    result.setCode(1);
                }else {
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/projectSystem/info"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<ProjectSystem>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "save",produces = CommonUtils.MediaTypeJSON)
    public String saveProjectSystem(@RequestBody Map<String,Object> param){
        Result<Integer> result= new Result<>();
        result.setCode(0);
        try {
            Object projectObj = param.get("projectId");
            Object systemIdsObj = param.get("systemIds");
            if(projectObj==null||systemIdsObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                String systemIds= systemIdsObj.toString();
                int num = projectSystemService.saveSystemIds(projectId, systemIds);
                if(num>0){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/projectSystem/save"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "remove",produces = CommonUtils.MediaTypeJSON)
    public String removeProjectSystem(@RequestBody Map<String,Object> param){
        Result<Integer> result= new Result<>();
        result.setCode(0);
        try {
            Object idObj = param.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                projectSystemService.removeProjectSystem(id);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/projectSystem/remove"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
