package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.events.TouchEvent;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.IOUtils;

import java.io.InputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class AndroidEngine extends Engine {

    private static final Logger logger = LoggerFactory.getLogger(AndroidEngine.class);
    private View view;

    public AndroidEngine(EngineFactory factory, View view, float initialWidth, float initialHeight) {
        super(factory);
        this.view = view;
        init(initialWidth, initialHeight);
    }

    @Override
    public Matrix createMatrix() {
        return new AndroidMatrix();
    }

    public Image loadImage(String resource) {
        final InputStream inputStream = getFactory().getClass().getResourceAsStream(resource);
        try {
            final Bitmap bm = BitmapFactory.decodeStream(inputStream);
            return new AndroidImage(bm);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
/*
    @Override
    public boolean onUpdate() {
        try {
            android.os.Debug.startMethodTracing("spacearena-update");
            return super.onUpdate();
        } finally {
            android.os.Debug.stopMethodTracing();
        }
    }

    @Override
    public void onDraw(DrawContext context) {
        android.os.Debug.startMethodTracing("spacearena-draw");
        super.onDraw(context);
        android.os.Debug.stopMethodTracing();
    }
*/
    public static TouchEvent.Action mapAction(int motionAction) {
        switch (motionAction) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_POINTER_DOWN:
            return TouchEvent.Action.DOWN;

        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            return TouchEvent.Action.UP;

        case MotionEvent.ACTION_MOVE:
            return TouchEvent.Action.MOVE;

        case MotionEvent.ACTION_CANCEL:
            return TouchEvent.Action.CANCEL;
        }
        throw new IllegalArgumentException(String.format("Unknown action code: 0x%02x", motionAction));
    }

    private String getActionCode(int code) {
        switch (code) {
        case MotionEvent.ACTION_DOWN: return "ACTION_DOWN";
        case MotionEvent.ACTION_POINTER_DOWN: return "ACTION_POINTER_DOWN";
        case MotionEvent.ACTION_UP: return "ACTION_UP";
        case MotionEvent.ACTION_POINTER_UP: return "ACTION_POINTER_UP";
        case MotionEvent.ACTION_MOVE: return "ACTION_MOVE";
        case MotionEvent.ACTION_CANCEL: return "ACTION_CANCEL";
        default: return "UNKNOWN";
        }
    }

    public boolean enableInput(InputType inputType) {
        if (inputType == InputType.TOUCH) {

            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    final int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
                    final TouchEvent.Action action = mapAction(actionCode);

                    // ACTION_POINTER_ID_MASK = ACTION_POINTER_INDEX_MASK
                    // Name of constant ACTION_POINTER_ID_MASK is wrong and renamed in new versions
                    final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                            >> MotionEvent.ACTION_POINTER_ID_SHIFT;
                    final int pointerCount = event.getPointerCount();
                    final TouchEvent touchEvent = new TouchEvent(action, pointerCount, pointerIndex);
                    for (int i=0; i<touchEvent.getPointerCount(); i++) {
                        final float x = event.getX(i);
                        final float y = event.getY(i);
                        final int id = event.getPointerId(i);
                        touchEvent.setPointer(i, id, x, y);
                    }

                    AndroidEngine.this.scheduleEvent(touchEvent);
                    return true;
                }
            });
            return true;
        }
        return false;
    }

}
