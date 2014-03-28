package ru.spacearena.jogl;

import javax.media.opengl.GL2ES2;

/**
* @author Vyacheslav Mayorov
* @since 2014-28-03
*/
public class ShaderUtils {

    public static final String[] STRING_BUF = new String[1];
    public static final int[] INT_BUF = new int[1];

    public static int compile(GL2ES2 gl, String text, int type) {
        final int id = gl.glCreateShader(type);

        STRING_BUF[0] = text;
        gl.glShaderSource(id, 1, STRING_BUF, null);
        STRING_BUF[0] = null;

        gl.glCompileShader(id);
        return id;
    }

    public static int compileProgram(GL2ES2 gl, String vertexCode, String fragmentCode) {
        final int v = ShaderUtils.compile(gl, vertexCode, GL2ES2.GL_VERTEX_SHADER);
        final int f = ShaderUtils.compile(gl, fragmentCode, GL2ES2.GL_FRAGMENT_SHADER);
        final int s = gl.glCreateProgram();
        gl.glAttachShader(s, v);
        gl.glAttachShader(s, f);
        gl.glLinkProgram(s);
        gl.glValidateProgram(s);
        return s;
    }

}
