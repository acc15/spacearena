package ru.spacearena.engine;

import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.input.InputEvent;
import ru.spacearena.engine.input.InputType;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public abstract class Engine {

    private long lastTime = -1;
    protected EngineObject root;
    protected final EngineFactory factory;

    protected Engine(EngineFactory factory) {
        this.factory = factory;
    }

    protected void init() {
        this.root = factory.createRoot(this);
        this.root.onInit(this);
    }

    public boolean onUpdate() {
        final long currentTime = currentTime();
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

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public abstract Matrix createMatrix();
    public abstract Image loadImage(String resource);
    public abstract void enableInput(InputType inputType);
    public abstract float getWidth();
    public abstract float getHeight();

}
