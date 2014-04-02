package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class PositionColorProgram extends ShaderProgram {

    private static final PositionColorProgram instance = new PositionColorProgram();

    public static final int POSITION_ATTR = 0;
    public static final int MATRIX_UNIFORM = 0;
    public static final int COLOR_UNIFORM = 1;

    private PositionColorProgram() {
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

    public static PositionColorProgram getInstance() {
        return instance;
    }

}
