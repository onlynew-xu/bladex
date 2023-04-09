package com.steelman.iot.platform.dao;

import com.steelman.iot.platform.entity.User;
import com.steelman.iot.platform.entity.dto.EntityDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User findByUserToken(@Param("token") String token);

    User selectByUsername(@Param("username") String username);

    User selectByUsernamePwd(@Param("username") String username,@Param("passWord") String passWord);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 修改密码
     * @param username
     * @param password
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username,@Param("password") String password);

    /**
     * 获取用户的头像
     * @param userId
     * @return
     */
    String getImgByUserId(@Param("userId") Long userId);

    /**
     * 更新用户密码
     * @param userId
     * @param password
     * @return
     */
    int updatePasswordByUserId(@Param("userId") Long userId, @Param("password") String password);

    List<EntityDto> getUserProjectTask(@Param("projectId") Long projectId);

    List<User> findAll();

    /**
     * 初始化密码
     * @param user
     * @return
     */
    int initializePassword(User user);

    User findByUserName(@Param("name") String name);
}