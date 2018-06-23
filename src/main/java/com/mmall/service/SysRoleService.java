package com.mmall.service;

import com.mmall.dto.AclModuleLevelDto;
import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import com.mmall.param.RoleParam;

import java.util.List;

/**
 * Created by xuning on 2018/6/5.
 */
public interface SysRoleService {

    void  save(RoleParam param);
    void update(RoleParam param);
    List<SysRole> getAll();
     List<SysRole> getRoleListByUserId(int userId) ;
     List<SysRole> getRoleListByAclId(int aclId);

     List<SysUser> getUserListByRoleList(List<SysRole> roleList);


}
