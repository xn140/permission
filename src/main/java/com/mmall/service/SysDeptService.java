package com.mmall.service;

import com.mmall.param.DeptParam;

/**
 * Created by xuning on 2018/5/7.
 */
public interface SysDeptService {

    void save(DeptParam deptParam);

    void update(DeptParam deptParam);

    void delete(int deptId);
}
