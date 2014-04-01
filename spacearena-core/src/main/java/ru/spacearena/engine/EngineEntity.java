package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-20-02
 */
public interface EngineEntity {
    void onAttach(Engine engine);

    void onDetach(Engine engine);

    void onSize(float width, float height);

    boolean onInput(InputEvent inputEvent);

    boolean onUpdate(float seconds);

    void onDraw(GLDrawContext context);
}
