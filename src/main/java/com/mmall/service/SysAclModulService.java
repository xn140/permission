package com.mmall.service;

import com.mmall.param.AclModuleParam;

/**
 * Created by xuning on 2018/6/2.
 */
public interface SysAclModulService {

    void save(AclModuleParam aclModuleParam);

    void update(AclModuleParam param);

    void delete(int aclModuleId);
}
