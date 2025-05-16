package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Account{
    private Integer id;             // 用户ID（自增主键）
    private String username;        // 登录用户名（唯一约束）
    private String password;        // 加密后的密码
    private String role;            // 用户角色（默认USER）
    private String email;           // 电子邮箱（唯一约束）
    private String phone;           // 联系电话（加密存储）
    private String avatar;          // 头像URL地址
    private Integer status;         // 用户状态：1-正常，0-禁用
    private LocalDateTime createTime;  // 用户注册时间
    private LocalDateTime updateTime;  // 用户最后修改时间
    private String name;            // 昵称
    private String token;

    private String newPassword;
}
