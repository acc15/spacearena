package ru.spacearena.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.events.EngineEvent;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.events.InputEvent;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public final class Engine {

    private static final float MAX_COMPENSATION_TIME = 0.5f;

    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    private final Debug debug = new Debug();
    private final EngineFactory factory;
    private final InputContext inputContext;
    private final EngineEntity root;
    private DrawContext2f drawContext = null;

    private float secondsPerFrame = 0;
    private float compensationTime = 0f;

    private final Timer timer = new NanoTimer();

    private final ConcurrentLinkedQueue<EngineEvent> pendingEvents = new ConcurrentLinkedQueue<EngineEvent>();

    public Engine(EngineFactory factory,
                  InputContext inputContext) {
        this.factory = factory;
        this.inputContext = inputContext;
        this.root = factory.createRoot(this);
        this.root.onAttach(this);
    }

    public EngineFactory getFactory() {
        return factory;
    }

    public DrawContext2f getDrawContext() { return drawContext; }

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

    public void onUpdate() {
        // probably this isn't right place for sleep
        // because this method is called from Android onDrawFrame and JOGL display
        // OpenGL context should be already initialized and update call should be able to manipulate OpenGL objects
        // (for example - load texture or upload VBO)
        final float secondsElapsed = pauseBeforeNextFrameIfNeeded();
        EngineEvent inputEvent;
        while ((inputEvent = pendingEvents.poll()) != null) {
            inputEvent.run(this);
        }
        root.onUpdate(secondsElapsed);
    }

    public void onSize(float width, float height) {
        root.onSize(width, height);
    }

    public void onDraw() {
        root.onDraw(drawContext);
    }

    public boolean onInput(InputEvent event) {
        root.onInput(event);
        return true;
    }

    public void onInit(DrawContext2f context) {
        if (this.drawContext != null) {
            throw new IllegalArgumentException("Draw context already initialized");
        }
        this.drawContext = context;
        drawContext.init();
        root.onInit(drawContext);
    }

    public void onDispose() {
        root.onDispose(drawContext);
        drawContext.dispose();
        this.drawContext = null;
    }

    public void scheduleEvent(EngineEvent event) {
        pendingEvents.add(event);
    }

    public boolean enableInput(InputType inputType) {
        return inputContext.enableInput(this, inputType);
    }

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
