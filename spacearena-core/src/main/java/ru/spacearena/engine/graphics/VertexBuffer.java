package ru.spacearena.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBuffer {

    private final FloatBuffer buffer;
    private VertexBufferLayout layout;

    public VertexBuffer(int size) {
        buffer = ByteBuffer.allocateDirect(OpenGL.Type.FIXED.toBytes(size)).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
    }

    public VertexBuffer reset() {
        buffer.rewind();
        return this;
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public VertexBuffer layout(VertexBufferLayout l) {
        this.layout = l;
        return this;
    }

    public int getSizeInBytes() {
        return OpenGL.Type.FLOAT.toBytes(buffer.position());
    }

    public FloatBuffer prepareBuffer() {
        return prepareBuffer(0);
    }

    public FloatBuffer prepareBuffer(int item) {
        buffer.position(layout.getOffset(item));
        return buffer;
    }

    public VertexBuffer put(float x) {
        buffer.put(x);
        return this;
    }

    public VertexBuffer put(float x, float y) {
        buffer.put(x).put(y);
        return this;
    }

    public VertexBuffer put(float x, float y, float z) {
        buffer.put(x).put(y).put(z);
        return this;
    }

    public VertexBuffer put(float x, float y, float z, float w) {
        buffer.put(x).put(y).put(z).put(w);
        return this;
    }

    public VertexBuffer put(float[] points) {
        buffer.put(points);
        return this;
    }

    public VertexBuffer put(float[] points, int start, int size) {
        buffer.put(points, start, size);
        return this;
    }
}
