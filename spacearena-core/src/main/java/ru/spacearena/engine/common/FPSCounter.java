package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class FPSCounter extends EngineObject {

    private float frameCount = 0;
    private float totalTime = 0f;

    private float fps = 0f;

    public float getFps() {
        return fps;
    }

    public boolean onUpdate(float seconds) {
        ++frameCount;
        totalTime += seconds;
        if (totalTime > 1f) {
            final float fullSeconds = FloatMathUtils.floor(totalTime);
            fps = frameCount / fullSeconds;
            totalTime -= fullSeconds;
            frameCount = 0;
        }
        return true;
    }
}
