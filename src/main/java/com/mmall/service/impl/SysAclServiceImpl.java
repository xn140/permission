package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysAclMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysAcl;
import com.mmall.param.AclParam;
import com.mmall.service.SysAclService;
import com.mmall.service.SysLogService;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuning on 2018/6/4.
 */
@Service
public class SysAclServiceImpl implements SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;
    public void save(AclParam param) {
        BeanValidator.check(param);

        //验证同一模块下权限点的名称是否相同
        if (chekExist(param.getAclModuleId(),param.getName(),null)){

            throw new ParamException("同一权限模块下存在相同的权限点名称");
        }
        //拼接实体类
        SysAcl acl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null,acl);

    }

    public void update(AclParam param) {
        BeanValidator.check(param);
        if (chekExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "待更新的权限点不存在");

        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);

        sysLogService.saveAclLog(before,after);

    }

    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page) {

        BeanValidator.check(page);
        int count=sysAclMapper.countByAclModuleId(aclModuleId);
        if(count>0){

            List<SysAcl> list = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
            PageResult pageResult=PageResult.<SysAcl>builder().data(list).total(count).build();
            return pageResult;
        }
        return PageResult.<SysAcl>builder().build();
    }

    private boolean chekExist(int aclModuleId,String name,Integer id){

          return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }

    private String generateCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
    }



}
