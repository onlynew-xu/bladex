package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project/system/manager")
public class ProjectSystemManagerController extends BaseController {
    @Resource
    private ProjectSystemService projectSystemService;

    @PostMapping(value = "/list",produces = CommonUtils.MediaTypeJSON)
    public String systemList(@RequestBody Map<String,Object> paramMap){
        Result<List<ProjectSystemDetail>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<ProjectSystemDetail> allSystem = projectSystemService.getAllSystem(projectId);
                if(CollectionUtils.isEmpty(allSystem)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(allSystem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/system/manager/list"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<ProjectSystemDetail>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "/update/status",produces = CommonUtils.MediaTypeJSON)
    public String updateSystemStatus(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object systemIdObj = paramMap.get("systemId");
            Object statusObj = paramMap.get("status");
            if(projectIdObj==null||systemIdObj==null||statusObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long systemId=Long.parseLong(systemIdObj.toString());
                Integer status=Integer.parseInt(statusObj.toString());
                projectSystemService.updateStatus(projectId,systemId,status);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/project/system/manager/update/status"));
            result = Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
