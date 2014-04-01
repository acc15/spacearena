package ru.spacearena.jogl;

import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.jogl.engine.JoglAdapter;
import ru.spacearena.jogl.engine.JoglListener;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglWindow implements JoglListener {

    private volatile boolean running = true;

    public static void main(String[] args) {

        final JoglWindow w = new JoglWindow();

        final GLProfile glp = GLProfile.getDefault();
        final GLCapabilities caps = new GLCapabilities(glp);

        final GLWindow window = GLWindow.create(caps);
        window.setSize(800, 600);
        window.setTitle("SpaceArena");
        window.addWindowListener(new WindowAdapter() {
            public void windowDestroyNotify(WindowEvent arg0) {
                System.exit(0);
            }
        });

        final Screen screen = window.getScreen();
        if (!screen.isNativeValid()) {
            screen.createNative();
        }

        window.setPosition((screen.getWidth() - window.getWidth()) / 2, (screen.getHeight() - window.getHeight()) / 2);
        window.addGLEventListener(new JoglAdapter(w));
        window.setVisible(true);
        w.mainLoop(window);
    }

    public void init(OpenGL gl) {
        // TODO implement..

    }

    public void dispose(OpenGL gl) {
        // TODO implement..

    }

    public void display(OpenGL gl) {
        gl.clear(OpenGL.COLOR_BUFFER_BIT);

    }

    public void reshape(OpenGL gl, int x, int y, int width, int height) {
        // TODO implement..

    }

    public void mainLoop(GLAutoDrawable drawable) {
        final Timer timer = new NanoTimer();

        while (running) {
            final float dt = timer.reset();
            drawable.display();
            Thread.yield();
        }
    }

}
