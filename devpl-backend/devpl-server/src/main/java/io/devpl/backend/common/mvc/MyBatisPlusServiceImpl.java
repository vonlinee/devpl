package io.devpl.backend.common.mvc;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 基础服务类，所有Service都要继承
 */
public abstract class MyBatisPlusServiceImpl<M extends MyBatisPlusMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

}
