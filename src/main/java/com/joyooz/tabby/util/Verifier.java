package com.joyooz.tabby.util;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Verifier {
    //验证用户名是否合法
    public boolean isUsernameValid(String username) {
        //第一阶段只验证是否为空
        if((username!=null)&&(!username.equals(""))) {
            return true;
        }else{
            return false;
        }
    }
}
