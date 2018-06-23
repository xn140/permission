package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.beans.LogType;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysLogMapper;
import com.mmall.dao.SysRoleAclMapper;
import com.mmall.model.SysLogWithBLOBs;
import com.mmall.model.SysRoleAcl;
import com.mmall.service.SysRoleAclService;
import com.mmall.util.IpUtil;
import com.mmall.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xuning on 2018/6/9.
 */
@Service
public class SysRoleAclServiceImpl implements SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysLogMapper sysLogMapper;
    @Override
    public void changeRoleAcls(int roleId, List<Integer> aclIdList) {
        //该角色当前的权限
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if(originAclIdList.size()==aclIdList.size()){

            Set<Integer> originAclIdSet = Sets.<Integer>newHashSet(originAclIdList);
            Set<Integer> aclIdSet=Sets.newHashSet(aclIdList);
            //原来的权限，除去新分配的权限。如果除完之后为空，说明权限分配前后都一样
            originAclIdSet.removeAll(aclIdSet);

            if(CollectionUtils.isEmpty(originAclIdSet)){

                return ;
            }

        }
        updateRoleAcls(roleId, aclIdList);

        saveRoleAclLog(roleId, originAclIdList, aclIdList);

    }
    @Transactional
    private void updateRoleAcls(int roleId,List<Integer> aclIdList){
        //删除之前的分配的权限
        sysRoleAclMapper.deleteByRoleId(roleId);
        if(CollectionUtils.isEmpty(aclIdList)){
            return;

        }
        List<SysRoleAcl>  roleAclList=Lists.newArrayList();
        for(Integer aclId:aclIdList){

            SysRoleAcl roleAcl=SysRoleAcl.builder().roleId(roleId).aclId(aclId)
                                         .operator(RequestHolder.getCurrentUser().getUsername())
                                         .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                                         .operateTime(new Date()).build();
             roleAclList.add(roleAcl);
        }

        //批量新增
        sysRoleAclMapper.batchInsert(roleAclList);

    }



    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
