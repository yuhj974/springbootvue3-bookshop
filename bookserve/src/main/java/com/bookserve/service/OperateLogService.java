package com.bookserve.service;

import com.bookserve.pojo.Address;
import com.bookserve.pojo.OperateLog;
import com.bookserve.pojo.PageResult;

import java.util.List;

public interface OperateLogService {
    PageResult<OperateLog> getAlloperateLog(Integer pageNum, Integer pageSize, String operationName);

    void deleteAddress(List<Integer> ids);
}
