package com.mmall.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mmall.common.JsonData;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;
import com.mmall.service.*;
import com.mmall.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xuning on 2018/6/5.
 */
@RequestMapping("/sys/role")
@Controller
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private UserService userService;

    @RequestMapping("role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }

    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@RequestParam("roleId") int roleId ){
        List<AclModuleLevelDto> aclModuleLevelDtos = sysTreeService.roleTree(roleId);
        return JsonData.success(aclModuleLevelDtos);
    }
    @RequestMapping("changeAcls.json")
    @ResponseBody
    public JsonData changeAcl(@RequestParam("roleId") int roleId,@RequestParam(value = "aclIds",required = false,defaultValue = "") String aclIds){
       List<Integer> aclIdList= StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId,aclIdList);
        return JsonData.success();

    }

    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId){
        //已选的用户列表
         List<SysUser> selectUserList=sysRoleUserService.getListByRoleId(roleId);
        //全部用户列表
        List<SysUser> allUserList = userService.getAll();

        Set<Integer> selectUserSet=selectUserList.stream().map(e -> e.getId()).collect(Collectors.toSet());
        //未选中的列表
        List<SysUser> unSelectUserList= Lists.newArrayList();
        for (SysUser user:allUserList){

            if(user.getStatus()==1 && !selectUserSet.contains(user.getId())){

                unSelectUserList.add(user);
            }
        }

        Map<String,List<SysUser>> map= Maps.newHashMap();
        map.put("selected",selectUserList);
        map.put("unselected",unSelectUserList);
        return JsonData.success(map);
    }
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") int roleId,@RequestParam(value = "userIds",required = false,defaultValue = "") String userIds){
        List<Integer> userIdList= StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId,userIdList);
        return JsonData.success();
    }
}
