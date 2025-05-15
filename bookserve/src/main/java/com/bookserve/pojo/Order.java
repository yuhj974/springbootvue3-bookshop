package com.bookserve.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Integer id;             // 订单ID（自增主键）
    private Integer userId;         // 下单用户ID
    private String orderNumber;     // 订单号（唯一业务标识）
    private BigDecimal totalAmount; // 订单总金额
    private String status;          // 订单状态：已付款/已发货/已完成
    private Integer addressId;      // 收货地址ID

    private LocalDateTime paymentTime;   // 支付时间

    private LocalDateTime deliveryTime;  // 发货时间

    private LocalDateTime receiveTime;   // 确认收货时间
    private LocalDateTime createTime;    // 订单创建时间
    private LocalDateTime updateTime;    // 订单最后修改时间

    private String username;
    private String detailAddress;
    private String consignee;
    private String phone;
    private List<OrderItem> orderItems;
}
