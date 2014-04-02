package ru.spacearena.engine;

import ru.spacearena.engine.events.EngineEvent;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

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
    private float secondsPerFrame = 0;
    private float compensationTime = 0f;

    private final Timer timer = new NanoTimer();

    private final ConcurrentLinkedQueue<EngineEvent> pendingEvents = new ConcurrentLinkedQueue<EngineEvent>();

    protected Engine(EngineFactory factory) {
        this.factory = factory;
    }

    protected void init() {
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
        if (secondsPerFrame <= 0) {
            return secondsElapsed;
        }

        compensationTime += secondsElapsed - secondsPerFrame;
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
    }

    public void onDraw(DrawContext context) {
        root.onDraw(context);
    }

    public boolean onInput(InputEvent event) {
        root.onInput(event);
        return true;
    }

    public void scheduleEvent(EngineEvent event) {
        pendingEvents.add(event);
    }

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

    public float getSecondsPerFrame() {
        return secondsPerFrame;
    }

    public void setSecondsPerFrame(float secondsPerFrame) {
        this.secondsPerFrame = secondsPerFrame;
    }

    public float getMaxFPS() {
        return secondsPerFrame > 0 ? 1/ secondsPerFrame : 0;
    }

    public void setMaxFPS(float maxFPS) {
        this.secondsPerFrame = maxFPS > 0 ? 1/maxFPS : 0;
    }

    public Timer getTimer() {
        return timer;
    }

    public Debug getDebug() {
        return debug;
    }

}
