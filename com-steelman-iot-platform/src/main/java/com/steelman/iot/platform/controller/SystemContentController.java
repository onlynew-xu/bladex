package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.SystemContent;
import com.steelman.iot.platform.service.SystemContentService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/system/content")
public class SystemContentController extends BaseController {
    @Resource
    private SystemContentService systemContentService;

    @RequestMapping(value="save" ,method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String save(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object contentObj = paramMap.get("content");
            Object markObj = paramMap.get("mark");
            Object systemIdObj = paramMap.get("systemId");
            Object projectIdObj = paramMap.get("projectId");
            if(contentObj==null||markObj==null||systemIdObj==null||projectIdObj==null){
                 result = Result.paramError(result);
            }else{
                String content=contentObj.toString();
                String mark=markObj.toString();
                Long systemId=Long.parseLong(systemIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                SystemContent systemContent=new SystemContent();
                systemContent.setContent(content);
                systemContent.setMark(mark);
                systemContent.setSystemId(systemId);
                systemContent.setProjectId(projectId);
                systemContent.setCreateTime(new Date());
                systemContent.setUpdateTime(systemContent.getCreateTime());
                systemContentService.save(systemContent);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/system/content/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value ="info",produces = CommonUtils.MediaTypeJSON,method =RequestMethod.POST )
    public String info(@RequestBody Map<String,Object> paramMap){
        Result<SystemContent> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object systemIdObj = paramMap.get("systemId");
            if(projectIdObj==null||systemIdObj==null){
                result = Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long systemId=Long.parseLong(systemIdObj.toString());
                SystemContent systemContent=systemContentService.getByProjectIdAndSystemId(projectId,systemId);
                if(systemContent!=null){
                    result.setCode(1);
                    result.setData(systemContent);
                }else{
                    Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/system/content/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<SystemContent>>(){}.getType();
        return JsonUtils.toJson(result,type);

    }

    @RequestMapping(value="update" ,method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String update(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            Object contentObj = paramMap.get("content");
            Object markObj = paramMap.get("mark");
            if(idObj==null){
                result = Result.paramError(result);
            }else{
                String content=contentObj.toString();
                String mark=markObj.toString();
                SystemContent systemContent=new SystemContent();
                systemContent.setContent(content);
                systemContent.setMark(mark);
                systemContent.setUpdateTime(new Date());
                systemContentService.updateById(systemContent);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/system/content/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


}
