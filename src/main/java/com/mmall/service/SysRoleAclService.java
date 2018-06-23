package com.mmall.service;

import java.util.List;

/**
 * Created by xuning on 2018/6/9.
 */
public interface SysRoleAclService {

    void changeRoleAcls(int roleId,List<Integer> aclIdList);
}
