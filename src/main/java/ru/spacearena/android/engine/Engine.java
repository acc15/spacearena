package ru.spacearena.android.engine;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class Engine {

    private long lastTime = -1;

    private EngineObject root;
    private Frame frame = new Frame();

    public void setViewport(int width, int height) {
        frame.width = width;
        frame.height = height;
    }

    public Engine(EngineObject engineObject) {
        this.root = engineObject;
    }

    //private Matrix scale = new Matrix();


    public void init() {
        root.init(frame);
        //scale.setScale(1/3f, 1/3f);
    }

    public void process() {
        final long currentTime = System.currentTimeMillis();
        if (lastTime >= 0) {
            final long delta = currentTime - lastTime;
            frame.timeDelta = (float)delta/1000;
            root.process(frame);
        }
        lastTime = currentTime;
    }

    public void onSize(int width, int height) {
        if (width != frame.width || height != frame.height) {
            setViewport(width, height);
            root.onSize(frame);
        }
    }


    public void render(Canvas canvas) {
        //canvas.setMatrix(scale);
        root.render(canvas);
    }

    public boolean onTouch(MotionEvent event) {
        return root.onTouch(event);
    }
}
