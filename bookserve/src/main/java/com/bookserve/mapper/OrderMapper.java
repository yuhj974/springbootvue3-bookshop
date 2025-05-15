package com.bookserve.mapper;

import com.bookserve.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
   // List<Order> getAllOrder(String username, String status, String detailAddress, LocalDateTime paymentTime, LocalDateTime deliveryTime, LocalDateTime receiveTime);

    List<Order> getAllOrder(
            @Param("username") String username,
            @Param("status") String status,
            @Param("detailAddress") String detailAddress,
            @Param("paymentTime") String paymentTime,
            @Param("deliveryTime") String deliveryTime,
            @Param("receiveTime") String receiveTime
    );

    void deleteOrder(List<Integer> ids);

    Order getOrderById(Integer id);

    void updateById(Order order);

    List<Order> getOrderByUserId(Integer userId);

    void addOrder(Order order);
}
