package ru.spacearena.android.engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.texture.Texture;

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

    public void glGetIntegerv(int parameter, int[] values, int offset) {
        GLES20.glGetIntegerv(parameter, values, offset);
    }

    public void glGetIntegerv(int parameter, IntBuffer buf) {
        GLES20.glGetIntegerv(parameter, buf);
    }

    public void glLineWidth(float width) {
        GLES20.glLineWidth(width);
    }

    public void glViewport(int x, int y, int width, int height) {
        GLES20.glViewport(x, y, width, height);
    }

    public void glClearColor(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
    }

    public void glClear(int mask) {
        GLES20.glClear(mask);
    }

    public int glCreateShader(int type) {
        return GLES20.glCreateShader(type);
    }

    public void glShaderSource(int shaderId, String source) {
        GLES20.glShaderSource(shaderId, source);
    }

    public void glCompileShader(int shaderId) {
        GLES20.glCompileShader(shaderId);
    }

    public int glGetShaderiv(int shaderId, int param) {
        GLES20.glGetShaderiv(shaderId, param, INT_BUF, 0);
        return INT_BUF[0];
    }

    public String glGetShaderInfoLog(int shaderId) {
        return GLES20.glGetShaderInfoLog(shaderId);
    }

    public void glDeleteShader(int shaderId) {
        GLES20.glDeleteShader(shaderId);
    }

    public int glCreateProgram() {
        return GLES20.glCreateProgram();
    }

    public void glLinkProgram(int programId) {
        GLES20.glLinkProgram(programId);
    }

    public void glAttachShader(int programId, int shaderId) {
        GLES20.glAttachShader(programId, shaderId);
    }

    public void glDeleteProgram(int programId) {
        GLES20.glDeleteProgram(programId);
    }

    public void glValidateProgram(int programId) {
        GLES20.glValidateProgram(programId);
    }

    public int getProgram(int programId, int param) {
        GLES20.glGetProgramiv(programId, param, INT_BUF, 0);
        return INT_BUF[0];
    }

    public String glGetProgramInfoLog(int programId) {
        return GLES20.glGetProgramInfoLog(programId);
    }

    public void glUseProgram(int programId) {
        GLES20.glUseProgram(programId);
    }

    public void glBindAttribLocation(int programId, int attrIndex, String attribute) {
        GLES20.glBindAttribLocation(programId, attrIndex, attribute);
    }

    public int glGetAttribLocation(int programId, String attribute) {
        return GLES20.glGetAttribLocation(programId, attribute);
    }

    public int glGetUniformLocation(int programId, String uniform) {
        return GLES20.glGetUniformLocation(programId, uniform);
    }

    public void glBindBuffer(int type, int bufferId) {
        GLES20.glBindBuffer(type, bufferId);
    }

    public void glBufferData(int type, int bufferSize, Buffer buffer, int usage) {
        GLES20.glBufferData(type, bufferSize, buffer, usage);
    }

    public int glGenBuffer() {
        GLES20.glGenBuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glDeleteBuffer(int buf) {
        INT_BUF[0] = buf;
        GLES20.glDeleteBuffers(1, INT_BUF, 0);
    }

    public void glGenBuffers(int length, int[] bufs, int offset) {
        GLES20.glGenBuffers(length, bufs, offset);
    }

    public void glDeleteBuffers(int length, int[] bufs, int offset) {
        GLES20.glDeleteBuffers(length, bufs, offset);
    }

    public void glGenBuffers(int length, IntBuffer bufs) {
        GLES20.glGenBuffers(length, bufs);
    }

    public void glDeleteBuffers(int length, IntBuffer bufs) {
        GLES20.glDeleteBuffers(length, bufs);
    }

    public void glEnableVertexAttribArray(int attrIndex) {
        GLES20.glEnableVertexAttribArray(attrIndex);
    }

    public void glDisableVertexAttribArray(int attrIndex) {
        GLES20.glDisableVertexAttribArray(attrIndex);
    }

    public void glVertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, Buffer buffer) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, buffer);
    }

    public void glVertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, int offset) {
        GLES20.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, offset);
    }

    public void glVertexAttrib1f(int attrIndex, float x) {
        GLES20.glVertexAttrib1f(attrIndex, x);
    }

    public void glVertexAttrib2f(int attrIndex, float x, float y) {
        GLES20.glVertexAttrib2f(attrIndex, x, y);
    }

    public void glVertexAttrib3f(int attrIndex, float x, float y, float z) {
        GLES20.glVertexAttrib3f(attrIndex, x, y, z);
    }

    public void glVertexAttrib4f(int attrIndex, float x, float y, float z, float w) {
        GLES20.glVertexAttrib4f(attrIndex, x, y, z, w);
    }

    public void glVertexAttrib1fv(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib1fv(attrIndex, values, offset);
    }

    public void glVertexAttrib2fv(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib2fv(attrIndex, values, offset);
    }

    public void glVertexAttrib3fv(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib3fv(attrIndex, values, offset);
    }

    public void glVertexAttrib4fv(int attrIndex, float[] values, int offset) {
        GLES20.glVertexAttrib4fv(attrIndex, values, offset);
    }

    public void glVertexAttrib1fv(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib1fv(attrIndex, buffer);
    }

    public void glVertexAttrib2fv(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib2fv(attrIndex, buffer);
    }

    public void glVertexAttrib3fv(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib3fv(attrIndex, buffer);
    }

    public void glVertexAttrib4fv(int attrIndex, FloatBuffer buffer) {
        GLES20.glVertexAttrib4fv(attrIndex, buffer);
    }

    public void glUniformMatrix2fv(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix2fv(location, count, false, values, offset);
    }

    public void glUniformMatrix3fv(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix3fv(location, count, false, values, offset);
    }

    public void glUniformMatrix4fv(int location, int count, float[] values, int offset) {
        GLES20.glUniformMatrix4fv(location, count, false, values, offset);
    }

    public void glUniformMatrix2fv(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix2fv(location, count, false, buffer);
    }

    public void glUniformMatrix3fv(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix3fv(location, count, false, buffer);
    }

    public void glUniformMatrix4fv(int location, int count, FloatBuffer buffer) {
        GLES20.glUniformMatrix4fv(location, count, false, buffer);
    }

    public void glUniform1f(int location, float x) {
        GLES20.glUniform1f(location, x);
    }

    public void glUniform2f(int location, float x, float y) {
        GLES20.glUniform2f(location, x, y);
    }

    public void glUniform3f(int location, float x, float y, float z) {
        GLES20.glUniform3f(location, x, y, z);
    }

    public void glUniform4f(int location, float x, float y, float z, float w) {
        GLES20.glUniform4f(location, x, y, z, w);
    }

    public void glUniform1fv(int location, int count, float[] values, int offset) {
        GLES20.glUniform1fv(location, count, values, offset);
    }

    public void glUniform2fv(int location, int count, float[] values, int offset) {
        GLES20.glUniform2fv(location, count, values, offset);
    }

    public void glUniform3fv(int location, int count, float[] values, int offset) {
        GLES20.glUniform3fv(location, count, values, offset);
    }

    public void glUniform4fv(int location, int count, float[] values, int offset) {
        GLES20.glUniform4fv(location, count, values, offset);
    }

    public void glUniform1fv(int location, int count, FloatBuffer buf) {
        GLES20.glUniform1fv(location, count, buf);
    }

    public void glUniform2fv(int location, int count, FloatBuffer buf) {
        GLES20.glUniform2fv(location, count, buf);
    }

    public void glUniform3fv(int location, int count, FloatBuffer buf) {
        GLES20.glUniform3fv(location, count, buf);
    }

    public void glUniform4fv(int location, int count, FloatBuffer buf) {
        GLES20.glUniform4fv(location, count, buf);
    }

    public void glUniform1i(int location, int x) {
        GLES20.glUniform1i(location, x);
    }

    public void glUniform2i(int location, int x, int y) {
        GLES20.glUniform2i(location, x, y);
    }

    public void glUniform3i(int location, int x, int y, int z) {
        GLES20.glUniform3i(location, x, y, z);
    }

    public void glUniform4i(int location, int x, int y, int z, int w) {
        GLES20.glUniform4i(location, x, y, z, w);
    }

    public void glUniform1iv(int location, int count, int[] values, int offset) {
        GLES20.glUniform1iv(location, count, values, offset);
    }

    public void glUniform2iv(int location, int count, int[] values, int offset) {
        GLES20.glUniform2iv(location, count, values, offset);
    }

    public void glUniform3iv(int location, int count, int[] values, int offset) {
        GLES20.glUniform3iv(location, count, values, offset);
    }

    public void glUniform4iv(int location, int count, int[] values, int offset) {
        GLES20.glUniform4iv(location, count, values, offset);
    }

    public void glUniform1iv(int location, int count, IntBuffer buf) {
        GLES20.glUniform1iv(location, count, buf);
    }

    public void glUniform2iv(int location, int count, IntBuffer buf) {
        GLES20.glUniform2iv(location, count, buf);
    }

    public void glUniform3iv(int location, int count, IntBuffer buf) {
        GLES20.glUniform3iv(location, count, buf);
    }

    public void glUniform4iv(int location, int count, IntBuffer buf) {
        GLES20.glUniform4iv(location, count, buf);
    }

    public void glDrawArrays(int type, int offset, int count) {
        GLES20.glDrawArrays(type, offset, count);
    }

    public void glDrawElements(int type, int count, int indexType, Buffer indices) {
        GLES20.glDrawElements(type, count, indexType, indices);
    }

    public void glDrawElements(int type, int count, int indexType, int indexOffset) {
        GLES20.glDrawElements(type, count, indexType, indexOffset);
    }

    public void glGenTextures(int count, IntBuffer buf) {
        GLES20.glGenTextures(count, buf);
    }

    public void glGenTextures(int count, int[] textures, int offset) {
        GLES20.glGenTextures(count, textures, offset);
    }

    public int glGenTexture() {
        GLES20.glGenTextures(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glBindTexture(int type, int id) {
        GLES20.glBindTexture(type, id);
    }

    public void glDeleteTextures(int count, int[] textures, int offset) {
        GLES20.glDeleteTextures(count, textures, offset);
    }

    public void glDeleteTextures(int count, IntBuffer buf) {
        GLES20.glDeleteTextures(count, buf);
    }

    public void glDeleteTexture(int id) {
        INT_BUF[0] = id;
        GLES20.glDeleteTextures(1, INT_BUF, 0);
    }

    public void glPixelStore(int type, int alignment) {
        GLES20.glPixelStorei(type, alignment);
    }

    public void glTexImage2D(int target, int level, int width, int height, int format, int type, Buffer data) {
        GLES20.glTexImage2D(target, level, format, width, height, 0, format, type, data);
    }

    public void glTexImage2D(Texture texture, int level, URL url) {
        glTexImage2D(texture, level, 0, 0, url);
    }

    public void glTexImage2D(Texture texture, int level, int format, int type, URL url) {
        InputStream stream = null;
        Bitmap bitmap = null;
        try {
            stream = url.openStream();
            bitmap = BitmapFactory.decodeStream(stream);
            if (format == 0 || type == 0) {
                GLUtils.texImage2D(TEXTURE_2D, level, bitmap, 0);
            } else {
                GLUtils.texImage2D(TEXTURE_2D, level, format, bitmap, type, 0);
            }
            if (level == 0) {
                texture.setDimension(bitmap.getWidth(), bitmap.getHeight());
            }
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

    public void glTexParameter(int type, int parameter, float[] params, int offset) {
        GLES20.glTexParameterfv(type, parameter, params, offset);
    }

    public void glTexParameter(int type, int parameter, FloatBuffer buf) {
        GLES20.glTexParameterfv(type, parameter, buf);
    }

    public void glTexParameter(int type, int parameter, float value) {
        GLES20.glTexParameterf(type, parameter, value);
    }

    public void glTexParameter(int type, int parameter, int[] params, int offset) {
        GLES20.glTexParameteriv(type, parameter, params, offset);
    }

    public void glTexParameter(int type, int parameter, IntBuffer buf) {
        GLES20.glTexParameteriv(type, parameter, buf);
    }

    public void glTexParameter(int type, int parameter, int value) {
        GLES20.glTexParameteri(type, parameter, value);
    }

    public void glEnable(int what) {
        GLES20.glEnable(what);
    }

    public void glDisable(int what) {
        GLES20.glDisable(what);
    }

    public void glActiveTexture(int unit) {
        GLES20.glActiveTexture(unit);
    }

    public void glBlendFunc(int sfactor, int dfactor) {
        GLES20.glBlendFunc(sfactor, dfactor);
    }

    public void glDepthFunc(int func) {
        GLES20.glDepthFunc(func);
    }
}
