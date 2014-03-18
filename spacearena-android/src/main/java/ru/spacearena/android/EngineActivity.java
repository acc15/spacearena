package ru.spacearena.android;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import ru.spacearena.android.engine.AndroidEngine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.game.GameFactory;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= SDK_VERSION_HONEYCOMB) {
            getWindow().addFlags(FLAG_HARDWARE_ACCELERATED);
        }

        final SurfaceView surfaceView = new SurfaceView(this);
        setContentView(surfaceView);

        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        final EngineFactory factory = new GameFactory();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            private SurfaceDrawThread surfaceDrawThread = null;

            public void surfaceCreated(SurfaceHolder holder) {
                final Rect r = holder.getSurfaceFrame();
                if (surfaceDrawThread == null) {
                    surfaceDrawThread = new SurfaceDrawThread(holder,
                            new AndroidEngine(factory, surfaceView, r.right, r.bottom));
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

}
