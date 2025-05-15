package com.bookserve.service.impl;

import com.bookserve.mapper.BookMapper;
import com.bookserve.pojo.Book;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.BookService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;

    @Override
    public PageResult<Book> getAllBook(Integer pageNum, Integer pageSize,String name, String author, Integer status, String categoryName) {

        PageHelper.startPage(pageNum,pageSize);
        List<Book> bookList = bookMapper.getAllBook( name, author, status, categoryName);
        Page<Book> page = (Page<Book>) bookList;
        PageResult<Book> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public void addBook(Book book) {
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        bookMapper.addBook(book);
    }

    @Override
    public Book getBookById(Integer id) {
        Book book = bookMapper.getBookById(id);
        return book;
    }

    @Override
    public void updateBook(Book book) {
        book.setUpdateTime(LocalDateTime.now());
        bookMapper.updateBook(book);
    }

    @Override
    public void deleteBook(List<Integer> ids) {

        bookMapper.deleteBook(ids);
    }

    @Override
    public List<Book> getBookByCategoryId(Integer id) {
     List<Book> bookList =   bookMapper.getBookByCategoryId(id);
        return bookList;

    }

    @Override
    public boolean existsBook(Integer bookId) {
        return bookMapper.getfindBookById(bookId);

    }
}
