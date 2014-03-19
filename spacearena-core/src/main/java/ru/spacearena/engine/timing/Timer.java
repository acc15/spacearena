package ru.spacearena.engine.timing;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-03
 */
public interface Timer {
    boolean isStarted();
    void start();
    void stop();
    float reset();
}
