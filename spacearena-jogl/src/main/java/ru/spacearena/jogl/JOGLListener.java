package ru.spacearena.jogl;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.math.Matrix3F;
import ru.spacearena.engine.util.TempUtils;
import ru.spacearena.jogl.shaders.PositionColorProgram2f;
import ru.spacearena.jogl.shaders.ShaderProgram;

import javax.media.opengl.*;
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
    private float[] mat3 = new float[] {1, 0, 0, 0, 1, 0, 0, 0, 1};
    private int[] bufs = new int[1];

    private List<Triangle> triangles = new ArrayList<Triangle>();

    public static void main(String[] args) {

        final JoglListener w = new JoglListener();

        final GLProfile glp = GLProfile.getDefault();
        final GLCapabilities caps = new GLCapabilities(glp);

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


    public void init(GLAutoDrawable drawable) {

        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

        for (int i = 0; i < 100000; i++) {
            final Triangle t = new Triangle();
            t.positionX = TempUtils.RAND.nextFloat() * drawable.getWidth();
            t.positionY = TempUtils.RAND.nextFloat() * drawable.getHeight();
            t.velocityX = TempUtils.RAND.nextFloat() * 100f;
            t.velocityY = TempUtils.RAND.nextFloat() * 100f;
            t.color = Color.rgb(TempUtils.RAND.nextIntBetween(128, 255),
                    TempUtils.RAND.nextIntBetween(128, 255),
                    TempUtils.RAND.nextIntBetween(128, 255));
            t.size = TempUtils.RAND.nextFloat() * 30f;
            triangles.add(t);
        }
        triangleBuf = Buffers.newDirectFloatBuffer(7 * 3 * triangles.size());

        final GL2ES2 gl = drawable.getGL().getGL2ES2();

        defaultProgram.compile(gl);
        gl.glUseProgram(defaultProgram.getId());

        final Triangle t = new Triangle();
        t.size = 1f;
        gl.glGenBuffers(bufs.length,bufs,0);

    }

    private FloatBuffer triangleBuf;

    public void makeTriangleBuf(Triangle t, FloatBuffer buf) {
        for (int i=0; i<t.getVertexCount(); i++) {
            buf.put(t.getVertexX(i));
            buf.put(t.getVertexY(i));
            buf.put(1f);
            buf.put(Color.redFloat(t.color));
            buf.put(Color.greenFloat(t.color));
            buf.put(Color.blueFloat(t.color));
            buf.put(Color.alphaFloat(t.color));
        }
    }

    public void dispose(GLAutoDrawable drawable) {
    }


    public void display(GLAutoDrawable drawable) {
        final GL2ES2 gl = drawable.getGL().getGL2ES2();
        gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT);
        if (triangleBuf != null) {

            triangleBuf.rewind();
            for (Triangle t: triangles) {
                makeTriangleBuf(t, triangleBuf);
            }
            triangleBuf.rewind();

            gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, bufs[0]);

            final int size = triangleBuf.limit() * 4;
            gl.glBufferData(GL2ES2.GL_ARRAY_BUFFER, size, triangleBuf, GL2ES2.GL_STATIC_DRAW);


            final Matrix3F m = new Matrix3F();
            m.postTranslate(-1f, 1f);
            m.postScale(2f / w, -2f / h);
            m.getValues9(mat3);

            defaultProgram.bindAttr(gl, PositionColorProgram2f.POSITION_ATTRIB, 3, 7, 0);
            defaultProgram.bindAttr(gl, PositionColorProgram2f.COLOR_ATTRIB, 4, 7, 3);
            defaultProgram.bindUniform(gl, PositionColorProgram2f.MATRIX_UNIFORM, true, mat3);

            gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, triangles.size() * 3);
            defaultProgram.disableVertexAttrib(gl);

            gl.glDeleteBuffers(bufs.length, bufs, 0);
            gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, 0);
        }

        //gl.glBindBuffer(GL2ES2.GL_ARRAY_BUFFER, 0);

//
//        final FloatBuffer fbtest = ByteBuffer.allocateDirect(80).order(ByteOrder.nativeOrder()).asFloatBuffer();
//
//        gl.glGenBuffers(1, new int[10], 0);
//        gl.glBufferData(GL.GL_ARRAY_BUFFER, );
//
//        gl.glVertexAttribPointer(positionHandler, 3, GL2ES2.GL_FLOAT, false, 7 * 4, fbtest);
//        gl.glEnableVertexAttribArray(positionHandler);
//
//        gl.glVertexAttribPointer(colorHandler, 4, GL2ES2.GL_FLOAT, false, 7*4, 3*4);
//        gl.glEnableVertexAttribArray(colorHandler);
//
//        gl.glUniformMatrix3fv(matrixHandler, 1, false, mat3, 0);
//        gl.glDrawArrays(GL2ES2.GL_TRIANGLES, 0, );
        /*
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(-1f, 1f, 0f);
        gl.glScalef(2f / w, -2f / h, 1f);
        */


        /*gl.glBegin(GL.GL_TRIANGLES);
        for (Triangle t : triangles) {
            gl.glColor3f(Color.redFloat(t.color), Color.greenFloat(t.color), Color.blueFloat(t.color));
            for (int i=0; i<t.getVertexCount(); i++) {
                gl.glVertex2d(t.getVertexX(i), t.getVertexY(i));
            }
        }
        gl.glEnd();
        */
        //gl.

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL gl = drawable.getGL();
        gl.glViewport(x, y, width, height);
        this.w = width;
        this.h = height;
    }

    public void mainLoop(GLAutoDrawable drawable) {
        long t = -1L;
        while (running) {

            final long ct = System.currentTimeMillis();
            final float dt = (t < 0 ? 0f : (float) (ct - t) / 1000f);
            t = ct;

            for (Triangle o : triangles) {
                o.update(w, h, dt);
            }

            drawable.display();
            Thread.yield();
        }
    }

}
