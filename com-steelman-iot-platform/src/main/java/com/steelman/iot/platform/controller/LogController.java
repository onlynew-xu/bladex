package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.LogLogin;
import com.steelman.iot.platform.entity.dto.LogLoginDto;
import com.steelman.iot.platform.entity.dto.LogOperatorDto;
import com.steelman.iot.platform.service.LogLoginService;
import com.steelman.iot.platform.service.LogOperatorService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.ApiOperation;
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

/**
 * @Author lsj
 * @DATE 2021/4/2 0002 15:15
 * @Description:
 */
@RestController
@RequestMapping("/api/log")

public class LogController extends BaseController{

    @Resource
    private LogLoginService logLoginService;
    @Resource
    private LogOperatorService logOperatorService;


    @PostMapping(value = "/login",produces = CommonUtils.MediaTypeJSON)
    public String getLoginLog(@RequestBody Map<String,Object> paramMap){
        Result<List<LogLoginDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object userIdObj = paramMap.get("userId");
            if(userIdObj==null){
                Result.paramError(result);
            }else{
                Long userId= Long.parseLong(userIdObj.toString());
                List<LogLogin> list=logLoginService.getLogLogin(userId);
                if(CollectionUtils.isEmpty(list)){
                    result=Result.empty(result);
                }else{
                    List<LogLoginDto> logLoginDtoList=new ArrayList<>();
                    for (LogLogin logLogin : list) {
                        logLoginDtoList.add(new LogLoginDto(logLogin.getUsername(),logLogin.getLoginTime()));
                    }
                    result.setCode(1);
                    result.setData(logLoginDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/log/login"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<LogLoginDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @ApiOperation("操作日志")
    @PostMapping("/operate")
    public String getOperateLog(@RequestBody Map<String,Object> paramMap){
        Result<List<LogOperatorDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<LogOperatorDto> logOperatorDtoList=logOperatorService.getLogOperateByProjectId(projectId);
                if(CollectionUtils.isEmpty(logOperatorDtoList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(logOperatorDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/log/operate"));
            result=Result.exceptionRe(result);

        }
        Type type=new TypeToken<Result<List<LogOperatorDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
