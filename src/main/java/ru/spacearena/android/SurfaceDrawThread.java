package ru.spacearena.android;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import ru.spacearena.android.engine.Engine;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class SurfaceDrawThread extends Thread implements SurfaceHolder.Callback {

    private final Engine engine;
    private final SurfaceHolder surfaceHolder;

    private volatile boolean running = false;

    public SurfaceDrawThread(SurfaceHolder surfaceHolder, Engine engine) {
        this.surfaceHolder = surfaceHolder;
        this.engine = engine;
        this.surfaceHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        finish();
    }

    @SuppressWarnings("LoopStatementThatDoesntLoop")
    private synchronized void finish() {
        this.running = false;
        while (true) {
            try {
                join();
            } catch (InterruptedException ignored) {
            }
            break;
        }
    }

    @Override
    public synchronized void start() {
        if (this.running) {
            throw new IllegalStateException("Draw thread already running");
        }
        this.running = true;
        super.start();
    }

    @Override
    public void run() {
        while (running) {
            engine.process();
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                engine.render(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
