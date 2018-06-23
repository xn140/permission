package com.mmall.service;

import com.mmall.model.SysAcl;

import java.util.List;

/**
 * Created by xuning on 2018/6/7.
 */
public interface SysCoreService  {
    List<SysAcl> getCurrentUserAclList();
    List<SysAcl> getRoleAclList(int roleId);
     List<SysAcl> getUserAclList(int userId) ;
    boolean hasUrlAcl(String url);

}
