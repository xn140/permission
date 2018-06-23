package com.mmall.service;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;

import java.util.List;

/**
 * Created by xuning on 2018/5/16.
 */
public interface UserService {

     void save(UserParam userParam);
     void update(UserParam userParam);

    SysUser findByKeyWord(String keyWord);

    PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery);

    List<SysUser> getAll();
}
