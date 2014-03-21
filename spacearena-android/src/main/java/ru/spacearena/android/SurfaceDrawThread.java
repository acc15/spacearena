package ru.spacearena.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.android.engine.AndroidDrawContext;
import ru.spacearena.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class SurfaceDrawThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SurfaceDrawThread.class);

    final Engine engine;
    final SurfaceHolder surfaceHolder;
    private final AndroidDrawContext drawContext = new AndroidDrawContext();

    private Thread thread = null;
    private volatile boolean running = false;

    public SurfaceDrawThread(SurfaceHolder surfaceHolder, Engine engine) {
        this.surfaceHolder = surfaceHolder;
        this.engine = engine;
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    public synchronized void stop() {
        this.running = false;
        while (true) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
            break;
        }
        thread = null;
    }

    public synchronized void start() {
        if (this.running) {
            throw new IllegalStateException("Draw thread already running");
        }
        this.running = true;
        thread = new Thread(this);
        thread.start();
    }

    private boolean onFrame() {
        if (!engine.onUpdate()) {
            return false;
        }

        final Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            logger.info("Canvas can't be locked. Waiting 100ms...");
            if (!running) {
                return false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Canvas wait interrupted");
            }
            return true;
        }
        try {
            engine.onDraw(drawContext.wrap(canvas));
        } finally {
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        return true;
    }

    public void run() {
        // ensures that timer will start with new time
        engine.getTimer().stop();
        while (running) {
            //Debug.startMethodTracing("spacearena-dt");
            if (!onFrame()) {
                //Debug.stopMethodTracing();
                break;
            }
            //Debug.stopMethodTracing();
        }
        logger.info("Drawing thread finished");
    }

}
