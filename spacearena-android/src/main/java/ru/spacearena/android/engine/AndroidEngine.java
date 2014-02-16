package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import ru.spacearena.android.engine.graphics.Image;
import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;
import ru.spacearena.android.engine.util.IOUtils;

import java.io.InputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-02
 */
public class AndroidEngine extends Engine {

    private View view;

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
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                    //return onInput(touchEvent);
                }
            });
        }
    }

}
