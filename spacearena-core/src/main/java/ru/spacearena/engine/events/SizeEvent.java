package ru.spacearena.engine.events;

import ru.spacearena.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class SizeEvent implements EngineEvent {

    private float width, height;

    public SizeEvent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void run(Engine engine) {
        engine.onSize(width, height);
    }
}
