package com.bookserve.service;

import com.bookserve.pojo.Book;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;

import java.util.List;

public interface BookService {
    PageResult<Book> getAllBook(Integer pageNum, Integer pageSize, String name, String author, Integer status, String categoryName);

    void addBook(Book book);

    Book getBookById(Integer id);

    void updateBook(Book book);

    void deleteBook(List<Integer> ids);

    List<Book> getBookByCategoryId(Integer id);

    boolean existsBook(Integer bookId);
}
