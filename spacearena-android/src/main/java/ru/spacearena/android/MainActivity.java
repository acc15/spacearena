package ru.spacearena.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.input.MotionType;
import ru.spacearena.game.GameFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class MainActivity extends Activity {

    // to make sure it compiles using old android SDK jars
    private static final int FLAG_HARDWARE_ACCELERATED = 0x01000000;
    private static final int SDK_VERSION_HONEYCOMB = 11;

    private MotionType mapMotionType(int action) {
        switch (action) {
        case MotionEvent.ACTION_UP: return MotionType.UP;
        case MotionEvent.ACTION_DOWN: return MotionType.DOWN;
        case MotionEvent.ACTION_MOVE: return MotionType.MOVE;
        default: return null;
        }
    }

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

        final Engine engine = GameFactory.createEngine(getResources());

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
                final MotionType type = mapMotionType(event.getAction());
                if (type == null) {
                    return false;
                }
                final List<Point2F> points = new ArrayList<Point2F>();
                for (int i=0; i<event.getPointerCount(); i++) {
                    points.add(Point2F.cartesian(event.getX(i), event.getY(i)));
                }
                return engine.touch(type, points);
            }
        });

        new SurfaceDrawThread(surfaceView.getHolder(), engine);
    }
}
