package ru.spacearena.jogl.engine;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.Texture;

import javax.media.opengl.GL2;
import java.io.IOException;
import java.net.URL;
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

    public void glGetIntegerv(int parameter, int[] values, int offset) {
        gl2.glGetIntegerv(parameter, values, offset);
    }

    public void glGetIntegerv(int parameter, IntBuffer buf) {
        gl2.glGetIntegerv(parameter, buf);
    }

    public void glViewport(int x, int y, int width, int height) {
        gl2.glViewport(x, y, width, height);
    }

    public void glClearColor(float r, float g, float b, float a) {
        gl2.glClearColor(r, g, b, a);
    }

    public void glClear(int mask) {
        gl2.glClear(mask);
    }

    public void glLineWidth(float width) {
        gl2.glLineWidth(width);
    }

    public void glShaderSource(int shaderId, String source) {
        STRING_BUF[0] = source;
        gl2.glShaderSource(shaderId, 1, STRING_BUF, null);
        STRING_BUF[0] = null;
    }

    public int glCreateShader(int type) {
        return gl2.glCreateShader(type);
    }

    public void glCompileShader(int shaderId) {
        gl2.glCompileShader(shaderId);
    }

    public int glGetShaderiv(int shaderId, int param) {
        gl2.glGetShaderiv(shaderId, param, INT_BUF, 0);
        return INT_BUF[0];
    }

    public String glGetShaderInfoLog(int shaderId) {
        final int l = glGetShaderiv(shaderId, OpenGL.GL_INFO_LOG_LENGTH);
        final byte[] buf = new byte[l];
        gl2.glGetShaderInfoLog(shaderId, l, null, 0, buf, 0);
        return new String(buf, 0, l-1);
    }

    public void glDeleteShader(int shaderId) {
        gl2.glDeleteShader(shaderId);
    }

    public int glCreateProgram() {
        return gl2.glCreateProgram();
    }

    public void glAttachShader(int programId, int shaderId) {
        gl2.glAttachShader(programId, shaderId);
    }

    public void glLinkProgram(int programId) {
        gl2.glLinkProgram(programId);
    }

    public void glDeleteProgram(int programId) {
        gl2.glDeleteProgram(programId);
    }

    public void glValidateProgram(int programId) {
        gl2.glValidateProgram(programId);
    }

    public int getProgram(int programId, int param) {
        gl2.glGetProgramiv(programId, param, INT_BUF, 0);
        return INT_BUF[0];
    }

    public String glGetProgramInfoLog(int programId) {
        final int l = getProgram(programId, OpenGL.GL_INFO_LOG_LENGTH);
        final byte[] buf = new byte[l];
        gl2.glGetProgramInfoLog(programId, l, null, 0, buf, 0);
        return new String(buf, 0, l-1);
    }

    public void glUseProgram(int programId) {
        gl2.glUseProgram(programId);
    }

    public void glBindAttribLocation(int programId, int attrIndex, String attribute) {
        gl2.glBindAttribLocation(programId, attrIndex, attribute);
    }

    public int glGetAttribLocation(int programId, String attribute) {
        return gl2.glGetAttribLocation(programId, attribute);
    }

    public int glGetUniformLocation(int programId, String uniform) {
        return gl2.glGetUniformLocation(programId, uniform);
    }

    public void glGenBuffers(int length, IntBuffer bufs) {
        gl2.glGenBuffers(length, bufs);
    }

    public void glGenBuffers(int length, int[] bufs, int offset) {
        gl2.glGenBuffers(length, bufs, offset);
    }

    public int glGenBuffer() {
        gl2.glGenBuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glDeleteBuffer(int buf) {
        INT_BUF[0] = buf;
        gl2.glDeleteBuffers(1, INT_BUF, 0);
    }

    public void glDeleteBuffers(int length, IntBuffer bufs) {
        gl2.glDeleteBuffers(length, bufs);
    }

    public void glDeleteBuffers(int length, int[] bufs, int offset) {
        gl2.glDeleteBuffers(length, bufs, offset);
    }

    public void glBindBuffer(int type, int bufferId) {
        gl2.glBindBuffer(type, bufferId);
    }

    public void glBufferData(int type, int bufferSize, Buffer buffer, int usage) {
        gl2.glBufferData(type, bufferSize, buffer, usage);
    }

    public void glVertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, Buffer buffer) {
        gl2.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, buffer);
    }

    public void glVertexAttribPointer(int attrIndex, int valueCount, int type, boolean normalized, int stride, int offset) {
        gl2.glVertexAttribPointer(attrIndex, valueCount, type, normalized, stride, offset);
    }

    public void glVertexAttrib1f(int attrIndex, float x) {
        gl2.glVertexAttrib1f(attrIndex, x);
    }

    public void glVertexAttrib2f(int attrIndex, float x, float y) {
        gl2.glVertexAttrib2f(attrIndex, x, y);
    }

    public void glVertexAttrib3f(int attrIndex, float x, float y, float z) {
        gl2.glVertexAttrib3f(attrIndex, x, y, z);
    }

    public void glVertexAttrib4f(int attrIndex, float x, float y, float z, float w) {
        gl2.glVertexAttrib4f(attrIndex, x, y, z, w);
    }

    public void glVertexAttrib1fv(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib1fv(attrIndex, values, offset);
    }

    public void glVertexAttrib2fv(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib2fv(attrIndex, values, offset);
    }

    public void glVertexAttrib3fv(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib3fv(attrIndex, values, offset);
    }

    public void glVertexAttrib4fv(int attrIndex, float[] values, int offset) {
        gl2.glVertexAttrib4fv(attrIndex, values, offset);
    }

    public void glVertexAttrib1fv(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib1fv(attrIndex, buffer);
    }

    public void glVertexAttrib2fv(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib2fv(attrIndex, buffer);
    }

    public void glVertexAttrib3fv(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib3fv(attrIndex, buffer);
    }

    public void glVertexAttrib4fv(int attrIndex, FloatBuffer buffer) {
        gl2.glVertexAttrib4fv(attrIndex, buffer);
    }

    public void glUniformMatrix2fv(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix2fv(location, count, false, values, offset);
    }

    public void glUniformMatrix3fv(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix3fv(location, count, false, values, offset);
    }

    public void glUniformMatrix4fv(int location, int count, float[] values, int offset) {
        gl2.glUniformMatrix4fv(location, count, false, values, offset);
    }

    public void glUniformMatrix2fv(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix2fv(location, count, false, buffer);
    }

    public void glUniformMatrix3fv(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix3fv(location, count, false, buffer);
    }

    public void glUniformMatrix4fv(int location, int count, FloatBuffer buffer) {
        gl2.glUniformMatrix4fv(location, count, false, buffer);
    }

    public void glUniform1f(int location, float x) {
        gl2.glUniform1f(location, x);
    }

    public void glUniform2f(int location, float x, float y) {
        gl2.glUniform2f(location, x, y);
    }

    public void glUniform3f(int location, float x, float y, float z) {
        gl2.glUniform3f(location, x, y, z);
    }

    public void glUniform4f(int location, float x, float y, float z, float w) {
        gl2.glUniform4f(location, x, y, z, w);
    }

    public void glUniform1fv(int location, int count, float[] values, int offset) {
        gl2.glUniform1fv(location, count, values, offset);
    }

    public void glUniform2fv(int location, int count, float[] values, int offset) {
        gl2.glUniform2fv(location, count, values, offset);
    }

    public void glUniform3fv(int location, int count, float[] values, int offset) {
        gl2.glUniform3fv(location, count, values, offset);
    }

    public void glUniform4fv(int location, int count, float[] values, int offset) {
        gl2.glUniform4fv(location, count, values, offset);
    }

    public void glUniform1fv(int location, int count, FloatBuffer buf) {
        gl2.glUniform1fv(location, count, buf);
    }

    public void glUniform2fv(int location, int count, FloatBuffer buf) {
        gl2.glUniform2fv(location, count, buf);
    }

    public void glUniform3fv(int location, int count, FloatBuffer buf) {
        gl2.glUniform3fv(location, count, buf);
    }

    public void glUniform4fv(int location, int count, FloatBuffer buf) {
        gl2.glUniform4fv(location, count, buf);
    }

    public void glUniform1i(int location, int x) {
        gl2.glUniform1i(location, x);
    }

    public void glUniform2i(int location, int x, int y) {
        gl2.glUniform2i(location, x, y);
    }

    public void glUniform3i(int location, int x, int y, int z) {
        gl2.glUniform3i(location, x, y, z);
    }

    public void glUniform4i(int location, int x, int y, int z, int w) {
        gl2.glUniform4i(location, x, y, z, w);
    }

    public void glUniform1iv(int location, int count, int[] values, int offset) {
        gl2.glUniform1iv(location, count, values, offset);
    }

    public void glUniform2iv(int location, int count, int[] values, int offset) {
        gl2.glUniform2iv(location, count, values, offset);
    }

    public void glUniform3iv(int location, int count, int[] values, int offset) {
        gl2.glUniform3iv(location, count, values, offset);
    }

    public void glUniform4iv(int location, int count, int[] values, int offset) {
        gl2.glUniform4iv(location, count, values, offset);
    }

    public void glUniform1iv(int location, int count, IntBuffer buf) {
        gl2.glUniform1iv(location, count, buf);
    }

    public void glUniform2iv(int location, int count, IntBuffer buf) {
        gl2.glUniform2iv(location, count, buf);
    }

    public void glUniform3iv(int location, int count, IntBuffer buf) {
        gl2.glUniform3iv(location, count, buf);
    }

    public void glUniform4iv(int location, int count, IntBuffer buf) {
        gl2.glUniform4iv(location, count, buf);
    }

    public void glEnableVertexAttribArray(int attrIndex) {
        gl2.glEnableVertexAttribArray(attrIndex);
    }

    public void glDisableVertexAttribArray(int attrIndex) {
        gl2.glDisableVertexAttribArray(attrIndex);
    }

    public void glDrawArrays(int type, int offset, int count) {
        gl2.glDrawArrays(type, offset, count);
    }

    public void glDrawElements(int type, int count, int indexType, Buffer indices) {
        gl2.glDrawElements(type, count, indexType, indices);
    }

    public void glDrawElements(int type, int count, int indexType, int indexOffset) {
        gl2.glDrawElements(type, count, indexType, indexOffset);

    }

    public void glGenTextures(int count, IntBuffer buf) {
        gl2.glGenTextures(count, buf);
    }

    public void glGenTextures(int count, int[] textures, int offset) {
        gl2.glGenTextures(count, textures, offset);
    }

    public int glGenTexture() {
        gl2.glGenTextures(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glDeleteTextures(int count, int[] textures, int offset) {
        gl2.glDeleteTextures(count, textures, offset);
    }

    public void glDeleteTextures(int count, IntBuffer buf) {
        gl2.glDeleteTextures(count, buf);
    }

    public void glDeleteTexture(int id) {
        INT_BUF[0] = id;
        gl2.glDeleteTextures(1, INT_BUF, 0);
    }

    public void glBindTexture(int type, int id) {
        gl2.glBindTexture(type, id);
    }

    public void glPixelStore(int type, int alignment) {
        gl2.glPixelStorei(type, alignment);
    }

    public void glTexImage2D(int target, int level, int width, int height,
                             int format, int type, Buffer data) {
        gl2.glTexImage2D(target, level, format, width, height, 0, format, type, data);
    }

    public void glTexParameter(int type, int parameter, float[] params, int offset) {
        gl2.glTexParameterfv(type, parameter, params, offset);
    }

    public void glTexParameter(int type, int parameter, FloatBuffer buf) {
        gl2.glTexParameterfv(type, parameter, buf);
    }

    public void glTexParameter(int type, int parameter, float value) {
        gl2.glTexParameterf(type, parameter, value);
    }

    public void glTexParameter(int type, int parameter, int[] params, int offset) {
        gl2.glTexParameteriv(type, parameter, params, offset);
    }

    public void glTexParameter(int type, int parameter, IntBuffer buf) {
        gl2.glTexParameteriv(type, parameter, buf);
    }

    public void glTexParameter(int type, int parameter, int value) {
        gl2.glTexParameteri(type, parameter, value);
    }

    public void glTexImage2D(Texture texture, int level, int format, int type, URL url) {
        try {
            texImage2D(texture, TextureIO.newTextureData(gl2.getGLProfile(), url, format, format, false, null), level, type);
        } catch (IOException e) {
            throw new RuntimeException("Can't load texture data from URL: " + url, e);
        }
    }

    public void glTexImage2D(Texture texture, int level, URL url) {
        try {
            texImage2D(texture, TextureIO.newTextureData(gl2.getGLProfile(), url, false, null), level, 0);
        } catch (IOException e) {
            throw new RuntimeException("Can't load texture data from URL: " + url, e);
        }
    }

    private void texImage2D(Texture t, TextureData td, int level, int type) {
        final int w = td.getWidth(), h = td.getHeight(), tf = td.getInternalFormat(), pf = td.getPixelFormat(),
                pt = type != 0 ? type : td.getPixelType();
        final Buffer buf = td.getBuffer();
        gl2.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT,td.getAlignment());
        gl2.glTexImage2D(GL_TEXTURE_2D, level, tf, w, h, 0, pf, pt, buf);
        if (level == 0) {
            t.setDimension(w, h);
            t.setFlipY(true);
        }
    }

    public void glEnable(int what) {
        gl2.glEnable(what);
    }

    public void glDisable(int what) {
        gl2.glDisable(what);
    }

    public void glActiveTexture(int unit) {
        gl2.glActiveTexture(unit);
    }

    public void glBlendFunc(int sfactor, int dfactor) {
        gl2.glBlendFunc(sfactor, dfactor);
    }

    public void glDepthFunc(int func) {
        gl2.glDepthFunc(func);
    }

    public void glDepthMask(boolean val) {
        gl2.glDepthMask(val);
    }

    public void glGenRenderbuffers(int length, int[] bufs, int offset) {
        gl2.glGenRenderbuffers(length, bufs, offset);
    }

    public void glDeleteRenderbuffers(int length, int[] bufs, int offset) {
        gl2.glDeleteRenderbuffers(length, bufs, offset);
    }

    public void glGenRenderbuffers(int length, IntBuffer bufs) {
        gl2.glGenRenderbuffers(length, bufs);
    }

    public void glDeleteRenderbuffers(int length, IntBuffer bufs) {
        gl2.glDeleteRenderbuffers(length, bufs);
    }

    public int glGenRenderbuffer() {
        gl2.glGenRenderbuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glDeleteRenderbuffer(int buf) {
        INT_BUF[0] = buf;
        gl2.glDeleteRenderbuffers(1, INT_BUF, 0);
    }

    public void glGenFramebuffers(int length, int[] bufs, int offset) {
        gl2.glGenFramebuffers(length, bufs, offset);
    }

    public void glDeleteFramebuffers(int length, int[] bufs, int offset) {
        gl2.glDeleteFramebuffers(length, bufs, offset);
    }

    public void glGenFramebuffers(int length, IntBuffer bufs) {
        gl2.glGenFramebuffers(length, bufs);
    }

    public void glDeleteFramebuffers(int length, IntBuffer bufs) {
        gl2.glDeleteFramebuffers(length, bufs);
    }

    public int glGenFrameBuffer() {
        gl2.glGenFramebuffers(1, INT_BUF, 0);
        return INT_BUF[0];
    }

    public void glDeleteFrameBuffer(int buf) {
        INT_BUF[0] = buf;
        gl2.glDeleteFramebuffers(1, INT_BUF, 0);
    }

    public void glBindRenderbuffer(int buf) {
        gl2.glBindRenderbuffer(GL_RENDERBUFFER, buf);
    }

    public void glRenderbufferStorage(int format, int width, int height) {
        gl2.glRenderbufferStorage(GL_RENDERBUFFER, format, width, height);
    }

    public void glBindFramebuffer(int buf) {
        gl2.glBindFramebuffer(GL_FRAMEBUFFER, buf);
    }

    public void glFramebufferRenderbuffer(int attachment, int renderbuffer) {
        gl2.glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderbuffer);
    }

    public void glFramebufferTexture2D(int attachment, int target, int texture, int level) {
        gl2.glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, target, texture, level);
    }

    public int glCheckFramebufferStatus() {
        return gl2.glCheckFramebufferStatus(GL_FRAMEBUFFER);
    }


}
