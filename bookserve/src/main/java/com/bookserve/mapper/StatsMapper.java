package com.bookserve.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatsMapper {
    // 分类统计
    List<Map<String, Object>> getCategoryStats();

    // 总订单数
    Long getTotalOrders();

    // 今日订单数
    Long getTodayOrders();

    // 总用户数
    Long getTotalUsers();

    // 今日新增用户
    Long getTodayUsers();


    Long getTotalBooks();
    Long getTodayBooks();
    Long getTotalStock();
    Long getTodayAddedStock();
    //Long getYesterdayDecreasedStock();


}
