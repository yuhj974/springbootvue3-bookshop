package com.bookserve.controller;

import com.bookserve.anno.LogOperation;
import com.bookserve.pojo.Order;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.OrderService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/orders")
@RestController
public class OrderController {
    @Autowired
    private OrderService  orderService;

    /*
    * 管理员获取所有订单
    * */
//    @GetMapping
//    public Result getAllOrder(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize, String username, String status,
//                              String detailAddress, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime paymentTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime deliveryTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime receiveTime)
//    {
//        PageResult<Order> pageResult = orderService.getAllOrder(pageNum,pageSize,username,status,detailAddress,paymentTime,deliveryTime,receiveTime);
//        return Result.success(pageResult);
//    }
//    ==========================================
    @GetMapping
    public Result getAllOrder(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            String username,
            String status,
            String detailAddress,
            @RequestParam(required = false) String paymentTime,  // 改为String类型
            @RequestParam(required = false) String deliveryTime, // 改为String类型
            @RequestParam(required = false) String receiveTime   // 改为String类型
    ) {
        PageResult<Order> pageResult = orderService.getAllOrder(
                pageNum, pageSize,
                username, status, detailAddress,
                paymentTime, deliveryTime, receiveTime
        );
        return Result.success(pageResult);
    }
//    ============================================

    /*
    * 管理员批量删除订单
    * */
    @LogOperation("批量删除订单")
    @DeleteMapping("/{ids}")
    public Result deleteOrder(@PathVariable("ids") List<Integer> ids) {
        orderService.deleteOrder(ids);
        return Result.success();
    }

   /*
   * 根据订单id获取订单
   * */
    @GetMapping("/{id}")
    public Result getOrderById(@PathVariable("id") Integer id) {
        Order order = orderService.getOrderById(id);
        return Result.success(order);
    }

    @PutMapping("/{id}/deliver")
    public Result deliverOrder(@PathVariable("id") Integer id) {
        boolean success = orderService.deliverOrder(id);
        return success ? Result.success() : Result.error("订单不存在或状态不可变更");
    }

    /*
    * 根据jwt令牌的用户id查询用户订单
    * */
    @GetMapping("/me")
    public Result getOrderByUserId(@RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            List<Order> orders = orderService.getOrderByUserId(userId);
            return Result.success(orders);
        } catch (Exception e) {
            return Result.error("令牌解析失败"+e);
        }
    }
    /*
    * 修改订单为已完成
    * */
    @PutMapping("/{id}/receive")
    public Result receiveOrder(@PathVariable("id") Integer id) {
        boolean success = orderService.receiveOrder(id);
        return success ? Result.success() : Result.error("订单不存在或状态不可变更");
    }
    /*
    * 根据jwt令牌里的用户id再根据书本id添加用户订单
    * */
    @PostMapping("/me")
    public Result addOrder(@RequestBody Order order, @RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            // 将userId设置到订单对象中（需要Order类有userId字段）
            order.setUserId(userId);

            orderService.addOrder(order, userId); // 传递userId到Service层
            return Result.success();
        } catch (RuntimeException e) { // 捕获业务异常
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("添加订单失败：" + e.getMessage());
        }
    }
   /* 单独添加订单*/
     @PostMapping( "/add")
     public Result add(@RequestBody Order order, @RequestHeader("token") String token) {
         try {
             String token1 = token.replace("token ", "");
             Integer userId = JwtUtils.extractUserId(token1);
             // 将userId设置到订单对象中（需要Order类有userId字段）
             order.setUserId(userId);

             orderService.add(order, userId); // 传递userId到Service层
             return Result.success();
         } catch (RuntimeException e) { // 捕获业务异常
             return Result.error(e.getMessage());
         } catch (Exception e) {
             return Result.error("添加订单失败：" + e.getMessage());
         }
     }
}
