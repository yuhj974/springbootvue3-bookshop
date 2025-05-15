package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Integer id;             // 管理员ID（自增主键）
    private String username;        // 管理员账号（唯一约束）
    private String password;        // 加密后的密码
    private String role;            // 角色
    private String name;
    private String avatar;          // 管理员头像URL地址
    private String token;
    private String newPassword;
}
