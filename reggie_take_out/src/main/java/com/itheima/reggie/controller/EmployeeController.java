package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
      String password =  employee.getPassword();//实体类
         password =  DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();//mybatis-plus的
        //条件构造器
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
         Employee emp = employeeService.getOne(queryWrapper);//
        if(emp==null) return R.error("登录失败"); //查不到用户名 返回错误信息 （实际上返回了一个R对象）

        if(!emp.getPassword().equals(password)) return R.error("登录失败");//密码错误

        if(emp.getStatus()==0) return R.error("账号被锁定");

        /*登录成功 把用户信息放到session里*/
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理session中保存的用户id
        request.getSession().removeAttribute("employee");
        return  R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
       //log.info("新增员工信息"+employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));//设置初始密码123456
     /* employee.setCreateTime(LocalDateTime.now());   //创建时间
      employee.setUpdateTime(LocalDateTime.now());    //修改时间*/
      //获得当前用户的id (创建者)
    /* Long empid =  (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empid);
        employee.setUpdateUser(empid);*/
         employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
     @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
       // log.info("page={},pageSize={},name={}",page,pageSize,name);
         //构造分页构造器
         Page pageInfo = new Page(page,pageSize);

         //构造条件构造器
         LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

         queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
         queryWrapper.orderByDesc(Employee::getName);
         employeeService.page(pageInfo,queryWrapper);
         return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee ){
       // log.info(employee.toString());
        long id = Thread.currentThread().getId();
       // log.info("线程id为:{}",id);
       /* employee.setUpdateTime(LocalDateTime.now());*/
      /*  employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));*/
        employeeService.updateById(employee);
        return  R.success("员工信息修改成功");

}
//路径变量 根据id查询数据 方便回显
@GetMapping("/{id}")
public R<Employee> getById(@PathVariable Long id){
     Employee employee =   employeeService.getById(id);
        return  R.success(employee);
}

@GetMapping("/check")
    public R<String> checkCount(HttpServletRequest request){
   Long id = (Long)request.getSession().getAttribute("employee");
   LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
   lambdaQueryWrapper.eq(Employee::getId,id);
 List<Employee> ans =  employeeService.list(lambdaQueryWrapper);
      for(Employee e:ans){
          if(e.getStatus()==0){

              return R.success("强制退出");
          }
    }
        return R.error("不退出");
}

}
