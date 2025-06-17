package com.bookserve.service;

import com.bookserve.pojo.Book;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;

import java.util.List;

public interface BookService {
    PageResult<Book> getAllBook(Integer pageNum, Integer pageSize, String name, String author, Integer status, String categoryName,Integer isRecommended);

    void addBook(Book book);

    Book getBookById(Integer id);

    void updateBook(Book book);

    void deleteBook(List<Integer> ids);

    List<Book> getBookByCategoryId(Integer id);

    boolean existsBook(Integer bookId);

    List<Book> getAllBooks(Book book);

    /**
     * 设置书本推荐状态
     * @param id 书本ID
     * @param recommendStatus 推荐状态：1-推荐，0-不推荐
     */
    void setRecommend(Integer id, Integer recommendStatus);

    /**
     * 获取所有推荐书本
     * @return 推荐书本列表
     */
    List<Book> getRecommendedBooks();


}
