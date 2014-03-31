package ru.spacearena.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.PMVMatrix;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.jogl.shaders.PositionColorProgram2f;
import ru.spacearena.jogl.shaders.ShaderProgram;

import javax.imageio.ImageIO;
import javax.media.opengl.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglListener implements GLEventListener {

    private volatile boolean running = true;
    private int w, h;

    private ShaderProgram defaultProgram = new PositionColorProgram2f();
    private PMVMatrix matrix = new PMVMatrix();
    private int[] bufs = new int[1];

    private List<Particle> particles = new ArrayList<Particle>();

    //Thread[main-Display-.windows_nil-1-EDT-1,5,main]: Warning: Default-EDT about (2) to stop, task executed.
    // Remaining tasks: 1 - Thread[main-Display-.windows_nil-1-EDT-1,5,main]


    public static void main(String[] args) {

        final JoglListener w = new JoglListener();

        final GLProfile glp = GLProfile.getDefault();
        final GLCapabilities caps = new GLCapabilities(glp);

//        final FPSAnimator a = new FPSAnimator(60);

        final GLWindow window = GLWindow.create(caps);
        window.setSize(800, 600);
        window.setTitle("SpaceArena");
        window.addWindowListener(new WindowAdapter() {
            public void windowDestroyNotify(WindowEvent arg0) {
                w.running = false;
            }
        });
        window.addMouseListener(new MouseAdapter() {

            private float mx, my;
            private boolean init = false;

            @Override
            public void mouseMoved(MouseEvent e) {
                final float x = e.getX() - w.getHalfWidth(), y = e.getY() - w.getHalfHeight();
                if (init) {
                    final float vx = x - mx, vy = y - my;
                    for (Particle p : w.particles) {
                        final float dx = p.getX() - x, dy = p.getY() - y;
                        final float l2 = FloatMathUtils.lengthSquare(dx, dy);
                        if (l2 > 900f || l2 <= 0) {
                            continue;
                        }

                        final float l = 1000f / FloatMathUtils.sqrt(l2);
                        p.setVelocity(vx * l, vy * l);

                    }
                }
                mx = x;
                my = y;
                init = true;
            }
        });

        final Screen screen = window.getScreen();
        if (!screen.isNativeValid()) {
            screen.createNative();
        }

        window.setPosition((screen.getWidth() - window.getWidth()) / 2, (screen.getHeight() - window.getHeight()) / 2);
        window.addGLEventListener(w);
        window.setVisible(true);
        w.mainLoop(window);
    }

    public float getHalfWidth() {
        return (float)w/2;
    }

    public float getHalfHeight() {
        return (float)h/2;
    }

    public float getLeft() {
        return -getHalfWidth();
    }

    public float getRight() {
        return getHalfWidth();
    }

    public float getTop() {
        return -getHalfHeight();
    }

    public float getBottom() {
        return getHalfHeight();
    }

    private BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Can't read image from file: " + fileName, e);
        }
    }

    private void initParticles(BufferedImage image) {

        final int iw = image.getWidth(), ih = image.getHeight();
        final float ihw = (float)iw/2, ihh = (float)ih/2;
        for (int y=0; y<ih; y++) {
            for (int x=0; x<iw; x++) {
                final int pixel = image.getRGB(x, y);
                final int alpha = Color.alpha(pixel);
                if (alpha == 0) {
                    continue;
                }


                final Particle p = new Particle();
                p.setTarget((float) x - ihw, (float) y - ihh);
                p.setPosition(TempUtils.RAND.nextFloatBetween(getLeft(), getRight()),
                        TempUtils.RAND.nextFloatBetween(getTop(), getBottom()));

                p.setColor(Color.redFloat(pixel), Color.greenFloat(pixel), Color.blueFloat(pixel));
                p.setVelocity(TempUtils.RAND.nextFloatBetween(-100f, 100f), TempUtils.RAND.nextFloatBetween(-100f, 100f));
                p.setRestitution(TempUtils.RAND.nextFloat());
                particles.add(p);

//                if (particles.size() > 500) {
//                    return;
//                }
            }
        }

    }

    public void init(GLAutoDrawable drawable) {

        w = drawable.getWidth();
        h = drawable.getHeight();

        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

        final BufferedImage img = loadImage("1.jpg");

//        final Particle p1 = new Particle();
//        p1.setPosition(TempUtils.RAND.nextFloatBetween(getLeft(), getRight()),
//                       TempUtils.RAND.nextFloatBetween(getTop(), getBottom()));
//        p1.setVelocity(TempUtils.RAND.nextFloatBetween(-100f, 100f), TempUtils.RAND.nextFloatBetween(-100f, 100f));
//        p1.setColor(1,1,1);
//        p1.setTarget(TempUtils.RAND.nextFloatBetween(getLeft(), getRight()),
//                TempUtils.RAND.nextFloatBetween(getTop(), getBottom()));
//        particles.add(p1);
//
//        final Particle p2 = new Particle();
//        p2.setColor(1,0,0);
//        p2.setPosition(p1.getTargetX(), p1.getTargetY());
//        p2.setTarget(p1.getTargetX(), p1.getTargetY());
//        particles.add(p2);
//

        initParticles(img);


        vertexBuf = Buffers.newDirectFloatBuffer(5 * particles.size());

        final GL2ES2 gl = drawable.getGL().getGL2ES2();

        defaultProgram.compile(gl);
        gl.glUseProgram(defaultProgram.getId());
        gl.glGenBuffers(bufs.length,bufs,0);

    }

    private FloatBuffer vertexBuf;

    public void dispose(GLAutoDrawable drawable) {
    }


    public void display(GLAutoDrawable drawable) {
        final GL2ES2 gl = drawable.getGL().getGL2ES2();
        gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);
        if (vertexBuf != null) {

            vertexBuf.rewind();
            for (Particle p: particles) {
                p.put(vertexBuf);
            }
            vertexBuf.rewind();

            gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, bufs[0]);

            final int size = vertexBuf.limit() * 4;
            gl.glBufferData(GL2ES2.GL_ARRAY_BUFFER, size, vertexBuf, GL2ES2.GL_STATIC_DRAW);

            defaultProgram.bindAttr(gl, PositionColorProgram2f.POSITION_ATTRIB, 2, 5, 0);
            defaultProgram.bindAttr(gl, PositionColorProgram2f.COLOR_ATTRIB, 3, 5, 2);
            defaultProgram.bindMatrix(gl, PositionColorProgram2f.MATRIX_UNIFORM, matrix.glGetMatrixf());

            gl.glDrawArrays(GL2ES2.GL_POINTS, 0, particles.size());
            defaultProgram.disableVertexAttrib(gl);

            gl.glDeleteBuffers(bufs.length, bufs, 0);
            gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, 0);
        }

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.w = width;
        this.h = height;

        final GL gl = drawable.getGL();
        gl.glViewport(x, y, width, height);

        matrix.glLoadIdentity();
        //matrix.glTranslatef(-1f, 1f, 0f);
        matrix.glScalef(2f/w, -2f/h, 1f);
    }

    public void mainLoop(GLAutoDrawable drawable) {
        final Timer timer = new NanoTimer();
        while (running) {
            final float dt = timer.reset();
            for (Particle p: particles) {
                p.update(this, dt);
            }
            drawable.display();
            Thread.yield();
        }
        System.exit(0);
    }

}
