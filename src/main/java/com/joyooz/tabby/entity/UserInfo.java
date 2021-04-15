package com.joyooz.tabby.entity;

import lombok.Getter;
import lombok.Setter;


//pojo
@Getter
@Setter
public class UserInfo {
   //用户名
    private String username;
   //所获积分
    private int codes;
   //用户等级
    private int level;

    //无参构造函数
    public UserInfo() {};

    //含参构造函数
    public UserInfo(String username,int codes,int level) {
        this.username=username;
        this.codes=codes;
        this.level=level;
    }
}
