package com.mmall.service;

import com.mmall.model.SysUser;

import java.util.List;

/**
 * Created by xuning on 2018/6/10.
 */
public interface SysRoleUserService {
    List<SysUser> getListByRoleId(int roleId);

    void changeRoleUsers(int roleId,List<Integer> userIdList);
}
