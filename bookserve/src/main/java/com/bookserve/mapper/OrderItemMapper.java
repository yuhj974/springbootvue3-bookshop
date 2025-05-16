package com.bookserve.mapper;

import com.bookserve.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderItemMapper {

    OrderItem getOrderItemByOrderId(Integer orderItemId);

    List<OrderItem> selectByOrderId(Integer orderId);

    void deleteOrderItemByOrderId(List<Integer> ids);

    // 插入单个订单项
    void insert(OrderItem item);

    void batchInsert(List<OrderItem> items);
}
