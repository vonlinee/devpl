package org.greenrobot.eventbus.extension;

import org.greenrobot.eventbus.Subscription;
import org.greenrobot.eventbus.SubscriptionMatcher;

public class EventNameSubscriptionMatcher implements SubscriptionMatcher {

    private final String eventName;

    public EventNameSubscriptionMatcher(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public boolean matches(Object subscriber, Subscription subscription) {
        return false;
    }
}
