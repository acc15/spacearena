package ru.spacearena.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import ru.spacearena.android.engine.Engine;
import ru.spacearena.android.engine.EngineContainer;
import ru.spacearena.android.engine.Point;
import ru.spacearena.android.engine.common.Background;
import ru.spacearena.android.engine.common.FPSDisplay;
import ru.spacearena.android.engine.events.MotionType;
import ru.spacearena.android.game.Ship;
import ru.spacearena.android.game.Sky;

import java.util.AbstractList;
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

        final Engine engine = new Engine(new EngineContainer().
                add(new Background()).
                add(new Sky()).
                add(new Ship(getResources())).
                add(new FPSDisplay()));

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, final MotionEvent event) {
                final MotionType type = mapMotionType(event.getAction());
                if (type == null) {
                    return false;
                }

                final List<Point> points = new AbstractList<Point>() {
                    @Override
                    public Point get(int i) {
                        return Point.create(event.getX(i), event.getY(i));
                    }

                    @Override
                    public int size() {
                        return event.getPointerCount();
                    }
                };
                return engine.touch(type, points);
            }
        });
        new SurfaceDrawThread(surfaceView.getHolder(), engine);

    }
}
