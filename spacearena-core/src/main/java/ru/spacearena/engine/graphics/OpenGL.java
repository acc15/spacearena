package ru.spacearena.engine.graphics;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * <p>Interface which forwards calls to real OpenGL (OpenGL 2 over JOGL for desktops, OpenGL ES 2 for Android)
 * implementation.</p>
 *
 * <p>It targets simplification, abstraction of access and making calls more java-friendly by:<ul>
 *     <li>using enums instead of integer constants</li>
 *     <li>eliminating all string length parameters</li>
 *     <li>removing methods and parameters which is not in subset of native Android GLES2 implementation and JOGL GL2
 *      (for example look at {@code glGetProgramInfoLog} and {@code glGetShaderInfoLog})</li>
 * </ul></p>
 *
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public interface OpenGL {

    public static final int DEPTH_BUFFER_BIT = 0x00000100;
    public static final int STENCIL_BUFFER_BIT = 0x00000400;
    public static final int COLOR_BUFFER_BIT = 0x00004000;


    public enum PrimitiveType {
        POINTS(0x0000),
        LINES(0x0001),
        LINE_LOOP(0x0002),
        LINE_STRIP(0x0003),
        TRIANGLES(0x0004),
        TRIANGLE_STRIP(0x0005),
        TRIANGLE_FAN(0x0006);

        private int glCode;
        PrimitiveType(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    public enum BufferUsage {
        STREAM_DRAW(0x88E0),
        STATIC_DRAW(0x88E4),
        DYNAMIC_DRAW(0x88E8);

        private int glCode;
        BufferUsage(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    public enum BufferType {
        ARRAY(0x8892),
        ELEMENT_ARRAY(0x8893);

        private int glCode;
        BufferType(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    public enum ShaderType {
        VERTEX(0x8B31),
        FRAGMENT(0x8B30);

        private int glCode;
        ShaderType(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() {
            return glCode;
        }
    }

    public enum ShaderParam {
        SHADER_TYPE(0x8B4F),
        DELETE_STATUS(0x8B80),
        COMPILE_STATUS(0x8B81),
        INFO_LOG_LENGTH(0x8B84),
        SHADER_SOURCE_LENGTH(0x8B88);

        private int glCode;
        ShaderParam(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    public enum Type {
        BYTE(0x1400, 1),
        UNSIGNED_BYTE(0x1401, 1),
        SHORT(0x1402, 2),
        FLOAT(0x1406, 4),
        FIXED(0x140C, 4),
        UNSIGNED_SHORT(0x1403, 2);

        private int glCode;
        private int size;

        Type(int glCode, int size) {
            this.glCode = glCode;
            this.size = size;
        }

        public int getSize() { return size; }
        public int glCode() { return glCode; }
    }

    public enum ProgramParam {
        DELETE_STATUS(0x8B80),
        LINK_STATUS(0x8B82),
        VALIDATE_STATUS(0x8B83),
        INFO_LOG_LENGTH(0x8B84),
        ATTACHED_SHADERS(0x8B85),
        ACTIVE_ATTRIBUTES(0x8B89),
        ACTIVE_ATTRIBUTE_MAX_LENGTH(0x8B8A),
        ACTIVE_UNIFORMS(0x8B86),
        ACTIVE_UNIFORM_MAX_LENGTH(0x8B87);

        private int glCode;
        ProgramParam(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    void viewport(int x, int y, int width, int height);

    void clearColor(float r, float g, float b, float a);
    void clear(int mask);

    int createShader(ShaderType type);
    void shaderSource(int shaderId, String source);
    void compileShader(int shaderId);
    int getShader(int shaderId, ShaderParam param);
    String getShaderInfoLog(int shaderId);
    void deleteShader(int shaderId);

    int createProgram();
    void attachShader(int programId, int shaderId);
    void linkProgram(int programId);
    void deleteProgram(int programId);
    void validateProgram(int programId);
    int getProgram(int programId, ProgramParam param);
    String getProgramInfoLog(int programId);
    void useProgram(int programId);

    void bindAttribLocation(int programId, int attrIndex, String attribute);
    int getAttribLocation(int programId, String attribute);
    int getUniformLocation(int programId, String uniform);

    void genBuffers(int length, int[] bufs, int offset);
    void deleteBuffers(int length, int[] bufs, int offset);
    void genBuffers(int length, IntBuffer bufs);
    void deleteBuffers(int length, IntBuffer bufs);
    int genBuffer();
    void deleteBuffer(int buf);

    void bindBuffer(BufferType type, int bufferId);
    void bufferData(BufferType type, int bufferSize, Buffer buffer, BufferUsage usage);

    void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, Buffer buffer);
    void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, int offset);

    void vertexAttrib(int attrIndex, float x);
    void vertexAttrib(int attrIndex, float x, float y);
    void vertexAttrib(int attrIndex, float x, float y, float z);
    void vertexAttrib(int attrIndex, float x, float y, float z, float w);
    void vertexAttrib1(int attrIndex, float[] values, int offset);
    void vertexAttrib2(int attrIndex, float[] values, int offset);
    void vertexAttrib3(int attrIndex, float[] values, int offset);
    void vertexAttrib4(int attrIndex, float[] values, int offset);
    void vertexAttrib1(int attrIndex, FloatBuffer buffer);
    void vertexAttrib2(int attrIndex, FloatBuffer buffer);
    void vertexAttrib3(int attrIndex, FloatBuffer buffer);
    void vertexAttrib4(int attrIndex, FloatBuffer buffer);

    void uniformMatrix2(int location, int count, float[] values, int offset);
    void uniformMatrix3(int location, int count, float[] values, int offset);
    void uniformMatrix4(int location, int count, float[] values, int offset);
    void uniformMatrix2(int location, int count, FloatBuffer buffer);
    void uniformMatrix3(int location, int count, FloatBuffer buffer);
    void uniformMatrix4(int location, int count, FloatBuffer buffer);
    void uniform(int location, float x);
    void uniform(int location, float x, float y);
    void uniform(int location, float x, float y, float z);
    void uniform(int location, float x, float y, float z, float w);
    void uniform1(int location, int count, float[] values, int offset);
    void uniform2(int location, int count, float[] values, int offset);
    void uniform3(int location, int count, float[] values, int offset);
    void uniform4(int location, int count, float[] values, int offset);
    void uniform1(int location, int count, FloatBuffer buf);
    void uniform2(int location, int count, FloatBuffer buf);
    void uniform3(int location, int count, FloatBuffer buf);
    void uniform4(int location, int count, FloatBuffer buf);

    void enableVertexAttribArray(int attrIndex);
    void disableVertexAttribArray(int attrIndex);

    void drawArrays(PrimitiveType type, int offset, int count);
    public void drawElements(PrimitiveType type, int count, Type indexType, Buffer indices);
    public void drawElements(PrimitiveType type, int count, Type indexType, int indexOffset);

}
