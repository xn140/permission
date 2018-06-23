package com.mmall.controller;

import com.mmall.common.JsonData;
import com.mmall.dao.SysUserMapper;
import com.mmall.dto.DeptLevelDto;
import com.mmall.param.DeptParam;
import com.mmall.service.SysDeptService;
import com.mmall.service.SysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuning on 2018/5/7.
 */
@RequestMapping("/sys/dept")
@Controller
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page(){

        return new ModelAndView("dept");
    }
     @RequestMapping("/save.json")
     @ResponseBody
    public JsonData saveDept(DeptParam deptParam){
         sysDeptService.save(deptParam);
         return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam deptParam){
        sysDeptService.update(deptParam);
        return JsonData.success();

    }
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        List<DeptLevelDto> treeList = sysTreeService.deptTree();
        return JsonData.success(treeList);
    }
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") int id){
         sysDeptService.delete(id);
         return JsonData.success();

    }
}
