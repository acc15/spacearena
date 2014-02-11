package ru.spacearena.engine.common;

import ru.spacearena.engine.handlers.UpdateHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FPSHandler implements UpdateHandler {

    private float fps = 0f;

    public boolean onUpdate(float seconds) {
        fps = 1/seconds;
        return true;
    }
}
