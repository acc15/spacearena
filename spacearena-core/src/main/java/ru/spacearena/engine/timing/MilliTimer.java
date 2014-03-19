package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public class MilliTimer extends AbstractTimer {
    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    @Override
    public float toSeconds(long time) {
        return (float)time/1000f;
    }
}
