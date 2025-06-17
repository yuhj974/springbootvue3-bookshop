package com.bookserve.service;

import com.bookserve.pojo.Order;
import com.bookserve.pojo.PageResult;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
   //PageResult<Order> getAllOrder(Integer pageNum, Integer pageSize, String username, String status, String detailAddress, LocalDateTime paymentTime, LocalDateTime deliveryTime, LocalDateTime receiveTime);

    PageResult<Order> getAllOrder(Integer pageNum, Integer pageSize, String username, String status, String detailAddress, String paymentTime, String deliveryTime, String receiveTime);
    void deleteOrder(List<Integer> ids);


    Order getOrderById(Integer id);

    boolean deliverOrder(Integer id);

    List<Order> getOrderByUserId(Integer userId);

    boolean receiveOrder(Integer id);

    void addOrder(Order order, Integer userId);

    void add(Order order, Integer userId);
}
