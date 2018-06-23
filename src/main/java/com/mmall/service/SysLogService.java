package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.model.*;
import com.mmall.param.SearchLogParam;

/**
 * Created by xuning on 2018/6/14.
 */
public interface SysLogService {


    public void saveDeptLog(SysDept before, SysDept after);

    public void saveUserLog(SysUser before, SysUser after) ;
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) ;

    public void saveAclLog(SysAcl before, SysAcl after);

    public void saveRoleLog(SysRole before, SysRole after) ;

    PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param, PageQuery page);

    void recover(int id);


}
