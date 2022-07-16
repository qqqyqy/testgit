package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /*新增套餐 并且同时保存菜品和套餐的关联 （2张表）*/
    public void saveWithDish(SetmealDto setmealDto);
      void removeWithDish(List<Long> ids);
}
