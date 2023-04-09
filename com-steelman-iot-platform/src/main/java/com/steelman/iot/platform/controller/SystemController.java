package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.SysSystem;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.SystemService;
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

/**
 * @author tang
 * date 2020-12-02
 */
@RestController
@RequestMapping("/api/system")
public class SystemController extends BaseController{
    @Resource
    private SystemService systemService;

    /**
     * 获取项目的系统
     * @param paramMap
     * @return
     */
    @Log("获取项目的系统")
    @PostMapping(value = "getProjectSystem",produces = CommonUtils.MediaTypeJSON)
    public String getSystemsByProject(@RequestBody Map<String,Object> paramMap){
        Result<List<SysSystem>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectObj.toString());
                List<SysSystem> sysSystemList=systemService.getByProjectId(projectId);
                if(!CollectionUtils.isEmpty(sysSystemList)){
                    result.setData(sysSystemList);
                    result.setCode(1);
                }else{
                    result = Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/system/getProjectSystem"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<SysSystem>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取所有系统列表
     * @return
     */
    @PostMapping(value = "/systemList",produces = CommonUtils.MediaTypeJSON)
    public String systemList(){
        Result<List<SysSystem>> result=new Result<>();
        result.setCode(0);
        try {
            List<SysSystem> sysSystemList = systemService.selectByAll();
            if(CollectionUtils.isEmpty(sysSystemList)){
                result=Result.empty(result);
            }else{
                result.setData(sysSystemList);
                result.setCode(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/system/systemList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<SysSystem>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
