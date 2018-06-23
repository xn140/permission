package com.mmall.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mmall.exception.PermissionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by xuning on 2018/5/1.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/hh.json")
    @ResponseBody
    public String test(){
         throw new RuntimeException();
        //return "hello permission";

    }
}
