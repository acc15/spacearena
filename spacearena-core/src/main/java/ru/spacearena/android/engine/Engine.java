package ru.spacearena.android.engine;

import ru.spacearena.android.engine.graphics.DrawContext;
import ru.spacearena.android.engine.input.InputEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;
    private EngineObject root;

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
    }

    public void onInit(EngineFactory factory) {
        root.onInit(factory);
    }

    public boolean onUpdate() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime < 0) {
            lastTime = currentTime;
        }
        final float seconds = (float)(currentTime-lastTime)/1000;
        lastTime = currentTime;
        return root.onUpdate(seconds);
    }

    public void onSize(float width, float height) {
        root.onSize(width, height);
    }

    public void onDraw(DrawContext context) {
        EngineObject.drawObject(context, root);
    }

    public boolean onInput(InputEvent event) {
        return root.onInput(event);
    }

}
