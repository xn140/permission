package com.mmall.dao;

import com.mmall.beans.PageQuery;
import com.mmall.beans.PageResult;
import com.mmall.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
    //使用邮箱和手机号验证登录，具有唯一性
    SysUser findByKeyWord(String keyWord);
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    int countByDeptId(int deptId);
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("pageQuery")PageQuery pageQuery);

    List<SysUser> getByIdList(@Param("IdList") List<Integer> IdList);
    List<SysUser> getAll();
}