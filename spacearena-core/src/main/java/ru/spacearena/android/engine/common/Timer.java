package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class Timer extends EngineObject {

    private final float timeout;
    private float elapsed;

    public Timer(float timeout) {
        this(timeout, false);
    }

    public Timer(float timeout, boolean callFirst) {
        this.timeout = timeout;
        this.elapsed = callFirst ? timeout : 0;
    }

    public boolean onUpdate(float seconds) {
        elapsed += seconds;
        if (elapsed < timeout) {
            return true;
        }

        final int tickTimes = (int) (elapsed/timeout);
        elapsed = elapsed % timeout;
        for (int i=0; i<tickTimes; i++) {
            if (!super.onUpdate(elapsed)) {
                return false;
            }
            elapsed = 0;
        }
        return true;
    }
}
