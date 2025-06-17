package com.bookserve.controller;

import com.bookserve.pojo.Admin;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.AdminService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admins")
@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    /*
    * 查询所有管理员
    * */

    @GetMapping
    public Result getAllAdmin(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize,
                              String username) {
        PageResult<Admin> pageResult = adminService.getAllAdmin(pageNum, pageSize, username);
        return Result.success(pageResult);
    }
    /*
    * 根据id查询管理员
    * */
    @GetMapping("/{id}")
    public Result getAdminById(@PathVariable Integer id) {
        Admin admin = adminService.getAdminById(id);
        return Result.success(admin);
    }
    /*
    * 修改管理员
    * */
    @PutMapping
    public Result updateAdmin(@RequestBody Admin admin) {
        adminService.updateAdmin(admin);
        return Result.success();
    }
    /*
    * 添加管理员
    * */
    @PostMapping
    public Result addAdmin(@RequestBody Admin admin) {
        adminService.addAdmin(admin);
        return Result.success();
    }
    /*
    * 删除管理员
    * */
    @DeleteMapping("/{id}")
    public Result deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return Result.success();
    }

    /*
    * 根据jwt令牌的id查询管理员
    * */
    @GetMapping("/me")
    public Result getAdminByToken(@RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer adminId = JwtUtils.extractUserId(token1);
            Admin admin = adminService.getAdminById(adminId);
            return Result.success(admin);
        } catch (Exception e) {
            return Result.error("令牌解析失败");
        }
    }

}
