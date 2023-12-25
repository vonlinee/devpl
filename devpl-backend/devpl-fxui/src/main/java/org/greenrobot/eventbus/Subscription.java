/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.greenrobot.eventbus;

import java.lang.reflect.InvocationTargetException;

/**
 * 订阅单元，以Subscription为基本单位存储每个订阅项目
 */
public interface Subscription {

    /**
     * 修改当前订阅的有效状态
     * @param active
     */
    void setActive(boolean active);

    /**
     * 当前订阅是否有效
     * @return
     */
    boolean isActive();

    @Override
    int hashCode();

    @Override
    boolean equals(Object obj);

    /**
     * 执行订阅者方法
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object invokeSubscriber(Object... args) throws InvocationTargetException, IllegalAccessException;

    /**
     * 获取当前订阅的线程模式
     * @return
     */
    ThreadMode getThredMode();

    /**
     * 获取优先级
     * @return
     */
    int getPriority();
}