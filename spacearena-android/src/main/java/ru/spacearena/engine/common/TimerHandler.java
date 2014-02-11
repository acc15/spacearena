package ru.spacearena.engine.common;

import ru.spacearena.engine.handlers.UpdateHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class TimerHandler implements UpdateHandler {

    private final float timeout;
    private UpdateHandler updateHandler;

    private float elapsed = 0;

    public TimerHandler(float timeout, UpdateHandler updateHandler) {
        this.timeout = timeout;
        this.updateHandler = updateHandler;
    }

    public boolean onUpdate(float seconds) {
        elapsed += seconds;
        final int tickTimes = (int) (elapsed/timeout);
        for (int i=0; i<tickTimes; i++) {
            if (!updateHandler.onUpdate(elapsed)) {
                return false;
            }
            elapsed = 0;
        }
        return true;
    }
}
