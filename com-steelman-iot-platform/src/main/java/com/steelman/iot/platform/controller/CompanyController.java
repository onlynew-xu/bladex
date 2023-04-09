package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Company;
import com.steelman.iot.platform.entity.CompanyType;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.service.CompanyService;
import com.steelman.iot.platform.service.CompanyTypeService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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

@RestController
@RequestMapping("/api/company")
public class CompanyController extends BaseController {
    @Resource
    private CompanyService companyService;
    @Resource
    private CompanyTypeService companyTypeService;

    /**
     * 公司类型列表信息
     * @return
     */
    @PostMapping(value = "/companyType/list",produces = CommonUtils.MediaTypeJSON)
    public String companyTypeList(){
        Result<List<EntityDto>> result=new Result<>();
        try {
            List<CompanyType> dataList= companyTypeService.findAll();
            if(!CollectionUtils.isEmpty(dataList)){
                List<EntityDto> entityDtoList=new ArrayList<>();
                for (CompanyType companyType : dataList) {
                    entityDtoList.add(new EntityDto(companyType.getId(),companyType.getName(),null));
                }
                result.setCode(1);
                result.setData(entityDtoList);
            }else{
               result= Result.empty(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/companyType/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 公司列表信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/company/info/list",produces = CommonUtils.MediaTypeJSON)
    public String companyListInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<Company>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object companyTypeIdObj = paramMap.get("companyTypeId");
            if(projectIdObj==null||companyTypeIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long companyTypeId=Long.parseLong(companyTypeIdObj.toString());
                List<Company> companyList= companyService.findByProjectId(projectId,Integer.parseInt(companyTypeId.toString()));
                if(CollectionUtils.isEmpty(companyList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(companyList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/info/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<Company>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改公司信息 详情信息
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/company/detail",produces = CommonUtils.MediaTypeJSON)
    public String companyDetail(@RequestBody Map<String,Object> paramMap){
        Result<Company> result=new Result<>();
        result.setCode(0);
        try {
            Object companyIdObj = paramMap.get("companyId");
            if(companyIdObj==null){
                result=Result.paramError(result);
            }else{
                Long companyId=Long.parseLong(companyIdObj.toString());
                Company company= companyService.findByid(companyId);
                if(company==null){
                    result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(company);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/detail"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Company>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 修改公司信息
     * @param paraMap
     * @return
     */
    @PostMapping(value = "/company/update",produces = CommonUtils.MediaTypeJSON)
    public String companyUpdate(@RequestBody Map<String,Object> paraMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object companyIdObj = paraMap.get("companyId");
            Object projectIdObj = paraMap.get("projectId");
            if(companyIdObj==null||projectIdObj==null){
               result= Result.paramError(result);
            }else{
                Long companyId = Long.parseLong(companyIdObj.toString());
                Long projectId = Long.parseLong(projectIdObj.toString());
                Company company=new Company();
                company.setId(companyId);
                company.setProjectId(projectId);
                Object companyNameObj = paraMap.get("companyName");
                if(companyNameObj!=null&& !StringUtils.isEmpty(companyNameObj.toString())){
                    String companyName=companyNameObj.toString();
                    Company companyOld=companyService.findByName(projectId,companyName);
                    if(companyOld!=null){
                        result.setData(0);
                        result.setMsg("名称信息已存在");
                        Type type=new  TypeToken<Result<Integer>>(){}.getType();
                        return JsonUtils.toJson(result,type);
                    }else{
                        company.setName(companyName);
                    }
                }
                Object telObj = paraMap.get("tel");
                if(telObj!=null&& !StringUtils.isEmpty(telObj.toString())){
                    company.setTel(telObj.toString());
                }
                Object addressObj = paraMap.get("address");
                if(addressObj!=null && !StringUtils.isEmpty(addressObj.toString())){
                    company.setAddress(addressObj.toString());
                }
                Object leaderObj = paraMap.get("leader");
                if(leaderObj!=null && !StringUtils.isEmpty(leaderObj.toString())){
                    company.setLeader(leaderObj.toString());
                }
                Object companyTypeIdObj = paraMap.get("companyTypeId");
                if(companyTypeIdObj!=null && !StringUtils.isEmpty(companyTypeIdObj.toString())){
                    company.setType(Long.parseLong(companyTypeIdObj.toString()));
                }
                company.setUpdateTime(new Date());
                companyService.update(company);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new  TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加公司
     * @param paramMap
     * @return
     */
    @PostMapping(value = "/company/save",produces = CommonUtils.MediaTypeJSON)
    public String saveCompany(@RequestBody Map<String,Object> paramMap){
         Result<Integer> result=new Result<>();
         result.setCode(0);
        try {
            Company company=new Company();
            String paramError=checkParam(paramMap,company);
            if(!StringUtils.isEmpty(paramError)){
               result= Result.paramError(result);
               result.setMsg(paramError);
            }else{
                companyService.insert(company);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
         return JsonUtils.toJson(result,type);
    }

    /**
     * 删除公司
     * @param pramMap
     * @return
     */
    @PostMapping(value = "/company/delete",produces = CommonUtils.MediaTypeJSON)
    public String companyDelete(@RequestBody Map<String,Object> pramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object companyIdObj = pramMap.get("companyId");
            Object projectIdObj = pramMap.get("projectId");
            if(companyIdObj==null||projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long companyId = Long.parseLong(companyIdObj.toString());
                companyService.removeById(companyId);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/delete"));
            result=Result.exceptionRe(result);
        }
        Type type=new  TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 公司列表 用于下拉框
     * @param paraMap
     * @return
     */
    @PostMapping(value = "/company/list",produces = CommonUtils.MediaTypeJSON)
    public String companyList(@RequestBody Map<String,Object> paraMap){
        Result<List<EntityDto>> result =new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paraMap.get("projectId");
            Object typeObj = paraMap.get("type");
            if(projectIdObj==null||typeObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId = Long.parseLong(projectIdObj.toString());
                int type = Integer.parseInt(typeObj.toString());
                List<Company> companyList= companyService.findByProjectId(projectId,type);
                if(CollectionUtils.isEmpty(companyList)){
                   result= Result.empty(result);
                }else{
                    List<EntityDto> dtoList=new ArrayList<>();
                    for (Company company : companyList) {
                        dtoList.add(new EntityDto(company.getId(),company.getName(),company.getProjectId()));
                    }
                    result.setData(dtoList);
                    result.setCode(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/company/company/list"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 检验公司的参数信息
     * @param paramMap
     * @param company
     * @return
     */
    private String checkParam(Map<String,Object> paramMap,Company company) {
        List<String> errorList= new ArrayList<>();

        Object companyNameObj = paramMap.get("companyName");
        if(companyNameObj==null||StringUtils.isEmpty(companyNameObj.toString())){
            errorList.add("公司名称信息错误");
        }else{
            company.setName(companyNameObj.toString());
        }
        Object addressObj = paramMap.get("address");
        if(addressObj==null||StringUtils.isEmpty(addressObj.toString())){
            errorList.add("公司地址信息错误");
        }else{
            company.setAddress(addressObj.toString());
        }
        Object leaderObj = paramMap.get("leader");
        if(leaderObj==null||StringUtils.isEmpty(leaderObj.toString())){
            errorList.add("公司负责人信息错误");
        }else{
            company.setLeader(leaderObj.toString());
        }
        Object telObj = paramMap.get("tel");
        if(telObj==null||StringUtils.isEmpty(telObj.toString())){
            errorList.add("公司电话信息错误");
        }else{
            company.setTel(telObj.toString());
        }
        Object companyTypeObj = paramMap.get("companyTypeId");
        if(companyTypeObj==null||StringUtils.isEmpty(companyTypeObj.toString())){
            errorList.add("公司类型信息错误");
        }else{
            company.setType(Long.parseLong(companyTypeObj.toString()));
        }
        Object projectIdObj = paramMap.get("projectId");
        if(projectIdObj==null||StringUtils.isEmpty(projectIdObj.toString())){
            errorList.add("项目Id信息错误");
        }else{
            company.setProjectId(Long.parseLong(projectIdObj.toString()));
        }
        if(CollectionUtils.isEmpty(errorList)){
            Date date=new Date();
            company.setCreateTime(date);
            company.setUpdateTime(date);
            return  null;
        }else{
            String join = org.apache.commons.lang3.StringUtils.join(errorList, ",");
            return join;
        }


    }
}
