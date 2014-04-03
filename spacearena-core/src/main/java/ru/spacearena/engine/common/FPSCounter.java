package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FPSCounter extends EngineObject {

    private int frameCount = 0;
    private float totalTime = 0f;

    private float fps = 0f;

    public int getFrameCount() {
        return frameCount;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public float getFps() {
        return fps;
    }

    protected boolean computeFPS(float seconds) {
        ++frameCount;
        totalTime += seconds;
        if (totalTime <= 1f) {
            return false;
        }

        final float fullSeconds = FloatMathUtils.floor(totalTime);
        fps = (float)frameCount / fullSeconds;
        totalTime -= fullSeconds;
        frameCount = 0;
        return true;
    }

    public boolean onUpdate(float seconds) {
        computeFPS(seconds);
        return true;
    }
}
