package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;             // 图书ID（自增主键）
    private Integer categoryId;     // 所属分类ID
    private String title;           // 图书名称
    private String author;          // 图书作者
    private BigDecimal price;       // 销售价格（精确到小数点后两位）
    private Integer stock;          // 库存数量
    private String coverImage;      // 封面图片URL地址
    private String description;     // 图书详情描述
    private Integer status;         // 状态：1-上架，0-下架
    private LocalDateTime createTime;  // 图书创建时间
    private LocalDateTime updateTime;  // 图书最后修改时间
    private Integer isRecommended; // 是否为推荐书本：1-是，0-否

    private String categoryName;
}