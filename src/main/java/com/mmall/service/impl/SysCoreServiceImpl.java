package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.beans.CacheKeyConstants;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.dao.SysRoleUserMapper;
import com.mmall.model.SysAcl;
import com.mmall.model.SysUser;
import com.mmall.service.SysCacheService;
import com.mmall.service.SysCoreService;
import com.mmall.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by xuning on 2018/6/7.
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysCacheService sysCacheService;
    @Override
    public List<SysAcl> getCurrentUserAclList() {

        //获取用户的id
        int userId= RequestHolder.getCurrentUser().getId();
         return getUserAclList(userId);
    }
    //获取角色的已分配权限列表
    @Override
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if(CollectionUtils.isEmpty(aclIdList)){
          return Lists.newArrayList();

        }
        return sysAclMapper.getByIdList(aclIdList);
    }

   //查询一个用户的权限
    public  List<SysAcl> getUserAclList(int userId){
       //如果是超级管理员，直接取出所有的权限
        if(isSuperAdmin()){

          return sysAclMapper.getAll();
        }
        //用户的所有角色
      List<Integer> roleIdList=sysRoleUserMapper.getRoleIdListByUserId(userId);

        if(CollectionUtils.isEmpty(roleIdList)){

            return Lists.newArrayList();

        }
        //根据用户的角色获取用户分配的权限点的总和
        List<Integer> userAclIdList=sysRoleAclMapper.getAclIdListByRoleIdList(roleIdList);
        if(CollectionUtils.isEmpty(userAclIdList)){
            return Lists.newArrayList();

        }
        //返回用户的所有权限
        return sysAclMapper.getByIdList(userAclIdList);
    }

    public boolean isSuperAdmin() {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }


    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }
        List<SysAcl> userAclList = getCurrentUserAclList();

        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;
        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }

            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        //如果所有的权限都为空，或状态都不为1
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }


    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
