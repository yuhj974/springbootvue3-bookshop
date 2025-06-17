package com.bookserve.mapper;

import com.bookserve.pojo.OperateLog;
import com.bookserve.pojo.PageResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("INSERT INTO operate_log " +
            "(operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time, operation_name) " +
            "VALUES " +
            "(#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime}, #{operationName})")
    public void insert(OperateLog log);

    // 查询日志数据
    List<OperateLog> getAllAddress(String operationName);

    void deleteAddress(List<Integer> ids);
}
