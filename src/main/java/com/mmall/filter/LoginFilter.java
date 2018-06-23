package com.mmall.filter;


import com.mmall.common.RequestHolder;
import com.mmall.model.SysUser;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xuning on 2018/5/22.
 */
public class LoginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        SysUser user =(SysUser) request.getSession().getAttribute("user");

        if(user==null){
            //路径如果不加/,默认是在当前路径下跳转。
          String path="/signin.jsp";
            response.sendRedirect(path);


            return ;
        }

        RequestHolder.add(user);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest,servletResponse);
        return ;
    }

    public void destroy() {

    }


    public ModelAndView  test(){

        return new ModelAndView("signin.jsp");
    }


}
