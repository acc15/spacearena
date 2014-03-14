package ru.spacearena.engine;

import ru.spacearena.engine.events.EngineEvent;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.InputType;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public abstract class Engine {

    private final Debug debug = new Debug();
    private final EngineFactory factory;

    private EngineEntity root;
    private float width, height;
    private long lastTime = -1;

    private final ConcurrentLinkedQueue<EngineEvent> pendingEvents = new ConcurrentLinkedQueue<EngineEvent>();

    protected Engine(EngineFactory factory) {
        this.factory = factory;
    }

    protected void init(float width, float height) {
        this.width = width;
        this.height = height;
        this.root = factory.createRoot(this);
        this.root.onInit(this);
    }

    public EngineFactory getFactory() {
        return factory;
    }

    public boolean onUpdate() {
        EngineEvent inputEvent;
        while ((inputEvent = pendingEvents.poll()) != null) {
            inputEvent.run(this);
        }

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
        this.width = width;
        this.height = height;
    }

    public void onDraw(DrawContext context) {
        root.onDraw(context);
    }

    public boolean onInput(InputEvent event) {
        root.onInput(event);
        return true;
    }

    public long currentTime() {
        return System.currentTimeMillis();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void scheduleEvent(EngineEvent event) {
        pendingEvents.add(event);
    }

    public abstract Matrix createMatrix();
    public abstract Image loadImage(String resource);
    public abstract boolean enableInput(InputType inputType);

    public static final class Debug {

        boolean drawVelocities = false;
        boolean drawBounds = false;
        boolean drawConvexShapes = false;

        public boolean isDrawVelocities() {
            return drawVelocities;
        }

        public void setDrawVelocities(boolean drawVelocities) {
            this.drawVelocities = drawVelocities;
        }

        public boolean isDrawBounds() {
            return drawBounds;
        }

        public void setDrawBounds(boolean drawBounds) {
            this.drawBounds = drawBounds;
        }

        public boolean isDrawConvexShapes() {
            return drawConvexShapes;
        }

        public void setDrawConvexShapes(boolean drawConvexShapes) {
            this.drawConvexShapes = drawConvexShapes;
        }

        public void setDrawAll(boolean value) {
            this.drawBounds = value;
            this.drawConvexShapes = value;
            this.drawVelocities = value;
        }
    }

    public Debug getDebug() {
        return debug;
    }
}
