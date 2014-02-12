package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FPSCounter extends EngineObject {

    private float fps = 0f;

    public float getFps() {
        return fps;
    }

    public boolean onUpdate(float seconds) {
        fps = 1/seconds;
        return true;
    }
}
