package ru.spacearena.engine;

import ru.spacearena.engine.events.EngineEvent;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.timing.MilliTimer;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.FloatMathUtils;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public abstract class Engine {

    private static final float MAX_COMPENSATION_TIME = 0.5f;

    private final Debug debug = new Debug();
    private final EngineFactory factory;

    private EngineEntity root;
    private float width, height;
    private float maxFPS = 10;
    private float compensationTime = 0f;

    private final Timer timer = new MilliTimer();

    private final ConcurrentLinkedQueue<EngineEvent> pendingEvents = new ConcurrentLinkedQueue<EngineEvent>();

    protected Engine(EngineFactory factory) {
        this.factory = factory;
    }

    protected void init(float width, float height) {
        this.width = width;
        this.height = height;
        this.root = factory.createRoot(this);
        this.root.onAttach(this);
    }

    public EngineFactory getFactory() {
        return factory;
    }

    private float pauseBeforeNextFrame(float secondElapsed) {
        if (maxFPS <= 0) {
            return secondElapsed;
        }

        final float secondsPerFrame = 1/maxFPS;
        compensationTime = FloatMathUtils.min(MAX_COMPENSATION_TIME, compensationTime + secondElapsed - secondsPerFrame);
        if (compensationTime >= 0f) {
            return secondElapsed;
        }

        final long timeToSleep = FloatMathUtils.round(-compensationTime * 1000);
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        secondElapsed -= compensationTime;
        compensationTime = 0f;
        return secondElapsed;
    }

    public boolean onUpdate() {
        if (!timer.isStarted()) {
            timer.start();
            return true;
        }

        float secondElapsed = pauseBeforeNextFrame(timer.reset());
        EngineEvent inputEvent;
        while ((inputEvent = pendingEvents.poll()) != null) {
            inputEvent.run(this);
        }
        return root.onUpdate(secondElapsed);
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

    public float getMaxFPS() {
        return maxFPS;
    }

    public void setMaxFPS(float maxFPS) {
        this.maxFPS = maxFPS;
    }

    public Debug getDebug() {
        return debug;
    }
}
