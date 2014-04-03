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
    private OpenGL.BufferType type;
    private OpenGL.BufferUsage usage;

    public VertexBufferObject(Definition definition) {
        this.type = definition.getBufferType();
        this.usage = definition.getBufferUsage();
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void upload(OpenGL gl, VertexBuffer buffer) {
        this.layout = buffer.getLayout();
        this.sizeInBytes = buffer.getSizeInBytes();
        if (id == 0) {
            id = gl.genBuffer();
        }
        bind(gl);
        gl.bufferData(type, sizeInBytes, buffer.prepareBuffer(), usage);
        unbind(gl);
    }

    public void bind(OpenGL gl) {
        if (id == 0) {
            throw new IllegalStateException("Buffer not created");
        }
        gl.bindBuffer(type, id);
    }

    public void unbind(OpenGL gl) {
        gl.bindBuffer(type, 0);
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
