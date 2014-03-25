package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public class MilliTimer extends AbstractTimer {
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
    public float toSeconds(long time) {
        return (float)time/1000f;
    }
}
