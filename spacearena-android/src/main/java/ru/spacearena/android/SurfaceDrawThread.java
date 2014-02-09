package ru.spacearena.android;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.Point2F;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class SurfaceDrawThread implements Runnable, SurfaceHolder.Callback {

    private static final Logger logger = LoggerFactory.getLogger(SurfaceDrawThread.class);

    private final Engine engine;
    private final SurfaceHolder surfaceHolder;

    private Thread thread = null;
    private volatile boolean running = false;

    public SurfaceDrawThread(SurfaceHolder surfaceHolder, Engine engine) {
        this.surfaceHolder = surfaceHolder;
        this.engine = engine;
        this.surfaceHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        final Rect frame = holder.getSurfaceFrame();
        engine.resize(Point2F.cartesian(frame.right, frame.bottom));
        start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        engine.resize(Point2F.cartesian(width, height));
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private synchronized void stop() {
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

    private synchronized void start() {
        if (this.running) {
            throw new IllegalStateException("Draw thread already running");
        }
        this.running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        engine.init();

        final Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);

        long prevTime = System.currentTimeMillis();
        while (running) {

            engine.process();

            final long currentTime = System.currentTimeMillis();
            final float fps = 1000f / (currentTime - prevTime);
            prevTime = currentTime;

            final Canvas canvas = surfaceHolder.lockCanvas(null);
            if (canvas == null) {
                logger.info("Canvas can't be locked. Waiting 100ms...");
                if (!running) {
                    break;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    logger.error("Canvas wait interrupted");
                }
                continue;
            }
            try {
                canvas.drawColor(Color.BLACK);
                canvas.drawText("FPS: " + fps, 100, 100, paint);
                engine.render(canvas);
            } finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        logger.info("Drawing thread finished");
    }

}
