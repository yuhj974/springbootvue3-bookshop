package com.bookserve.service.impl;

import com.bookserve.mapper.OrderItemMapper;
import com.bookserve.pojo.OrderItem;
import com.bookserve.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderItem getOrderItemByOrderId(Integer orderItemId) {
     OrderItem  orderItem =   orderItemMapper.getOrderItemByOrderId(orderItemId);
     return orderItem;

    }

    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        return orderItemMapper.selectByOrderId(orderId);
    }
}
