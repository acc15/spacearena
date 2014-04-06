package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.graphics.OpenGL;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-01-04
 */
public class AndroidGLES2 implements OpenGL {

    private static final Logger logger = LoggerFactory.getLogger(AndroidGLES2.class);

    private final int[] INT_BUF = new int[1];

    public void getInteger(int parameter, int[] values, int offset) {
        GLES20.glGetIntegerv(parameter, values, offset);
    }

    public void getInteger(int parameter, IntBuffer buf) {
        GLES20.glGetIntegerv(parameter, buf);
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

    public int createShader(int type) {
        return GLES20.glCreateShader(type);
    }

    public void shaderSource(int shaderId, String source) {
        GLES20.glShaderSource(shaderId, source);
    }

    public void compileShader(int shaderId) {
        GLES20.glCompileShader(shaderId);
    }

    public int getShader(int shaderId, int param) {
        GLES20.glGetShaderiv(shaderId, param, INT_BUF, 0);
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

    public int getProgram(int programId, int param) {
        GLES20.glGetProgramiv(programId, param, INT_BUF, 0);
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

    public void bindBuffer(int type, int bufferId) {
        GLES20.glBindBuffer(type, bufferId);
    }

    public void bufferData(int type, int bufferSize, Buffer buffer, int usage) {
        GLES20.glBufferData(type, bufferSize, buffer, usage);
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

    public void vertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, Buffer buffer) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, buffer);
    }

    public void vertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, int offset) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, offset);
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

    public void uniform(int location, int x) {
        GLES20.glUniform1i(location, x);
    }

    public void uniform(int location, int x, int y) {
        GLES20.glUniform2i(location, x, y);
    }

    public void uniform(int location, int x, int y, int z) {
        GLES20.glUniform3i(location, x, y, z);
    }

    public void uniform(int location, int x, int y, int z, int w) {
        GLES20.glUniform4i(location, x, y, z, w);
    }

    public void uniform1(int location, int count, int[] values, int offset) {
        GLES20.glUniform1iv(location, count, values, offset);
    }

    public void uniform2(int location, int count, int[] values, int offset) {
        GLES20.glUniform2iv(location, count, values, offset);
    }

    public void uniform3(int location, int count, int[] values, int offset) {
        GLES20.glUniform3iv(location, count, values, offset);
    }

    public void uniform4(int location, int count, int[] values, int offset) {
        GLES20.glUniform4iv(location, count, values, offset);
    }

    public void uniform1(int location, int count, IntBuffer buf) {
        GLES20.glUniform1iv(location, count, buf);
    }

    public void uniform2(int location, int count, IntBuffer buf) {
        GLES20.glUniform2iv(location, count, buf);
    }

    public void uniform3(int location, int count, IntBuffer buf) {
        GLES20.glUniform3iv(location, count, buf);
    }

    public void uniform4(int location, int count, IntBuffer buf) {
        GLES20.glUniform4iv(location, count, buf);
    }

    public void drawArrays(int type, int offset, int count) {
        GLES20.glDrawArrays(type, offset, count);
    }

    public void drawElements(int type, int count, int indexType, Buffer indices) {
        GLES20.glDrawElements(type, count, indexType, indices);
    }

    public void drawElements(int type, int count, int indexType, int indexOffset) {
        GLES20.glDrawElements(type, count, indexType, indexOffset);
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

    public void bindTexture(int type, int id) {
        GLES20.glBindTexture(type, id);
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

    public void pixelStore(int type, int alignment) {
        GLES20.glPixelStorei(type, alignment);
    }

    public void texImage2D(int target, int level, int width, int height, int format, int type, Buffer data) {
        GLES20.glTexImage2D(target, level, format, width, height, 0, format, type, data);
    }

    public void texImage2D(int target, int level, URL url) {
        InputStream stream = null;
        Bitmap bitmap = null;
        try {
            stream = url.openStream();
            bitmap = BitmapFactory.decodeStream(stream);
            GLUtils.texImage2D(target, level, bitmap, 0);
        } catch (IOException e) {
            throw new RuntimeException("Can't read texture from URL: " + url, e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.error("Can't close stream opened from URL \"" + url + "\" to load texture", e);
                }
            }
        }
    }

    public void texParameter(int type, int parameter, float[] params, int offset) {
        GLES20.glTexParameterfv(type, parameter, params, offset);
    }

    public void texParameter(int type, int parameter, FloatBuffer buf) {
        GLES20.glTexParameterfv(type, parameter, buf);
    }

    public void texParameter(int type, int parameter, float value) {
        GLES20.glTexParameterf(type, parameter, value);
    }

    public void texParameter(int type, int parameter, int[] params, int offset) {
        GLES20.glTexParameteriv(type, parameter, params, offset);
    }

    public void texParameter(int type, int parameter, IntBuffer buf) {
        GLES20.glTexParameteriv(type, parameter, buf);
    }

    public void texParameter(int type, int parameter, int value) {
        GLES20.glTexParameteri(type, parameter, value);
    }

    public void enable(int what) {
        GLES20.glEnable(what);
    }

    public void disable(int what) {
        GLES20.glDisable(what);
    }

    public void activeTexture(int unit) {
        GLES20.glActiveTexture(unit);
    }
}
