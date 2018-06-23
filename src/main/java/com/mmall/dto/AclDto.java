package com.mmall.dto;

import com.mmall.model.SysAcl;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Created by xuning on 2018/6/7.
 */
@Data
public class AclDto extends SysAcl {
     //是否选中
    private boolean checked=false;
    //是否有权限
    private boolean hasAcl=false;

    public static AclDto adpt(SysAcl acl){
        AclDto aclDto = new AclDto();
        BeanUtils.copyProperties(acl,aclDto);
        return aclDto;
    }

}
