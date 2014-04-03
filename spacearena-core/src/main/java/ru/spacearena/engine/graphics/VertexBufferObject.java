package ru.spacearena.engine.graphics;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
class VertexBufferObject {

    private int id;

    private final OpenGL.BufferType type;
    private final OpenGL.BufferUsage usage;
    private VertexBufferLayout layout;

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

    public boolean isUploaded() { return layout != null; }

    public void upload(OpenGL gl, VertexBuffer buffer) {
        create(gl);

        final int size = buffer.getSizeInBytes();
        this.layout = buffer.getLayout();
        gl.bufferData(type, size, buffer.prepareBuffer(), usage);
    }

    public void bind(OpenGL gl) {
        if (id == 0) {
            throw new IllegalStateException("Buffer not created");
        }
        gl.bindBuffer(type, id);
    }

    public void delete(OpenGL gl) {
        if (this.id != 0) {
            gl.deleteBuffer(id);
            this.id = 0;
        }
    }

    public void create(OpenGL gl) {
        if (id == 0) {
            id = gl.genBuffer();
        }
    }
}
