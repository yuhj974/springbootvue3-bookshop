package com.bookserve.service;

import com.bookserve.pojo.PageResult;
import com.bookserve.pojo.Review;

import java.util.List;

public interface ReviewService {
    PageResult<Review> getAllReview(Integer pageNum, Integer pageSize, String username, String title, Integer rating);

    void deleteReview(List<Integer> ids);

    List<Review> getReviewByBookId(Integer bookId);

    void addReview(Review review);

    List<Review> getReviewByUserId(Integer userId);
}
