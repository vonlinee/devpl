package org.greenrobot.eventbus;

/**
 * 发布事件: EventBus会特殊处理
 */
public final class PostEvent {

    String eventName;
    // 必填参数
    Object event;
    SubscriptionMatcher subscriptionMatcher;

    public PostEvent(Object event) {
        this.event = event;
    }

    public PostEvent(String eventName, Object event) {
        this.eventName = eventName;
        this.event = event;
    }

    public PostEvent(String eventName, Object event, SubscriptionMatcher matcher) {
        this.eventName = eventName;
        this.event = event;
        this.subscriptionMatcher = matcher;
    }

    public boolean isEventNameMatches(String subscriberEventName) {
        return eventName == null || eventName.equals(subscriberEventName);
    }

    public boolean matches(Object subscriber, Subscription subscription) {
        return subscriptionMatcher == null || subscriptionMatcher.matches(subscriber, subscription);
    }
}
