package com.bookserve.controller;

import com.bookserve.pojo.Account;
import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.User;
import com.bookserve.service.UserService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /*
     * 查询所有用户
     * */
    @GetMapping
    public Result getAllUser(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,
                             String username, String phone, Integer status )

     {
         System.out.println("分页参数 - pageNum: " + pageNum + ", pageSize: " + pageSize);
        PageResult<User> userList = userService.getAllUser(pageNum, pageSize, username, phone, status);

        return Result.success(userList);
    }

    /*
     * 添加用户
     * */
    @PostMapping
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success();
    }

    /*
     * 根据id查询用户
     * */
    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return Result.success(user);
    }

    /*
    * 修改用户
    * */
    @PutMapping
    public Result updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }

//    @PutMapping
//    public Result updateUser(@RequestBody User user) {
//        try {
//            // 1. 更新用户信息
//            user.setUpdateTime(LocalDateTime.now()); // 设置更新时间
//            int rows = userService.updateUser(user);
//            if (rows == 0) {
//                return Result.error("用户不存在");
//            }
//
//            // 2. 获取完整用户信息
//            User updatedUser = userService.getUserById(user.getId());
//
//            // 3. 生成新的JWT令牌
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("id", updatedUser.getId());
//            claims.put("username", updatedUser.getUsername());
//            claims.put("name", updatedUser.getName());
//            claims.put("avatar", updatedUser.getAvatar());
//            claims.put("email", updatedUser.getEmail());
//            claims.put("phone", updatedUser.getPhone());
//
//            String token = JwtUtils.generateToken(claims); // 你的JWT生成方法
//
//            // 4. 构造返回数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("id", updatedUser.getId());
//            data.put("username", updatedUser.getUsername());
//            data.put("name", updatedUser.getName());
//            data.put("avatar", updatedUser.getAvatar());
//            data.put("email", updatedUser.getEmail());
//            data.put("phone", updatedUser.getPhone());
//            data.put("token", token);
//
//            return Result.success(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error("修改失败");
//        }
//    }

    /*
    * 批量删除用户
    * */

    @DeleteMapping("/{ids}")
    public Result deleteUser(@PathVariable("ids") List<Integer> ids) {
        userService.deleteUser(ids);
        return Result.success();
    }

    /*
    * 根据jwt令牌的用户id查询用户
    * */
    @GetMapping("/me")
    public Result getUserByToken(@RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            User user = userService.getUserById(userId);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("令牌解析失败");
        }
    }

    /*
    * 用户修改密码
    * */
    @PutMapping("/password")
    public Result updatePassword(
            @RequestBody User requestUser,
            @RequestHeader("token") String authHeader
    ) throws Exception{
        // 解析JWT获取用户ID
        String token = authHeader.replace("token", "");
        Integer userId = JwtUtils.extractUserId(token);

        // 设置用户ID防止篡改
        requestUser.setId(userId);

        userService.updatePassword(requestUser);
        return Result.success();
    }



}
