package com.bookserve.mapper;

import com.bookserve.pojo.Cart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {

    List<Cart> getAllCart(Integer pageNum, Integer pageSize, String username);

    void deleteCart(List<Integer> ids);

    List<Cart> getCartByUserId(Integer userId);

    // 添加或更新购物车项
    @Insert("INSERT INTO cart (user_id, book_id, quantity) " +
            "VALUES (#{userId}, #{bookId}, #{quantity}) " +
            "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)")
    int upsertCartItem(Cart item);

    // 检查是否存在相同记录
    @Select("SELECT COUNT(*) FROM cart WHERE user_id = #{userId} AND book_id = #{bookId}")
    boolean existsItem(@Param("userId") Integer userId, @Param("bookId") Integer bookId);

    void addQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);


    // 根据用户ID和书籍ID删除购物车项
    int deleteByUserIdAndBookId(
            @Param("userId") Integer userId,
            @Param("bookId") Integer bookId
    );
}
