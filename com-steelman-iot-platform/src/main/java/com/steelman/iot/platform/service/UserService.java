package com.steelman.iot.platform.service;

import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.entity.dto.EntityDto;
import com.steelman.iot.platform.entity.dto.UserDto;
import com.steelman.iot.platform.entity.dto.UserSimpleInfo;
import com.steelman.iot.platform.largescreen.vo.ProjectSimInfo;

import java.util.List;
import java.util.Map;

/**
 * @author tang
 * date 2020-11-23
 */
public interface UserService {

    User findByUsername(String username);

    Integer updatePassword(String username,String password);

    User findByUserToken(String token);
    User findById(Long id);
    User findByUsernamePwd(String username,String PassWord);
    void updateUser(User user);

    /**
     * 获取用户的所有项目信息
     * @param userId
     * @return
     */
    List<ProjectSimInfo> getProjectList(Long userId);

    /**
     * 获取用户头像
     * @param userId
     * @return
     */
    String getImgByUserId(Long userId);

    /**
     * 更新用户密码
     * @param userId
     * @param password
     * @return
     */
    Integer updatePasswordByUserId(Long userId, String password);

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    UserSimpleInfo getUserSimpleInfo(Long userId);

    /**
     * 获取分给巡检任务的用户
     * @param projectId
     * @return
     */
    List<EntityDto> getUserProject( Long projectId);

    /**
     * 获取所有用户
     * @return
     */
    List<User> findAll();

    /**
     * 保存用户
     * @param paramMap
     * @return
     */
    String saveUser (Map<String, Object> paramMap) throws Exception;

    /**
     * 更新用户信息
     * @param userDto
     * @return
     */
    Boolean updateDto(UserDto userDto);

    /**
     * 初始化密码
     * @param userId
     * @return
     */
    Boolean initializePassword(Long userId);

    /**
     * 删除用户
     * @param userId
     * @return
     */
    Boolean removeUser(Long userId);
}
