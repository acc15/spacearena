package ru.spacearena.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBuffer {

    public static final int FLOAT_SIZE = 4;
    public static final int MAX_ITEMS = 8;

    int item = 0;
    int size = -1;
    final int[] items = new int[MAX_ITEMS];

    final FloatBuffer buffer;

    public VertexBuffer(int size) {
        buffer = ByteBuffer.allocateDirect(size * FLOAT_SIZE).
                order(ByteOrder.nativeOrder()).
                asFloatBuffer();
    }

    public VertexBuffer reset() {
        this.item = 0;
        this.size = -1;
        buffer.rewind();
        return this;
    }

    public int getCount(int item) {
        return item > 0 ? items[item] - items[item - 1] : items[item];
    }

    public int getOffset(int i) {
        return i > 0 ? items[i-1] : 0;
    }

    public int getStride() {
        return items[item-1] * FLOAT_SIZE;
    }

    public VertexBuffer layout(int size) {
        items[item] = item > 0 ? items[item-1] + size : size;
        ++item;
        return this;
    }

    private FloatBuffer storeSizeAndRewindTo(int offset) {
        if (this.size < 0) {
            this.size = buffer.position() * FLOAT_SIZE;
        }
        buffer.position(offset);
        return buffer;
    }

    public int size() {
        return size < 0 ? buffer.position() * FLOAT_SIZE : size;
    }

    public FloatBuffer getBuffer() {
        return storeSizeAndRewindTo(0);
    }

    public FloatBuffer getBuffer(int item) {
        return storeSizeAndRewindTo(getOffset(item));
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
