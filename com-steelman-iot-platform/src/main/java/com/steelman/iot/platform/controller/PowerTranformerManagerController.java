package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.PowerIncoming;
import com.steelman.iot.platform.entity.PowerTransformer;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.PowerIncomingService;
import com.steelman.iot.platform.service.PowerTransformerService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
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
 * @Author lsj
 * @DATE 2021/3/16 0016 14:22
 * @Description:
 */
@RestController
@RequestMapping("/api/power/manager/transformer")
public class PowerTranformerManagerController  extends BaseController {

    @Resource
    private PowerTransformerService powerTransformerService;
    @Resource
    private PowerIncomingService powerIncomingService;

    @Log("获取电房的变压器列表")
    @RequestMapping(value = "list",produces = CommonUtils.MediaTypeJSON)
    public String transformerList(@RequestBody Map<String,Object> paramMap){
        Result<List<PowerTransformer>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object powerIdObj = paramMap.get("powerId");
            if(projectIdObj==null||powerIdObj==null){
                result = Result.paramError(result);
            }else{
                Long powerId=Long.parseLong(powerIdObj.toString());
                List<PowerTransformer> powerTransformers = powerTransformerService.selectByPowerId(powerId);
                if(CollectionUtils.isEmpty(powerTransformers)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(powerTransformers);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<PowerTransformer>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加变压器
     * @param paramMap
     * @return
     */
    @Log("添加变压器")
    @PostMapping(value = "/save", produces = CommonUtils.MediaTypeJSON)
    public String saveTransformer(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            PowerTransformer powerTransformer=new PowerTransformer();
            String msg= checkTransformerParam(paramMap,powerTransformer);
            if(StringUtils.isNotBlank(msg)){
               result= Result.paramError(result);
               result.setMsg(msg);
            }else{
                PowerTransformer powerTransformerOld=powerTransformerService.selectByTransformNameAndPowerId(powerTransformer.getName(),powerTransformer.getPowerId());
                if(powerTransformerOld!=null){
                    result.setMsg("变压器名称已存在，请修改后重试");
                }else{
                    powerTransformer.setStatus(0); //默认关闭
                    powerTransformer.setRelationStatus(0);
                    Date date = new Date();
                    powerTransformer.setUpdateTime(date);
                    powerTransformer.setCreateTime(date);
                    powerTransformerService.insert(powerTransformer);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    private String checkTransformerParam(Map<String, Object> paramMap, PowerTransformer powerTransformer) {
        List<String> errorList=new ArrayList<>();
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null){
            errorList.add("项目id参数信息错误");
        }else{
            powerTransformer.setProjectId(Long.parseLong(projectIdObj.toString()));
        }
        Object brandObj = paramMap.get("brand");
        if(brandObj==null){
            errorList.add("品牌参数信息错误");
        }else{
            powerTransformer.setBrand(brandObj.toString());
        }
        Object capacityObj = paramMap.get("capacity");
        if(capacityObj==null){
            errorList.add("额定容量参数信息错误");
        }else{
            powerTransformer.setCapacity(capacityObj.toString());
        }
        Object nameObj = paramMap.get("name");
        if(nameObj==null){
            errorList.add("名称参数信息错误");
        }else{
            powerTransformer.setName(nameObj.toString());
        }
        Object powerIdObj = paramMap.get("powerId");
        if(powerIdObj==null){
            errorList.add("电房Id参数信息错误");
        }else{
            powerTransformer.setPowerId(Long.parseLong(powerIdObj.toString()));
        }
        if(!CollectionUtils.isEmpty(errorList)){
            String join = StringUtils.join(errorList, ",");
            return join;
        }else{
            return null;
        }
    }

    /**
     * 修改变压器属性
     * @param paramMap
     * @return
     */
    @Log("修改变压器属性")
    @PostMapping(value = "/update", produces = CommonUtils.MediaTypeJSON)
    public String updateTransformer(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object projectIdObj = paramMap.get("projectId");
            if(transformerIdObj==null||projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long  transformerId=Long.parseLong(transformerIdObj.toString());
                PowerTransformer transformer = new PowerTransformer();
                transformer.setId(transformerId);
                Object name = paramMap.get("name");
                Boolean flag=false;
                if(name!=null){
                    transformer.setName(name.toString());
                    flag=true;
                }
                Object capacityObj = paramMap.get("capacity");
                if(capacityObj!=null){
                    transformer.setCapacity(capacityObj.toString());
                    flag=true;
                }
                Object brandObj = paramMap.get("brand");
                if(brandObj!=null){
                    transformer.setBrand(brandObj.toString());
                    flag=true;
                }
                if(flag){
                    transformer.setUpdateTime(new Date());
                    powerTransformerService.update(transformer);
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改变压器状态
     * @param paramMap
     * @return
     */
    @Log("修改变压器状态")
    @PostMapping(value = "/update/status",produces = CommonUtils.MediaTypeJSON)
    public String updateTransformerStatus(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object statusObj = paramMap.get("status");
            if(transformerIdObj==null||statusObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                Integer status=Integer.parseInt(statusObj.toString());
                PowerTransformer transformer = new PowerTransformer();
                transformer.setId(transformerId);
                transformer.setStatus(status);
                transformer.setUpdateTime(new Date());
                powerTransformerService.update(transformer);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/update/status"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 删除变压器
     * @param paramMap
     * @return
     */
    @Log("删除变压器信息")
    @PostMapping(value = "/remove",produces = CommonUtils.MediaTypeJSON)
    public String deleteTransformer(@RequestBody Map<String,Object> paramMap) {
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            if(transformerIdObj==null){
                result  = Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                List<PowerIncoming> list = powerIncomingService.getIncomingList(transformerId);
                if(CollectionUtils.isEmpty(list)){
                    powerTransformerService.delete(transformerId);
                    result.setCode(1);
                    result.setData(1);
                }else{
                    result.setMsg("请先删除相关配电柜之后，再删除变压器");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @Log("获取变压器后置菜单信息")
    @PostMapping(value = "/menuInfo",produces = CommonUtils.MediaTypeJSON)
    public  String menuInfo(@RequestBody Map<String,Object> paramMap){
        Result<Map<Integer,String>> result=new Result<>();
        result.setCode(0);
        try {
            Object transformerIdObj = paramMap.get("transformerId");
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null||transformerIdObj==null){
               result= Result.paramError(result);
            }else{
                Long transformerId=Long.parseLong(transformerIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Map<Integer,String> menuInfoMap=powerTransformerService.selectCount(transformerId,projectId);
                if(CollectionUtils.isEmpty(menuInfoMap)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(menuInfoMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/power/manager/transformer/menuInfo"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Map<Integer,String>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
