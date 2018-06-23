package com.mmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xuning on 2018/5/17.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.page")
    public ModelAndView admin(){
        return new ModelAndView("admin");

    }
}
