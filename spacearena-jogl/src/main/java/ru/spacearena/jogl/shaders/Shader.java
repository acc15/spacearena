package ru.spacearena.jogl.shaders;

import javax.media.opengl.GL2ES2;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class Shader extends CompilableObject {

    private final String[] source;
    private final int type;
    private final int[] status = new int[1];

    public Shader(int type, String source) {
        this.type = type;
        this.source = new String[] {source};
    }

    @Override
    protected int doCompile(GL2ES2 gl) {
        final int id = gl.glCreateShader(type);
        gl.glShaderSource(id, source.length, source, null);
        gl.glCompileShader(id);

        gl.glGetShaderiv(id, GL2ES2.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GL2ES2.GL_FALSE) {
            return id;
        }

        gl.glGetShaderiv(id, GL2ES2.GL_INFO_LOG_LENGTH, status, 0);

        final int logLength = status[0];
        final byte[] buf = new byte[logLength];
        gl.glGetShaderInfoLog(id, logLength, null, 0, buf, 0);

        gl.glDeleteShader(id);

        final String str = new String(buf, 0, logLength-1).trim();
        throw new RuntimeException("Can't compile shader: " + str);
    }

}
