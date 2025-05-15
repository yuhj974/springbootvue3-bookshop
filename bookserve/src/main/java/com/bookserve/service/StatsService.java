package com.bookserve.service;

import com.bookserve.exception.BusinessException;
import com.bookserve.mapper.StatsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j  // 使用 Lombok 记录日志
public class StatsService {
    @Autowired
    private StatsMapper statsMapper;

    public List<Map<String, Object>> getCategoryStats() {
        try {
            return statsMapper.getCategoryStats();
        } catch (Exception e) {
            log.error("获取分类统计失败: {}", e.getMessage());
            throw new BusinessException("统计服务暂不可用");
        }
    }

    public Map<String, Long> getGeneralStats() {
        Map<String, Long> stats = new HashMap<>();
        try {
            stats.put("totalOrders", statsMapper.getTotalOrders());
            stats.put("todayOrders", statsMapper.getTodayOrders());
            stats.put("totalUsers", statsMapper.getTotalUsers());
            stats.put("todayUsers", statsMapper.getTodayUsers());

            // 新增图书统计
            stats.put("totalBooks", statsMapper.getTotalBooks());
            stats.put("todayBooks", statsMapper.getTodayBooks());
            stats.put("totalStock", statsMapper.getTotalStock());
            stats.put("todayAddedStock", statsMapper.getTodayAddedStock());
            //stats.put("yesterdayDecreasedStock", statsMapper.getYesterdayDecreasedStock());
        } catch (Exception e) {
            log.error("获取综合统计失败: {}", e.getMessage());
            throw new BusinessException("统计服务异常");
        }
        return stats;
    }
}
