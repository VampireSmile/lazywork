package com.sboot.rbac;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class RbacApplicationTests {

    @Resource
    Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    @Test
    void contextLoads() {
        //插入数据库表的原始数据必须是加密后的密码，否则后面验证时会报错
        String encode = pbkdf2PasswordEncoder.encode("123456");
        System.out.println(encode);
    }

}
