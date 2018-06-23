package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;

import java.util.List;

/**
 * Created by xuning on 2018/6/4.
 */
public interface SysAclService {
     void save(AclParam param);
     void update(AclParam param);

     PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery pageQuery);

}
