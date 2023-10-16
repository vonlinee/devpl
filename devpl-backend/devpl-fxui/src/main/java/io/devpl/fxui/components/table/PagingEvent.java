package io.devpl.fxui.components.table;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TableColumn;

/**
 * 分页事件
 * @see TableColumn#editCommitEvent()
 */
public class PagingEvent extends Event {

    public static final EventType<PagingEvent> ANY =
            new EventType<>(Event.ANY, "ANY");

    public static final EventType<PagingEvent> CHANGE =
            new EventType<>(ANY, "CHANGE");

    private int pageNum;
    private int pageSize;

    public PagingEvent(EventType<PagingEvent> eventType) {
        super(eventType);
    }

    public PagingEvent(final @NamedArg("source") Object source,
                       final @NamedArg("target") EventTarget target,
                       final @NamedArg("eventType") EventType<PagingEvent> eventType) {
        super(source, target, eventType);
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public static PagingEvent pageChange(int pageNum, int pageSize) {
        PagingEvent event = new PagingEvent(PagingEvent.CHANGE);
        event.pageNum = pageNum;
        event.pageSize = pageSize;
        return event;
    }
}
