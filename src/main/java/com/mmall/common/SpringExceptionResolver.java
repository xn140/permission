package com.mmall.common;

import com.mmall.exception.ParamException;
import com.mmall.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuning on 2018/5/2.
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver{

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
       String url=httpServletRequest.getRequestURL().toString();
       String defaultMsg="System error";
       ModelAndView mv;
       //要求所有请求json数据的请求都以.json结尾

        if(url.endsWith(".json")){
            if(e instanceof PermissionException || e instanceof ParamException){
                JsonData jsonData = JsonData.fail(e.getMessage());
                mv=new ModelAndView("jsonView",jsonData.toMap());
            }else{
                log.error("unknown json exception, url:" + url,e);
                JsonData jsonData = JsonData.fail(defaultMsg);
                mv=new ModelAndView("jsonView",jsonData.toMap());
            }

        }else if(url.endsWith(".page")){//所有请求页面的请求都以.page结尾
            log.error("unknown page exception, url:" + url,e);
            JsonData jsonData = JsonData.fail(defaultMsg);
            mv=new ModelAndView("exception",jsonData.toMap());
        }else{
            log.error("unknow exception, url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView", result.toMap());
        }
        return mv;
    }
}
