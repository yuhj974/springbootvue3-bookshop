package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private Integer id;             // 地址ID（自增主键）
    private Integer userId;         // 关联用户ID
    private String consignee;       // 收货人姓名
    private String phone;           // 收货人联系电话
    private String detailAddress;   // 详细地址信息
    private Integer isDefault;      // 是否默认地址：true-是，false-否
    private LocalDateTime createTime;  // 地址创建时间
    private LocalDateTime updateTime;  // 地址最后修改时间

    private String username;
}