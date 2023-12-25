package org.greenrobot.eventbus;

import org.greenrobot.eventbus.extension.TrueSubscriptionMatcher;

@FunctionalInterface
public interface SubscriptionMatcher {

    boolean matches(Object subscriber, Subscription subscription);

    SubscriptionMatcher TRUE = TrueSubscriptionMatcher.INSTANCE;
}
