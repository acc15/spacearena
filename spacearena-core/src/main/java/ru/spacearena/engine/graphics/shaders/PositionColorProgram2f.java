package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class PositionColorProgram2f extends ShaderProgram {

    private static final PositionColorProgram2f instance = new PositionColorProgram2f();

    public static final int POSITION_ATTRIB = 0;
    public static final int COLOR_ATTRIB = 1;
    public static final int MATRIX_UNIFORM = 0;

    private PositionColorProgram2f() {
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

    public static PositionColorProgram2f getInstance() {
        return instance;
    }

}
