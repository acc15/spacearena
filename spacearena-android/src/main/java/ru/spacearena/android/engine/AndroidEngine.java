package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputType;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.util.IOUtils;

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

    public boolean enableInput(InputType inputType) {
        if (inputType == InputType.TOUCH) {
            /*
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    pointers.clear();
                    return engine.onInput(pointers.values());
                }

                final int action = event.getAction() & MotionEvent.ACTION_MASK;
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                        >> MotionEvent.ACTION_POINTER_ID_SHIFT;

                for (int i=0; i<event.getPointerCount(); i++) {
                    final int ptrId = event.getPointerId(i);
                    if (action == MotionEvent.ACTION_POINTER_UP && pointerIndex == i) {
                        pointers.remove(ptrId);
                    } else {
                        pointers.put(ptrId, Point2F.xy(event.getX(i), event.getY(i)));
                    }
                }
                return engine.onInput(pointers.values());
                }
            });*/
            return true;
        }
        return false;
    }

}
