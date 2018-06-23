package com.mmall.controller;

import com.google.common.collect.Maps;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.JsonData;
import com.mmall.model.SysUser;
import com.mmall.param.DeptParam;
import com.mmall.param.UserParam;
import com.mmall.service.SysRoleService;
import com.mmall.service.SysRoleUserService;
import com.mmall.service.SysTreeService;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by xuning on 2018/5/16.
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleService sysRoleService;
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam userParam){
        userService.save(userParam);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam userParam){
        userService.update(userParam);
        return JsonData.success();

    }
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId") int deptId, PageQuery pageQuery){
        PageResult<SysUser> pageResult = userService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(pageResult);
    }
    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId){
        Map<String,Object> map= Maps.newHashMap();
        map.put("acls",sysTreeService.userAclTree(userId));
        map.put("roles",sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
}
