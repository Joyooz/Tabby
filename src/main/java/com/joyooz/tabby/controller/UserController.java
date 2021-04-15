package com.joyooz.tabby.controller;

import com.joyooz.tabby.entity.User;
import com.joyooz.tabby.entity.UserInfo;
import com.joyooz.tabby.service.UserService;
import com.joyooz.tabby.util.Verifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    //注入userService
    @Autowired
    UserService userService;

    //注入验证工具verifier
    @Autowired
    Verifier verifier;

    //注册新用户业务
    @PostMapping("/register")
    public String register(@RequestParam("username")String username, @RequestParam("pwd")String pwd) {
        //验证用户名合法性
        if(!verifier.isUsernameValid(username)) {
            //不合法直接返回"NO"
            return "NO";
        }else{
            return userService.registerUser(username,pwd);
        }
    }

    //登录业务
    @PostMapping("/login")
    public String login(@RequestParam("username")String username, @RequestParam("pwd")String pwd, HttpSession session) {
        //验证用户名合法性
        if(!verifier.isUsernameValid(username)) {
            //不合法
            return "NO";
        }else{
            //合法，调用userService查看是否匹配
            String userId=userService.loginByUsernameAndPwd(username,pwd);
            if(userId.equals("NO")) {
                //登录失败
                return "NO";
            }else{
                //用户名密码验证成功
                try{
                    User user=userService.getUserByUserId(userId);
                    session.setAttribute("user",user);
                }catch (Exception e) {
                    //登录失败
                    return "NO";
                }
                //登录成功
                return "YES";
            }
        }
    }

    //退出登录
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "YES";
    }

    //修改用户名
    @PostMapping("/updateUsername")
    public String updateUsername(@RequestParam("username")String username,HttpSession session) {
        //验证用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }else{
            return userService.updateUsername((User)session.getAttribute("user"),username);
        }
    }

    //修改密码
    @PostMapping("/updatePwd")
    public String updatePwd(@RequestParam("pwd")String pwd,@RequestParam("newpwd")String newpwd,HttpSession session) {
        //验证用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }else{
            return userService.updatePwd((User)session.getAttribute("user"),pwd,newpwd);
        }
    }

    //获取用户信息GET
    @GetMapping("/getUserInfo")
    public UserInfo getUserInfo(HttpSession session) {
        //验证用户是否登录
        if(session.getAttribute("user")==null) {
            return null;
        }else{
            return userService.getUserInfo((User)session.getAttribute("user"));
        }
    }

    //管理员禁封用户
    @PostMapping("/banUser")
    public String banUser(@RequestParam("userId")String userId,HttpSession session) {
        //验证用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }else{
            return userService.banUserByUserId((User)session.getAttribute("user"),userId);
        }
    }

    //管理员解封用户
    @PostMapping("/debanUser")
    public String debanUser(@RequestParam("userId")String userId,HttpSession session) {
        //验证用户是否登录
        if(session.getAttribute("user")==null) {
            return "NO";
        }else{
            return userService.banUserByUserId((User)session.getAttribute("user"),userId);
        }
    }

}