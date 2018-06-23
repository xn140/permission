package com.mmall.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.service.SysCoreService;
import com.mmall.service.SysTreeService;
import com.mmall.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xuning on 2018/5/12.
 */
@Service
@Slf4j
public class SysTreeServiceImpl implements SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;
     @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysCoreService sysCoreService;
    @Autowired
    private SysAclMapper sysAclMapper;


    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adpt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclDtoListToTree(aclDtoList);
    }
    @Override
    public List<AclModuleLevelDto> roleTree(int roleId) {
        //当前用户得分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        //当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        //用户的权限封装到一个set中
        Set<Integer> userAclIdSet=userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        //角色的权限封装到一个set中
        Set<Integer> roleAclIdSet=roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
       //获取所有的权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
      //  aclSet.addAll(userAclList);//@EqualsAndHashCode(of = "id")//比较equql和hashcode的时候取决于id  在使用set的addAll时候，相同id的值不会出现多个
        List<AclDto> aclDtoList=Lists.newArrayList();
        for(SysAcl acl:allAclList){

            AclDto aclDto = AclDto.adpt(acl);
            if(userAclIdSet.contains(acl.getId())){
                 aclDto.setChecked(true);
            }
            if(roleAclIdSet.contains(acl.getId())){
                aclDto.setHasAcl(true);
            }
            aclDtoList.add(aclDto);

        }
        return aclDtoListToTree(aclDtoList);
    }

    private List<AclModuleLevelDto> aclDtoListToTree(List<AclDto> aclDtoList){

        if(CollectionUtils.isEmpty(aclDtoList)){
            return Lists.newArrayList();
        }
        List<AclModuleLevelDto> aclModuleLevelList=aclTree();
      Multimap<Integer,AclDto> moduleIdAclMap=ArrayListMultimap.create();
      for(AclDto aclDto:aclDtoList){

          if(aclDto.getStatus()==1){
              moduleIdAclMap.put(aclDto.getAclModuleId(),aclDto);
          }
      }
        bindAclModuleWithOrder(aclModuleLevelList,moduleIdAclMap);
      return aclModuleLevelList;
    }

    private void bindAclModuleWithOrder(List<AclModuleLevelDto> aclModuleLevelList,Multimap<Integer,AclDto> moduleIdAclMap ){
       if(CollectionUtils.isEmpty(aclModuleLevelList)){
           return ;

       }

       for(AclModuleLevelDto aclModuleLevelDto:aclModuleLevelList){

           List<AclDto> aclDtos = (List<AclDto>)moduleIdAclMap.get(aclModuleLevelDto.getId());
           if(CollectionUtils.isNotEmpty(aclDtos)){

               Collections.sort(aclDtos, new Comparator<AclDto>() {
                   @Override
                   public int compare(AclDto o1, AclDto o2) {
                       return o1.getSeq()-o2.getSeq();
                   }
               });

               aclModuleLevelDto.setAclList(aclDtos);

               //递归处理下一层级
               bindAclModuleWithOrder(aclModuleLevelDto.getAclModuleList(),moduleIdAclMap);
           }
       }

    }

    public List<AclModuleLevelDto> aclTree() {
        //获取所有的权限
        List<SysAclModule> aclModuleList=sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList=new ArrayList<AclModuleLevelDto>();

        for(SysAclModule aclModule:aclModuleList){

            AclModuleLevelDto dto = AclModuleLevelDto.adapt(aclModule);
            dtoList.add(dto);
        }
        return aclModuleListToTree(dtoList);
    }



    //递归生成权限树
    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList){
         if(CollectionUtils.isEmpty(dtoList)){

             return Lists.newArrayList();
         }

         Multimap<String,AclModuleLevelDto> levelAclModuleMap=ArrayListMultimap.create();
          List<AclModuleLevelDto> rootList=Lists.newArrayList();
         for(AclModuleLevelDto aclModuleLevelDto:dtoList){
             levelAclModuleMap.put(aclModuleLevelDto.getLevel(),aclModuleLevelDto);
                 if(LevelUtil.ROOT.equals(aclModuleLevelDto.getLevel())){
                     rootList.add(aclModuleLevelDto);

                 }
         }

         Collections.sort(rootList,aclModuleSeqComparator);
         transforAclModuleTree(dtoList,LevelUtil.ROOT,levelAclModuleMap);

         return rootList;
    }


    public void transforAclModuleTree(List<AclModuleLevelDto> dtoList,String level,Multimap<String,AclModuleLevelDto> aclModuleLevelDtoMultimap){

        for(int i=0;i<dtoList.size();i++){

            //获取下一层的level
            String nextLevel=LevelUtil.calculateLevel(level,dtoList.get(i).getId());
            //取出对应的权限模块列表
            List<AclModuleLevelDto> aclList=(List<AclModuleLevelDto>)aclModuleLevelDtoMultimap.get(nextLevel);

            if(CollectionUtils.isNotEmpty(aclList)){
                Collections.sort(aclList,aclModuleSeqComparator);
                dtoList.get(i).setAclModuleList(aclList);
                transforAclModuleTree(aclList,nextLevel,aclModuleLevelDtoMultimap);
            }
        }

    }
    //部门树
    public List<DeptLevelDto> deptTree() {
        //获取所有部门
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList= Lists.newArrayList();
        for(SysDept dept: deptList){
            //将dept转换成deptleveldto,
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }

        return deptListToTree(dtoList);
    }


    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelDtoList){
       //判断
        if(deptLevelDtoList.isEmpty()){
            return Lists.newArrayList();

        }
      //新的数据结构，以level为key,deptLevelDto的列表为value如：level  ->[deptLevelDto1,deptLevelDto2]
        Multimap<String,DeptLevelDto> levelDeptMap= ArrayListMultimap.create();

        //根节点部门
        List<DeptLevelDto> roolList=Lists.newArrayList();
        for(DeptLevelDto dto:deptLevelDtoList){
            //据说可以把level相同的数据自动封装到一个集合中，对应了Mutimap的数据结构
            levelDeptMap.put(dto.getLevel(),dto);
            //根节点数据
            if(LevelUtil.ROOT.equals(dto.getLevel())){
                roolList.add(dto);
            }

          //对根节点部门进行排序,.sort方法默认是升序的
            Collections.sort(roolList,deptSeqComparator);
        }
        //递归生成树
        transformDeptTree(roolList,LevelUtil.ROOT,levelDeptMap);
        return roolList;
    }

   //
    public void transformDeptTree(List<DeptLevelDto> deptLevelList,String level,Multimap<String,DeptLevelDto> deptLevelMap){

        for(int i=0;i<deptLevelList.size();i++){
            //取出该层的每一个元素
            DeptLevelDto deptLevelDto=deptLevelList.get(i);
            //计算下一层级
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            //处理下一层//从map中取出下一层及的数据
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>)deptLevelMap.get(nextLevel);

            if(CollectionUtils.isNotEmpty(tempDeptList)){
               //排序
                Collections.sort(tempDeptList,deptSeqComparator);
                //设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                //进入到下一层处理数据
                 transformDeptTree(tempDeptList,nextLevel,deptLevelMap);
            }

        }
    }

    //比较器
    private Comparator<DeptLevelDto> deptSeqComparator=new Comparator<DeptLevelDto>() {
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };

    private Comparator<AclModuleLevelDto> aclModuleSeqComparator=new Comparator<AclModuleLevelDto>() {
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq()-o2.getSeq();
        }
    };
}
