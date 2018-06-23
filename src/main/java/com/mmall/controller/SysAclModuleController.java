package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.param.AclModuleParam;
import com.mmall.service.SysAclModulService;
import com.mmall.service.SysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xuning on 2018/5/31.
 */
@Controller
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Autowired
    private SysAclModulService aclModulService;

    @Autowired
    private SysTreeService sysTreeService;
    @RequestMapping("/acl.page")
    public ModelAndView page(){

        return new ModelAndView("acl");
    }
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(AclModuleParam param){
        aclModulService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(AclModuleParam param){
        aclModulService.update(param);
        return JsonData.success();

    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){

        return JsonData.success(sysTreeService.aclTree());
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id){
        aclModulService.delete(id);
        return JsonData.success();

    }
}
