package org.greenrobot.eventbus;

import java.lang.reflect.InvocationTargetException;

/**
 * 基于内部类接口或者Lambda形式的方法调用的订阅
 */
public class CallbackSubscription<P, R> extends GenericSubscription {

    EventCallback<P, R> eventCallback;

    Class<?> parameterType;
    Class<?> returnType;

    public CallbackSubscription(EventCallback<P, R> callback) {
        if (callback == null) {
            throw new NullPointerException();
        }
        resolveType(this.eventCallback = callback);
    }

    /**
     * 解析lambda表达式的类型
     * @param callback
     */
    private void resolveType(EventCallback<P, R> callback) {
        Class<?>[] types = null;
        if (types == null) {
            throw new RuntimeException("unexpected exception: the type of EventCallback is null");
        }
        if (types.length != 2) {
            throw new RuntimeException("unexpected exception: the type of EventCallback is 2, but found " + types.length);
        }
        parameterType = types[0];
        returnType = types[1];
    }

    /**
     * @param args 事件发布时的参数
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Override
    public Object invokeSubscriber(Object... args) throws InvocationTargetException, IllegalAccessException {
        // 安全调用处理

        // 1.判断发布事件的参数个数
        if (args == null) {

        }

        Object parameter = null;
        if (parameterType.isArray()) {
            parameter = args;
        } else {

        }

        return null;
    }

    @Override
    public ThreadMode getThredMode() {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
