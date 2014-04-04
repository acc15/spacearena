package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBufferObject {

    public static interface Definition {
        OpenGL.BufferType getBufferType();
        OpenGL.BufferUsage getBufferUsage();
    }

    private int id;
    private VertexBufferLayout layout = null;
    private int sizeInBytes = 0;

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void upload(OpenGL gl, VertexBufferObject.Definition definition, VertexBuffer buffer) {
        this.layout = buffer.getLayout();
        this.sizeInBytes = buffer.getSize();
        if (id == 0) {
            id = gl.genBuffer();
        }
        final OpenGL.BufferType bufferType = definition.getBufferType();
        gl.bindBuffer(bufferType, id);
        gl.bufferData(bufferType, sizeInBytes, buffer.prepareBuffer(), definition.getBufferUsage());
        gl.bindBuffer(bufferType, 0);
    }

    public void delete(OpenGL gl) {
        if (this.id != 0) {
            gl.deleteBuffer(id);
            reset();
        }
    }

    public void reset() {
        this.id = 0;
        this.sizeInBytes = 0;
        this.layout = null;
    }

    public int getId() {
        return id;
    }
}
