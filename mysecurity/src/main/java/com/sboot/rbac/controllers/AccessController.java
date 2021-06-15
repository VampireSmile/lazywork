package com.sboot.rbac.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author yinjiawei
 * @date 2021/06/15 23:03
 * @description TODO
 */
@Controller
public class AccessController {
    private static final Logger log = LoggerFactory.getLogger(AccessController.class);
    @Resource
    Pbkdf2PasswordEncoder pbkdf2PasswordEncoder;

    @RequestMapping("/my/login")
    public String showLogin() {
        log.info("showlogin");
        return "index";
    }

    @RequestMapping("/my/error")
    public String showError() {
        return "merror";
    }

    @RequestMapping("/user/home")
    public String showHome() {
        log.info("showHome");
        return "home";
    }

    /**
     * 返回加密后的密码
     *
     * @param password
     * @param model
     * @return
     */
    @GetMapping("/my/encode/{password}")
    @ResponseBody
    public String showEncodedPass(@PathVariable String password, Model model) {
        return pbkdf2PasswordEncoder.encode(password);
    }

    @RequestMapping("/user/self")
    public String showUser() {
        return "user";
    }

    @RequestMapping("/admin/self")
    public String showAdmin() {
        return "admin";
    }

}
