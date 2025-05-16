package com.bookserve.controller;

import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.Review;
import com.bookserve.service.ReviewService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/reviews")
@RestController
@Slf4j
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    /*
    * 管理员查询所有评论
    * */
    @GetMapping
    public Result getAllReview(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,String username, String title,Integer rating) {
     PageResult<Review> pageResult = reviewService.getAllReview(pageNum,pageSize,username,title,rating);
        return Result.success(pageResult);

    }

    /*
    * 管理员批量删除评论
    * */
    @DeleteMapping("/{ids}")
    public Result deleteReview(@PathVariable("ids") List<Integer> ids) {
        reviewService.deleteReview(ids);
        return Result.success();
    }

    /*
    * 根据书本id查询评论
    * */
    @GetMapping("/book/{bookId}")
    public Result getReviewByBookId(@PathVariable("bookId") Integer bookId) {
        List<Review> reviews = reviewService.getReviewByBookId(bookId);
        return Result.success(reviews);
    }

    /*
    * 添加评论
    * */
    @PostMapping("/books/{bookId}")
    public Result addReview(
            @PathVariable Integer bookId,
            @RequestBody Review review,
            @RequestHeader("token") String authHeader
    ) {
        try {

            log.info("收到评论请求：bookId={}, review={}", bookId, review);
            // 1. 解析JWT获取用户ID
            String token = authHeader.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token);


            // 2. 强制设置关联字段
            review.setUserId(userId);   // 覆盖前端可能传递的值
            review.setBookId(bookId);    // 使用路径参数
            review.setCreateTime(LocalDateTime.now());
            review.setUpdateTime(LocalDateTime.now());

            // 3. 保存评论
            reviewService.addReview(review);
            return Result.success();
        } catch (Exception e) {
            log.error("评论保存失败", e); // 添加详细错误日志
            return Result.error("评论添加失败: " + e.getMessage());
        }
    }

    /*
    * 根据jwt令牌的用户id查询用户评论
    * */
    @GetMapping("/me")
    public Result getReviewByUserId(@RequestHeader("token") String token) {
        try {
            String token1 = token.replace("token ", "");
            Integer userId = JwtUtils.extractUserId(token1);
            List<Review> reviews = reviewService.getReviewByUserId(userId);
            return Result.success(reviews);
        } catch (Exception e) {
            return Result.error("令牌解析失败");
        }
    }
}
