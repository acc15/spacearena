package ru.spacearena.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.timing.NanoTimer;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.jogl.matrix.Matrix4F;
import ru.spacearena.jogl.shaders.PositionColorProgram2f;
import ru.spacearena.jogl.shaders.ShaderProgram;

import javax.media.opengl.*;
import java.nio.FloatBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglListener implements GLEventListener {

    private volatile boolean running = true;
    private int w, h;

    private ShaderProgram defaultProgram = new PositionColorProgram2f();
    private Matrix4F viewMatrix = new Matrix4F();
    private Matrix4F viewModelMatrix = new Matrix4F();
    private int[] bufs = new int[1];

    private Triangle triangle = new Triangle();

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

    public void init(GLAutoDrawable drawable) {

        w = drawable.getWidth();
        h = drawable.getHeight();

        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

        triangle.setPosition(TempUtils.RAND.nextFloatBetween(getLeft(), getRight()),
                TempUtils.RAND.nextFloatBetween(getTop(), getBottom()));
        triangle.setVelocity(TempUtils.RAND.nextFloatBetween(-100, 100),
                    TempUtils.RAND.nextFloatBetween(-100, 100));
        triangle.setColor(TempUtils.RAND.nextFloat(), TempUtils.RAND.nextFloat(), TempUtils.RAND.nextFloat());
        triangle.setSize(50);

        vertexBuf = Buffers.newDirectFloatBuffer(5 * 3);

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
            triangle.put(vertexBuf);
            vertexBuf.rewind();

            viewModelMatrix.set(viewMatrix);
            viewModelMatrix.postMultiply(triangle.matrix);

            gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, bufs[0]);

            final int size = vertexBuf.limit() * 4;
            gl.glBufferData(GL2ES2.GL_ARRAY_BUFFER, size, vertexBuf, GL2ES2.GL_STATIC_DRAW);

            defaultProgram.bindAttr(gl, PositionColorProgram2f.POSITION_ATTRIB, 2, 5, 0);
            defaultProgram.bindAttr(gl, PositionColorProgram2f.COLOR_ATTRIB, 3, 5, 2);
            defaultProgram.bindMatrix(gl, PositionColorProgram2f.MATRIX_UNIFORM, viewModelMatrix.m);

            gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 3);

            final Triangle t2 = new Triangle();
            t2.setSize(50f);
            t2.setColor(1,1,1);
            vertexBuf.rewind();
            t2.put(vertexBuf);
            vertexBuf.rewind();
            gl.glBufferData(GL2ES2.GL_ARRAY_BUFFER, size, vertexBuf, GL2ES2.GL_STATIC_DRAW);

            defaultProgram.bindAttr(gl, PositionColorProgram2f.POSITION_ATTRIB, 2, 5, 0);
            defaultProgram.bindAttr(gl, PositionColorProgram2f.COLOR_ATTRIB, 3, 5, 2);
            defaultProgram.bindMatrix(gl, PositionColorProgram2f.MATRIX_UNIFORM, viewMatrix.m);
            gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, 3);

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

        viewMatrix.identity();
        viewMatrix.postScale(2f / w, -2f / h);
        viewMatrix.postTranslate(-100f, -100f);
    }

    public void mainLoop(GLAutoDrawable drawable) {
        final Timer timer = new NanoTimer();
        while (running) {
            final float dt = timer.reset();
            triangle.update(this, dt);


            drawable.display();
            Thread.yield();
        }
        System.exit(0);
    }

}
