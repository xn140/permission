package com.mmall.beans;

import com.google.common.collect.Lists;
import lombok.*;

import java.util.List;

@Data
@Builder
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total = 0;
}
