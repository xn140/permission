package com.mmall.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by xuning on 2018/5/7.
 */
@Data
public class DeptParam {

    private Integer id;
    @NotBlank(message = "部门名称不能为空")
    @Length(max=15,min = 2,message = "部门名称长度在2-15")
    private String name;
    private Integer parentId=0;
    @NotNull(message = "展示顺序不可以为空")
    private Integer seq;
    @Length(max=150,message = "备注的长度需要在150以内")
    private String remark;
}
