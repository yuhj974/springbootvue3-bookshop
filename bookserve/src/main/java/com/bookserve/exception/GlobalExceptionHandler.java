package com.bookserve.exception;


import com.bookserve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler
//    public Result handleException(Exception e){
//        log.error("程序出错啦~", e);
//        return Result.error("出错啦, 请联系管理员~");
//    }
@ExceptionHandler
public Result handleException(Exception e){
    // 先检查是否是分类有书籍的运行时异常
    if (e instanceof RuntimeException
            && e.getMessage().contains("该分类下有书籍，无法删除")) {
        return Result.error(e.getMessage());
    }

    log.error("程序出错啦~", e);
    return Result.error("出错啦, 请联系管理员~");
}

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("程序出错啦~", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errMsg = message.substring(i);
        String[] arr = errMsg.split(" ");
        return Result.error( arr[2] + " 已存在");
    }

    /**
     * 声明异常处理的方法 - BusinessException
     */
    @ExceptionHandler
    public Result handleBuinessException(BusinessException businessException) {
        log.error("服务器异常", businessException);
        return Result.error(businessException.getMessage());
    }
    /*
    *
    * */

    // 新增分类包含书籍的异常处理
    @ExceptionHandler
    public Result handleCategoryHasBooksException(CategoryHasBooksException e) {
        log.warn("业务校验异常：{}", e.getMessage());  // 使用warn级别更合理
        return Result.error(e.getMessage());  // 直接返回友好提示
    }

    // 修改原通用异常处理（降级处理）
   // @ExceptionHandler
//    public Result handleException(Exception e){
//        // 先检查是否是分类有书籍的运行时异常
//        if (e instanceof RuntimeException
//                && e.getMessage().contains("该分类下有书籍，无法删除")) {
//            return Result.error(e.getMessage());
//        }
//
//        log.error("系统异常：", e);
//        return Result.error("系统繁忙，请稍后再试~");
//    }
//    @ResponseBody // 返回json串
//    public Result error(CustomException e) {
//        return Result.error(e.getCode(), e.getMsg());
//    }
}
