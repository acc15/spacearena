package ru.spacearena.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import ru.spacearena.android.engine.AndroidEngineFactory;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.Point2F;
import ru.spacearena.game.GameFactory;

import java.util.LinkedHashMap;

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

        Engine.init(new AndroidEngineFactory());
        final Engine engine = GameFactory.createEngine();

        final LinkedHashMap<Integer,Point2F> pointers = new LinkedHashMap<Integer, Point2F>();
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    pointers.clear();
                    return engine.onTouch(pointers.values());
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
                return engine.onTouch(pointers.values());
            }
        });

        new SurfaceDrawThread(surfaceView.getHolder(), engine);
    }
}