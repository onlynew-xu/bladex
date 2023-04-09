package com.steelman.iot.platform.controller;

import com.google.gson.reflect.TypeToken;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.entity.UserProjectRole;
import com.steelman.iot.platform.entity.dto.UserDto;
import com.steelman.iot.platform.entity.dto.UserSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import com.steelman.iot.platform.log.Log;
import com.steelman.iot.platform.service.UserProjectRoleService;
import com.steelman.iot.platform.service.UserService;
import com.steelman.iot.platform.utils.CommonUtils;
import com.steelman.iot.platform.utils.ExceptionUtils;
import com.steelman.iot.platform.utils.JsonUtils;
import com.steelman.iot.platform.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author tang
 * date 2020-12-01
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private RedisTemplate redisTemplate;
    @Value("${fileUpload}")
    private String fileLoad;
    @Value("${iot.image.url}")
    private String imgUrl;
    @Resource
    private UserProjectRoleService userProjectRoleService;

    /**
     * 修改密码
     * @return
     */
    @Log("修改密码")
    @PostMapping(value ="updatePassword",produces = CommonUtils.MediaTypeJSON)
    public String updatePassword(@RequestBody Map<String,Object> paramMap){
        Result<String> result=new Result<>();
        result.setCode(0);
        try {
            Object userObj = paramMap.get("userId");
            Object oldPasswordObj = paramMap.get("oldPassword");
            Object newPasswordObj = paramMap.get("newPassword");
            Object reNewPasswordObj = paramMap.get("reNewPassword");
            if(userObj==null||oldPasswordObj==null||newPasswordObj==null||reNewPasswordObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(userObj.toString());
                User user = userService.findById(userId);
                if(user==null){
                    result.setMsg("用户不存在！");
                }else{

                    String oldPassword=oldPasswordObj.toString();
                    String newPassword=newPasswordObj.toString();
                    String reNewPassword=reNewPasswordObj.toString();
                    boolean matches = bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
                    if(!matches){
                        result.setMsg("旧密码错误！");
                    }else{
                        if(!newPassword.equals(reNewPassword)){
                            result.setMsg("两次输入的密码不一致！");
                        }else{
                            if(StringUtils.isBlank(newPassword)||StringUtils.isBlank(reNewPassword)){
                                result.setMsg("新密码不能为空！");
                            }else{
                                if(oldPasswordObj.equals(newPassword)){
                                    result.setMsg("新旧密码一致,请重新输入");
                                }else{
                                    String password = bCryptPasswordEncoder.encode(newPassword);
                                    Integer count= userService.updatePasswordByUserId(userId,password);
                                    if(count>0){
                                        result.setMsg("success");
                                        result.setCode(1);
                                    }else{
                                        result.setMsg("失败！ 请练习管理员");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/updatePassword"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<String>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 获取用户所有的项目
     * @param paramData
     * @return
     */
    @Log("获取用户所有的项目")
    @PostMapping(value ="projectList",produces = CommonUtils.MediaTypeJSON)
    public String getProjectByUserId(@RequestBody Map<String,Object> paramData){
        Object userObj = paramData.get("userId");
        Result<List<ProjectSimInfo>> result=new Result<>();
        result.setCode(0);
        try {
            if(userObj==null){
                result.setMsg("param error");
            }else{
                Long userId=Long.parseLong(userObj.toString());
                List<ProjectSimInfo> data=userService.getProjectList(userId);
                if(CollectionUtils.isEmpty(data)){
                    result.setMsg("no data");
                }else{
                    result.setCode(1);
                    result.setData(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/projectList"));
            result.setMsg("exception");
        }
        Type type=new TypeToken<Result<List<ProjectSimInfo>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "userSimpleInfo",produces = CommonUtils.MediaTypeJSON)
    public String getUserSimpleInfo(@RequestBody Map<String,Object> paramData){
        Result<UserSimpleInfo> result=new Result<>();
        result.setCode(0);
        try {
            Object userObj = paramData.get("userId");
            if(userObj!=null){
                Long userId=Long.parseLong(userObj.toString());
                UserSimpleInfo userSimpleInfo=userService.getUserSimpleInfo(userId);
                if(userSimpleInfo!=null){
                    result.setData(userSimpleInfo);
                    result.setCode(1);
                }else{
                    result= Result.empty(result);
                }
            }else{
                result= Result.paramError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/userSimpleInfo"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<UserSimpleInfo>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }

    @RequestMapping(value = "authority",produces = CommonUtils.MediaTypeJSON)
    public String getUserAuthority(@RequestBody Map<String,Object> paramData){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object userObj = paramData.get("userId");
            if(userObj!=null){
                Long userId=Long.parseLong(userObj.toString());
                User user=userService.findById(userId);
                if(user!=null){
                    Integer admin = user.getAdmin();
                    if(admin.equals(2)){
                        result.setData(1);
                        result.setCode(1);
                        List<String> authList=new ArrayList<>();
                        authList.add("ROLE_authority");
                        redisTemplate.opsForValue().set(user.getUsername(),authList,30, TimeUnit.MINUTES);
                    }else{
                        result.setCode(1);
                        result.setData(0);
                        result.setMsg("权限不足");
                    }
                }else{
                    result= Result.empty(result);
                }
            }else{
                result= Result.paramError(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/authority"));
            result=Result.exceptionRe(result);

        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return  JsonUtils.toJson(result,type);
    }

    /**
     * 获取所有用户
     * @return
     */
    @RequestMapping(value = "all",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String allUser(){
        Result<List<UserDto>> result=new Result<>();
        result.setCode(1);
        try {
            List<User> userList=userService.findAll();
            List<UserDto> userDtoList=new ArrayList<>();
            for (User user : userList) {
                userDtoList.add(new UserDto(user.getId(),user.getUsername(),user.getName(),user.getEmail(),user.getMobile(),user.getStatus(),user.getAdmin(),user.getSex(),user
                .getBirth(),user.getPicture(),user.getPictureUrl(),user.getAddress(),user.getHobby(),user.getProvince(),user.getCity(),user.getDistrict(),
                        user.getDept(),user.getCreateTime(),user.getUpdateTime()));
            }
            result.setCode(1);
            result.setData(userDtoList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/all"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<List<User>>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 头像上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/picture/upload",method = RequestMethod.POST,produces = CommonUtils.MediaTypeJSON)
    public String uploadPicture(@RequestParam("file") MultipartFile file){
        Result<Map<String,String>> result=new Result<>();
        result.setCode(0);
        try {
            String originalFilename = file.getOriginalFilename();
            //获取文件名后缀
            int index = originalFilename.lastIndexOf(".");
            String substring = originalFilename.substring(index);
            String s = String.valueOf(System.currentTimeMillis());
            Random random=new Random();
            int i = random.nextInt(1000);
            String fileChild=s+String.valueOf(i)+substring;
            String canonicalPath=null;
            if(fileLoad.equals("dev")){
                //本地测试环境
                File picFile=new File("");
                canonicalPath = picFile.getCanonicalPath()+"/images/user/";
            }else{
                //先上环境
                canonicalPath="/steelman/nginx/images/user/";
            }
            //拼接文件全名
            File fileName= new File(canonicalPath,fileChild);
            if (!fileName.getParentFile().exists()) {
                //创建多层文件夹
                fileName.getParentFile().mkdirs();
            }
            file.transferTo(fileName);
            Map<String,String> dataMap=new LinkedHashMap<>();
            dataMap.put("picture",fileChild);
            dataMap.put("pictureUrl",imgUrl+"/user/"+fileChild);
            result.setData(dataMap);
            result.setCode(1);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/picture/upload"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 添加用户
     * @param paramMap
     * @return
     */
    @RequestMapping(value ="save",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String save(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            String msg=userService.saveUser(paramMap);
            if(StringUtils.isNotBlank(msg)){
                result.setMsg(msg);
            }else{
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/save"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken< Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 更新用户返回信息
     * @param paramMap
     * @return
     */
    @RequestMapping(value ="/update/info",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String updateInfo(@RequestBody Map<String,Object> paramMap){
        Result<UserDto> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(idObj.toString());
                User user = userService.findById(userId);
                UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getMobile(), user.getStatus(), user.getAdmin(), user.getSex(), user
                        .getBirth(),user.getPicture(), user.getPictureUrl(), user.getAddress(), user.getHobby(), user.getProvince(), user.getCity(), user.getDistrict(),
                        user.getDept(), user.getCreateTime(), user.getUpdateTime());
                result.setCode(1);
                result.setData(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/update/info"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<UserDto>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 更新用户信息
     * @param userDto
     * @return
     */
    @RequestMapping(value ="/update",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String updateUser(@RequestBody UserDto userDto){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Boolean flag= userService.updateDto(userDto);
            if(flag){
                result.setCode(1);
                result.setData(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/update"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }

    /**
     * 初始化密码为123456
     * @param paramMap
     * @return
     */
    @RequestMapping(value ="/initialize/password",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String updateUser(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
                result=Result.paramError(result);
            }else{
                Long userId=Long.parseLong(idObj.toString());
                Boolean flag= userService.initializePassword(userId);
                if(flag){
                    result.setCode(1);
                    result.setData(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/initialize/password"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }


    @RequestMapping(value ="/remove",produces = CommonUtils.MediaTypeJSON,method = RequestMethod.POST)
    public String removeUser(@RequestBody Map<String,Object> paramMap){
        Result<Integer> result=new Result<>();
        result.setCode(0);
        try {
            Object idObj = paramMap.get("id");
            if(idObj==null){
               result= Result.paramError(result);
            }else{
                Long userId=Long.parseLong(idObj.toString());
                List<UserProjectRole> userProjectRoleList = userProjectRoleService.findByUserId(userId);
                if(CollectionUtils.isEmpty(userProjectRoleList)){
                    Boolean flag= userService.removeUser(userId);
                    if(flag){
                      result.setCode(1);
                      result.setData(1);
                    }
                }else{
                    result.setMsg("请先删除关联的项目信息");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackMsg(e,"/api/user/remove"));
            result=Result.exceptionRe(result);
        }
        Type type=new TypeToken<Result<Integer>>(){}.getType();
        return JsonUtils.toJson(result,type);
    }
}
