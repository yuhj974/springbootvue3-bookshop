package com.bookserve.service.impl;

import com.bookserve.mapper.CartMapper;
import com.bookserve.pojo.Cart;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.BookService;
import com.bookserve.service.CartService;
import com.bookserve.utils.Result;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private BookService bookService;


    @Override
    public PageResult<Cart> getAllCart(Integer pageNum, Integer pageSize, String username) {
        PageHelper.startPage(pageNum,pageSize);

        List<Cart> cartList = cartMapper.getAllCart(pageNum, pageSize, username);
        Page<Cart> page = (Page<Cart>) cartList;
        PageResult<Cart> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;

    }

    @Override
    public void deleteCart(List<Integer> ids) {
        cartMapper.deleteCart(ids);
    }

    @Override
    public List<Cart> getCartByUserId(Integer userId) {
      return   cartMapper.getCartByUserId(userId);

    }

    @Override
    public void addToCart(Integer userId, Integer bookId, Integer quantity) {
        // 验证图书是否存在
        if (!bookService.existsBook(bookId)) {
            throw new IllegalArgumentException("图书不存在");
        }

        // 构建购物车项
        Cart item = new Cart();
        item.setUserId(userId);
        item.setBookId(bookId);
        item.setQuantity(quantity);

        // 执行数据库操作
        int affectedRows = cartMapper.upsertCartItem(item);
        if (affectedRows == 0) {
            throw new RuntimeException("添加购物车失败");
        }
    }

    @Override
    public void addQuantity(Integer id, Integer quantity) {
        cartMapper.addQuantity(id, quantity);


    }
}

