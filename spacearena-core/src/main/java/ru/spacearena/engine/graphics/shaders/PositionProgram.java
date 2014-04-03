package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.VertexBufferLayout;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class PositionProgram extends Program {

    public static final Definition DEFINITION = new Definition() {
        public Program createProgram() {
            return new PositionProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int MATRIX_UNIFORM = 0;
    public static final int COLOR_UNIFORM = 1;

    public static final VertexBufferLayout LAYOUT_P2 = new VertexBufferLayout.Builder().size(2).build();

    private PositionProgram() {
        addShader(new Shader(OpenGL.ShaderType.VERTEX,
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "void main()" +
                "{" +
                "gl_Position = u_MVPMatrix * a_Position;" +
                "}"));
        addShader(new Shader(OpenGL.ShaderType.FRAGMENT,
                "precision mediump float;" +
                "uniform vec4 u_Color;" +
                "void main()" +
                "{" +
                "gl_FragColor = u_Color;" +
                "}"));
        addAttribute("a_Position");
        addUniform("u_MVPMatrix");
        addUniform("u_Color");
    }
}
