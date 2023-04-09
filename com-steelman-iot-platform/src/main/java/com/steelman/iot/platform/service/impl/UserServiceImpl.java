package com.steelman.iot.platform.service.impl;

import com.steelman.iot.platform.dao.UserDao;
import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.UserDto;
import com.steelman.iot.platform.entity.dto.UserSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;
import com.steelman.iot.platform.service.ProjectService;
import com.steelman.iot.platform.service.UserProjectRoleService;
import com.steelman.iot.platform.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
@Service("userService")
public class UserServiceImpl extends BaseService implements UserService {
    @Resource
    private UserDao userDao;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ProjectService projectService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private UserProjectRoleService userProjectRoleService;
    @Override
    public User findByUsername(String username) {
        return userDao.selectByUsername(username);
    }

    @Override
    public Integer updatePassword(String username,String password) {
        return userDao.updatePasswordByUsername(username,passwordEncoder.encode(password));
    }

    public User findByUsernamePwd(String username,String passWord) {
        return userDao.selectByUsernamePwd(username,passWord);
    }
    public User findById(Long id) {
        return userDao.selectByPrimaryKey(id);
    }
    public User findByUserToken(String token) {
        return userDao.findByUserToken(token);
    }
    public void updateUser(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<ProjectSimInfo> getProjectList(Long userId) {
        return projectService.getProjectSimInfoByUserId(userId);
    }

    @Override
    public String getImgByUserId(Long userId) {
        return userDao.getImgByUserId(userId);
    }

    @Override
    public Integer updatePasswordByUserId(Long userId, String password) {
        return userDao.updatePasswordByUserId(userId,password);
    }

    @Override
    public UserSimpleInfo getUserSimpleInfo(Long userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if(user!=null){
            UserSimpleInfo simpleInfo=new UserSimpleInfo();
            simpleInfo.setUserId(user.getId());
            simpleInfo.setUsername(user.getUsername());
            simpleInfo.setName(user.getName());
            simpleInfo.setImg(user.getPictureUrl());
            return simpleInfo;
        }
        return null;
    }

    @Override
    public List<EntityDto> getUserProject( Long projectId) {
        return userDao.getUserProjectTask(projectId);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveUser(Map<String, Object> paramMap)throws Exception {
        User user=new User();
        String msg=checkParam(paramMap,user);
        if(StringUtils.isNotBlank(msg)){
            return msg;
        }else{
            user.setCreateTime(new Date());
            user.setUpdateTime(user.getCreateTime());
            user.setStatus(1);
            userDao.insertSelective(user);
        }
        return null;
    }

    private String checkParam(Map<String, Object> paramMap, User user) throws Exception {
        List<String> msgList=new ArrayList<>();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Object username = paramMap.get("username");
        Object name = paramMap.get("name");
        Object password = paramMap.get("password");
        Object email = paramMap.get("email");
        Object mobile = paramMap.get("mobile");
        Object admin = paramMap.get("admin");
        Object creatorId = paramMap.get("creatorId");
        Object sex = paramMap.get("sex");
        Object birth = paramMap.get("birth");
        Object picture = paramMap.get("picture");
        Object pictureUrl = paramMap.get("pictureUrl");
        Object address = paramMap.get("address");
        Object hobby = paramMap.get("hobby");
        Object province = paramMap.get("province");
        Object city = paramMap.get("city");
        Object district = paramMap.get("district");
        Object dept = paramMap.get("dept");
        if(username==null|| StringUtils.isBlank(username.toString())){
            msgList.add("缺少登录名");
        }else{
            user.setUsername(username.toString());
        }
        if(name==null|| StringUtils.isBlank(name.toString())){
            msgList.add("缺少用户名");
        }else{
            User userOld=userDao.findByUserName(name.toString());
            if(userOld!=null){
                msgList.add("用户名已存在");
            }else{
                user.setName(name.toString());
            }
        }
        if(password==null|| StringUtils.isBlank(password.toString())){
            msgList.add("缺少密码");
        }else{
            String encode = bCryptPasswordEncoder.encode(password.toString());
            user.setPassword(encode);
        }
        if(email==null){
            msgList.add("邮箱信息错误");
        }else{
            user.setEmail(email.toString());
        }
        if(mobile==null){
            msgList.add("手机信息错误");
        }else{
            user.setMobile(mobile.toString());
        }
        if(admin==null){
            msgList.add("管理员需要选择");
        }else{
            user.setAdmin(Integer.parseInt(admin.toString()));
        }
        if(creatorId==null){
            msgList.add("创建者id 错误");
        }else{
            user.setCreatorId(Long.parseLong(creatorId.toString()));
        }
        if(sex==null){
            msgList.add("性别信息错误");
        }else{
            user.setSex(Integer.parseInt(sex.toString()));
        }
        if(birth==null){
            msgList.add("生日信息错误");
        }else{
            if(StringUtils.isNotBlank(birth.toString())){
                Date  format = simpleDateFormat.parse(birth.toString());
                user.setBirth(format);
            }else{
                user.setBirth(null);
            }
        }
        if(picture==null){
            msgList.add("头像信息错误");
        }else{
            if(StringUtils.isNotBlank(picture.toString())){
                user.setPicture(picture.toString());
            }else{
                //没传头像信息 使用默认信息
                user.setPicture("leo.jpeg");
            }
        }
        if(pictureUrl==null){
            msgList.add("头像地址信息错误");
        }else{
            if(StringUtils.isNotBlank(pictureUrl.toString())){
                user.setPictureUrl(pictureUrl.toString());
            }else{
                //没传头像地址信息 使用默认信息
                user.setPictureUrl("http://www.steelman-iot.com/images/user/leo.jpeg");
            }
        }
        if(address==null){
            msgList.add("地址信息错误");
        }else{
            if(StringUtils.isNotBlank(address.toString())){
                user.setAddress(address.toString());
            }else{
                //没传地址信息 使用空
                user.setAddress(null);
            }
        }
        if(hobby==null){
            msgList.add("爱好信息错误");
        }else{
            if(StringUtils.isNotBlank(hobby.toString())){
                user.setHobby(hobby.toString());
            }else{
                //没传爱好信息 使用空
                user.setHobby(null);
            }
        }
        if(province==null){
            msgList.add("省信息错误");
        }else{
            if(StringUtils.isNotBlank(province.toString())){
                user.setProvince(province.toString());
            }else{
                //没传省信息 使用空
                user.setProvince(null);
            }
        }
        if(city==null){
            msgList.add("市信息错误");
        }else{
            if(StringUtils.isNotBlank(city.toString())){
                user.setCity(city.toString());
            }else{
                //没传市信息 使用空
                user.setCity(null);
            }
        }
        if(district==null){
            msgList.add("区信息错误");
        }else{
            if(StringUtils.isNotBlank(district.toString())){
                user.setDistrict(district.toString());
            }else{
                //没传区信息 使用空
                user.setCity(null);
            }
        }
        if(dept==null){
            msgList.add("部门信息错误");
        }else{
            if(StringUtils.isNotBlank(dept.toString())){
                user.setDistrict(dept.toString());
            }else{
                //没传部门信息 使用空
                user.setDept(null);
            }
        }
        if(CollectionUtils.isEmpty(msgList)){
            return null;
        }else{
            return StringUtils.join(msgList,",");
        }
    }

    @Override
    public Boolean updateDto(UserDto userDto) {
        if(userDto!=null){
            User user=new User();
            user.setId(userDto.getId());
            Boolean flag=false;
            String username = userDto.getUsername();
            if(StringUtils.isNotBlank(username)){
                flag=true;
                user.setUsername(username);
            }
            String name = userDto.getName();
            if(StringUtils.isNotBlank(name)){
                flag=true;
                user.setName(name);
            }
            String email = userDto.getEmail();
            if(StringUtils.isNotBlank(email)){
                flag=true;
                user.setEmail(email);
            }
            String mobile = userDto.getMobile();
            if(StringUtils.isNotBlank(mobile)){
                flag=true;
                user.setMobile(mobile);
            }
            Integer status = userDto.getStatus();
            if(status!=null){
                flag=true;
                user.setStatus(status);
            }
            Integer admin = userDto.getAdmin();
            if(admin!=null){
                flag=true;
                user.setAdmin(admin);
            }
            Integer sex = userDto.getSex();
            if(sex!=null){
                flag=true;
                user.setSex(sex);
            }
            Date birth = userDto.getBirth();
            if(birth!=null){
                flag=true;
                user.setBirth(birth);
            }
            String picture = userDto.getPicture();
            if(picture!=null){
                flag=true;
                user.setPicture(picture);
            }

            String pictureUrl = userDto.getPictureUrl();
            if(pictureUrl!=null){
                flag=true;
                user.setPictureUrl(pictureUrl);
            }
            String address = userDto.getAddress();
            if(address!=null){
                flag=true;
                user.setAddress(address);
            }
            String hobby = userDto.getHobby();
            if(hobby!=null){
                flag=true;
                user.setHobby(hobby);
            }
            String province = userDto.getProvince();
            if(province!=null){
                flag=true;
                user.setProvince(province);
            }
            String city = userDto.getCity();
            if(city!=null){
                flag=true;
                user.setCity(city);
            }
            String district = userDto.getDistrict();
            if(district!=null){
                flag=true;
                user.setDistrict(district);
            }
            String dept = userDto.getDept();
            if(dept!=null){
                flag=true;
                user.setDept(dept);
            }
            if(flag){
                user.setUpdateTime(new Date());
                userDao.updateByPrimaryKeySelective(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean initializePassword(Long userId) {
        User user=new User();
        user.setId(userId);
        user.setPassword(bCryptPasswordEncoder.encode("123456"));
        user.setUpdateTime(new Date());
        userDao.initializePassword(user);
        return true;
    }

    @Override
    public Boolean removeUser(Long userId) {
        userDao.deleteByPrimaryKey(userId);
        return true;
    }
}
