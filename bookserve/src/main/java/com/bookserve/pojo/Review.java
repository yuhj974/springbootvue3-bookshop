package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Integer id;             // 评价ID（自增主键）
    private Integer userId;         // 评价用户ID
    private Integer bookId;         // 关联图书ID
    private String content;         // 评价内容
    private Integer rating;         // 评分（1-5星）
    private LocalDateTime createTime;  // 评价时间
    private LocalDateTime updateTime;  // 评价最后修改时间

    private String username;
    private String title;
    private String name;
    private String avatar;          // 头像URL地址


}
