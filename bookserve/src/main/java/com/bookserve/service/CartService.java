package com.bookserve.service;

import com.bookserve.pojo.Cart;
import com.bookserve.pojo.PageResult;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface CartService {
    PageResult<Cart> getAllCart(Integer pageNum, Integer pageSize, String username);

    void deleteCart(List<Integer> ids);

    List<Cart> getCartByUserId(Integer userId);

    void addToCart(Integer userId, Integer bookId, @Min(1) Integer quantity);

    void addQuantity(Integer id, @Min(1) Integer quantity);
}
