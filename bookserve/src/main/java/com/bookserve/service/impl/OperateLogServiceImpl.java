package com.bookserve.service.impl;

import com.bookserve.mapper.OperateLogMapper;
import com.bookserve.pojo.Address;
import com.bookserve.pojo.OperateLog;
import com.bookserve.pojo.PageResult;
import com.bookserve.service.OperateLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogServiceImpl implements OperateLogService {
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> getAlloperateLog(Integer pageNum, Integer pageSize, String operationName) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperateLog> addressList = operateLogMapper.getAllAddress(operationName);
        Page<OperateLog> page = (Page<OperateLog>) addressList;
        PageResult<OperateLog> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public void deleteAddress(List<Integer> ids) {
        operateLogMapper.deleteAddress(ids);
    }
}
