package com.bookserve.controller;

import com.bookserve.pojo.Cart;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.BookService;
import com.bookserve.service.CartService;
import com.bookserve.utils.JwtUtils;
import com.bookserve.utils.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/carts")
@RestController
//@RequiredArgsConstructor
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private BookService bookService;

    /*
     * 管理员查询用户所有购物车
     * */
    @GetMapping
    public Result getAllCart(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize, String username) {

     PageResult<Cart> pageResult = cartService.getAllCart(pageNum, pageSize, username);

        return Result.success(pageResult);
    }

    /*
    * 管理员批量删除购物车
    * */
    @DeleteMapping("/{ids}")
    public Result deleteCart(@PathVariable("ids") List<Integer> ids) {
        cartService.deleteCart(ids);
        return Result.success();
    }

    /*
    * 根据用户id查询购物车
    * */
    @GetMapping("/userid")
    public Result getCartByUserId(@RequestHeader("token") String token)throws Exception {
        if (token == null) {
            return Result.error("请先登录");
        }
        String token1 = token.replace("token ", "");
        Integer userId = JwtUtils.extractUserId(token1);
        List<Cart> carts = cartService.getCartByUserId(userId);
        return Result.success(carts);
    }

    /*
    * 根据jwt令牌里的用户id再根据书本id添加购物车
    * */
    @PostMapping("/items")
    public Result addToCart (
            @RequestBody @Valid Cart dto,
            @RequestHeader("token") String token) throws Exception{
        if (token == null) {
            return Result.error("请先登录");
        }

        String token1 = token.replace("token ", "");
        Integer userId = JwtUtils.extractUserId(token1);
        cartService.addToCart(userId, dto.getBookId(), dto.getQuantity());
        return Result.success();
    }
    /*
    * 添加购物车书本数量
    * */
    @PutMapping("/items/{id}/{quantity}")
    public Result addQuantity(@PathVariable("id") Integer id, @PathVariable("quantity") @Min(1) Integer quantity) {
        cartService.addQuantity(id, quantity);
        return Result.success();
    }
//    /*
//    * 根据jwt令牌里的用户id再根据书本id添加购物车
//    * */
//    @PostMapping("/items/{bookId}")
//    public Result addToCart (
//            @PathVariable Integer bookId,
//            @RequestParam @Min(1) Integer quantity,
//            @RequestHeader("token") String token) throws Exception{
//                if (token == null) {
//                    return Result.error("请先登录");
//                }
//                String token1 = token.replace("token ", "");
//                Integer userId = JwtUtils.extractUserId(token1);
//                cartService.addToCart(userId, bookId, quantity);
//                return Result.success();
//    }

}
