package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBufferObject extends VBODefinition {

    private int id;

    private VertexBufferLayout layout = null;
    private int sizeInBytes = -1;

    public VertexBufferObject(OpenGL.BufferType type, OpenGL.BufferUsage usage) {
        super(type, usage);
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public boolean isCreated() {
        return id != 0;
    }

    public boolean isUploaded() {
        return sizeInBytes >= 0;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void upload(OpenGL gl, VertexBuffer buffer) {
        this.layout = buffer.getLayout();
        this.sizeInBytes = buffer.getSizeInBytes();

        create(gl);
        bind(gl);
        gl.bufferData(getBufferType(), sizeInBytes, buffer.prepareBuffer(), getBufferUsage());
        unbind(gl);
    }

    public void bind(OpenGL gl) {
        if (id == 0) {
            throw new IllegalStateException("Buffer not created");
        }
        gl.bindBuffer(getBufferType(), id);
    }

    public void unbind(OpenGL gl) {
        gl.bindBuffer(getBufferType(), 0);
    }

    public void delete(OpenGL gl) {
        if (this.id != 0) {
            gl.deleteBuffer(id);
            markDead();
        }
    }

    public void create(OpenGL gl) {
        if (id == 0) {
            id = gl.genBuffer();
        }
    }

    public void markDead() {
        this.id = 0;
        this.layout = null;
    }

    public int getId() {
        return id;
    }
}
