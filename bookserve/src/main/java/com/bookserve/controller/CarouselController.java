package com.bookserve.controller;

import com.bookserve.pojo.Carousel;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.CarouselService;
import com.bookserve.utils.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    // 分页查询轮播图
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            String title,Integer status) {
        PageResult pageInfo = carouselService.listCarousels(pageNum, pageSize,title,status);
        return Result.success(pageInfo);
    }

    // 新增轮播图
    @PostMapping("/add")
    public Result add(@RequestBody Carousel carousel) {
        int result = carouselService.addCarousel(carousel);
        return result > 0 ? Result.success() : Result.error("新增失败");
    }

    // 修改轮播图
    @PutMapping("/update")
    public Result update(@RequestBody Carousel carousel) {
        int result = carouselService.updateCarousel(carousel);
        return result > 0 ? Result.success() : Result.error("更新失败");
    }

    // 删除/批量删除轮播图
    @DeleteMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Result.error("请选择要删除的轮播图");
        }
        int result = carouselService.deleteCarousels(ids);
        return result > 0 ? Result.success() : Result.error("删除失败");
    }
}