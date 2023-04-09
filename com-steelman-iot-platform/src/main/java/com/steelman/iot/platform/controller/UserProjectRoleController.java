package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.*;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.UserProjectRoleDto;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.*;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author tang
 * date 2020-11-23
 */
@RestController
@RequestMapping("/api/userProjectRole")
public class UserProjectRoleController extends BaseController {
    @Resource
    private UserProjectRoleService userProjectRoleService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private LogProjectUserService logProjectUserService;
    @Resource
    private ProjectService projectService;
    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;

    /**
     *  用户进行项目授权
     * @return
     */
    @Log("项目授权")
    @PostMapping(value = "/role",produces = CommonUtils.MediaTypeJSON)
    public String getByUserIdAndProjectId(@RequestBody Map<String,Object> paramMap){
        Result<String> result=new Result<>();
        result.setCode(0);
        try {
            Object userObj=paramMap.get("userId");
            Object projectObj=paramMap.get("projectId");
            if(userObj==null||projectObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(userObj.toString());
                Long projectId=  Long.parseLong(projectObj.toString());
                UserProjectRoleDto userProjectRoleDto = userProjectRoleService.findByUserIdAndProjectId(userId, projectId);
                if(userProjectRoleDto!=null){
                    String roleName = userProjectRoleDto.getRoleName();
                    String username  = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    LogProjectUser logProjectUser=new LogProjectUser();
                    Date date=new Date();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    logProjectUser.setLoginTime(simpleDateFormat.format(date));
                    logProjectUser.setCreateTime(date);
                    logProjectUser.setUpdateTime(date);
                    logProjectUser.setProjectId(projectId);
                    logProjectUser.setProjectName(userProjectRoleDto.getProjectName());
                    logProjectUser.setRoleId(userProjectRoleDto.getRoleId());
                    logProjectUser.setRoleName(roleName);
                    logProjectUser.setUsername(username.split("-")[0]);
                    logProjectUser.setUserId(userId);
                    logProjectUserService.saveLogProjectUser(logProjectUser);
                    List<String> authList=new ArrayList<>();
                    authList.add("ROLE_"+roleName);
                    redisTemplate.opsForValue().set(username,authList,30, TimeUnit.MINUTES);
                    result.setCode(1);
                    result.setData(roleName);
                }else{
                  result=  Result.empty(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/role"));
            result= Result.exceptionRe(result);
        }
        Type  type=new TypeToken<Result<String>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取用户的所有相关项目
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String listInfo(@RequestBody Map<String,Object> paramMap){
        Result<List<UserProjectRoleDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(idObj.toString());
                List<UserProjectRoleDto> userProjectRoleDtoList=userProjectRoleService.findDtoByUserId(userId);
                if(CollectionUtils.isEmpty(userProjectRoleDtoList)){
                  result=  Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(userProjectRoleDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/list"));
            result= Result.exceptionRe(result);

        }
        Type type=new TypeToken<Result<List<UserProjectRoleDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
    @RequestMapping(value = "/project/list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String selectProjectList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object userIdObj = paramMap.get("userId");
            if(userIdObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(userIdObj.toString());
                List<UserProjectRoleDto> userProjectRoleDtoList=userProjectRoleService.findDtoByUserId(userId);
                List<Project> projectList = projectService.findAll();
                List<EntityDto> entityDtoList=new ArrayList<>();
                Set<Long> set=new HashSet<>();
                if(!CollectionUtils.isEmpty(userProjectRoleDtoList)){
                    for (UserProjectRoleDto userProjectRoleDto : userProjectRoleDtoList) {
                        set.add(userProjectRoleDto.getProjectId());
                    }
                }
                for (Project project : projectList) {
                    if(!set.contains(project.getId())){
                        entityDtoList.add(new EntityDto(project.getId(),project.getName(),project.getId()));
                    }
                }
                result.setCode(1);
                result.setData(entityDtoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/project/list"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "/role/list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String selectRoleList(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object userIdOBj = paramMap.get("userId");
            if(projectIdObj==null||userIdOBj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long userId=Long.parseLong(userIdOBj.toString());
                List<EntityDto> entityDtoList=new ArrayList<>();
                UserProjectRole userProjectRole= userProjectRoleService.getSuperAdmin(projectId);
                List<Role> roleList = roleService.getRoleList();
                if(userProjectRole==null||userProjectRole.getUserId().equals(userId)){
                    for (Role role : roleList) {
                        entityDtoList.add(new EntityDto(role.getId(),role.getDefinition(),null));
                    }
                }else{
                    for (Role role : roleList) {
                        if(!role.getId().equals(1L)){
                            entityDtoList.add(new EntityDto(role.getId(),role.getDefinition(),null));
                        }
                    }
                }
                result.setCode(1);
                result.setData(entityDtoList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/role/list"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 用户添加项目权限
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "save",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String saveUserProjectRole(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object userIdObj = paramMap.get("userId");
            Object projectIdObj = paramMap.get("projectId");
            Object roleIdObj = paramMap.get("roleId");
            if(userIdObj==null||projectIdObj==null||roleIdObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(userIdObj.toString());
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long roleId=Long.parseLong(roleIdObj.toString());
                UserProjectRole userProjectRole=new UserProjectRole();
                userProjectRole.setProjectId(projectId);
                userProjectRole.setUserId(userId);
                userProjectRole.setRoleId(roleId);
                userProjectRole.setCreateTime(new Date());
                userProjectRole.setUpdateTime(userProjectRole.getCreateTime());
                userProjectRoleService.insertData(userProjectRole);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/save"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 用户修改项目权限
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "update",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String updateUserProjectRole(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            Object roleIdObj = paramMap.get("roleId");
            if(idObj==null||roleIdObj==null){
                result=Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                Long roleId=Long.parseLong(roleIdObj.toString());
                UserProjectRole userProjectRole=new UserProjectRole();
                userProjectRole.setId(id);
                userProjectRole.setRoleId(roleId);
                userProjectRole.setUpdateTime(new Date());
                userProjectRoleService.updateUserProjectRole(userProjectRole);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/update"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 用户删除项目权限
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "remove",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String removeUserProjectRole(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long id=Long.parseLong(idObj.toString());
                userProjectRoleService.remove(id);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/remove"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 项目用户列表
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/project/user/list",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String projectUserList(@RequestBody Map<String,Object> paramMap){
        Result<List<UserProjectRoleDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<UserProjectRoleDto> userProjectRoleDtoList=userProjectRoleService.findDtoByProjectId(projectId);
                if(CollectionUtils.isEmpty(userProjectRoleDtoList)){
                   result= Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(userProjectRoleDtoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/project/user/list"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<UserProjectRoleDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "/project/user/select",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String userSelect(@RequestBody Map<String,Object> paramMap){
        Result<List<EntityDto>> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            if(projectIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                List<UserProjectRoleDto> userProjectRoleDtoList=userProjectRoleService.findDtoByProjectId(projectId);
                List<User> userList=userService.findAll();
                Set<Long> userSet=new HashSet<>();
                List<EntityDto> entities=new ArrayList<>();
                if(!CollectionUtils.isEmpty(userProjectRoleDtoList)){
                    for (UserProjectRoleDto userProjectRoleDto : userProjectRoleDtoList) {
                        userSet.add(userProjectRoleDto.getUserId());
                    }
                }
                for (User user : userList) {
                    Long id = user.getId();
                    if(!userSet.contains(id)){
                        entities.add(new EntityDto(id,user.getUsername(),null));
                    }
                }
                if(CollectionUtils.isEmpty(entities)){
                    result=Result.empty(result);
                }else{
                    result.setCode(1);
                    result.setData(entities);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/project/user/select"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<EntityDto>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "/project/user/save",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String projectUserSave(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object projectIdObj = paramMap.get("projectId");
            Object userIdObj = paramMap.get("userId");
            Object roleIdObj = paramMap.get("roleId");
            if(projectIdObj==null||userIdObj==null||roleIdObj==null){
                result=Result.paramError(result);
            }else{
                Long projectId=Long.parseLong(projectIdObj.toString());
                Long userId=Long.parseLong(userIdObj.toString());
                Long roleId=Long.parseLong(roleIdObj.toString());
                UserProjectRole userProjectRole=new UserProjectRole();
                userProjectRole.setRoleId(roleId);
                userProjectRole.setProjectId(projectId);
                userProjectRole.setUserId(userId);
                userProjectRole.setCreateTime(new Date());
                userProjectRole.setUpdateTime(userProjectRole.getCreateTime());
                userProjectRoleService.insertData(userProjectRole);
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/userProjectRole/project/user/save"));
            result= Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


}
