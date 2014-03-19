package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public class MilliTimer implements Timer {

    private long lastTime;
    private boolean started = false;

    public boolean isStarted() {
        return started;
    }

    public void start() {
        started = true;
        lastTime = System.currentTimeMillis();
    }

    public void stop() {
        started = false;
    }

    public float reset() {
        final long currentTime = System.currentTimeMillis();
        final float amount = (currentTime - lastTime)/1000f;
        lastTime = currentTime;
        return amount;
    }
}
