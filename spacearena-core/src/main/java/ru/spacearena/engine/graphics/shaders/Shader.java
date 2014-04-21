package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.GLDrawContext;
import ru.spacearena.engine.graphics.GLObjectDefinition;
import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class Shader {

    public static class Definition implements GLObjectDefinition<Shader> {

        private final String source;
        private final int type;

        public Definition(int type, String source) {
            this.type = type;
            this.source = source;
        }

        public Shader create(GLDrawContext context) {
            final OpenGL gl = context.getGL();

            final Shader shader = new Shader(gl.glCreateShader(type));
            gl.glShaderSource(shader.id, source);
            gl.glCompileShader(shader.id);
            if (gl.glGetShaderiv(shader.id, OpenGL.GL_COMPILE_STATUS) == 0) {
                final String log = gl.glGetShaderInfoLog(shader.id);
                gl.glDeleteShader(shader.id);
                throw new RuntimeException("Can't compile shader: " + log);
            }
            return shader;
        }

        public void reference(GLDrawContext context, Shader object) {
            ++object.c;
        }

        public void delete(GLDrawContext context, Shader object) {
            if (object.c == 0) {
                throw new IllegalStateException("Attempt to delete shader which isn't present in GL");
            }
            --object.c;
            if (object.c <= 0) {
                context.getGL().glDeleteShader(object.id);
            }
        }
    }

    private final int id;
    private int c;

    public Shader(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
