package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

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

    public static final VertexBufferLayout LAYOUT_P2 = new VertexBufferLayout.Builder().floats(2).build();

    private PositionProgram() {
        shader(OpenGL.VERTEX_SHADER,
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "void main()" +
                "{" +
                "gl_Position = u_MVPMatrix * a_Position;" +
                "}");
        shader(OpenGL.FRAGMENT_SHADER,
                "precision mediump float;" +
                "uniform vec4 u_Color;" +
                "void main()" +
                "{" +
                "gl_FragColor = u_Color;" +
                "}");
        attribute("a_Position");
        uniform("u_MVPMatrix");
        uniform("u_Color");
    }
}
