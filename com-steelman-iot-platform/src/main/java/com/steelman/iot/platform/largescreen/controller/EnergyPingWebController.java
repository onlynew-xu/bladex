package com.steelman.iot.platform.largescreen.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.controller.BaseController;
import com.steelman.iot.platform.entity.vo.EnergyConsumeStatistic;
import com.steelman.iot.platform.entity.vo.EnergyTotalSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.EnergyTotal;
import com.steelman.iot.platform.largescreen.vo.EquipmentStatistic;
import com.steelman.iot.platform.service.EnergyPingWebService;
import com.steelman.iot.platform.utils.CommonUtils;
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
@RequestMapping("/api/energy/ping/web")
@Api(tags = "能源管理大屏接口",value = "能源管理数据大屏")
@ApiSupport(order = 3)
public class EnergyPingWebController extends BaseController {

    @Resource
    private EnergyPingWebService energyPingWebService;

    @ApiOperation(value = "设备总数及在线率",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 1)
    @RequestMapping(value = "/equipment/status/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String equipmentStatus(@RequestBody Map<String,Object> paramMap){
        Result<EquipmentStatistic> result=new Result<>();
        result.setCode(0);
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            result=Result.paramError(result);
        }else{
            Long projectId=Long.parseLong(projectIdObj.toString());
            EquipmentStatistic equipmentStatistic=energyPingWebService.getEquipmentStatusStatistic(projectId);
            if(equipmentStatistic!=null){
                result.setCode(1);
                result.setData(equipmentStatistic);
            }else{
               result= Result.empty(result);
            }
        }
        Type type=new TypeToken<Result<EquipmentStatistic>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "消耗总量统计",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 2)
    @RequestMapping(value = "/equipment/total/energy",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getTotalEnergy(@RequestBody Map<String,Object> paramMap){
        Result<EnergyTotal> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                EnergyTotal energyTotal=energyPingWebService.getTotalEnergy(projectId);
                if(energyTotal==null){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(energyTotal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Type type=new TypeToken<Result<EnergyTotal>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "设备类型统计",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 3)
    @RequestMapping(value = "/equipment/type/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getEquipmentConsumeType(@RequestBody Map<String,Object> paramMap){
        Result<List<EnergyConsumeStatistic>> result=new Result<>();
        result.setCode(0);
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            result=Result.paramError(result);
        }else{
            Long projectId=Long.parseLong(projectIdObj.toString());
            List<EnergyConsumeStatistic> data=energyPingWebService.getEquipmentConsumeType(projectId);
            if(CollectionUtils.isEmpty(data)){
                result=Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(data);
            }
        }
        Type type=new TypeToken<Result<List<EnergyConsumeStatistic>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    @ApiOperation(value = "能耗排行榜",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 4)
    @RequestMapping(value = "/equipment/measure/statistic",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getMeasureRank(@RequestBody Map<String,Object> paramMap){
        Result<List<EnergyTotalSimpleInfo>> result=new Result<>();
        result.setCode(0);
        Object projectIdObj = paramMap.get("projectId");
        try {
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectIdObj.toString());
                List<EnergyTotalSimpleInfo>  dataList= energyPingWebService.getMeasureRank(projectId);
                if(CollectionUtils.isEmpty(dataList)){
                    result= Result.empty(result);
                }else{
                    result.setData(dataList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            result=Result.exceptionRe(result);
            e.printStackTrace();
        }
        Type type=new TypeToken<Result<List<EnergyTotalSimpleInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @ApiOperation(value = "能耗告警信息",notes = "传入项目Id(例如:projectId:1)")
    @ApiOperationSupport(order = 5)
    @RequestMapping(value = "/equipment/energy/warn",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    @ApiImplicitParam(name = "token",value = "token令牌",required=true,paramType="header")
    public String getEnergyWarn(@RequestBody Map<String,Object> paramMap){
        return null;
    }


}
