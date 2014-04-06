package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.Color;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBuffer {

    private ByteBuffer buffer;
    private VertexBufferLayout layout;
    private int size = -1;

    public VertexBuffer(int bytes) {
        buffer = ByteBuffer.allocateDirect(bytes).order(ByteOrder.nativeOrder());
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public VertexBuffer reset(VertexBufferLayout l) {
        this.layout = l;
        buffer.rewind();
        this.size = -1;
        return this;
    }

    public int getSize() {
        return size < 0 ? buffer.position() : size;
    }

    public ByteBuffer prepareBuffer() {
        return prepareBuffer(0);
    }

    public ByteBuffer prepareBuffer(int item) {
        if (size < 0) {
            size = buffer.position();
        }
        buffer.position(layout.getOffset(item));
        return buffer;
    }

    public VertexBuffer put(float x) {
        buffer.putFloat(x);
        return this;
    }

    public VertexBuffer put(float x, float y) {
        buffer.putFloat(x).putFloat(y);
        return this;
    }

    public VertexBuffer put(float x, float y, float z) {
        buffer.putFloat(x).putFloat(y).putFloat(z);
        return this;
    }

    public VertexBuffer put(float x, float y, float z, float w) {
        buffer.putFloat(x).putFloat(y).putFloat(z).putFloat(w);
        return this;
    }

    public VertexBuffer putRGB(Color color) {
        return put(color.r, color.g, color.b);
    }

    public VertexBuffer putRGBA(Color color) {
        return put(color.r, color.g, color.b, color.a);
    }

    public VertexBuffer put(float[] points) {
        return put(points, 0, points.length);
    }

    public VertexBuffer put(float[] points, int start, int size) {
        for (int i=0; i<size; i++) {
            buffer.putFloat(points[start+i]);
        }
        return this;
    }
}
