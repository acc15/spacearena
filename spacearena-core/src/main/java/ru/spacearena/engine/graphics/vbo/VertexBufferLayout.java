package ru.spacearena.engine.graphics.vbo;

import cern.colt.list.IntArrayList;
import ru.spacearena.engine.graphics.OpenGL;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayout {

    public static final OpenGL.Type DEFAULT_TYPE = OpenGL.Type.FLOAT;

    private final int[] offsets;

//    public VertexBufferLayout(OpenGL.Type type, int... offsets) {
//        this.offsets = offsets;
//        offsets[0] = type.toBytes(offsets[0]);
//        for (int i=1; i<offsets.length; i++) {
//            offsets[i] = type.toBytes(offsets[i]) + offsets[i-1];
//        }
//    }

    private VertexBufferLayout(int... byteOffsets) {
        this.offsets = byteOffsets;
    }

    /**
     * Returns vertex attribute data offset in {@link OpenGL.Type} elements
     * @param index attrSize index
     * @return vertex attrSize data offset in {@link OpenGL.Type} elements
     */
    public int getOffset(int index, OpenGL.Type type) {
        return type.toTypes(getOffsetInBytes(index));
    }

    /**
     * Returns vertex attribute data offset in {@link #DEFAULT_TYPE} elements
     * @param index attrSize index
     * @return vertex attrSize data offset in {@link #DEFAULT_TYPE} elements
     */
    public int getOffset(int index) {
        return DEFAULT_TYPE.toTypes(getOffsetInBytes(index));
    }

    /**
     * Returns vertex attribute data in bytes
     * @param index attrSize index
     * @return vertex attribute data attrSize in bytes
     */
    public int getOffsetInBytes(int index) {
        return checkArg(index) > 0 ? offsets[index-1] : 0;
    }

    /**
     * Returns count of {@link OpenGL.Type type} elements used for vertex attribute
     * @param index attribute index
     * @param type attribute type
     * @return count of elements measured using specified {@link OpenGL.Type}
     */
    public int getCount(int index, OpenGL.Type type) {
        return type.toTypes(getCountInBytes(index));
    }

    /**
     * Returns count of {@link #DEFAULT_TYPE} elements used for vertex attribute
     * @param index attribute index
     * @return count of elements measured using specified {@link OpenGL.Type}
     */
    public int getCount(int index) {
        return DEFAULT_TYPE.toTypes(getCountInBytes(index));
    }

    /**
     * Returns count of bytes used for vertex attrSize
     * @param index attrSize index
     * @return count of bytes
     */
    public int getCountInBytes(int index) {
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

        public Builder size(int amount, OpenGL.Type type) {
            return sizeInBytes(type.toBytes(amount));
        }

        public Builder size(int amount) {
            return sizeInBytes(DEFAULT_TYPE.toBytes(amount));
        }

        public Builder sizeInBytes(int bytes) {
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
