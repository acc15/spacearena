package ru.spacearena.jogl.engine;

import ru.spacearena.engine.graphics.OpenGL;

import javax.media.opengl.GL2;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class JoglGL2 implements OpenGL {

    private GL2 gl2;

    private final String[] STRING_BUF = new String[1];
    private final int[] INT_BUF = new int[1];

    public void setGL2(GL2 gl2) {
        this.gl2 = gl2;
    }

    public void getInteger(GenericParameter parameter, int[] values, int offset) {
        gl2.glGetIntegerv(parameter.glCode(), values, offset);
    }

    public void getInteger(GenericParameter parameter, IntBuffer buf) {
        gl2.glGetIntegerv(parameter.glCode(), buf);
    }

    public void viewport(int x, int y, int width, int height) {
        gl2.glViewport(x, y, width, height);
    }

    public void clearColor(float r, float g, float b, float a) {
        gl2.glClearColor(r, g, b, a);
    }

    public void clear(int mask) {
        gl2.glClear(mask);
    }

    public void lineWidth(float width) {
        gl2.glLineWidth(width);
    }

    public void shaderSource(int shaderId, String source) {
        STRING_BUF[0] = source;
        gl2.glShaderSource(shaderId, 1, STRING_BUF, null);
        STRING_BUF[0] = null;
    }

    public int createShader(ShaderType type) {
        return gl2.glCreateShader(type.glCode());
    }

    public void compileShader(int shaderId) {
        gl2.glCompileShader(shaderId);
    }

    public int getShader(int shaderId, ShaderParam param) {
        gl2.glGetShaderiv(shaderId, param.glCode(), INT_BUF, 0);
        return INT_BUF[0];
    }

    public String getShaderInfoLog(int shaderId) {
        final int l = getShader(shaderId, ShaderParam.INFO_LOG_LENGTH);
        final byte[] buf = new byte[l];
        gl2.glGetShaderInfoLog(shaderId, l, null, 0, buf, 0);
        return new String(buf, 0, l-1);
    }

    public void deleteShader(int shaderId) {
        gl2.glDeleteShader(shaderId);
    }

    public int createProgram() {
        return gl2.glCreateProgram();
    }

    public void attachShader(int programId, int shaderId) {
        gl2.glAttachShader(programId, shaderId);
    }

    public void linkProgram(int programId) {
        gl2.glLinkProgram(programId);
    }

    public void deleteProgram(int programId) {
        gl2.glDeleteProgram(programId);
    }

    public void validateProgram(int programId) {
        gl2.glValidateProgram(programId);
    }

    public int getProgram(int programId, ProgramParam param) {
        gl2.glGetProgramiv(programId, param.glCode(), INT_BUF, 0);
        return INT_BUF[0];
    }

    public String getProgramInfoLog(int programId) {
        final int l = getProgram(programId, ProgramParam.INFO_LOG_LENGTH);
        final byte[] buf = new byte[l];
        gl2.glGetProgramInfoLog(programId, l, null, 0, buf, 0);
        return new String(buf, 0, l-1);
    }

    public void useProgram(int programId) {
        gl2.glUseProgram(programId);
    }

    public void bindAttribLocation(int programId, int attrIndex, String attribute) {
        gl2.glBindAttribLocation(programId, attrIndex, attribute);
    }

    public int getAttribLocation(int programId, String attribute) {
        return gl2.glGetAttribLocation(programId, attribute);
    }

    public int getUniformLocation(int programId, String uniform) {
        return gl2.glGetUniformLocation(programId, uniform);
    }

    public void genBuffers(int length, IntBuffer bufs) {
        gl2.glGenBuffers(length, bufs);
    }

    public void genBuffers(int length, int[] bufs, int offset) {
        gl2.glGenBuffers(length, bufs, offset);
    }

    public int genBuffer() {
        gl2.glGenBuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void deleteBuffer(int buf) {
        INT_BUF[0] = buf;
        gl2.glDeleteBuffers(1, INT_BUF, 0);
    }

    public void deleteBuffers(int length, IntBuffer bufs) {
        gl2.glDeleteBuffers(length, bufs);
    }

    public void deleteBuffers(int length, int[] bufs, int offset) {
        gl2.glDeleteBuffers(length, bufs, offset);
    }

    public void bindBuffer(BufferType type, int bufferId) {
        gl2.glBindBuffer(type.glCode(), bufferId);
    }

    public void bufferData(BufferType type, int bufferSize, Buffer buffer, BufferUsage usage) {
        gl2.glBufferData(type.glCode(), bufferSize, buffer, usage.glCode());
    }

    public void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, Buffer buffer) {
        gl2.glVertexAttribPointer(attrIndex, valueCount, type.glCode(), normalized, stride, buffer);
    }

    public void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, int offset) {
        gl2.glVertexAttribPointer(attrIndex, valueCount, type.glCode(), normalized, stride, offset);
    }

    public void vertexAttrib(int attrIndex, float x) {
        gl2.glVertexAttrib1f(attrIndex, x);
    }

    public void vertexAttrib(int attrIndex, float x, float y) {
        gl2.glVertexAttrib2f(attrIndex, x, y);
    }

    public void vertexAttrib(int attrIndex, float x, float y, float z) {
        gl2.glVertexAttrib3f(attrIndex, x, y, z);
    }

    public void vertexAttrib(int attrIndex, float x, float y, float z, float w) {
        gl2.glVertexAttrib4f(attrIndex, x, y, z, w);
    }

    public void vertexAttrib1(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib1fv(attrIndex, values, offset);
    }

    public void vertexAttrib2(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib2fv(attrIndex, values, offset);
    }

    public void vertexAttrib3(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib3fv(attrIndex, values, offset);
    }

    public void vertexAttrib4(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib4fv(attrIndex, values, offset);
    }

    public void vertexAttrib1(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib1fv(attrIndex, buffer);
    }

    public void vertexAttrib2(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib2fv(attrIndex, buffer);
    }

    public void vertexAttrib3(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib3fv(attrIndex, buffer);
    }

    public void vertexAttrib4(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib4fv(attrIndex, buffer);
    }

    public void uniformMatrix2(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix2fv(location, count, false, values, offset);
    }

    public void uniformMatrix3(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix3fv(location, count, false, values, offset);
    }

    public void uniformMatrix4(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix4fv(location, count, false, values, offset);
    }

    public void uniformMatrix2(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix2fv(location, count, false, buffer);
    }

    public void uniformMatrix3(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix3fv(location, count, false, buffer);
    }

    public void uniformMatrix4(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix4fv(location, count, false, buffer);
    }

    public void uniform(int location, float x) {
        gl2.glUniform1f(location, x);
    }

    public void uniform(int location, float x, float y) {
        gl2.glUniform2f(location, x, y);
    }

    public void uniform(int location, float x, float y, float z) {
        gl2.glUniform3f(location, x, y, z);
    }

    public void uniform(int location, float x, float y, float z, float w) {
        gl2.glUniform4f(location, x, y, z, w);
    }

    public void uniform1(int location, int count, float[] values, int offset) {
        gl2.glUniform1fv(location, count, values, offset);
    }

    public void uniform2(int location, int count, float[] values, int offset) {
        gl2.glUniform2fv(location, count, values, offset);
    }

    public void uniform3(int location, int count, float[] values, int offset) {
        gl2.glUniform3fv(location, count, values, offset);
    }

    public void uniform4(int location, int count, float[] values, int offset) {
        gl2.glUniform4fv(location, count, values, offset);
    }

    public void uniform1(int location, int count, FloatBuffer buf) {
        gl2.glUniform1fv(location, count, buf);
    }

    public void uniform2(int location, int count, FloatBuffer buf) {
        gl2.glUniform2fv(location, count, buf);
    }

    public void uniform3(int location, int count, FloatBuffer buf) {
        gl2.glUniform3fv(location, count, buf);
    }

    public void uniform4(int location, int count, FloatBuffer buf) {
        gl2.glUniform4fv(location, count, buf);
    }

    public void enableVertexAttribArray(int attrIndex) {
        gl2.glEnableVertexAttribArray(attrIndex);
    }

    public void disableVertexAttribArray(int attrIndex) {
        gl2.glDisableVertexAttribArray(attrIndex);
    }

    public void drawArrays(PrimitiveType type, int offset, int count) {
        gl2.glDrawArrays(type.glCode(), offset, count);
    }

    public void drawElements(PrimitiveType type, int count, Type indexType, Buffer indices) {
        gl2.glDrawElements(type.glCode(), count, indexType.glCode(), indices);
    }

    public void drawElements(PrimitiveType type, int count, Type indexType, int indexOffset) {
        gl2.glDrawElements(type.glCode(), count, indexType.glCode(), indexOffset);

    }

    public void genTextures(int count, IntBuffer buf) {
        gl2.glGenTextures(count, buf);
    }

    public void genTextures(int count, int[] textures, int offset) {
        gl2.glGenTextures(count, textures, offset);
    }

    public int genTexture() {
        gl2.glGenTextures(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void deleteTextures(int count, int[] textures, int offset) {
        gl2.glDeleteTextures(count, textures, offset);
    }

    public void deleteTextures(int count, IntBuffer buf) {
        gl2.glDeleteTextures(count, buf);
    }

    public void deleteTexture(int id) {
        INT_BUF[0] = id;
        gl2.glDeleteTextures(1, INT_BUF, 0);
    }

    public void bindTexture(TextureType type, int id) {
        gl2.glBindTexture(type.glCode(), id);
    }

    public void texImage2D(TextureTarget target, int level, int width, int height,
                           TextureFormat format, TexelType type, Buffer data) {
        gl2.glTexImage2D(target.glCode(), level, format.glCode(),
                width, height, 0,
                format.glCode(), type.glCode(), data);
    }
}
