package ru.spacearena.engine;

import ru.spacearena.engine.events.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class EngineObject implements EngineEntity {

    public void onAttach(Engine engine) {
    }

    public void onDetach(Engine engine) {
    }

    public void onSize(float width, float height) {
    }

    /**
     * Called when some input event occurs
     * @param inputEvent input event
     * @return {@code true} if event was consumed
     */
    public boolean onInput(InputEvent inputEvent) {
        return false;
    }

    public boolean onUpdate(float seconds) {
        return true;
    }

    public void onDraw(DrawContext context) {
    }

}
