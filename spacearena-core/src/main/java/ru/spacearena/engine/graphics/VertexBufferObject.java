package ru.spacearena.engine.graphics;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
class VertexBufferObject {

    private int id;

    private final OpenGL.BufferType type;
    private final OpenGL.BufferUsage usage;
    private VertexBufferLayout layout = null;
    private int sizeInBytes = -1;

    public VertexBufferObject(OpenGL.BufferType type, OpenGL.BufferUsage usage) {
        this.type = type;
        this.usage = usage;
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
