package com.joyooz.tabby.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    //一个由10个数字组成的字符串，每个用户互不相同，是查找用户的主关键字
    private String userId;
    //用户名可以自由修改，但是每位用户的用户名不能重复
    private String username;
    //加密存储的密码
    private String pwd;
    //表示用户获得的积分数
    private int coins;
    //表示用户类型：普通用户/管理员
    private int userType;
    //表示用户是否被禁封
    private boolean isBanned;
}
