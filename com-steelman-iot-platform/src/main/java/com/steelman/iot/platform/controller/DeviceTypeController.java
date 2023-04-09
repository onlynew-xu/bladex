package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.DeviceType;
import com.steelman.iot.platform.entity.DeviceTypePicture;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.DeviceTypeService;
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

@RestController
@RequestMapping("/api/deviceType")
public class DeviceTypeController extends BaseController {
    @Resource
    private DeviceTypeService deviceTypeService;

    @PostMapping(value = "/deviceTypeList",produces = CommonUtils.MediaTypeJSON)
    public String deviceType(){
        Result<List<DeviceType>> result=new Result<>();
        result.setCode(0);
        try {
            List<DeviceType> deviceTypeList = deviceTypeService.listAll();
            if(CollectionUtils.isEmpty(deviceTypeList)){
                result=Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(deviceTypeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/deviceType/deviceTypeList"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<DeviceType>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @PostMapping(value = "/picture",produces = CommonUtils.MediaTypeJSON)
    public String deviceTypePicture(@RequestBody Map<String,Object> paramMap){
        Result<DeviceTypePicture> result=new Result<>();
        result.setCode(0);
        try {
            Object deviceTypeIdObj = paramMap.get("deviceTypeId");
            if(deviceTypeIdObj==null){
              result=  Result.paramError(result);
            }else{
                Long deviceTypeId=Long.parseLong(deviceTypeIdObj.toString());
                DeviceTypePicture deviceTypePicture=  deviceTypeService.findByDeviceTypeId(deviceTypeId);
                if(deviceTypePicture!=null){
                    result.setData(deviceTypePicture);
                    result.setCode(1);
                }else{
                    result= Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/deviceType/picture"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<DeviceTypePicture>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @RequestMapping(value = "/system/list",produces = CommonUtils.MediaTypeJSON)
    public String systemList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object systemIdObj = paramMap.get("systemId");
            Object typeObj = paramMap.get("type");
            if(projectIdObj==null||systemIdObj==null||typeObj==null){
               result= Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long systemId=Long.parseLong(systemIdObj.toString());
                Integer type=Integer.parseInt(typeObj.toString());
                List<EntityDto> dataList=deviceTypeService.getSystemList(projectId,systemId,type);
                if(CollectionUtils.isEmpty(dataList)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(dataList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/deviceType/system/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }
}
