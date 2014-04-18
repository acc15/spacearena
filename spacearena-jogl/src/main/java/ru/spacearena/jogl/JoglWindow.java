package ru.spacearena.jogl;

import com.jogamp.newt.MonitorDevice;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.parse.Arguments;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.game.GameFactory;
import ru.spacearena.jogl.engine.JoglGL2;
import ru.spacearena.jogl.engine.NewtInputContext;

import javax.media.nativewindow.util.DimensionImmutable;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglWindow {

    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 600;
    private final EngineFactory factory;

    public JoglWindow(EngineFactory factory) {
        this.factory = factory;
    }

    public static class Args {
        public float ppi;
        public int width, height;
        public boolean fullscreen;
    }

    public Args parseArgs(String[] args) {
        final Arguments a = Arguments.parse(args);
        final Args p = new Args();
        p.ppi = a.getFloat("ppi", 0);
        p.width = a.getInt("width", DEFAULT_WIDTH);
        p.height = a.getInt("height", DEFAULT_HEIGHT);
        p.fullscreen = a.has("fullscreen");
        return p;
    }

    public float computeDensityScale(Screen screen) {

        float ppi = 0;

        final int sw = screen.getWidth(), sh = screen.getHeight();
        for (MonitorDevice m: screen.getMonitorDevices()) {
            DimensionImmutable di = m.getSizeMM();
            final int xPPI = FloatMathUtils.round(sw / di.getWidth() * DrawContext.INCH_PER_MM),
                      yPPI = FloatMathUtils.round(sh / di.getHeight() * DrawContext.INCH_PER_MM);
            ppi = FloatMathUtils.min(ppi, FloatMathUtils.min(xPPI, yPPI));
        }
        return FloatMathUtils.isZero(ppi) ? DrawContext.DEFAULT_DENSITY_SCALE : ppi/DrawContext.DENSITY_SCALE_PPI;
    }

    public void start(String[] args) {

        final Args a = parseArgs(args);

        final GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        caps.setHardwareAccelerated(true);
        final GLWindow window = GLWindow.create(caps);
        window.addWindowListener(new WindowAdapter() {
            public void windowDestroyNotify(WindowEvent arg0) {
                System.exit(0);
            }
        });

        final Screen screen = window.getScreen();
        if (!screen.isNativeValid()) {
            screen.createNative();
        }

        window.setSize(a.width, a.height);
        window.setTitle("SpaceArena");
        window.setPosition((screen.getWidth() - window.getWidth()) / 2, (screen.getHeight() - window.getHeight()) / 2);
        window.setVisible(true);

        final InputContext inputContext = new NewtInputContext(window);
        final JoglGL2 gl = new JoglGL2();
        final DrawContext drawContext = new DrawContext(gl);
        final Engine engine = new Engine(factory, inputContext);
        window.addGLEventListener(new GLEventListener() {
            public void init(GLAutoDrawable drawable) {
                gl.setGL2(drawable.getGL().getGL2());
                drawContext.densityScale(FloatMathUtils.isZero(a.ppi)
                        ? computeDensityScale(screen)
                        : a.ppi/DrawContext.DENSITY_SCALE_PPI);
                engine.onInit(drawContext);
            }

            public void dispose(GLAutoDrawable drawable) {
                engine.onDispose();
                gl.setGL2(null);
            }

            public void display(GLAutoDrawable drawable) {
                engine.onUpdate();
                engine.onDraw();
            }

            public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
                gl.viewport(x,y,width,height);
                engine.onSize(width, height);
            }
        });

        final Animator animator = new Animator(window);
        animator.setRunAsFastAsPossible(true);
        animator.start();
    }

    public static void main(String[] args) {
        new JoglWindow(new GameFactory()).start(args);
    }

}
