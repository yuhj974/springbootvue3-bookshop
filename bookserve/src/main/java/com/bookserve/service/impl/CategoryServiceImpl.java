package com.bookserve.service.impl;

import com.bookserve.exception.CategoryHasBooksException;
import com.bookserve.mapper.BookMapper;
import com.bookserve.mapper.CategoryMapper;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.CategoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BookMapper bookMapper;


    /*
     *
     * 查询所有分类
     * */
    @Override
    public PageResult<Category> getAllCategory( Integer pageNum, Integer pageSize , String name) {

        PageHelper.startPage(pageNum,pageSize);
        List<Category> categoryList = categoryMapper.getAllCategory(name);
        Page<Category> page = (Page<Category>) categoryList;
        PageResult<Category> pageResult = new PageResult<>(page.getTotal(),page.getResult());


        return pageResult;
    }

    /*
     * 根据ID查询分类信息
     * */
    @Override
    public Category getCategoryById(Integer id) {
        Category category = categoryMapper.getCategoryById(id);
        return category;
    }


    /*
     * 修改分类信息
     * */

    @Override
    public void updateCategory(Category category) {

        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCategory(category);


    }
    /*
     * 新增分类
     * */
    @Override
    public void addCategory(Category category) {

        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.addCategory(category);
    }

    /*
     * 根据id删除分类
     * */
    @Override
    public void deleteCategory(Integer id) {
        // 检查分类下是否有书籍
        Integer bookCount = bookMapper.countBooksByCategoryId(id);
        if (bookCount > 0) {
            throw new CategoryHasBooksException("该分类下有书籍，无法删除");
        }
        categoryMapper.deleteCategory(id);
    }

    @Override
    public List<Category> getAllCategory2() {
    List <Category> categoryList =    categoryMapper.getAllCategory2();
    return categoryList;

    }

}
