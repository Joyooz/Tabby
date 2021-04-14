package com.joyooz.tabby;

import com.joyooz.tabby.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TabbyApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void newId() {
        //测试成功，已改为private
        //String s=userService.getNewId();
        //System.out.println(s);
    }

}
