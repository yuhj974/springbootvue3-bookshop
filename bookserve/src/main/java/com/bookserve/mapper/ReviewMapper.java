package com.bookserve.mapper;

import com.bookserve.pojo.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    List<Review> getAllReview(Integer pageNum, Integer pageSize, String username, String title, Integer rating);

    void deleteReview(List<Integer> ids);

    List<Review> getReviewByBookId(Integer bookId);

    void insert(Review review);

    List<Review> getReviewByUserId(Integer userId);
}
