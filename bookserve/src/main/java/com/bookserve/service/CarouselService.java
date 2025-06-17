package com.bookserve.service;

import com.bookserve.pojo.Carousel;
import com.bookserve.pojo.PageResult;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CarouselService {
    // 分页查询
    PageResult<Carousel> listCarousels(int pageNum, int pageSize, String title, Integer status);

    // 更新轮播图
    int updateCarousel(Carousel carousel);

    // 删除/批量删除
    int deleteCarousels(List<Integer> ids);
    // 新增轮播图
    int addCarousel(Carousel carousel);
}