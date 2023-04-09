package com.steelman.iot.platform.largescreen.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.controller.BaseController;
import com.steelman.iot.platform.entity.Power;
import com.steelman.iot.platform.entity.PowerPicture;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.vo.DeviceTypeAlarmStatistic;
import com.steelman.iot.platform.entity.vo.PowerAlarmWarnVo;
import com.steelman.iot.platform.largescreen.vo.PowerDataInfo;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceCount;
import com.steelman.iot.platform.largescreen.vo.PowerDeviceStatus;
import com.steelman.iot.platform.largescreen.vo.PowerRecentInfo;
import com.steelman.iot.platform.service.AlarmWarnService;
import com.steelman.iot.platform.service.PowerService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/power/web")
@Api(tags = "智慧电房大屏接口",value = "智慧电房大屏数据")
@ApiSupport(order = 2)
public class PowerPingWebController extends BaseController {
    @Resource
    private PowerService powerService;
    @Resource
    private AlarmWarnService alarmWarnService;



    @ApiOperation(value = "所有电房中设备的数量统计类",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 1)
    @RequestMapping(value = "/power/info/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getPowerDeviceInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerDeviceCount> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                PowerDeviceCount powerDeviceCount=powerService.getPowerCountInfo(projectId);
                if(powerDeviceCount==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerDeviceCount>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @ApiOperation(value = "已绑定的智能设备的在线统计",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 2)
    @RequestMapping(value = "/power/device/status",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String powerDeviceStatus(@RequestBody Map<String,Object> paramMap){
        Result<PowerDeviceStatus> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                PowerDeviceStatus powerDeviceStatus=powerService.getPowerDeviceStatus(projectId);
                if(powerDeviceStatus==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDeviceStatus);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/device/status"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerDeviceStatus>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @ApiOperation(value = "获取所有电房的温度和湿度数据",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 3)
    @RequestMapping(value = "/power/humidity/data",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getPowerData(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerDataInfo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<PowerDataInfo> powerDataInfoList=powerService.getPowerDataInfo(projectId);
                if(CollectionUtils.isEmpty(powerDataInfoList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerDataInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/humidity/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerDataInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @ApiOperation(value = "获取所有电房未处理的预警信息",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 4)
    @RequestMapping(value = "/power/alarm/data",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getPowerAlarmData(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerAlarmWarnVo>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                List<PowerAlarmWarnVo> vos = alarmWarnService.getPowerAlarmWarnVo(projectId, 2000L);
                if(CollectionUtils.isEmpty(vos)){
                   result =Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(vos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/alarm/data"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerAlarmWarnVo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @ApiOperation(value = "获取指定电房告警类型占比",notes = "传入项目Id和电房Id(例如:projectId:1,powerId:1)")
    @ApiOperationSupport(order = 5)
    @RequestMapping(value = "/power/alarm/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getPowerAlarmStatistic(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object powerIdObj = paramMap.get("powerId");
            if(projectIdObj==null||powerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId= Long.parseLong(projectIdObj.toString());
                Long powerId= Long.parseLong(powerIdObj.toString());
                List<DeviceTypeAlarmStatistic> deviceTypeAlarmStatistics=powerService.getPowerAlarmStatistic(projectId,powerId);
                if(CollectionUtils.isEmpty(deviceTypeAlarmStatistics)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(deviceTypeAlarmStatistics);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/alarm/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @ApiOperation(value = "获取指定电房中电房设备的数量",notes = "传入项目Id和电房Id(例如:projectId:1,powerId:1)")
    @ApiOperationSupport(order = 6)
    @RequestMapping(value = "/power/equipment/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getPowerEquipment(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object powerIdObj = paramMap.get("powerId");
            if(projectIdObj==null||powerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long powerId= Long.parseLong(powerIdObj.toString());
                List<DeviceTypeAlarmStatistic> powerEquipment=powerService.getPowerEquipment(projectId,powerId);
                if(CollectionUtils.isEmpty(powerEquipment)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerEquipment);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/equipment/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }



    @ApiOperation(value = "获取指定电房的电流电压等信息",notes = "传入电房Id(例如:powerId:1)")
    @ApiOperationSupport(order = 7)
    @RequestMapping(value = "/power/base/info",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String powerBaseInfo(@RequestBody Map<String,Object> paramMap){
        Result<PowerRecentInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                PowerRecentInfo dataMap=powerService.getPowerBaseInfo(powerId);
                if(dataMap==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/base/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }



    @ApiOperation(value = "获取指定电房的实景图和系统图",notes = "传入电房Id(例如:powerId:1)")
    @ApiOperationSupport(order = 7)
    @RequestMapping(value = "/power/picture",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String powerPicture(@RequestBody Map<String,Object> paramMap){
        Result<PowerPicture> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            if(powerIdObj==null){
                result=Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                PowerPicture dataMap=powerService.getPowerPicture(powerId);
                if(dataMap==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/picture"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }


    @ApiOperation(value = "获取所有电房的信息",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 8)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    @RequestMapping(value = "/power/list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String getPowerList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<Power> dataList=powerService.selectByProjectId(projectId);
                if(CollectionUtils.isEmpty(dataList)){
                    result=Result.empty(result);
                }else{
                    List<EntityDto> data=new ArrayList<>();
                    for (Power power : dataList) {
                        data.add(new EntityDto(power.getId(),power.getName(),power.getProjectId()));
                    }
                    result.setCode(1);
                    result.setData(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/web/power/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }








}
