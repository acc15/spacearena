package ru.spacearena.jogl;

import com.jogamp.newt.Screen;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.util.TempUtils;

import javax.media.opengl.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-28-03
 */
public class JoglListener implements GLEventListener {

    private volatile boolean running = true;
    private int w, h;
    private int defaultShader;

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

        for (int i = 0; i < 1000; i++) {
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

        defaultShader = ShaderUtils.compileProgram(drawable.getGL().getGL2ES2(),
                "uniform mat3 u_MVPMatrix;      \n"     // Константа отвечающая за комбинацию матриц МОДЕЛЬ/ВИД/ПРОЕКЦИЯ.
              + "attribute vec3 a_Position;     \n"     // Информация о положении вершин.
              + "attribute vec4 a_Color;        \n"     // Информация о цвете вершин.
              + "varying vec4 v_Color;          \n"     // Это будет передано в фрагментный шейдер.
              + "void main()                    \n"     // Начало программы вершинного шейдера.
              + "{                              \n"
              + "   v_Color = a_Color;          \n"     // Передаем цвет для фрагментного шейдера.
                        // Он будет интерполирован для всего треугольника.
              + "   gl_Position = u_MVPMatrix   \n"     // gl_Position специальные переменные используемые для хранения конечного положения.
              + "               * a_Position;   \n"     // Умножаем вершины на матрицу для получения конечного положения
              + "}                              \n",   // в нормированных координатах экрана.,

                "precision mediump float;       \n"     // Устанавливаем по умолчанию среднюю точность для переменных. Максимальная точность
              + "varying vec4 v_Color;          \n"     // Цвет вершинного шейдера преобразованного
              + "void main()                    \n"     // Точка входа для фрагментного шейдера.
              + "{                              \n"
              + "   gl_FragColor = v_Color;     \n"     // Передаем значения цветов.
              + "}                              \n");

    }

    public void dispose(GLAutoDrawable drawable) {
    }

    public void display(GLAutoDrawable drawable) {
        final GL2ES2 gl = drawable.getGL().getGL2ES2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
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
