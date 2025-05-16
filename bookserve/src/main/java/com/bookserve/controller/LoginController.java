package com.bookserve.controller;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.Admin;
import com.bookserve.pojo.User;
import com.bookserve.service.AdminService;
import com.bookserve.service.UserService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;


    /*
    * 登录接口
    * */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if ("ADMIN".equals(account.getRole())) {
           result = adminService.login(account);

        } else if ("USER".equals(account.getRole())) {
            result = userService.login(account);

        }
        if (result == null) {
            return Result.error("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", result.getId());
        claims.put("username", result.getUsername());
        claims.put("role", result.getRole());
        claims.put("name", result.getName());
        claims.put("avatar", result.getAvatar());
        String jwt = JwtUtils.generateToken(claims);
        return Result.success(jwt);
    }

    /*
    * 用户注册
    * */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }





    }


