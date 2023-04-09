package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.Role;
import com.steelman.iot.platform.service.RoleService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping(value = "/api/role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String getRoleList(){
        Result<List<Role>> result=new Result<>();
        result.setCode(0);
        try {
            List<Role> roleList = roleService.getRoleList();
            if(CollectionUtils.isEmpty(roleList)){
                result=Result.empty(result);
            }else{
                result.setCode(1);
                result.setData(roleList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/role/list"));
            Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<Role>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
