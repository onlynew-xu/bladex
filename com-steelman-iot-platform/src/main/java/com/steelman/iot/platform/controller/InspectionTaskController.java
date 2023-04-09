package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.InspectionTask;
import com.steelman.iot.platform.entity.dto.InspectionTaskDto;
import com.steelman.iot.platform.service.InspectionTaskService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(tags = "巡检任务")
@RequestMapping("/api/inspection/task")
public class InspectionTaskController extends  BaseController {
    @Resource
    private InspectionTaskService inspectionTaskService;

    /**
     * 统计用户未处理的巡检任务的数量
     * @param paramData
     * @return
     */
    @PostMapping(value = "/inHandler/count",produces = CommonUtils.MediaTypeJSON)
    public String inHandlerCount(@RequestBody Map<String,Object> paramData) {
        Object projectObj = paramData.get("projectId");
        Object userObj = paramData.get("userId");
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            if (projectObj == null || userObj == null) {
                result.setMsg("param error");
            } else {
                Long projectId = Long.parseLong(projectObj.toString());
                Long userId = Long.parseLong(userObj.toString());
                Integer inHandlerCount = inspectionTaskService.getInHandlerCount(projectId, userId);
                if (inHandlerCount != null) {
                    result.setCode(1);
                    result.setData(inHandlerCount);
                } else {
                    result.setMsg("empty");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/inspection/task/inHandler/count"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 获取所有巡检任务
     * @param paramData
     * @return
     */
    @Resource
    private InspectionTaskService taskService;
    @ApiOperation("巡检任务列表")
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String inspectionTaskList(@RequestBody Map<String,Object> paramMap) {
        Result<List<InspectionTaskDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object userIdObj = paramMap.get("userId");
            Object statusObj = paramMap.get("status");
            if (Objects.isNull(projectIdObj)|| Objects.isNull(userIdObj)||Objects.isNull(statusObj)) {
                Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long userId=Long.parseLong(userIdObj.toString());
                Integer status=Integer.parseInt(statusObj.toString());
                List<InspectionTaskDto> inspectionTaskDtoList=inspectionTaskService.getUserTask(projectId,userId,status);
                if(CollectionUtils.isEmpty(inspectionTaskDtoList)){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(inspectionTaskDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/inspection/task/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<InspectionTaskDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }
    /**
     * 更新任务的状态
     * @param paramData
     * @return
     */
   @ApiOperation("更新任务的状态")
    @PostMapping(value = "/update/status",produces = CommonUtils.MediaTypeJSON)
    public String updateStatus(@RequestBody Map<String,Object> paramData){
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object statusObj = paramData.get("status");
            Object idObj = paramData.get("id");
            if (statusObj == null || idObj == null) {
                result=Result.paramError(result);
            } else {
                Integer status = Integer.parseInt(statusObj.toString());
                Long id = Long.parseLong(idObj.toString());
                if(status>1){
                    result=Result.paramError(result);
                }else{
                    status=status+1;
                    InspectionTask task=new InspectionTask();
                    task.setId(id);
                    task.setStatus(status);
                    Date date=new Date();
                    if(status.equals(1)){
                        task.setBeginTime(date);
                    }
                    if(status.equals(2)){
                        task.setFinishTime(date);
                    }
                    task.setUpdateTime(date);
                    Boolean flag = inspectionTaskService.updateTask(task);
                    if(flag){
                        result.setCode(1);
                        result.setData(1);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/inspection/task/update/status"));
            result= Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<Integer>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }
    @ApiOperation("删除任务")
    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String deleteTask(@RequestBody Map<String,Object> paramData){
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object userObj = paramData.get("userId");
            Object taskObj = paramData.get("taskId");
            if (taskObj==null) {
                result=Result.paramError(result);
            } else {
                Long userId = Long.parseLong(userObj.toString());
                Long taskId = Long.parseLong(taskObj.toString());
                Boolean flag = inspectionTaskService.deleteTask(userId,taskId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/inspection/task/remove"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

}
