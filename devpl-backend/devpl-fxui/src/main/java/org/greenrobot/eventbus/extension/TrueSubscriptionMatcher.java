package org.greenrobot.eventbus.extension;

import org.greenrobot.eventbus.Subscription;
import org.greenrobot.eventbus.SubscriptionMatcher;

public class TrueSubscriptionMatcher implements SubscriptionMatcher {

    public static final TrueSubscriptionMatcher INSTANCE = new TrueSubscriptionMatcher();

    @Override
    public boolean matches(Object subscriber, Subscription subscription) {
        return true;
    }
}
