package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class Shader {

    private final String source;
    private final int type;

    private int id;
    private int c;

    public Shader(int type, String source) {
        this.type = type;
        this.source = source;
    }

    public boolean isCompiled() {
        return c != 0;
    }

    public int getId() {
        return id;
    }

    public void compile(OpenGL gl) {
        if (c == 0) {
            this.id = doCompile(gl);
        }
        ++c;
    }

    public void delete(OpenGL gl) {
        if (c == 0) {
            throw new IllegalStateException("Attempt to delete shader which isn't present in GL");
        }
        --c;
        if (c == 0) {
            doDelete(gl);
            this.id = 0;
        }
    }

    public void reset() {
        this.c = 0;
        this.id = 0;
    }

    private int doCompile(OpenGL gl) {
        final int id = gl.glCreateShader(type);
        gl.glShaderSource(id, source);
        gl.glCompileShader(id);
        if (gl.glGetShaderiv(id, OpenGL.COMPILE_STATUS) == 0) {
            final String log = gl.glGetShaderInfoLog(id);
            gl.glDeleteShader(id);
            throw new RuntimeException("Can't compile shader: " + log);
        }
        return id;
    }

    private void doDelete(OpenGL gl) {
        gl.glDeleteShader(getId());
    }

}
