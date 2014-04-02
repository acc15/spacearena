package ru.spacearena.engine.graphics;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-03-04
 */
public class VertexBufferLayout {

    public static final int MAX_ATTR_COUNT = 8;
    public static final OpenGL.Type DEFAULT_TYPE = OpenGL.Type.FLOAT;

    private int[] offsets = new int[MAX_ATTR_COUNT];
    private int index = 0;

    /**
     * Resets buffer layout
     * @return {@code this}
     */
    public VertexBufferLayout reset() {
        index = 0;
        return this;
    }

    /**
     * Specifies size for {@link #getAttribute()} attribute and advances attribute index
     * @param size amount of #({@link OpenGL.Type}) used for vertex attribute
     * @return {@code this}
     */
    public VertexBufferLayout attrSize(int size, OpenGL.Type type) {
        return attrBytes(type.toBytes(size));
    }

    /**
     * Specifies size for {@link #getAttribute()} attribute in {@link #DEFAULT_TYPE} and advances attribute index
     * @param size amount of #({@link #DEFAULT_TYPE}) elements used for vertex attribute
     * @return {@code this}
     */
    public VertexBufferLayout attrSize(int size) {
        return attrSize(size, DEFAULT_TYPE);
    }

    /**
     * Specifies amount of bytes for {@link #getAttribute()} attribute in bytes and advances attribute index
     * @param bytes amount of bytes used for vertex attrSize
     * @return {@code this}
     */
    public VertexBufferLayout attrBytes(int bytes) {
        offsets[index] = (index > 0 ? offsets[index-1] : 0) + bytes;
        ++index;
        return this;
    }

    /**
     * Returns vertex attribute data offset in {@link OpenGL.Type} elements
     * @param attr attrSize index
     * @return vertex attrSize data offset in {@link OpenGL.Type} elements
     */
    public int getOffset(int attr, OpenGL.Type type) {
        return type.toTypes(getOffsetInBytes(attr));
    }

    /**
     * Returns vertex attribute data offset in {@link #DEFAULT_TYPE} elements
     * @param attr attrSize index
     * @return vertex attrSize data offset in {@link #DEFAULT_TYPE} elements
     */
    public int getOffset(int attr) {
        return getOffset(attr, DEFAULT_TYPE);
    }

    /**
     * Returns vertex attribute data in bytes
     * @param attr attrSize index
     * @return vertex attribute data attrSize in bytes
     */
    public int getOffsetInBytes(int attr) {
        return checkArg(attr) > 0 ? offsets[attr-1] : 0;
    }

    /**
     * Returns count of {@link OpenGL.Type type} elements used for vertex attribute
     * @param attr attribute index
     * @param type attribute type
     * @return count of elements measured using specified {@link OpenGL.Type}
     */
    public int getCount(int attr, OpenGL.Type type) {
        return type.toTypes(getCountInBytes(attr));
    }

    /**
     * Returns count of {@link #DEFAULT_TYPE} elements used for vertex attribute
     * @param attr attribute index
     * @return count of elements measured using specified {@link OpenGL.Type}
     */
    public int getCount(int attr) {
        return getCount(attr, DEFAULT_TYPE);
    }

    /**
     * Returns count of bytes used for vertex attrSize
     * @param attr attrSize index
     * @return count of bytes
     */
    public int getCountInBytes(int attr) {
        return checkArg(attr) > 0 ? offsets[attr] - offsets[attr-1] : offsets[attr];
    }

    /**
     * Returns amount of bytes used for single vertex
     * @return amount of bytes used for single vertex
     */
    public int getStride() {
        checkAttr();
        return offsets[index-1];
    }

    /**
     * Returns attrSize index for which next call to {@link #attrSize} will append attrSize information.
     * Or simply it return count of specified attributes
     * @return current attrSize index
     */
    public int getAttribute() {
        return index;
    }

    private int checkArg(int arg) {
        if (arg < 0 || arg >= index) {
            throw new IllegalArgumentException("Offset not specified for attrSize index " + index);
        }
        return arg;
    }

    private int checkAttr() {
        if (index <= 0) {
            throw new IllegalArgumentException("Offset not specified for attrSize index " + index);
        }
        return index;
    }

}
