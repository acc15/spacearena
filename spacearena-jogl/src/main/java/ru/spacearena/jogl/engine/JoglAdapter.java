package ru.spacearena.jogl.engine;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class JoglAdapter implements GLEventListener {

    private final JoglGL2 gl = new JoglGL2();
    private final JoglListener listener;
    private boolean debug = false;

    public JoglAdapter(JoglListener listener) {
        this.listener = listener;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public GL2 getGL2(GLAutoDrawable drawable) {
        final GL2 gl2 = drawable.getGL().getGL2();
        return debug ? new DebugGL2(gl2) : gl2;
    }

    public void init(GLAutoDrawable drawable) {
        try {
            gl.setGL2(getGL2(drawable));
            listener.init(gl);
        } finally {
            gl.setGL2(null);
        }
    }

    public void dispose(GLAutoDrawable drawable) {
        try {
            gl.setGL2(getGL2(drawable));
            listener.dispose(gl);
        } finally {
            gl.setGL2(null);
        }
    }

    public void display(GLAutoDrawable drawable) {
        try {
            gl.setGL2(getGL2(drawable));
            listener.display(gl);
        } finally {
            gl.setGL2(null);
        }
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        try {
            gl.setGL2(getGL2(drawable));
            listener.reshape(gl, x, y, width, height);
        } finally {
            gl.setGL2(null);
        }
    }
}
