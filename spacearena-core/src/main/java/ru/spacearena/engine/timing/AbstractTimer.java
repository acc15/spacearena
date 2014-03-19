package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public abstract class AbstractTimer implements Timer {

    public abstract long getCurrentTime();
    public abstract float toSeconds(long time);

    private boolean started = false;
    private long time = 0;

    public boolean isStarted() {
        return started;
    }

    public void start() {
        started = true;
        time = getCurrentTime();
    }

    public void stop() {
        started = false;
    }

    public float reset() {
        if (!started) {
            throw new IllegalStateException("Timer not started");
        }
        final long ct = getCurrentTime();
        final long diff = ct - time;
        time = ct;
        return toSeconds(diff);
    }

    public float getElapsedSeconds() {
        return toSeconds(getCurrentTime() - time);
    }
}
