package com.bookserve.controller;

import com.bookserve.anno.LogOperation;
import com.bookserve.mapper.BookMapper;
import com.bookserve.pojo.Category;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.CategoryService;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping("/categorys")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookMapper  bookMapper;


    /*
    *
    * 查询所有分类
    * */
    @GetMapping
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize,String name ){

        PageResult categoryList = categoryService.getAllCategory(pageNum, pageSize, name);

        return Result.success(categoryList);
    }

    /*
    * 根据ID查询分类信息
    * */
    @GetMapping("/{id}")
    public Result getCategoryById(@PathVariable Integer id){
        Category category = categoryService.getCategoryById(id);
        return Result.success(category);
    }

    /*
    * 修改分类信息
    * */

    @PutMapping
    public Result updateCategory(@RequestBody Category category){

        categoryService.updateCategory(category);
        return Result.success();
    }

    /*
    * 新增分类
    * */
    @LogOperation
    @PostMapping
    public Result addCategory(@RequestBody Category category){

        categoryService.addCategory(category);
        return Result.success();
    }

    /*
    * 根据id删除分类
    * */
    @DeleteMapping("/{id}")
    public Result deleteCategory(@PathVariable("id") Integer id){
//        // 检查分类下是否有书籍
//        Integer bookCount = bookMapper.countBooksByCategoryId(id);
//        if (bookCount > 0) {
//            throw new RuntimeException("该分类下有书籍，无法删除");
//        }

        categoryService.deleteCategory(id);
        return Result.success();
    }
    @GetMapping("/all")
    public Result getAllCategory(){

        List<Category> categoryList = categoryService.getAllCategory2();
        return Result.success(categoryList);
    }
}
