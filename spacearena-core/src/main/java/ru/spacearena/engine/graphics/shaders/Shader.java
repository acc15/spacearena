package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class Shader {

    private final String source;
    private final OpenGL.ShaderType type;

    private int id;
    private int c;

    public Shader(OpenGL.ShaderType type, String source) {
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

    private int doCompile(OpenGL gl) {
        final int id = gl.createShader(type);
        gl.shaderSource(id, source);
        gl.compileShader(id);
        if (gl.getShader(id, OpenGL.ShaderParam.COMPILE_STATUS) == 0) {
            final String log = gl.getShaderInfoLog(id);
            gl.deleteShader(id);
            throw new RuntimeException("Can't compile shader: " + log);
        }
        return id;
    }

    private void doDelete(OpenGL gl) {
        gl.deleteShader(getId());
    }
}
