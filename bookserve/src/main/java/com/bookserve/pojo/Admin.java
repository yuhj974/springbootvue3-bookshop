package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends Account{
    private Integer id;             // 管理员ID（自增主键）
    private String username;        // 管理员账号（唯一约束）
    private String password;        // 加密后的密码
    private String role;            // 角色（默认ADMIN）
    private LocalDateTime createTime;  // 账号创建时间
    private LocalDateTime updateTime;  // 账号最后修改时间
    private String avatar;          // 管理员头像URL地址
    private String name;            // 管理员昵称
}
