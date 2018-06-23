package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.beans.Mail;
import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysUserMapper;
import com.mmall.exception.ParamException;
import com.mmall.model.SysUser;
import com.mmall.param.UserParam;
import com.mmall.service.SysLogService;
import com.mmall.service.UserService;
import com.mmall.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xuning on 2018/5/16.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogService sysLogService;
    @Transactional
    public void save(UserParam userParam) {
        //检验参数
        BeanValidator.check(userParam);
        //校验邮箱是否重复
        if(checkMail(userParam.getMail(),userParam.getId())){
            log.error("邮箱已经被占用");
           throw new ParamException("邮箱已经占用");
        }
        //校验手机号是否重复
        if(checkTelephone(userParam.getTelephone(),userParam.getId())){
            log.error("手机号已经被占用");
            throw new ParamException("手机号已经被占用");
        }

        //TODO 密码生成工具类
        String passward= PasswordUtil.randomPassword();

         String encryptPassword= MD5Util.encrypt(passward);
        //构建用户对象
        SysUser user=SysUser.builder().username(userParam.getUsername()).telephone(userParam.getTelephone())
                                      .mail(userParam.getMail()).deptId(userParam.getDeptId()).password(encryptPassword).status(userParam.getStatus())
                                      .remark(userParam.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        //用户信息生成之后，需要邮件通知用户
        Set<String> reciver=new HashSet<String>();
        reciver.add(user.getMail());
        Mail mail=Mail.builder().subject("邮件通知").message(user.getUsername()+"您初始密码"+passward).receivers(reciver).build();
        MailUtil.send(mail);
        //插入数据
        sysUserMapper.insertSelective(user);

        sysLogService.saveUserLog(null,user);
    }
   @Transactional
    public void update(UserParam userParam){
      BeanValidator.check(userParam);
        //校验邮箱是否重复
        if(checkMail(userParam.getMail(),userParam.getId())){
            log.error("邮箱已经被占用");
            throw new ParamException("邮箱已经占用");
        }
        //校验手机号是否重复
        if(checkTelephone(userParam.getTelephone(),userParam.getId())){
            log.error("手机号已经被占用");
            throw new ParamException("手机号已经被占用");
        }
        //查询更新前用户信息
        SysUser before=sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(before,"更新用户不存在");
        SysUser after=SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).telephone(userParam.getTelephone())
                .mail(userParam.getMail()).deptId(userParam.getDeptId()).status(userParam.getStatus())
                .remark(userParam.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);

        sysLogService.saveUserLog(before,after);

    }

    public SysUser findByKeyWord(String keyWord) {
        return sysUserMapper.findByKeyWord(keyWord);
    }

    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery) {

        BeanValidator.check(pageQuery);

        int count=sysUserMapper.countByDeptId(deptId);

        if(count>0){
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, pageQuery);

            PageResult result=PageResult.<SysUser>builder().total(count).data(list).build();
            return result;
        }
        return PageResult.<SysUser>builder().build();
    }

    private boolean checkMail(String mail,Integer userId){
        return sysUserMapper.countByMail(mail,userId)>0;
    }

    private  boolean checkTelephone(String telephone,Integer userId){
        return sysUserMapper.countByTelephone(telephone,userId)>0;
    }


    public List<SysUser> getAll(){

        return sysUserMapper.getAll();
    }


}
