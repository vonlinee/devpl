package org.greenrobot.eventbus.extension;// PENDING_DOC_REVIEW

import java.util.EventListener;

/**
 * Handler for events of a specific class / type.
 */
@FunctionalInterface
public interface EventHandler<E, R> extends EventListener {
    /**
     * Invoked when a specific event of the type for which this handler is
     * registered happens.
     * @param event the event which occurred
     */
    R handle(E event);
}