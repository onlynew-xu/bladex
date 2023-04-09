package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Monitor;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.MonitorService;
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
@RequestMapping("/api/monitor")
public class MonitorController extends BaseController {
    @Resource
    private MonitorService monitorService;
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String listAll(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result  =new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<Monitor> monitorList=monitorService.selectByProjectId(projectId);
                if(CollectionUtils.isEmpty(monitorList)){
                   result= Result.empty(result);
                }else{
                    List<EntityDto> dataList=new ArrayList<>();
                    for (Monitor monitor : monitorList) {
                        dataList.add(new EntityDto(monitor.getId(),monitor.getName(),monitor.getProjectId()));
                    }
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/monitor/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
