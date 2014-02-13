package ru.spacearena.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import ru.spacearena.android.engine.AndroidImage;
import ru.spacearena.android.engine.AndroidMatrix;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineEnvironment;
import ru.spacearena.android.engine.graphics.Image;
import ru.spacearena.android.engine.graphics.Matrix;
import ru.spacearena.android.engine.input.InputType;
import ru.spacearena.android.engine.util.IOUtils;
import ru.spacearena.game.GameFactory;

import java.io.InputStream;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class EngineActivity extends Activity {

    // to make sure it compiles using old android SDK jars
    private static final int FLAG_HARDWARE_ACCELERATED = 0x01000000;
    private static final int SDK_VERSION_HONEYCOMB = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= SDK_VERSION_HONEYCOMB) {
            getWindow().setFlags(
                    FLAG_HARDWARE_ACCELERATED,
                    FLAG_HARDWARE_ACCELERATED);
        }

        final SurfaceView surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            private SurfaceDrawThread surfaceDrawThread = null;

            public void surfaceCreated(SurfaceHolder holder) {
                final Rect r = holder.getSurfaceFrame();
                if (surfaceDrawThread == null) {

                    final Engine engine = GameFactory.createEngine(new EngineEnvironment() {
                        public Matrix createMatrix() {
                            return new AndroidMatrix();
                        }

                        public float getWidth() {
                            return r.right;
                        }

                        public float getHeight() {
                            return r.bottom;
                        }

                        public Image loadImage(String resource) {
                            final InputStream inputStream = EngineActivity.this.getClass().getResourceAsStream(resource);
                            try {
                                final Bitmap bm = BitmapFactory.decodeStream(inputStream);
                                return new AndroidImage(bm);
                            } finally {
                                IOUtils.closeQuietly(inputStream);
                            }
                        }

                        public void enableInput(InputType inputType) {
                            if (inputType == InputType.TOUCH) {
                                surfaceView.setOnTouchListener(new View.OnTouchListener() {
                                    public boolean onTouch(View v, MotionEvent event) {
                                        // TODO call engine.onInput()
                                        return false;
                                    }
                                });
                            }
                        }
                    });

                    surfaceDrawThread = new SurfaceDrawThread(holder, engine);
                } else {
                    surfaceDrawThread.engine.onSize(r.right, r.bottom);
                }
                surfaceDrawThread.start();
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceDrawThread.engine.onSize(width, height);
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                surfaceDrawThread.stop();
            }
        });


    }


        /*
        final LinkedHashMap<Integer,Point2F> pointers = new LinkedHashMap<Integer, Point2F>();
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
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
        });
        */
}
