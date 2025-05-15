package com.bookserve.service;

import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;

import java.util.List;

public interface CategoryService {

    /*
     *
     * 查询所有分类
     * */
    PageResult<Category> getAllCategory( Integer pageNum, Integer pageSize,  String name);

    /*
     * 根据ID查询分类信息
     * */
    Category getCategoryById(Integer id);


    /*
     * 修改分类信息
     * */

    void updateCategory(Category category);

    /*
     * 新增分类
     * */
    void addCategory(Category category);

    /*
     * 根据id删除分类
     * */
    void deleteCategory(Integer id);

    List<Category> getAllCategory2();
}
