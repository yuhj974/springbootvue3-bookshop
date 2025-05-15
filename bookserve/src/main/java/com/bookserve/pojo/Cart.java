package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Integer id;             // 购物车项ID（自增主键）
    private Integer userId;         // 关联用户ID
    private Integer bookId;         // 关联图书ID
    private Integer quantity;       // 购买数量（默认1）
    private LocalDateTime createTime;  // 购物车项创建时间
    private LocalDateTime updateTime;  // 购物车项最后修改时间

    private String username;
    private String title;
    private String author;
    private String coverImage;
    private BigDecimal totalPrice;
}
