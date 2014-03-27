package ru.spacearena.engine;

import ru.spacearena.engine.events.EngineEvent;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public abstract class Engine {

    private static final float MAX_COMPENSATION_TIME = 0.5f;

    private final Debug debug = new Debug();
    private final EngineFactory factory;

    private final HashMap<String, Image> imageCache = new HashMap<String, Image>();

    private EngineEntity root;
    private float width, height;
    private float deltaTime = 0;
    private float compensationTime = 0f;

    private final Timer timer = new NanoTimer();

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

    private float pauseBeforeNextFrame() {
        if (!timer.isStarted()) {
            timer.start();
            return 0;
        }

        final float secondsElapsed = timer.reset();
        if (deltaTime <= 0) {
            return secondsElapsed;
        }

        compensationTime += secondsElapsed - deltaTime;
        if (compensationTime > MAX_COMPENSATION_TIME) {
            compensationTime = MAX_COMPENSATION_TIME;
        }

        if (compensationTime >= 0f) {
            return secondsElapsed;
        }

        final long timeToSleep = (long)(-compensationTime * 1000);
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final float realSleepTime = timer.reset();
        compensationTime += realSleepTime;
        return secondsElapsed + realSleepTime;
    }

    public boolean onUpdate() {
        final float secondsElapsed = pauseBeforeNextFrame();
        EngineEvent inputEvent;
        while ((inputEvent = pendingEvents.poll()) != null) {
            inputEvent.run(this);
        }
        return root.onUpdate(secondsElapsed);
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

    public Image getImage(String resource) {
        Image img = imageCache.get(resource);
        if (img == null) {
            img = loadImage(resource);
            imageCache.put(resource, img);
        }
        return img;
    }

    public abstract Matrix createMatrix();

    public abstract Image createImage(int width, int height);

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

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public float getMaxFPS() {
        return deltaTime > 0 ? 1/deltaTime : 0;
    }

    public void setMaxFPS(float maxFPS) {
        this.deltaTime = maxFPS > 0 ? 1/maxFPS : 0;
    }

    public Timer getTimer() {
        return timer;
    }

    public Debug getDebug() {
        return debug;
    }

    protected abstract Image loadImage(String resource);

}
