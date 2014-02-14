package ru.spacearena.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.input.InputType;
import ru.spacearena.engine.input.TouchEvent;
import ru.spacearena.engine.util.IOUtils;

import java.io.InputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class AndroidEngine extends Engine {

    private View view;
    private float width, height;

    public AndroidEngine(EngineFactory factory, View view, float initialWidth, float initialHeight) {
        super(factory);
        this.view = view;
        this.width = initialWidth;
        this.height = initialHeight;
        init();
    }

    @Override
    public Matrix createMatrix() {
        return new AndroidMatrix();
    }

    public Image loadImage(String resource) {
        final InputStream inputStream = factory.getClass().getResourceAsStream(resource);
        try {
            final Bitmap bm = BitmapFactory.decodeStream(inputStream);
            return new AndroidImage(bm);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void onSize(float width, float height) {
        super.onSize(width, height);
        this.width = width;
        this.height = height;
    }

    public void enableInput(InputType inputType) {
        if (inputType == InputType.TOUCH) {
            final TouchEvent touchEvent = new TouchEvent();
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    final int action = event.getAction() & MotionEvent.ACTION_MASK;
                    final int pointerId = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                            >> MotionEvent.ACTION_POINTER_ID_SHIFT;
                    switch (action) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent.setAction(TouchEvent.Action.DOWN);
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        touchEvent.setAction(TouchEvent.Action.UP);
                        break;

                    default:
                        touchEvent.setAction(TouchEvent.Action.MOVE);
                        break;
                    }
                    touchEvent.setPointerId(pointerId);
                    touchEvent.setX(event.getX(pointerId));
                    touchEvent.setY(event.getY(pointerId));
                    return onInput(touchEvent);
                }
            });
        }
    }

}
