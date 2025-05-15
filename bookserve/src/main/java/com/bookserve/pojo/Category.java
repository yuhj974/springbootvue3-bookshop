package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;             // 分类ID（自增主键）
    private String name;            // 分类名称（唯一约束）
    private LocalDateTime createTime;  // 分类创建时间
    private LocalDateTime updateTime;  // 分类最后修改时间


}
