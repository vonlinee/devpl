package io.devpl.generator.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.page.PageResult;

public class BusinessUtils {

    public static <T> PageResult<T> page2List(IPage<T> page) {
        return new PageResult<>(page.getRecords(), page.getTotal());
    }
}
