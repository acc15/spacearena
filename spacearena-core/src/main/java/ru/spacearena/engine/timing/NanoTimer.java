package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public class NanoTimer extends AbstractTimer {
    @Override
    public long getCurrentTime() {
        return System.nanoTime();
    }

    @Override
    public float toSeconds(long time) {
        return (float)time/1000000000f;
    }
}
