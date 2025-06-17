package com.bookserve.controller;

import com.bookserve.anno.LogOperation;
import com.bookserve.pojo.Book;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.BookService;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/books")
@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    /*
    * 分页查询所有图书
    * */
    @GetMapping("/all")
    public Result getAllBook(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10")
                             Integer pageSize, String name, String author, Integer status, String categoryName,Integer isRecommended){


        PageResult bookList = bookService.getAllBook(pageNum, pageSize, name, author, status, categoryName,isRecommended);
        System.out.print(bookList);
     return Result.success(bookList);


    }

    /*
    * 新增图书
    * */
    @LogOperation("新增书本")
    @PostMapping
    public Result addBook(@RequestBody Book book){
        bookService.addBook(book);
        return Result.success();
    }

    /*
    * 根据id查询图书
    * */

    @GetMapping("/{id}")
    public Result getBookById(@PathVariable Integer id){
        Book book = bookService.getBookById(id);
        return Result.success(book);
    }

    /*
    * 修改图书
    * */
    @PutMapping
    public Result updateBook(@RequestBody Book book){
        bookService.updateBook(book);
        return Result.success();
    }

    /*
    * 删除图书
    * */
    @DeleteMapping("/{ids}")
    public Result deleteBook(@PathVariable("ids") List<Integer> ids){

        bookService.deleteBook(ids);
        return Result.success();
    }

    /*
    * 根据分类id查询图书
    * */
    @GetMapping("/category/{id}")
    public Result getBookByCategoryId(@PathVariable("id") Integer id){
        List<Book> books = bookService.getBookByCategoryId(id);
        return Result.success(books);
    }
    /*
    * 查询所有图书
    * */
    @GetMapping("/full")
    public Result getAllBook(Book book ){
        List<Book> books = bookService.getAllBooks(book);
        return Result.success(books);
    }

    /*
     * 设为推荐书本
     */
    @PutMapping("/recommend/{id}")
    public Result setRecommend(@PathVariable Integer id, @RequestParam Integer recommendStatus) {
        bookService.setRecommend(id, recommendStatus);
        return Result.success();
    }


    /*
     * 获取推荐书本列表
     */
    @GetMapping("/recommended")
    public Result getRecommendedBooks() {
        List<Book> books = bookService.getRecommendedBooks();
        return Result.success(books);
    }
}
