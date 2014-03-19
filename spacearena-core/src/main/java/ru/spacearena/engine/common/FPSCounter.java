package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FPSCounter extends EngineObject {

    private int frameCount = 0;
    private float totalTime = 0f;

    public float getFps() {
        return (float)frameCount/totalTime;
    }

    public boolean onUpdate(float seconds) {
        ++frameCount;
        totalTime += seconds;
        return true;
    }
}
