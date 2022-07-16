package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公共字段自动填充的作用是 不同表中的相同字段可以统一处理 不必在controller当中单独处理
 */
@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    @Override/*插入操作自动填充*/
    public void insertFill(MetaObject metaObject) {
metaObject.setValue("createTime", LocalDateTime.now());
metaObject.setValue("updateTime",LocalDateTime.now());
metaObject.setValue("createUser",BaseContext.getCurrentId());
metaObject.setValue("updateUser",BaseContext.getCurrentId());
      /* log.info("公共字段自动填充insert");
        log.info(metaObject.toString());*/
    }

    @Override/**/
    public void updateFill(MetaObject metaObject) {
        /*log.info("公共字段自动填充update");
log.info(metaObject.toString());*/
        long id = Thread.currentThread().getId();
       // log.info("线程id为:{}",id);
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }
}
