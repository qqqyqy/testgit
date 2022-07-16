package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器 支持通配符
    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        /**
         * 1 获取本次请求的URI
         * 2 判断本次请求是否需要处理
         * 3 如果不需要处理 直接放行
         * 4 判断登录状态 如果已经登录 直接放行
         * 5 如果没有登录 返回登录的结果
         *
         */

       String requestURI = request.getRequestURI();
       String [] urls = new String[]{    //定义不需要拦截的请求 如：登录 登出 静态资源
               "/employee/login",
               "/employee/logout",
               "/backend/**",
               "/front/**"
       };

      boolean check = check(urls,requestURI);//请求路径匹配了预先的定义
       if(check){
           filterChain.doFilter(request,response); //放行
           return;  //放行以后直接结束
       }
     if(request.getSession().getAttribute("employee")!=null){
       Long empId = (Long) request.getSession().getAttribute("employee");
         BaseContext.setCurrentId(empId);

       long id = Thread.currentThread().getId();
         log.info("线程id为:{}",id);
              filterChain.doFilter(request,response);       //已经登陆了 直接放行
                return;
     }

     //通过输出流响应客户端
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
      return;
    }


    public boolean check(String[] urls,String requestURI){
   for(String url:urls)
   {
     boolean match =  PATH_MATCHER.match(url,requestURI);
     if(match){
         return true;
     }
   }
   return false;
    }
}
