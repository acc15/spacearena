package ru.spacearena.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    private final Debug debug = new Debug();
    private final EngineFactory factory;
    private final DrawContext context;
    private EngineEntity root;

    private float secondsPerFrame = 0;
    private float compensationTime = 0f;

    private final Timer timer = new NanoTimer();

    private final ConcurrentLinkedQueue<EngineEvent> pendingEvents = new ConcurrentLinkedQueue<EngineEvent>();

    protected Engine(EngineFactory factory, DrawContext context) {
        this.factory = factory;
        this.context = context;
    }

    protected void init() {
        this.root = factory.createRoot(this);
    }

    public EngineFactory getFactory() {
        return factory;
    }

    public DrawContext getContext() { return context; }

    private float pauseBeforeNextFrameIfNeeded() {
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
            logger.error("Frame sleep interrupted", e);
        }

        final float realSleepTime = timer.reset();
        compensationTime += realSleepTime;
        return secondsElapsed + realSleepTime;
    }

    public boolean onUpdate() {
        // probably this isn't right place for sleep
        // because this method is called from Android onDrawFrame and JOGL display
        // OpenGL context should be already initialized and update call should be able to manipulate OpenGL objects
        // (for example - load texture or upload VBO)
        final float secondsElapsed = pauseBeforeNextFrameIfNeeded();
        EngineEvent inputEvent;
        while ((inputEvent = pendingEvents.poll()) != null) {
            inputEvent.run(this);
        }
        return root.onUpdate(secondsElapsed);
    }

    public void onSize(float width, float height) {
        root.onSize(width, height);
    }

    public void onDraw() {
        root.onDraw(context);
    }

    public boolean onInput(InputEvent event) {
        root.onInput(event);
        return true;
    }

    public void onInit() {
        context.init();
        root.onAttach(this);
    }

    public void onDispose() {
        root.onDetach(this);
        context.dispose();
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
