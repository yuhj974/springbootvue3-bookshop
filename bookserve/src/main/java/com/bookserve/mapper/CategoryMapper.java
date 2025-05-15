package com.bookserve.mapper;

import com.bookserve.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {


    /*
     *
     * 查询所有分类
     * */
    List<Category> getAllCategory(String name);

    /*
     * 根据ID查询分类信息
     * */
    Category getCategoryById(Integer id);


    /*
     * 修改分类信息
     * */
    @Update("update category set name=#{name},update_time=#{updateTime} where id=#{id}")
    void updateCategory(Category category);

    /*
     * 新增分类
     * */
    @Insert("insert into category(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void addCategory(Category category);

    /*
     * 根据id删除分类
     * */
    @Delete("delete from category where id=#{id}")
    void deleteCategory(Integer id);

    List<Category> getAllCategory2();
}
