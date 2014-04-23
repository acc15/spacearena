package ru.spacearena.engine.graphics.vbo;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayout {

    private final int[] offsets;
    private final int[] types;

    private VertexBufferLayout(int[] offsets, int[] types) {
        this.offsets = offsets;
        this.types = types;
    }

    /**
     * Returns vertex attribute data offset in bytes
     * @param index attribute index
     * @return vertex attribute data offset in bytes
     */
    public int getOffset(int index) {
        return index > 0 ? offsets[index-1] : 0;
    }

    /**
     * Returns count of types used for vertex attribute
     * @param index attribute index
     * @return count of types used for vertex attribute
     */
    public int getCount(int index) {
        return toTypes(getSize(index), getType(index));
    }

    /**
     * Returns type of attribute
     * @param index attribute index
     * @return vertex attribute type
     */
    public int getType(int index) {
        return types[index];
    }

    /**
     * Returns count of bytes used for vertex attribute
     * @param index attrSize index
     * @return count of bytes
     */
    public int getSize(int index) {
        return index > 0 ? offsets[index] - offsets[index-1] : offsets[index];
    }

    /**
     * Returns amount of bytes used for single vertex
     * @return amount of bytes used for single vertex
     */
    public int getStride() {
        return offsets[offsets.length-1];
    }

    public int getAttrCount() { return offsets.length; }

    public int computeBufferSize(int numVertices) {
        return numVertices * getStride();
    }

    public int computeVertexCount(int bufferSize) {
        return bufferSize / getStride();
    }

    public static int toBytes(int amount, int type) {
        switch (type) {
        case OpenGL.GL_BYTE:
        case OpenGL.GL_UNSIGNED_BYTE:
            return amount;

        case OpenGL.GL_SHORT:
        case OpenGL.GL_UNSIGNED_SHORT:
            return amount << 1;

        case OpenGL.GL_FIXED:
        case OpenGL.GL_FLOAT:
        case OpenGL.GL_INT:
        case OpenGL.GL_UNSIGNED_INT:
            return amount << 2;

        default:
            throw new UnsupportedOperationException("Unknown type: " + type);
        }
    }

    public static int toTypes(int bytes, int type) {
        switch (type) {
        case OpenGL.GL_BYTE:
        case OpenGL.GL_UNSIGNED_BYTE:
            return bytes;

        case OpenGL.GL_SHORT:
        case OpenGL.GL_UNSIGNED_SHORT:
            return bytes >> 1;

        case OpenGL.GL_FIXED:
        case OpenGL.GL_FLOAT:
        case OpenGL.GL_INT:
        case OpenGL.GL_UNSIGNED_INT:
            return bytes >> 2;

        default:
            throw new UnsupportedOperationException("Unknown type: " + type);
        }
    }

    public static int[] toArray(IntArrayList l) {
        final int size = l.size();
        final int[] src = l.elements();
        final int[] dst = new int[size];
        System.arraycopy(src, 0, dst, 0, size);
        return dst;
    }

    public static Builder create() {
        return new Builder();
    }

    public static final class Builder {

        private final IntArrayList offsets = new IntArrayList();
        private final IntArrayList types = new IntArrayList();

        public Builder shorts(int amount) {
            return attr(amount, OpenGL.GL_SHORT);
        }

        public Builder ushorts(int amount) {
            return attr(amount, OpenGL.GL_UNSIGNED_SHORT);
        }

        public Builder floats(int amount) {
            return attr(amount, OpenGL.GL_FLOAT);
        }

        public Builder bytes(int bytes) {
            return attr(bytes, OpenGL.GL_BYTE);
        }

        public Builder ubytes(int ubytes) {
            return attr(ubytes, OpenGL.GL_UNSIGNED_BYTE);
        }

        public Builder ints(int ints) {
            return attr(ints, OpenGL.GL_INT);
        }

        public Builder uints(int uints) {
            return attr(uints, OpenGL.GL_UNSIGNED_INT);
        }

        public Builder attr(int amount, int type) {
            offsets.add((offsets.isEmpty() ? 0 : offsets.get(offsets.size()-1)) + toBytes(amount, type));
            types.add(type);
            return this;
        }

        public VertexBufferLayout build() {
            return new VertexBufferLayout(toArray(offsets), toArray(types));
        }
    }


}
