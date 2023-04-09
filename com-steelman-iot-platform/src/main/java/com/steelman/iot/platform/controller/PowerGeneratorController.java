package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.PowerGenerator;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.PowerGeneratorService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/power/generator/manager")
public class PowerGeneratorController extends BaseController {
    @Resource
    private PowerGeneratorService powerGeneratorService;
    @Resource
    private PowerTransformerService powerTransformerService;

    /**
     * 发电机列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/list", produces = CommonUtils.MediaTypeJSON)
    public String getGeneratorList(@RequestBody Map<String, Object> paramMap) {
        Result<List<PowerGenerator>> result = new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if (transformerIdObj == null) {
                result = Result.paramError(result);
            } else {
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                List<PowerGenerator> list = powerGeneratorService.getGeneratorList(transformerId);
                if (CollectionUtils.isEmpty(list)) {
                    result = Result.empty(result);
                } else {
                    result.setCode(1);
                    result.setData(list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/list"));
            result=Result.exceptionRe(result);
        }
        Type type = new TypeToken<Result<List<PowerGenerator>>>() {
        }.getType();
        return JsonUtils.toJson(result, type);
    }

    /**
     * 添加变压器
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String saveGenerator(@RequestBody Map<String, Object> paramMap) {
        Result<Integer> result = new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object nameObj = paramMap.get("name");
            Object brandObj = paramMap.get("brand");
            Object powerGenerationObj = paramMap.get("powerGeneration");
            if (transformerIdObj == null || nameObj == null || brandObj == null || powerGenerationObj == null) {
                result = Result.paramError(result);
            } else {
                Long transformerId = Long.parseLong(transformerIdObj.toString());
                String name = nameObj.toString();
                String brand = brandObj.toString();
                String powerGeneration = powerGenerationObj.toString();
                PowerGenerator generatorOld=powerGeneratorService.getByTransformerANDName(transformerId,name);
                if(generatorOld!=null){
                    result.setMsg("名称已存在，请修改后重试");
                }else{
                    PowerTransformer transformer = powerTransformerService.getTransformerInfo(transformerId);
                    PowerGenerator generator = new PowerGenerator();
                    generator.setBrand(brand);
                    generator.setName(name);
                    generator.setProjectId(transformer.getProjectId());
                    generator.setPowerGeneration(powerGeneration);
                    generator.setPowerId(transformer.getPowerId());
                    generator.setStatus(0);
                    generator.setTransformerId(transformerId);
                    generator.setCreateTime(new Date());
                    generator.setUpdateTime(generator.getCreateTime());
                    powerGeneratorService.insert(generator);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改发电机返回信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update/info", produces = CommonUtils.MediaTypeJSON)
    public String updatePowerGeneratorInfo(@RequestBody Map<String,Object> paramMap) {
        Result<PowerGenerator> result=new Result<>();
        result.setCode(0);
        try {
            Object generatorIdObj = paramMap.get("generatorId");
            if(generatorIdObj==null){
                result=Result.paramError(result);
            }else{
                Long generatorId=  Long.parseLong(generatorIdObj.toString());
                PowerGenerator powerGenerator = powerGeneratorService.selectById(generatorId);
                if(powerGenerator==null){
                    result = Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerGenerator);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<PowerGenerator>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改发电机属性信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String updatePowerGenerator(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object generatorIdObj = paramMap.get("id");
            Object transformerIdObj = paramMap.get("transformerId");
            if(generatorIdObj==null||transformerIdObj==null){
               result= Result.paramError(result);
            }else{
                Boolean flag=false;
                Boolean nameFlag=false;
                Object nameObj = paramMap.get("name");
                Object brandObj = paramMap.get("brand");
                Object powerGenerationObj = paramMap.get("powerGeneration");
                PowerGenerator generator = new PowerGenerator();
                generator.setId(Long.parseLong(generatorIdObj.toString()));
                if(nameObj!=null){
                    generator.setName(nameObj.toString());
                    nameFlag=true;
                    flag=true;
                }
                if(nameFlag){
                    PowerGenerator byTransformerANDName = powerGeneratorService.getByTransformerANDName(Long.parseLong(transformerIdObj.toString()), nameObj.toString());
                    if(byTransformerANDName!=null){
                        result.setMsg("名称信息已存在,请修改后重试");
                        Type type=new TypeToken<Result<Integer>>(){}.getType();
                        return JsonUtils.toJson(result,type);
                    }
                }
                if(brandObj!=null){
                    generator.setBrand(brandObj.toString());
                    flag=true;
                }
                if(powerGenerationObj!=null){
                    generator.setPowerGeneration(powerGenerationObj.toString());
                    flag=true;
                }
                if(flag){
                    generator.setUpdateTime(new Date());
                    powerGeneratorService.update(generator);
                    result.setCode(1);
                    result.setData(1);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    /**
     * 修改发电机的状态
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/update/status", produces = CommonUtils.MediaTypeJSON)
    public String updateGeneratorStatus(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object generatorIdObj = paramMap.get("id");
            Object statusObj = paramMap.get("status");
            if(generatorIdObj==null||statusObj==null){
               result= Result.paramError(result);
            }else{
                PowerGenerator generator = new PowerGenerator();
                generator.setId(Long.parseLong(generatorIdObj.toString()));
                generator.setStatus(Integer.parseInt(statusObj.toString()));
                generator.setUpdateTime(new Date());
                powerGeneratorService.update(generator);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/update/status"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除发电机
     * @param paramMap
     * @return
     */
    @Log("删除发电机")
    @PostMapping(value = "/remove", produces = CommonUtils.MediaTypeJSON)
    public String removeGenerator(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object generatorIdObj = paramMap.get("generatorId");
            if(generatorIdObj==null){
                result= Result.paramError(result);
            }else{
                Long generatorId=Long.parseLong(generatorIdObj.toString());
                powerGeneratorService.removeById(generatorId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/generator/manager/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


}
