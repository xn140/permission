package com.mmall.service.impl;

import com.google.common.base.Preconditions;
import com.mmall.common.RequestHolder;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.dto.DeptLevelDto;
import com.mmall.exception.ParamException;
import com.mmall.model.SysDept;
import com.mmall.param.DeptParam;
import com.mmall.service.SysDeptService;
import com.mmall.service.SysLogService;
import com.mmall.util.BeanValidator;
import com.mmall.util.IpUtil;
import com.mmall.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by xuning on 2018/5/7.
 */
@Slf4j
@Service
public class SysDeptServiceImpl implements SysDeptService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysLogService sysLogService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Transactional
    public void save(DeptParam deptParam){
        //验证参数
        BeanValidator.check(deptParam);
        //验证同一层级下的部门名称是否重复
        if(checkExit(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
                throw new ParamException("同一层级下存在相同的部门名称");
        }
        //拼接实体类
        SysDept sysDept=SysDept.builder().name(deptParam.getName()).parentId(deptParam.getParentId())
                               .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();

        //获取level
        sysDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        sysDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysDept.setOperateTime(new Date());

        sysDeptMapper.insertSelective(sysDept);

        sysLogService.saveDeptLog(null,sysDept);

    }

    //更新部门
    @Transactional
    public void update(DeptParam deptParam){
        //验证参数
        BeanValidator.check(deptParam);

        //验证统一层级下的部门名称是否重复
        if(checkExit(deptParam.getParentId(),deptParam.getName(),deptParam.getId())){
            throw new ParamException("同一层级下部门名称相同");
        }
       //查询原始部门信息
        SysDept before = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        //验证原始部门是否存在
        Preconditions.checkNotNull(before,"待更新部门不存在");
        //更新之后的数据
        SysDept after=SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).parentId(deptParam.getParentId())
                .seq(deptParam.getSeq()).remark(deptParam.getRemark()).build();
        //补充数据
        //获取level
        after.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()),deptParam.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        //处理数据
        updateWithChild(before,after);

        sysLogService.saveDeptLog(before,after);
    }

    @Override
    public void delete(int deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(sysDept,"待删除的部门不存在，无法删除");

        if(sysDeptMapper.countByParentId(sysDept.getId())>0){

            throw new ParamException("代删除部门下有，子部门，无法删除");

        }

        if(sysUserMapper.countByDeptId(sysDept.getId())>0){

            throw new ParamException("代删除部门下有用户，无法删除");
        }

       sysDeptMapper.deleteByPrimaryKey(deptId);


    }

    @Transactional
    private void updateWithChild(SysDept before,SysDept after ){
       //取出新的level
        String newLevelPrefix=after.getLevel();
        String oldLevelPrefix=before.getLevel();
        //如果新部门的和原始部门的数据不一致，就说明需要更新，子级下的level
        if(!after.getLevel().equals(before.getLevel())){
          //取出当前部门的子部门
          List<SysDept> deptList=sysDeptMapper.getChildDeptListByLevel(oldLevelPrefix);
          //子部门不为空，需要处理
            if(CollectionUtils.isNotEmpty(deptList)){
                for(SysDept dept:deptList){
                   //获取子级数据的level
                    String level=dept.getLevel();
                    //如果是一oldLevelPrefix开头，需要修改
                    if(level.indexOf(oldLevelPrefix)==0){
                        //计算新的level
                         level=newLevelPrefix+level.substring(oldLevelPrefix.length());
                         dept.setLevel(level);
                    }
                }

                //批量更新数据
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        //跟新数据
        sysDeptMapper.updateByPrimaryKey(after);

    }
    private boolean checkExit(Integer parentId,String deptName,Integer deptId){

        return sysDeptMapper.countByNameAndParentId(parentId,deptName,deptId)>0;
    }

    private String getLevel(Integer deptId){
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        if(sysDept==null){
            return null;
        }
        return sysDept.getLevel();
    }
}
