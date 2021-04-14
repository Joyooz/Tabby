package com.joyooz.tabby.service;

import com.joyooz.tabby.dao.UserMapper;
import com.joyooz.tabby.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Random;

@Configuration
public class UserService {
    //注入容器中的UserMapper对象
    @Autowired
    UserMapper userMapper;

    //内部调用函数，用于生成新的随机userId
    private String getNewId() {
        String s="";
        boolean repeated;
        Random r=new Random();
        List<User> userList=userMapper.getUserList();
        do{
            repeated=false;
            //生成十位随机字符串
            for(int i=0;i<10;i++) {
                s+=(char)('0'+r.nextInt(10));
            }
            //验证是否重复
            for(User user:userList) {
                if(s.equals(user.getUserId())) {
                    repeated=true;
                }
            }
        }while(repeated);
        return s;
    }


}
