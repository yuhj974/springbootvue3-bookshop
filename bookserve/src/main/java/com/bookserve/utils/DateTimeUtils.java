package com.bookserve.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime parseStartTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return null;

        try {
            // 尝试解析完整时间格式
            return LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            // 解析日期格式并设置当天开始时间
            LocalDate date = LocalDate.parse(timeStr, DATE_FORMATTER);
            return date.atStartOfDay();
        }
    }

    public static LocalDateTime parseEndTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) return null;

        try {
            // 尝试解析完整时间格式
            return LocalDateTime.parse(timeStr, DATE_TIME_FORMATTER);
        } catch (Exception e) {
            // 解析日期格式并设置当天结束时间
            LocalDate date = LocalDate.parse(timeStr, DATE_FORMATTER);
            return date.atTime(23, 59, 59);
        }
    }
}
