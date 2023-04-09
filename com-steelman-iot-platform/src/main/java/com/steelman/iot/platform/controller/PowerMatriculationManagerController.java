package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.PowerMatriculation;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.entity.dto.PowerEntityDto;
import com.steelman.iot.platform.entity.dto.PowerMatriculationDto;
import com.steelman.iot.platform.service.PowerMatriculationService;
import com.steelman.iot.platform.service.PowerTransformerService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 母联柜管理接口
 */
@RestController
@RequestMapping("/api/power/matriculation/manager")
public class PowerMatriculationManagerController extends BaseController {
    @Resource
    private PowerMatriculationService powerMatriculationService;
    @Resource
    private PowerTransformerService powerTransformerService;


    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String getMatriculationList(@RequestBody Map<String,Object> paramMap) {
        Result<List<PowerMatriculationDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                List<PowerMatriculationDto> list = powerMatriculationService.getAllMatriculation(transformerId);
                if(CollectionUtils.isEmpty(list)){
                   result= Result.paramError(result);
                }else{
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerMatriculationDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取未被母联柜关联的变压器
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/transformer/list", produces = CommonUtils.MediaTypeJSON)
    public String getTransformerIdList(@RequestBody Map<String,Object> paramMap) {
        Result<List<PowerEntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object powerIdObj = paramMap.get("powerId");
            Object transformerIdObj = paramMap.get("transformerId");
            if(powerIdObj==null|| transformerIdObj==null){
                result= Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                List<PowerTransformer> list = powerTransformerService.getUnBandingTransformer(powerId);
                if(CollectionUtils.isEmpty(list)){
                    result= Result.paramError(result);
                }else{
                    List<PowerEntityDto> dataList=new ArrayList<>();
                    for (PowerTransformer powerTransformer : list) {
                        if(!powerTransformer.getId().equals(transformerId)){
                            dataList.add(new PowerEntityDto(powerTransformer.getId(),powerTransformer.getName(),null,powerTransformer.getId(),powerTransformer.getPowerId(),powerTransformer.getProjectId()));
                        }
                    }
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/transformer/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerEntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String saveMatriculation(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object brandObj = paramMap.get("brand");
            Object nameObj = paramMap.get("name");
            Object secondTransformerIdObj = paramMap.get("secondTransformerId");
            Object statusObj = paramMap.get("status");
            if(transformerIdObj==null||brandObj==null||nameObj==null||secondTransformerIdObj==null||statusObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                String brand = brandObj.toString();
                String name = nameObj.toString();
                Long secondTransformerId = Long.parseLong(secondTransformerIdObj.toString());
                Integer  status = Integer.parseInt(statusObj.toString());
                PowerTransformer transformerInfo = powerTransformerService.getTransformerInfo(transformerId);
                if(transformerInfo.getRelationStatus().equals(1)){
                    result.setMsg("变压器已绑定母联柜");
                    Type type=new TypeToken<Result<Integer>>(){}.getType();
                    return JsonUtils.toJson(result,type);
                }
                PowerMatriculation matriculationOld=powerMatriculationService.selectByPowerAndName(transformerInfo.getPowerId(),name);
                if(matriculationOld!=null){
                    result.setMsg("名称已存在，请稍后重试");
                }else{
                    List<PowerMatriculation> powerMatriculationList=powerMatriculationService.findByTransformerId(transformerId,secondTransformerId);
                    if(!CollectionUtils.isEmpty(powerMatriculationList)){
                        result.setMsg("变压器已绑定其他母联柜，请修改后重试");
                    }else{
                        PowerMatriculation matriculation = new PowerMatriculation();
                        matriculation.setBrand(brand);
                        matriculation.setName(name);
                        matriculation.setPowerId(transformerInfo.getPowerId());
                        matriculation.setProjectId(transformerInfo.getProjectId());
                        matriculation.setSecondTransformerId(secondTransformerId);
                        matriculation.setStatus(status);
                        matriculation.setFirstTransformerId(transformerId);
                        matriculation.setCreateTime(new Date());
                        matriculation.setUpdateTime(matriculation.getCreateTime());
                        powerMatriculationService.insert(matriculation);
                        powerTransformerService.updateRelationStatus(transformerId,1);
                        powerTransformerService.updateRelationStatus(secondTransformerId,1);
                    }
                    result.setCode(1);
                    result.setData(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String update(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object matriculationIdObj = paramMap.get("id");
            if(matriculationIdObj==null){
                result= Result.paramError(result);
            }else{
                Long matriculationId = Long.parseLong(matriculationIdObj.toString());
                Object brandObj = paramMap.get("brand");
                Object nameObj = paramMap.get("name");
                Boolean flag=false;
                Boolean nameFlag=false;
                PowerMatriculation powerMatriculation=new PowerMatriculation();
                if(nameObj!=null){
                    flag= true;
                    nameFlag=true;
                    powerMatriculation.setName(nameObj.toString());
                }
                if(nameFlag){
                    PowerMatriculation powerMatriculationOld=powerMatriculationService.findById(matriculationId);
                    PowerMatriculation powerMatriculation1 = powerMatriculationService.selectByPowerAndName(powerMatriculationOld.getPowerId(), nameFlag.toString());
                    if(powerMatriculation1!=null){
                        result.setMsg("名称已存在，请修改后重试");
                        Type type=new TypeToken<Result<Integer>>(){}.getType();
                        return JsonUtils.toJson(result,type);
                    }
                }
                if(brandObj!=null){
                    flag= true;
                    powerMatriculation.setBrand(brandObj.toString());
                }
                if(flag){
                    powerMatriculation.setId(matriculationId);
                    powerMatriculationService.update(powerMatriculation);
                    result.setCode(1);
                    result.setData(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/update/status", produces = CommonUtils.MediaTypeJSON)
    public String updateStatus(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object matriculationIdObj = paramMap.get("id");
            Object statusObj = paramMap.get("status");
            if(matriculationIdObj==null||statusObj==null){
                result= Result.paramError(result);
            }else{
                Long matriculationId = Long.parseLong(matriculationIdObj.toString());
                Integer  status = Integer.parseInt(statusObj.toString());
                PowerMatriculation powerMatriculation=new PowerMatriculation();
                powerMatriculation.setId(matriculationId);
                powerMatriculation.setStatus(status);
                powerMatriculationService.update(powerMatriculation);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/update/status"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String removeMatriculation(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object matriculationIdObj = paramMap.get("id");
            if(matriculationIdObj==null){
                result= Result.paramError(result);
            }else{
                Long matriculationId = Long.parseLong(matriculationIdObj.toString());
                PowerMatriculation powerMatriculation = powerMatriculationService.findById(matriculationId);
                powerTransformerService.updateRelationStatus(powerMatriculation.getFirstTransformerId(),0);
                powerTransformerService.updateRelationStatus(powerMatriculation.getSecondTransformerId(),0);
                powerMatriculationService.removeById(matriculationId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/matriculation/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }
}
