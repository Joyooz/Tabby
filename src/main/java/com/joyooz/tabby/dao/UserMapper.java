package com.joyooz.tabby.dao;

import com.joyooz.tabby.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface UserMapper {
    //获取用户列表
    List<User> getUserList();
    //根据id,用户名和密码新建用户
    void insertUser(@PathVariable("userId")String userId,@PathVariable("username")String username,@PathVariable("pwd")String pwd);
    //根据id修改用户密码
    void updatePwdByUserId(@PathVariable("pwd")String pwd,@PathVariable("userId")String userId);
    //根据id修改用户名
    void updateUsernameByUserId(@PathVariable("username")String username,@PathVariable("userId")String userId);
    //根据id修改用户积分数
    void updateCoinsByUserId(@PathVariable("coins")String coins,@PathVariable("userId")String userId);
    //根据id禁封用户
    void banUserByUserId(@PathVariable("userId")String userId);
    //根据id解封用户
    void debanUserByUserId(@PathVariable("userId")String userId);
}
