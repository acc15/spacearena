package ru.spacearena.engine.graphics.vbo;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayout {

    private final int[] offsets;

    private VertexBufferLayout(int... byteOffsets) {
        this.offsets = byteOffsets;
    }

    /**
     * Returns vertex attribute data in bytes
     * @param index attrSize index
     * @return vertex attribute data attrSize in bytes
     */
    public int getOffset(int index) {
        return checkArg(index) > 0 ? offsets[index-1] : 0;
    }

    /**
     * Returns count of bytes used for vertex attrSize
     * @param index attrSize index
     * @return count of bytes
     */
    public int getCount(int index) {
        return checkArg(index) > 0 ? offsets[index] - offsets[index-1] : offsets[index];
    }

    /**
     * Returns amount of bytes used for single vertex
     * @return amount of bytes used for single vertex
     */
    public int getStride() {
        return offsets[offsets.length-1];
    }

    private int checkArg(int arg) {
        if (arg < 0 || arg >= offsets.length) {
            throw new IllegalArgumentException("Offset not specified for item " + arg);
        }
        return arg;
    }

    public static final class Builder {

        private IntArrayList list = new IntArrayList();

        public Builder shorts(int amount) {
            return bytes(OpenGL.Type.SHORT.toBytes(amount));
        }

        public Builder floats(int amount) {
            return bytes(OpenGL.Type.FLOAT.toBytes(amount));
        }

        public Builder bytes(int bytes) {
            list.add((list.isEmpty() ? 0 : list.get(list.size()-1)) + bytes);
            return this;
        }

        public VertexBufferLayout build() {
            final int[] src = list.elements();
            final int[] dst = new int[list.size()];
            System.arraycopy(src,0,dst,0,list.size());
            return new VertexBufferLayout(dst);
        }
    }


}
