package io.fxtras.eventbus;

import javafx.application.Platform;
import org.greenrobot.eventbus.AsyncPoster;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.MainThreadSupport;
import org.greenrobot.eventbus.Poster;

public class JavaFXMainThreadSupport implements MainThreadSupport {

    /**
     * 是否是主线程，即FX线程
     * @return 是否主线程
     */
    @Override
    public boolean isMainThread() {
        return Platform.isFxApplicationThread();
    }

    @Override
    public Poster createPoster(EventBus eventBus) {
        return new AsyncPoster(eventBus);
    }
}
