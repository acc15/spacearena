package ru.spacearena.engine.graphics.vbo;

import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.vbo.strategies.TimeBasedShrinkStrategy;
import ru.spacearena.engine.graphics.vbo.strategies.GrowShrinkStrategy;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
* @author Vyacheslav Mayorov
* @since 2014-02-04
*/
public class VertexBuffer {

    public static final int DEFAULT_CAPACITY = 128; // 16 float 2D points

    private ByteBuffer buffer;
    private VertexBufferLayout layout;
    private int bufferSize;
    private GrowShrinkStrategy growShrinkStrategy;

    public VertexBuffer() {
        this(DEFAULT_CAPACITY);
    }

    public VertexBuffer(int capacity) {
        this(new TimeBasedShrinkStrategy(), capacity);
    }

    public VertexBuffer(GrowShrinkStrategy strategy) {
        this(strategy, DEFAULT_CAPACITY);
    }

    public VertexBuffer(GrowShrinkStrategy strategy, int capacity) {
        this.growShrinkStrategy = strategy;
        this.buffer = allocateBuffer(capacity);
    }

    public VertexBufferLayout getLayout() {
        return layout;
    }

    public VertexBuffer reset(VertexBufferLayout l, int requiredCapacity) {
        final int currentCapacity = buffer.capacity();
        final int newCapacity = growShrinkStrategy.computeCapacity(currentCapacity, requiredCapacity);
        if (newCapacity != currentCapacity) {
            buffer = allocateBuffer(newCapacity);
        } else {
            buffer.clear();
        }
        this.layout = l;
        this.bufferSize = -1;
        return this;
    }

    public VertexBuffer reset(VertexBufferLayout l) {
        return reset(l, getSize());
    }

    public int getSize() {
        return bufferSize < 0 ? buffer.position() : bufferSize;
    }

    public ByteBuffer prepareBuffer() {
        return prepareBuffer(0);
    }

    public ByteBuffer prepareBuffer(int item) {
        if (bufferSize < 0) {
            bufferSize = buffer.position();
        }
        buffer.position(layout.getOffset(item));
        return buffer;
    }

    private void ensureCanStoreMore(int sizeInBytes) {
        final int currentCapacity = buffer.capacity();
        final int requiredCapacity = buffer.position() + sizeInBytes;
        if (requiredCapacity < currentCapacity) {
            return;
        }

        final int newCapacity = growShrinkStrategy.computeCapacity(currentCapacity, requiredCapacity);
        if (newCapacity < requiredCapacity) {
            throw new BufferOverflowException();//"Can't store more " + sizeInBytes + " bytes");
        }

        final ByteBuffer oldBuffer = this.buffer;
        this.buffer = allocateBuffer(newCapacity);

        oldBuffer.flip();
        this.buffer.put(oldBuffer);
    }

    public VertexBuffer put(float x) {
        ensureCanStoreMore(VertexBufferLayout.toBytes(1, OpenGL.GL_FLOAT));
        buffer.putFloat(x);
        return this;
    }

    public VertexBuffer put(float x, float y) {
        ensureCanStoreMore(VertexBufferLayout.toBytes(2, OpenGL.GL_FLOAT));
        buffer.putFloat(x).putFloat(y);
        return this;
    }

    public VertexBuffer put(float x, float y, float z) {
        ensureCanStoreMore(VertexBufferLayout.toBytes(3, OpenGL.GL_FLOAT));
        buffer.putFloat(x).putFloat(y).putFloat(z);
        return this;
    }

    public VertexBuffer put(float x, float y, float z, float w) {
        ensureCanStoreMore(VertexBufferLayout.toBytes(4, OpenGL.GL_FLOAT));
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
        ensureCanStoreMore(VertexBufferLayout.toBytes(size, OpenGL.GL_FLOAT));
        for (int i=0; i<size; i++) {
            buffer.putFloat(points[start+i]);
        }
        return this;
    }

    private static ByteBuffer allocateBuffer(int capacity) {
        return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
    }
}
