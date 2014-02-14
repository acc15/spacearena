package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.input.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-11-02
 */
public class EngineObject {

    public void onInit(Engine engine) {
    }

    public void onSize(float width, float height) {
    }

    public boolean onInput(InputEvent inputEvent) {
        return false;
    }

    public boolean onUpdate(float seconds) {
        return true;
    }

    public boolean onPreDraw(DrawContext context) {
        return true;
    }

    public void onPostDraw(DrawContext context) {
    }

    public void onDraw(DrawContext context) {
    }

    protected static void drawObject(DrawContext context, EngineObject child) {
        if (!child.onPreDraw(context)) {
            return;
        }
        try {
            child.onDraw(context);
        } finally {
            child.onPostDraw(context);
        }
    }

}