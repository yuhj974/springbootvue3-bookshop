package com.bookserve.controller;

import com.bookserve.pojo.OrderItem;
import com.bookserve.service.OrderItemService;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/orderItems")
@RestController
public class OrderItemController {


    @Autowired
    private OrderItemService orderItemService;
    /*
    * 根据id获取订单项
    * */
    @GetMapping("/{ItemId}")
    public Result getOrderItemByOrderId(@PathVariable("ItemId") Integer orderItemId){
   OrderItem orderItem = orderItemService.getOrderItemByOrderId(orderItemId);
   return Result.success(orderItem);
    }

    /*
    * 根据关联订单id获取订单项
    * */
    @GetMapping("/order/{id}")
    public Result getOrderItemsByOrderId(@PathVariable("id") Integer orderId) {
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
        return Result.success(orderItems);
    }

}
