package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBufferObject {

    public static interface Definition {
        int getBufferType();
        int getBufferUsage();
    }

    private int id;
    private VertexBufferLayout layout = null;
    private int size = 0;

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public int getSize() {
        return size;
    }

    public void upload(OpenGL gl, VertexBufferObject.Definition definition, VertexBuffer buffer) {
        this.layout = buffer.getLayout();
        this.size = buffer.getSize();
        if (id == 0) {
            id = gl.glGenBuffer();
        }
        final int bufferType = definition.getBufferType();
        gl.glBindBuffer(bufferType, id);
        gl.glBufferData(bufferType, size, buffer.prepareBuffer(), definition.getBufferUsage());
        gl.glBindBuffer(bufferType, 0);
    }

    public void delete(OpenGL gl) {
        if (this.id != 0) {
            gl.glDeleteBuffer(id);
            reset();
        }
    }

    public void reset() {
        this.id = 0;
        this.size = 0;
        this.layout = null;
    }

    public int getId() {
        return id;
    }
}
