package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 底层基于代理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    /**
     * 用于处理重复异常
     */
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
    if(e.getMessage().contains("Duplicate entry")){
        String[] splits=e.getMessage().split(" ");
        String msg = splits[2]+"已存在";
       return R.error(msg);
    }
    return R.error("未知错误");
    }



    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }

}
