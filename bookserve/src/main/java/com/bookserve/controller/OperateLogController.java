package com.bookserve.controller;

import com.bookserve.pojo.Address;
import com.bookserve.pojo.OperateLog;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.OperateLogService;
import com.bookserve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OperateLogController {
    @Autowired
    private OperateLogService operateLogService;
    //分页查询所有操作日志
    @GetMapping("/operateLog")
    public Result getAlloperateLog(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, String operationName) {
        PageResult<OperateLog> pageResult =  operateLogService.getAlloperateLog(pageNum,pageSize,operationName);
        return Result.success(pageResult);

    }
    //批量删除操作日志
    @DeleteMapping("/operateLog/{ids}")
    public Result deleteAddress(@PathVariable("ids") List<Integer> ids) {
        operateLogService.deleteAddress(ids);
        return Result.success();
    }
}
