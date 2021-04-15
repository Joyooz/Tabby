package com.joyooz.tabby.service;

import com.joyooz.tabby.dao.UserMapper;
import com.joyooz.tabby.entity.User;
import com.joyooz.tabby.entity.UserInfo;
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

    //根据积分数计算用户等级
    public int getLevel(int coins) {
        if(coins==0) return 0;
        if(coins<=20) return 1;
        if(coins<=50) return 2;
        if(coins<=100) return 3;
        if(coins<=160) return 4;
        if(coins<=230) return 5;
        if(coins<=350) return 6;
        if(coins<=600) return 7;
        if(coins<=1000) return 8;
        if(coins<=2000) return 9;
        if(coins<=5000) return 10;
        return 11;
    }

    //根据用户名和密码注册新用户
    public String registerUser(String username,String pwd) {
        List<User> userList=userMapper.getUserList();
        for(User user:userList) {
            if(username.equals(user.getUsername())) {
                //发现用户名已存在
                return "NO";
            }
        }
        String userId=getNewId();
        try{
            userMapper.insertUser(userId,username,pwd);
        }catch (Exception e) {
            //数据库操作失败
            return "NO";
        }
        //操作成功
        return "YES";
    }

    //登录验证
    public String loginByUsernameAndPwd(String username,String pwd) {
        List<User> userList=userMapper.getUserList();
        for(User user:userList) {
            if(user.getUsername().equals(username)) {
                if(pwd.equals(user.getPwd())) {
                    return user.getUserId();
                }
            }
        }
        return "NO";
    }

    //根据用户id获取用户对象
    public User getUserByUserId(String userId) {
        List<User> userList=userMapper.getUserList();
        for(User user:userList) {
            if(userId.equals(user.getUserId())) {
                //查询成功，返回user对象
                return user;
            }
        }
        return null;//如果无法获取则返回null
    }

    //根据用户名获取用户对象
    public User getUserByUsername(String username) {
        List<User> userList=userMapper.getUserList();
        for(User user:userList) {
            if(username.equals(user.getUsername())) {
                //查询成功，返回user对象
                return user;
            }
        }
        return null;//如果无法获取则返回null
    }

    //修改密码
    public String updatePwd(User user,String pwd,String newpwd) {
        if(pwd.equals(user.getPwd())) {
            try{
                userMapper.updatePwdByUserId(newpwd,user.getUserId());
            }catch(Exception e) {
                //数据库操作失败
                return "NO";
            }
            //操作成功
            return "YES";
        }else{
            //原密码验证不匹配
            return "NO";
        }
    }

    //修改用户名
    public String updateUsername(User user,String username) {
        //查询用户名是否重复
        List<User> userList=userMapper.getUserList();
        for(User u:userList) {
            if(u.getUsername().equals(username)) {
                //与已有用户名重复
                return "NO";
            }
        }
        //未重复
        try{
            userMapper.updateUsernameByUserId(username,user.getUserId());
        }catch (Exception e) {
            //数据库操作失败
            return "NO";
        }
        //操作成功
        return "YES";
    }

    //根据用户对象获取用户信息
    public UserInfo getUserInfo(User user) {
        UserInfo userInfo=new UserInfo(user.getUsername(),user.getCoins(),getLevel(user.getCoins()));
        return userInfo;
    }

    //管理员禁封用户
    public String banUserByUserId(User user,String userId) {
        //判断是否是管理员
        if(user.getUserType()!=1) {
            //不是管理员
            return "NO";
        }else{
            try{
                userMapper.banUserByUserId(userId);
            }catch (Exception e) {
                //数据库访问错误
                return "NO";
            }
            //操作成功
            return "YES";
        }
    }

    //管理员解封用户
    public String debanUserByUserId(User user,String userId) {
        //判断是否是管理员
        if(user.getUserType()!=1) {
            //不是管理员
            return "NO";
        }else{
            try{
                userMapper.debanUserByUserId(userId);
            }catch (Exception e) {
                //数据库访问错误
                return "NO";
            }
            //操作成功
            return "YES";
        }
    }

}
