package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
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

    private final View view;

    public AndroidEngine(EngineFactory factory, View view, float initialWidth, float initialHeight) {
        super(factory);
        this.view = view;
        init(initialWidth, initialHeight);
    }

    @Override
    public Matrix createMatrix() {
        return new AndroidMatrix();
    }

    @Override
    public Image createImage(int width, int height) {
        return new AndroidImage(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
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
