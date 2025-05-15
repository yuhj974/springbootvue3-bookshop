package com.bookserve.service.impl;

import com.bookserve.exception.BusinessException;
import com.bookserve.mapper.OrderItemMapper;
import com.bookserve.mapper.OrderMapper;
import com.bookserve.mapper.ReviewMapper;
import com.bookserve.pojo.Order;
import com.bookserve.pojo.OrderItem;
import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.Review;
import com.bookserve.service.ReviewService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public PageResult<Review> getAllReview(Integer pageNum, Integer pageSize, String username, String title, Integer rating) {
        PageHelper.startPage(pageNum,pageSize);
        List<Review> reviewList = reviewMapper.getAllReview(pageNum, pageSize, username, title, rating);
        Page<Review> page = (Page<Review>) reviewList;
        PageResult<Review> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;

    }

    @Override
    public void deleteReview(List<Integer> ids) {
        reviewMapper.deleteReview(ids);
    }

    @Override
    public List<Review> getReviewByBookId(Integer bookId) {

        return reviewMapper.getReviewByBookId(bookId);
    }

    @Override
    public void addReview(Review review) {
        // 防御性校验核心关联字段
        if (review.getUserId() == null) {
            throw new IllegalArgumentException("用户标识缺失，请重新登录");
        }
        if (review.getBookId() == null) {
            throw new IllegalArgumentException("必须指定书籍编号");
        }

        List<Order> orders = orderMapper.getOrderByUserId(review.getUserId());
        if (orders.isEmpty()) {
            throw new BusinessException("请先购买图书再进行评价！");
        }



        boolean hasPurchasedBook = false;
        for (Order order : orders) {
            // 只有订单状态为"已完成"的订单才允许评价
            if ("已完成".equals(order.getStatus())) {
                List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
                if (orderItems != null && !orderItems.isEmpty()) {
                    hasPurchasedBook = orderItems.stream()
                            .anyMatch(item -> item.getBookId().equals(review.getBookId()));
                    if (hasPurchasedBook) {
                        break;
                    }
                }
            }
        }

        if (!hasPurchasedBook) {
            throw new BusinessException("您尚未购买此图书或订单未完成，无法进行评价！");
        }
        // 设置时间戳（防止前端伪造）
        LocalDateTime now = LocalDateTime.now();
        review.setCreateTime(now);
        review.setUpdateTime(now);

        // 执行持久化操作（示例使用MyBatis，实际根据你的ORM框架调整）
        try {
            reviewMapper.insert(review);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("禁止重复提交评论", e);
        } catch (DataAccessException e) {
            throw new RuntimeException("评论保存失败，请稍后重试", e);
        }
    }

    @Override
    public List<Review> getReviewByUserId(Integer userId) {
        return reviewMapper.getReviewByUserId(userId);

    }
}
