package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-04
 */
public class Watch extends EngineObject {

    private float time;

    public float getTime() {
        return time;
    }

    @Override
    public boolean onUpdate(float seconds) {
        time += seconds;
        return true;
    }
}
