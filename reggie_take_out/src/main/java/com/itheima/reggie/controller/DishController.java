package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService; //菜品
    @Autowired
    private DishFlavorService dishFlavorService; //口味

 @Autowired
 private CategoryService categoryService;
    @PostMapping
   public R<String> save(@RequestBody DishDto dishDto){
       dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
   }

   @GetMapping("/page")
   public R<Page> page(int page,int pageSize,String name ){
        Page<Dish> pageInfo = new Page<Dish>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
       LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       lambdaQueryWrapper.like(name!=null,Dish::getName,name);
       lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
       dishService.page(pageInfo,lambdaQueryWrapper);
       BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records =  pageInfo.getRecords();
     List<DishDto> list =  records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
              Long categoryId = item.getCategoryId();

              Category category = categoryService.getById(categoryId);
              if(category!=null){
                  String categoryName =  category.getName();
                  dishDto.setCategoryName(categoryName);
              }

             return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
       return R.success(dishDtoPage);
   }
@GetMapping("/{id}")  /**/
public R<DishDto> get(@PathVariable Long id){
    DishDto dishDto = dishService.getByIdWithFlavor(id);
    return  R.success(dishDto);
}

@PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
    dishService.updateWithFlavor(dishDto);
    return R.success("修改成功");
}


@DeleteMapping
    public R<String> delete(Long ids){
     dishService.removeById(ids);
     return R.success("删除成功");
}

@PutMapping("/status")
    public R<String> statusChange(@RequestBody Dish dish){

    dishService.updateById(dish);
    return R.success("修改成功");
}

@GetMapping("/list")
    public R<List<Dish>> list( Dish dish){
    LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
    lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
     lambdaQueryWrapper.eq(Dish::getStatus,1);
      List<Dish>list = dishService.list(lambdaQueryWrapper);
    return R.success(list);
}

}
