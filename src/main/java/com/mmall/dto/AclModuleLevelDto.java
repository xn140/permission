package com.mmall.dto;

import com.google.common.collect.Lists;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuning on 2018/6/3.
 */
@Data
public class AclModuleLevelDto extends SysAclModule {

    private List<AclModuleLevelDto> aclModuleList=new ArrayList<AclModuleLevelDto>();
    private List<AclDto> aclList= Lists.newArrayList();
    public static AclModuleLevelDto adapt(SysAclModule aclModule){

        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule,dto);
        return dto;
    }
}
