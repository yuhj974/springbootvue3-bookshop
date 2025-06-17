package com.bookserve.service.impl;

import com.bookserve.mapper.CarouselMapper;
import com.bookserve.pojo.Carousel;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.CarouselService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public PageResult<Carousel> listCarousels(int pageNum, int pageSize, String title, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        List<Carousel> carousels = carouselMapper.selectCarousels(title, status);
        Page<Carousel> page = (Page<Carousel>) carousels;
        PageResult<Carousel> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;

    }

    @Override
    public int updateCarousel(Carousel carousel) {
        // 设置更新时间（如果前端没传）
        if (carousel.getUpdateTime() == null) {
            carousel.setUpdateTime(LocalDateTime.now());
        }
        return carouselMapper.update(carousel);
    }

    @Transactional
    @Override
    public int deleteCarousels(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return carouselMapper.deleteByIds(ids);
    }
    @Override
    public int addCarousel(Carousel carousel) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        carousel.setCreateTime(now);
        carousel.setUpdateTime(now);

        // 设置默认排序值（如果未设置）
        if (carousel.getSort() == null) {
            carousel.setSort(0);
        }

        // 设置默认状态（如果未设置）
        if (carousel.getStatus() == null) {
            carousel.setStatus(1); // 默认启用
        }

        return carouselMapper.insert(carousel);
    }
}