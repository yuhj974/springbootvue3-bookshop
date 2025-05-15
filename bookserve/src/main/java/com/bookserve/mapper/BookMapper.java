package com.bookserve.mapper;

import com.bookserve.pojo.Book;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {

    List <Book> getAllBook(String name, String author, Integer status, String categoryName);

    @Insert("INSERT INTO book(category_id, title, author, price, stock, cover_image, description, status, create_time, update_time) " +
            "VALUES (" +
            "  (SELECT id FROM category WHERE name = #{categoryName}), " +
            "  #{title}, #{author}, #{price}, #{stock}, #{coverImage}, #{description}, #{status}, #{createTime}, #{updateTime}" +
            ")")
    void addBook(Book book);

    @Select("SELECT b.*, c.name AS categoryName " +
            "FROM book b " +
            "JOIN category c ON b.category_id = c.id " +
            "WHERE b.id = #{id}")
    Book getBookById(Integer id);

    @Update("UPDATE book SET category_id = (SELECT id FROM category WHERE name = #{categoryName}), title = #{title}, author = #{author}, price = #{price}, stock = #{stock}, cover_image = #{coverImage}, description = #{description}, status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    void updateBook(Book book);


    void deleteBook(List<Integer> ids);

    @Select("SELECT COUNT(*) FROM book WHERE category_id = #{id}")
    int countBooksByCategoryId(Integer id);

    List<Book> getBookByCategoryId(Integer id);

    boolean getfindBookById(Integer bookId);

    Book selectById(Integer bookId);

    void updateStock( @Param("bookId") int bookId, @Param("num") int num  );
}
