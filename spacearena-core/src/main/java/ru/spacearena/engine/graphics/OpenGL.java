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

    void lineWidth(float width);

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
        BYTE(0x1400, 0),
        UNSIGNED_BYTE(0x1401, 0),
        SHORT(0x1402, 1),
        FLOAT(0x1406, 2),
        FIXED(0x140C, 2),
        UNSIGNED_SHORT(0x1403, 2);

        private int glCode;
        private int shift;

        Type(int glCode, int shift) {
            this.glCode = glCode;
            this.shift = shift;
        }

        public int toBytes(int types) {
            return types << shift;
        }

        public int toTypes(int bytes) {
            return bytes >> shift;
        }

        public int getShift() { return shift; }
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

    public enum GenericParameter {
        ACTIVE_TEXTURE(0x84E0),
        ALIASED_LINE_WIDTH_RANGE(0x846E),
        ALIASED_POINT_SIZE_RANGE(0x846D),
        ALPHA_BITS(0x0D55),
        ARRAY_BUFFER_BINDING(0x8894),
        BLEND(0x0BE2),
        BLEND_COLOR(0x8005),
        BLEND_DST_ALPHA(0x80CA),
        BLEND_DST_RGB(0x80C8),
        BLEND_EQUATION_ALPHA(0x883D),
        BLEND_EQUATION_RGB(0x8009),
        BLEND_SRC_ALPHA(0x80CB),
        BLEND_SRC_RGB(0x80C9),
        BLUE_BITS(0x0D54),
        COLOR_CLEAR_VALUE(0x0C22),
        COLOR_WRITEMASK(0x0C23),
        COMPRESSED_TEXTURE_FORMATS(0x86A3),
        CULL_FACE(0x0B44),
        CULL_FACE_MODE(0x0B45),
        CURRENT_PROGRAM(0x8B8D),
        DEPTH_BITS(0x0D56),
        DEPTH_CLEAR_VALUE(0x0B73),
        DEPTH_FUNC(0x0B74),
        DEPTH_RANGE(0x0B70),
        DEPTH_TEST(0x0B71),
        DEPTH_WRITEMASK(0x0B72),
        DITHER(0x0BD0),
        ELEMENT_ARRAY_BUFFER_BINDING(0x8895),
        FRAMEBUFFER_BINDING(0x8CA6),
        FRONT_FACE(0x0B46),
        GENERATE_MIPMAP_HINT(0x8192),
        GREEN_BITS(0x0D53),
        IMPLEMENTATION_COLOR_READ_FORMAT(0x8B9B),
        IMPLEMENTATION_COLOR_READ_TYPE(0x8B9A),
        LINE_WIDTH(0x0B21),
        MAX_COMBINED_TEXTURE_IMAGE_UNITS(0x8B4D),
        MAX_CUBE_MAP_TEXTURE_SIZE(0x851C),
        MAX_FRAGMENT_UNIFORM_VECTORS(0x8DFD),
        MAX_RENDERBUFFER_SIZE(0x84E8),
        MAX_TEXTURE_IMAGE_UNITS(0x8872),
        MAX_TEXTURE_SIZE(0x0D33),
        MAX_VARYING_VECTORS(0x8DFC),
        MAX_VERTEX_ATTRIBS(0x8869),
        MAX_VERTEX_TEXTURE_IMAGE_UNITS(0x8B4C),
        MAX_VERTEX_UNIFORM_VECTORS(0x8DFB),
        MAX_VIEWPORT_DIMS(0x0D3A),
        NUM_COMPRESSED_TEXTURE_FORMATS(0x86A2),
        NUM_SHADER_BINARY_FORMATS(0x8DF9),
        PACK_ALIGNMENT(0x0D05),
        POLYGON_OFFSET_FACTOR(0x8038),
        POLYGON_OFFSET_FILL(0x8037),
        POLYGON_OFFSET_UNITS(0x2A00),
        RED_BITS(0x0D52),
        RENDERBUFFER_BINDING(0x8CA7),
        SAMPLE_ALPHA_TO_COVERAGE(0x809E),
        SAMPLE_BUFFERS(0x80A8),
        SAMPLE_COVERAGE(0x80A0),
        SAMPLE_COVERAGE_INVERT(0x80AB),
        SAMPLE_COVERAGE_VALUE(0x80AA),
        SAMPLES(0x80A9),
        SCISSOR_BOX(0x0C10),
        SCISSOR_TEST(0x0C11),
        SHADER_BINARY_FORMATS(0x8DF8),
        SHADER_COMPILER(0x8DFA),
        STENCIL_BACK_FAIL(0x8801),
        STENCIL_BACK_FUNC(0x8800),
        STENCIL_BACK_PASS_DEPTH_FAIL(0x8802),
        STENCIL_BACK_PASS_DEPTH_PASS(0x8803),
        STENCIL_BACK_REF(0x8CA3),
        STENCIL_BACK_VALUE_MASK(0x8CA4),
        STENCIL_BACK_WRITEMASK(0x8CA5),
        STENCIL_BITS(0x0D57),
        STENCIL_CLEAR_VALUE(0x0B91),
        STENCIL_FAIL(0x0B94),
        STENCIL_FUNC(0x0B92),
        STENCIL_PASS_DEPTH_FAIL(0x0B95),
        STENCIL_PASS_DEPTH_PASS(0x0B96),
        STENCIL_REF(0x0B97),
        STENCIL_TEST(0x0B90),
        STENCIL_VALUE_MASK(0x0B93),
        STENCIL_WRITEMASK(0x0B98),
        SUBPIXEL_BITS(0x0D50),
        TEXTURE_BINDING_2D(0x8069),
        TEXTURE_BINDING_CUBE_MAP(0x8514),
        UNPACK_ALIGNMENT(0x0CF5),
        VIEWPORT(0x0BA2);

        private int glCode;
        GenericParameter(int glCode) {
            this.glCode = glCode;
        }
        public int glCode() { return glCode; }
    }

    void getInteger(GenericParameter parameter, int[] values, int offset);
    void getInteger(GenericParameter parameter, IntBuffer buf);

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

//    void genTextures(int count, int[] textures, int offset);
//    int getTexture();
//
//    void bindTexture(int type, int id);

}
