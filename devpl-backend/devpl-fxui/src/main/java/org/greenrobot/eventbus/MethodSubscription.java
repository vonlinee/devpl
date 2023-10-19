package org.greenrobot.eventbus;

import java.lang.reflect.InvocationTargetException;

/**
 * 方法订阅:通过反射进行调用
 */
class MethodSubscription extends GenericSubscription {

    final Object subscriber;
    final SubscriberMethod subscriberMethod;

    MethodSubscription(Object subscriber, SubscriberMethod subscriberMethod) {
        this.subscriber = subscriber;
        this.subscriberMethod = subscriberMethod;
        active = true;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MethodSubscription) {
            MethodSubscription otherSubscription = (MethodSubscription) other;
            return subscriber == otherSubscription.subscriber && subscriberMethod.equals(otherSubscription.subscriberMethod);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return subscriber.hashCode() + subscriberMethod.methodString.hashCode();
    }

    @Override
    public Object invokeSubscriber(Object... args) throws InvocationTargetException, IllegalAccessException {
        return subscriberMethod.invoke(subscriber, args);
    }

    @Override
    public ThreadMode getThredMode() {
        return subscriberMethod.threadMode;
    }

    @Override
    public int getPriority() {
        return subscriberMethod.priority;
    }
}
