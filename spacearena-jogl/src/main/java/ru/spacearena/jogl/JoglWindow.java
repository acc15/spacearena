package ru.spacearena.jogl;

import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.game.GameFactory;
import ru.spacearena.jogl.engine.JoglEngine;
import ru.spacearena.jogl.engine.JoglGL2;

import javax.media.opengl.*;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglWindow implements GLEventListener {

    private final EngineFactory factory;

    private JoglEngine engine;
    private final JoglGL2 gl = new JoglGL2();
    private final DrawContext context = new DrawContext(gl);

    public JoglWindow(EngineFactory factory) {
        this.factory = factory;
    }

    public void start() {
        final GLCapabilities caps = new GLCapabilities(GLProfile.getDefault());
        caps.setHardwareAccelerated(true);
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
        window.setVisible(true);

        this.engine = new JoglEngine(factory, window);
        window.addGLEventListener(this);
        loop();
    }

    private void loop() {
        while (true) {
            if (!engine.onUpdate()) {
                return;
            }
            engine.getWindow().display();
        }
    }

    public void init(GLAutoDrawable drawable) {
        //drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));
        try {
            gl.setGL2(drawable.getGL().getGL2());
            context.init();
        } finally {
            gl.setGL2(null);
        }
    }

    public void dispose(GLAutoDrawable drawable) {
        try {
            gl.setGL2(drawable.getGL().getGL2());
            context.dispose();
        } finally {
            gl.setGL2(null);
        }
    }

    public void display(GLAutoDrawable drawable) {
        try {
            gl.setGL2(drawable.getGL().getGL2());
            engine.onDraw(context);
        } finally {
            gl.setGL2(null);
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        try {
            gl.setGL2(drawable.getGL().getGL2());
            gl.viewport(x,y,width,height);
            engine.onSize(width, height);
        } finally {
            gl.setGL2(null);
        }
    }

    public static void main(String[] args) {
        new JoglWindow(new GameFactory()).start();
    }

}
