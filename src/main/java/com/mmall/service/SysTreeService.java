package com.mmall.service;

import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;

import java.util.List;

/**
 * Created by xuning on 2018/5/12.
 */
public interface SysTreeService {

    List<DeptLevelDto> deptTree();

    List<AclModuleLevelDto> aclTree();

    List<AclModuleLevelDto> roleTree(int roleId);
    List<AclModuleLevelDto> userAclTree(int userId);
}
