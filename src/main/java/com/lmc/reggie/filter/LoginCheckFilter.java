package com.lmc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.lmc.reggie.common.BaseContext;
import com.lmc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**filterName:过滤器名字   urlPatterns：过滤器拦截哪些路径
 * @author 41150**/
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    /**路径匹配器，支持通配符**/
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //1、获取本次请求的URL
        String requestURI = request.getRequestURI();
        log.info("拦截到{}",requestURI);
        //定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端发送登陆请求
        };
        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1、判断后台登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已登录，用户Id为{}",request.getSession().getAttribute("employee"));
            Long emId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(emId);
//            long id=Thread.currentThread().getId();
//            log.info("线程ID为：{}",id);
            filterChain.doFilter(request,response);
            return;
        }
        //4-2、判断移动端登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("移动用户已登录，用户Id为{}",request.getSession().getAttribute("user"));
            Long userId=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
//            long id=Thread.currentThread().getId();
//            log.info("线程ID为：{}",id);
            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录则返回未登录结果
        log.info("用户未登陆",requestURI);
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
    /**路径匹配，检查本次请求是否需要放行**/
    public boolean check(String[] urls,String requestUrl){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return true;
            }
        }
        return false;
    }
}
