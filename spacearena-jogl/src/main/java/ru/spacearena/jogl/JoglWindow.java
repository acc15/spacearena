package ru.spacearena.jogl;

import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.TextureIO;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.EngineFactory;
import ru.spacearena.engine.events.InputContext;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.game.GameFactory;
import ru.spacearena.jogl.engine.JoglGL2;
import ru.spacearena.jogl.engine.NewtInputContext;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import java.io.IOException;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglWindow implements GLEventListener {

    private final EngineFactory factory;

    private Engine engine;
    private final JoglGL2 gl = new JoglGL2();

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

        final InputContext inputContext = new NewtInputContext(window);
        final DrawContext drawContext = new DrawContext(gl);
        this.engine = new Engine(factory, drawContext, inputContext);


        window.addGLEventListener(this);

        final Animator animator = new Animator(window);
        animator.setRunAsFastAsPossible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        gl.setGL2(drawable.getGL().getGL2());
        engine.onInit();


        final com.jogamp.opengl.util.texture.Texture t;
        try {
            t = TextureIO.newTexture(GameFactory.class.getResource("ship.png"), false, null);
            System.out.println(t.getWidth());
            System.out.println(t.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static void main(String[] args) {
        new JoglWindow(new GameFactory()).start();
    }

}
