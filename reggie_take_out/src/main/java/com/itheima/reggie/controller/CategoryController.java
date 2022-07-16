package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* @RestCpontroller = @ResponseBody + @Controller*/
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
     public R<Page> page(int page,int pageSize){
      Page<Category> pageInfo = new Page<>(page,pageSize);/*分页构造器*/
        /*条件构造器*/
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
     }

     @DeleteMapping
     public R<String> delete(Long id){
      /*categoryService.removeById(id);*/
         categoryService.remove(id);
      return R.success("分类信息删除成功");
     }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
public R<String> update(@RequestBody Category category){
categoryService.updateById(category);
return R.success("修改分类信息成功");
}

    /**
     * 根据条件查询 列表
     * @param category
     * @return
     */
    @GetMapping("/list")
public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
       List<Category> list = categoryService.list(lambdaQueryWrapper);
        return  R.success(list);
    }

}
