package com.bookserve.service.impl;

import com.bookserve.exception.BusinessException;
import com.bookserve.mapper.*;
import com.bookserve.pojo.*;
import com.bookserve.service.OrderItemService;
import com.bookserve.service.OrderService;
import com.bookserve.utils.DateTimeUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderItemMapper  orderItemMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private CartMapper cartMapper;

//    @Override
//    public PageResult<Order> getAllOrder(Integer pageNum, Integer pageSize, String username, String status, String detailAddress, LocalDateTime paymentTime, LocalDateTime deliveryTime, LocalDateTime receiveTime) {
//
//        PageHelper.startPage(pageNum,pageSize);
//         List<Order> orderResult =  orderMapper.getAllOrder(username, status, detailAddress, paymentTime, deliveryTime, receiveTime);
//        Page<Order> page = (Page<Order>) orderResult;
//        return new PageResult<>(page.getTotal(),page.getResult());
//
//
//    }

    @Override
    public PageResult<Order> getAllOrder(Integer pageNum, Integer pageSize, String username,
                                         String status, String detailAddress,
                                         String paymentTime, String deliveryTime,
                                         String receiveTime) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderResult = orderMapper.getAllOrder(
                username, status, detailAddress,
                paymentTime, deliveryTime, receiveTime
        );
        Page<Order> page = (Page<Order>) orderResult;
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOrder(List<Integer> ids) {
        orderMapper.deleteOrder(ids);
        //根据订单id删除该订单明细
        orderItemMapper.deleteOrderItemByOrderId(ids);


    }

    @Override
    public Order getOrderById(Integer id) {
      Order order =  orderMapper.getOrderById(id);
      return order;

    }

    @Override
    public boolean deliverOrder(Integer id) {
        Order order = orderMapper.getOrderById(id);

        // 验证订单存在且处于可发货状态（例如：待发货）
        if (order != null || "已付款".equals(order.getStatus())) {
            order.setStatus("已发货");
            order.setDeliveryTime(LocalDateTime.now());
            orderMapper.updateById(order);
            return true;
        }
        return false;
    }

    @Override
    public List<Order> getOrderByUserId(Integer userId) {
        return orderMapper.getOrderByUserId(userId);

    }

    @Override
    public boolean receiveOrder(Integer id) {
        Order order = orderMapper.getOrderById(id);
        if (order != null && "已发货".equals(order.getStatus())) {
            order.setStatus("已完成");
            order.setReceiveTime(LocalDateTime.now());
            orderMapper.updateById(order);
            return true;
        }
        return false;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrder(Order order, Integer userId) {
        // 原有的地址处理逻辑
        if (order.getAddressId() == null) {
            Address defaultAddress = addressMapper.findDefaultByUserId(order.getUserId());
            if (defaultAddress == null) {
                throw new BusinessException("请选择地址或设置默认地址");
            }
            order.setAddressId(defaultAddress.getId());
        } else {
            Address address = addressMapper.findByIdAndUserId(order.getAddressId(), order.getUserId());
            if (address == null) {
                throw new BusinessException("地址无效或不存在");
            }
        }
        // 新增：价格校验
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("订单金额计算异常");
        }

        // 订单号生成
        String orderNumber = "ORDER" + System.currentTimeMillis() + (int)(Math.random() * 9000 + 1000);
        order.setOrderNumber(orderNumber);

        order.setStatus("已付款");
        // 设置总金额（如果前端已经计算）
        order.setTotalAmount(order.getTotalAmount());
        order.setPaymentTime(LocalDateTime.now());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 保存主订单（需要确保MyBatis配置了useGeneratedKeys返回主键）
        orderMapper.addOrder(order);
        System.out.println("生成的主订单ID: " + order.getId()); // 确保非空
        //根据订单id生成订单明细
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException("订单明细不能为空");
        }
        for (OrderItem orderItem : orderItems) {
            // 校验必填字段
            if (orderItem.getBookId() == null) {
                throw new BusinessException("书籍ID不能为空");
            }
            if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
                throw new BusinessException("数量必须大于0");
            }

            // 查询书籍并设置价格
            Book book = bookMapper.selectById(orderItem.getBookId());
            if (book == null) {
                throw new BusinessException("图书不存在: " + orderItem.getBookId());
            }
            orderItem.setPrice(book.getPrice());
            orderItem.setOrderId(order.getId());
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setUpdateTime(LocalDateTime.now());

            // 插入明细
            try {
                orderItemMapper.insert(orderItem);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            bookMapper.updateStock(orderItem.getBookId(), orderItem.getQuantity());
            System.out.println("生成的订单明细ID: " + orderItem.getId());

            try {
                cartMapper.deleteByUserIdAndBookId(userId, orderItem.getBookId());
                System.out.println("已删除用户"+userId+"的购物车中书籍ID："+orderItem.getBookId());
            } catch (Exception e) {
                // 可根据业务需求决定是否要阻断流程
                System.err.println("购物车删除失败："+e.getMessage());
            }

        }


    }

    @Override
    public void add(Order order, Integer userId) {
        // 原有的地址处理逻辑
        if (order.getAddressId() == null) {
            Address defaultAddress = addressMapper.findDefaultByUserId(order.getUserId());
            if (defaultAddress == null) {
                throw new BusinessException("请选择地址或设置默认地址");
            }
            order.setAddressId(defaultAddress.getId());
        } else {
            Address address = addressMapper.findByIdAndUserId(order.getAddressId(), order.getUserId());
            if (address == null) {
                throw new BusinessException("地址无效或不存在");
            }
        }
        // 新增：价格校验
        if (order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("订单金额计算异常");
        }

        // 订单号生成
        String orderNumber = "ORDER" + System.currentTimeMillis() + (int)(Math.random() * 9000 + 1000);
        order.setOrderNumber(orderNumber);

        order.setStatus("已付款");
        // 设置总金额（如果前端已经计算）
        order.setTotalAmount(order.getTotalAmount());
        order.setPaymentTime(LocalDateTime.now());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 保存主订单（需要确保MyBatis配置了useGeneratedKeys返回主键）
        orderMapper.addOrder(order);
        System.out.println("生成的主订单ID: " + order.getId()); // 确保非空
        //根据订单id生成订单明细
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException("订单明细不能为空");
        }
        for (OrderItem orderItem : orderItems) {
            // 校验必填字段
            if (orderItem.getBookId() == null) {
                throw new BusinessException("书籍ID不能为空");
            }
            if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
                throw new BusinessException("数量必须大于0");
            }

            // 查询书籍并设置价格
            Book book = bookMapper.selectById(orderItem.getBookId());
            if (book == null) {
                throw new BusinessException("图书不存在: " + orderItem.getBookId());
            }
            orderItem.setPrice(book.getPrice());
            orderItem.setOrderId(order.getId());
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setUpdateTime(LocalDateTime.now());

            // 插入明细
            try {
                orderItemMapper.insert(orderItem);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            bookMapper.updateStock(orderItem.getBookId(), orderItem.getQuantity());
            System.out.println("生成的订单明细ID: " + orderItem.getId());



        }
    }


}
