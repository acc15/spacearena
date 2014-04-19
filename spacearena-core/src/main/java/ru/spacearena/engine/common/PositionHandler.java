package ru.spacearena.engine.common;

import ru.spacearena.engine.EngineObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class PositionHandler extends EngineObject {

    private Transform<?> source;
    private Transform<?> target;

    public PositionHandler(Transform<?> source, Transform<?> target) {
        this.source = source;
        this.target = target;
    }

    @Override
    public void onUpdate(float seconds) {
        target.setPosition(source.getPositionX(), source.getPositionY());
    }
}
