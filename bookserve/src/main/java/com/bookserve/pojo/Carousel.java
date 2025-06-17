package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carousel {
    // 轮播图ID
    private Integer id;

    // 图片URL
    private String imageUrl;

    // 关联图书ID（可选）
    private Integer bookId;

    // 轮播图标题
    private String title;

    // 排序（数字越小越靠前）
    private Integer sort = 0;

    // 状态：1-启用 0-禁用
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
    // 关联图书
    private String booktitle;
}
