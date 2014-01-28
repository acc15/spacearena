package ru.spacearena.android;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import ru.spacearena.android.engine.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class MainActivity extends Activity {

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

        final Engine engine = new Engine(new EngineContainer().
                add(new Background()).
                add(new Sky()).
                add(new Ship(getResources())).
                add(new FPSDisplay()));

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return engine.onTouch(event);
            }
        });
        new SurfaceDrawThread(surfaceView.getHolder(), engine);

    }
}
