package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;
import ru.spacearena.android.engine.util.FloatMathUtils;

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
        if (!FloatMathUtils.isZero(seconds)) {
            final float alpha = 0.6f;
            fps = fps * (1-alpha) + alpha/seconds;
        }
        return true;
    }
}
