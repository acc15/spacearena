package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public abstract class AbstractTimer implements Timer {

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
        final long ct = getCurrentTime();
        if (!started) {
            time = ct;
            started = true;
            return 0f;
        } else {
            final long diff = ct - time;
            time = ct;
            return toSeconds(diff);
        }
    }

    public long getTimestamp() {
        return time;
    }

    public float getElapsedSeconds() {
        return toSeconds(getCurrentTime() - time);
    }
}
