package ru.spacearena.android;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import ru.spacearena.android.engine.AndroidGLES2;
import ru.spacearena.android.engine.AndroidInputContext;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.game.GameFactory;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-01
 */
public class EngineActivity extends Activity {

    // to make sure it compiles using old android SDK jars
    private static final int FLAG_HARDWARE_ACCELERATED = 0x01000000;
    private static final int SDK_VERSION_HONEYCOMB = 11;

    private final EngineFactory factory;

    public EngineActivity(/*EngineFactory factory*/) {
        this.factory = new GameFactory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= SDK_VERSION_HONEYCOMB) {
            getWindow().addFlags(FLAG_HARDWARE_ACCELERATED);
        }

        final GLSurfaceView view = new GLSurfaceView(this);
        final AndroidGLES2 gles2 = new AndroidGLES2();
        final DrawContext drawContext = new DrawContext(gles2);
        final InputContext inputContext = new AndroidInputContext(view);
        final Engine engine = new Engine(factory, inputContext);
        view.setEGLContextClientVersion(2);
        view.setRenderer(new GLSurfaceView.Renderer() {

            private boolean initialized = false;

            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                if (initialized) {
                    engine.onDispose();
                }

                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                drawContext.setDensityScale(metrics.density, metrics.scaledDensity);
                engine.onInit(drawContext);
                initialized = true;
            }

            public void onSurfaceChanged(GL10 gl, int width, int height) {
                gles2.viewport(0,0,width,height);
                engine.onSize(width, height);
            }

            public void onDrawFrame(GL10 gl) {
                engine.onUpdate();
                engine.onDraw();
            }
        });
        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(view);
    }

}
