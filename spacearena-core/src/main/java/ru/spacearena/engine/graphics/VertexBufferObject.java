package ru.spacearena.engine.graphics;

import java.nio.Buffer;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
class VertexBufferObject {

    private int id;

    private final int[] items = new int[VertexBuffer.MAX_ITEMS];
    private int item = -1;
    private OpenGL.BufferType type;
    private OpenGL.BufferUsage usage;

    public VertexBufferObject(OpenGL.BufferType type, OpenGL.BufferUsage usage) {
        this.type = type;
        this.usage = usage;
    }

    public int getCount(int item) {
        return item > 0 ? items[item] - items[item - 1] : items[item];
    }

    public int getOffset(int i) {
        return i > 0 ? items[i-1] : 0;
    }

    public int getStride() {
        return items[item-1] * VertexBuffer.FLOAT_SIZE;
    }

    public boolean isCreated() {
        return id != 0;
    }

    public boolean isUploaded() { return item >= 0; }

    public void upload(OpenGL gl, VertexBuffer buffer) {
        create(gl);

        this.item = buffer.item;
        System.arraycopy(buffer.items, 0, items, 0, VertexBuffer.MAX_ITEMS);
        final Buffer b = buffer.getBuffer();
        gl.bufferData(type, buffer.size(), b, usage);
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
