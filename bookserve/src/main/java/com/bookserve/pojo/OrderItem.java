package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Integer id;             // 明细项ID（自增主键）
    private Integer orderId;        // 关联订单ID
    private Integer bookId;         // 关联图书ID
    private BigDecimal price;       // 下单时的单价快照
    private Integer quantity;       // 购买数量
    private LocalDateTime createTime;  // 明细项创建时间
    private LocalDateTime updateTime;  // 明细项最后修改时间


    private String title;
    private String orderNumber;     // 订单号（唯一业务标识）

}
