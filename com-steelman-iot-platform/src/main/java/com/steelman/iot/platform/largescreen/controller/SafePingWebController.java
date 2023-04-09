package com.steelman.iot.platform.largescreen.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.controller.BaseController;
import com.steelman.iot.platform.entity.vo.DeviceTypeAlarmStatistic;
import com.steelman.iot.platform.largescreen.vo.AlarmWarnDetailInfo;
import com.steelman.iot.platform.largescreen.vo.AlarmWarnStatistic;
import com.steelman.iot.platform.largescreen.vo.SafeDeviceCount;
import com.steelman.iot.platform.service.AlarmWarnService;
import com.steelman.iot.platform.service.DeviceService;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/safe/web")
@Api(tags = "用电安全大屏接口",value = "用电安全大屏数据")
@ApiSupport(order = 1)
public class SafePingWebController extends BaseController {
    @Resource
    private AlarmWarnService alarmWarnService;
    @Resource
    private DeviceService deviceService;

    @ApiOperation(value = "获取告警数据统计",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order=1)
    @RequestMapping(value = "/alarmWarn/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String alarmWarnStatistic(@RequestBody Map<String,Object> paramMap){
        Result<AlarmWarnStatistic> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                AlarmWarnStatistic alarmWarnStatistic= alarmWarnService.getSafePingCountStatistic(projectId);
                if(alarmWarnStatistic==null){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(alarmWarnStatistic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarmWarn/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<AlarmWarnStatistic>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "设备数量统计",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order=2)
    @RequestMapping(value = "/device/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String deviceStatistic(@RequestBody Map<String,Object> paramMap){
        Result<SafeDeviceCount> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectObj.toString());
                SafeDeviceCount safeDeviceCount=deviceService.safeDeviceStatus(projectId);
                if(safeDeviceCount!=null){
                    result.setData(safeDeviceCount);
                    result.setCode(1);
                }else{
                    result=Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/device/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SafeDeviceCount>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @ApiOperation(value = "告警月环比数量",notes = "传入项目Id(例如:projectId:1)")
    @RequestMapping(value = "/alarm/ring/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiOperationSupport(order=3)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getAlarmRingData(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,Object>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectObj.toString());
                Map<String,Object> dataMap=alarmWarnService.getSafeAlarmRingData(projectId);
                if(CollectionUtils.isEmpty(dataMap)){
                   result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarm/ring/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "近期告警数据统计",notes = "传入项目Id和类型(例如:projectId:1,type:1) type值1:近七天 2:近15日 3:近30天 ")
    @RequestMapping(value = "/alarm/recent/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiOperationSupport(order=4)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public  String alarmRecent(@RequestBody Map<String,Object> paramMap){
        Result<Map<String,Object>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            Object typeObj = paramMap.get("type");
            if(projectObj==null||typeObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                Map<String,Object> dataMap=alarmWarnService.getAlarmRecentData(projectId,type);
                if(CollectionUtils.isEmpty(dataMap)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarm/recent/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<String,Object>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "报警类型对应的数量统计",notes = "传入项目Id(例如:projectId:1)")
    @RequestMapping(value = "/alarm/type/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiOperationSupport(order = 5)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getAlarmTypeData(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<DeviceTypeAlarmStatistic> dataList=alarmWarnService.getAlarmTypeStatistic(projectId);
                if(CollectionUtils.isEmpty(dataList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarm/type/statistic"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "未处理的告警数量",notes="传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 6)
    @RequestMapping(value = "/alarm/inHandle/info",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getInHandleInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<AlarmWarnDetailInfo>>  result=new Result<>();
        result.setCode(0);
        try {
            Object projectObj = paramMap.get("projectId");
            if(projectObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectObj.toString());
                List<AlarmWarnDetailInfo> alarmWarnDetailInfos=alarmWarnService.getInHandlerWarnInfo(projectId);
                if(CollectionUtils.isEmpty(alarmWarnDetailInfos)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(alarmWarnDetailInfos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarm/inHandle/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<AlarmWarnDetailInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @ApiOperation(value = "区域告警数量",notes="传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 7)
    @RequestMapping(value = "/alarm/areaType/info",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getAlarmAreaType(@RequestBody Map<String,Object> paramMap){
        Result<List<DeviceTypeAlarmStatistic>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<DeviceTypeAlarmStatistic> dataMap=alarmWarnService.getAreaTypeAlarm(projectId);
                if(CollectionUtils.isEmpty(dataMap)){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/safe/web/alarm/areaType/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceTypeAlarmStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
