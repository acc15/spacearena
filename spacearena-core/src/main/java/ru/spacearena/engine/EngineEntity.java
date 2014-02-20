package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.input.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public interface EngineEntity {
    void onInit(Engine engine);

    void onSize(float width, float height);

    boolean onInput(InputEvent inputEvent);

    boolean onUpdate(float seconds);

    void onDraw(DrawContext context);
}
