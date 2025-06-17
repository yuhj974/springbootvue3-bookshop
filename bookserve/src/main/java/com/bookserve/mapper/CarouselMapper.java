package com.bookserve.mapper;
import com.bookserve.pojo.Carousel;
import com.bookserve.pojo.PageResult;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CarouselMapper {
    // 分页查询轮播图
    List<Carousel> selectCarousels(String title, Integer status);

    // 根据ID查询轮播图
    @Select("SELECT * FROM carousel WHERE id = #{id}")
    Carousel getById(Integer id);

    // 更新轮播图
    @Update("UPDATE carousel SET " +
            "image_url = #{imageUrl}, " +
            "book_id = #{bookId}, " +
            "title = #{title}, " +
            "sort = #{sort}, " +
            "status = #{status}, " +
            "update_time = NOW() " +
            "WHERE id = #{id}")
    int update(Carousel carousel);

    // 批量删除（支持单个和多个ID）
    int deleteByIds(@Param("ids") List<Integer> ids);

    @Insert("INSERT INTO carousel (image_url, book_id, title, sort, status, create_time, update_time) " +
            "VALUES (#{imageUrl}, #{bookId}, #{title}, #{sort}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Carousel carousel);
}
