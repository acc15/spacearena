package ru.spacearena.android.engine;

import android.opengl.GLES20;
import ru.spacearena.engine.graphics.OpenGL;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class AndroidGLES2 implements OpenGL {

    private final int[] INT_BUF = new int[1];

    public void getInteger(GenericParameter parameter, int[] values, int offset) {
        GLES20.glGetIntegerv(parameter.glCode(), values, offset);
    }

    public void getInteger(GenericParameter parameter, IntBuffer buf) {
        GLES20.glGetIntegerv(parameter.glCode(), buf);
    }

    public void lineWidth(float width) {
        GLES20.glLineWidth(width);
    }

    public void viewport(int x, int y, int width, int height) {
        GLES20.glViewport(x, y, width, height);
    }

    public void clearColor(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
    }

    public void clear(int mask) {
        GLES20.glClear(mask);
    }

    public int createShader(ShaderType type) {
        return GLES20.glCreateShader(type.glCode());
    }

    public void shaderSource(int shaderId, String source) {
        GLES20.glShaderSource(shaderId, source);
    }

    public void compileShader(int shaderId) {
        GLES20.glCompileShader(shaderId);
    }

    public int getShader(int shaderId, ShaderParam param) {
        GLES20.glGetShaderiv(shaderId, param.glCode(), INT_BUF, 0);
        return INT_BUF[0];
    }

    public String getShaderInfoLog(int shaderId) {
        return GLES20.glGetShaderInfoLog(shaderId);
    }

    public void deleteShader(int shaderId) {
        GLES20.glDeleteShader(shaderId);
    }

    public int createProgram() {
        return GLES20.glCreateProgram();
    }

    public void linkProgram(int programId) {
        GLES20.glLinkProgram(programId);
    }

    public void attachShader(int programId, int shaderId) {
        GLES20.glAttachShader(programId, shaderId);
    }

    public void deleteProgram(int programId) {
        GLES20.glDeleteProgram(programId);
    }

    public void validateProgram(int programId) {
        GLES20.glValidateProgram(programId);
    }

    public int getProgram(int programId, ProgramParam param) {
        GLES20.glGetProgramiv(programId, param.glCode(), INT_BUF, 0);
        return INT_BUF[0];
    }

    public String getProgramInfoLog(int programId) {
        return GLES20.glGetProgramInfoLog(programId);
    }

    public void useProgram(int programId) {
        GLES20.glUseProgram(programId);
    }

    public void bindAttribLocation(int programId, int attrIndex, String attribute) {
        GLES20.glBindAttribLocation(programId, attrIndex, attribute);
    }

    public int getAttribLocation(int programId, String attribute) {
        return GLES20.glGetAttribLocation(programId, attribute);
    }

    public int getUniformLocation(int programId, String uniform) {
        return GLES20.glGetUniformLocation(programId, uniform);
    }

    public void bindBuffer(BufferType type, int bufferId) {
        GLES20.glBindBuffer(type.glCode(), bufferId);
    }

    public void bufferData(BufferType type, int bufferSize, Buffer buffer, BufferUsage usage) {
        GLES20.glBufferData(type.glCode(), bufferSize, buffer, usage.glCode());
    }

    public int genBuffer() {
        GLES20.glGenBuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void deleteBuffer(int buf) {
        INT_BUF[0] = buf;
        GLES20.glDeleteBuffers(1, INT_BUF, 0);
    }

    public void genBuffers(int length, int[] bufs, int offset) {
        GLES20.glGenBuffers(length, bufs, offset);
    }

    public void deleteBuffers(int length, int[] bufs, int offset) {
        GLES20.glDeleteBuffers(length, bufs, offset);
    }

    public void genBuffers(int length, IntBuffer bufs) {
        GLES20.glGenBuffers(length, bufs);
    }

    public void deleteBuffers(int length, IntBuffer bufs) {
        GLES20.glDeleteBuffers(length, bufs);
    }

    public void enableVertexAttribArray(int attrIndex) {
        GLES20.glEnableVertexAttribArray(attrIndex);
    }

    public void disableVertexAttribArray(int attrIndex) {
        GLES20.glDisableVertexAttribArray(attrIndex);
    }

    public void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, Buffer buffer) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type.glCode(), normalized, stride, buffer);
    }

    public void vertexAttribPointer(int attrIndex, int valueCount, Type type, boolean normalized, int stride, int offset) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type.glCode(), normalized, stride, offset);
    }

    public void vertexAttrib(int attrIndex, float x) {
        GLES20.glVertexAttrib1f(attrIndex, x);
    }

    public void vertexAttrib(int attrIndex, float x, float y) {
        GLES20.glVertexAttrib2f(attrIndex, x, y);
    }

    public void vertexAttrib(int attrIndex, float x, float y, float z) {
        GLES20.glVertexAttrib3f(attrIndex, x, y, z);
    }

    public void vertexAttrib(int attrIndex, float x, float y, float z, float w) {
        GLES20.glVertexAttrib4f(attrIndex, x, y, z, w);
    }

    public void vertexAttrib1(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib1fv(attrIndex, values, offset);
    }

    public void vertexAttrib2(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib2fv(attrIndex, values, offset);
    }

    public void vertexAttrib3(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib3fv(attrIndex, values, offset);
    }

    public void vertexAttrib4(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib4fv(attrIndex, values, offset);
    }

    public void vertexAttrib1(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib1fv(attrIndex, buffer);
    }

    public void vertexAttrib2(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib2fv(attrIndex, buffer);
    }

    public void vertexAttrib3(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib3fv(attrIndex, buffer);
    }

    public void vertexAttrib4(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib4fv(attrIndex, buffer);
    }

    public void uniformMatrix2(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix2fv(location, count, false, values, offset);
    }

    public void uniformMatrix3(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix3fv(location, count, false, values, offset);
    }

    public void uniformMatrix4(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix4fv(location, count, false, values, offset);
    }

    public void uniformMatrix2(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix2fv(location, count, false, buffer);
    }

    public void uniformMatrix3(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix3fv(location, count, false, buffer);
    }

    public void uniformMatrix4(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix4fv(location, count, false, buffer);
    }

    public void uniform(int location, float x) {
        GLES20.glUniform1f(location, x);
    }

    public void uniform(int location, float x, float y) {
        GLES20.glUniform2f(location, x, y);
    }

    public void uniform(int location, float x, float y, float z) {
        GLES20.glUniform3f(location, x, y, z);
    }

    public void uniform(int location, float x, float y, float z, float w) {
        GLES20.glUniform4f(location, x, y, z, w);
    }

    public void uniform1(int location, int count, float[] values, int offset) {
        GLES20.glUniform1fv(location, count, values, offset);
    }

    public void uniform2(int location, int count, float[] values, int offset) {
        GLES20.glUniform2fv(location, count, values, offset);
    }

    public void uniform3(int location, int count, float[] values, int offset) {
        GLES20.glUniform3fv(location, count, values, offset);
    }

    public void uniform4(int location, int count, float[] values, int offset) {
        GLES20.glUniform4fv(location, count, values, offset);
    }

    public void uniform1(int location, int count, FloatBuffer buf) {
        GLES20.glUniform1fv(location, count, buf);
    }

    public void uniform2(int location, int count, FloatBuffer buf) {
        GLES20.glUniform2fv(location, count, buf);
    }

    public void uniform3(int location, int count, FloatBuffer buf) {
        GLES20.glUniform3fv(location, count, buf);
    }

    public void uniform4(int location, int count, FloatBuffer buf) {
        GLES20.glUniform4fv(location, count, buf);
    }

    public void drawArrays(PrimitiveType type, int offset, int count) {
        GLES20.glDrawArrays(type.glCode(), offset, count);
    }

    public void drawElements(PrimitiveType type, int count, Type indexType, Buffer indices) {
        GLES20.glDrawElements(type.glCode(), count, indexType.glCode(), indices);
    }

    public void drawElements(PrimitiveType type, int count, Type indexType, int indexOffset) {
        GLES20.glDrawElements(type.glCode(), count, indexType.glCode(), indexOffset);
    }

    public void genTextures(int count, IntBuffer buf) {
        GLES20.glGenTextures(count, buf);
    }

    public void genTextures(int count, int[] textures, int offset) {
        GLES20.glGenTextures(count, textures, offset);
    }

    public int genTexture() {
        GLES20.glGenTextures(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void bindTexture(TextureType type, int id) {
        GLES20.glBindTexture(type.glCode(), id);
    }

    public void deleteTextures(int count, int[] textures, int offset) {
        GLES20.glDeleteTextures(count, textures, offset);
    }

    public void deleteTextures(int count, IntBuffer buf) {
        GLES20.glDeleteTextures(count, buf);
    }

    public void deleteTexture(int id) {
        INT_BUF[0] = id;
        GLES20.glDeleteTextures(1, INT_BUF, 0);
    }

    public void pixelStore(PixelStore type, int alignment) {
        GLES20.glPixelStorei(type.glCode(), alignment);
    }

    public void texImage2D(TextureTarget target, int level, int width, int height, TextureFormat format, TexelType type, Buffer data) {
        GLES20.glTexImage2D(target.glCode(), level, format.glCode(), width, height, 0, format.glCode(), type.glCode(), data);
    }

    public void texParameter(TextureType type, TextureParameter parameter, float[] params, int offset) {
        GLES20.glTexParameterfv(type.glCode(), parameter.glCode(), params, offset);
    }

    public void texParameter(TextureType type, TextureParameter parameter, FloatBuffer buf) {
        GLES20.glTexParameterfv(type.glCode(), parameter.glCode(), buf);
    }

    public void texParameter(TextureType type, TextureParameter parameter, float value) {
        GLES20.glTexParameterf(type.glCode(), parameter.glCode(), value);
    }

    public void texParameter(TextureType type, TextureParameter parameter, int[] params, int offset) {
        GLES20.glTexParameteriv(type.glCode(), parameter.glCode(), params, offset);
    }

    public void texParameter(TextureType type, TextureParameter parameter, IntBuffer buf) {
        GLES20.glTexParameteriv(type.glCode(), parameter.glCode(), buf);
    }

    public void texParameter(TextureType type, TextureParameter parameter, int value) {
        GLES20.glTexParameteri(type.glCode(), parameter.glCode(), value);
    }
}
