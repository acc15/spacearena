package ru.spacearena.android.engine.common;

import ru.spacearena.android.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class PositionHandler extends EngineObject {

    private Transform source;
    private Transform target;

    public PositionHandler(Transform source, Transform target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public boolean onUpdate(float seconds) {
        target.setPosition(source.x, source.y);
        return true;
    }
}
