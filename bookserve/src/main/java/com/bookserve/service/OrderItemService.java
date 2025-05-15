package com.bookserve.service;

import com.bookserve.pojo.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItem getOrderItemByOrderId(Integer orderItemId);

    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
}
