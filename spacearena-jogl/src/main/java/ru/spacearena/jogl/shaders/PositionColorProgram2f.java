package ru.spacearena.jogl.shaders;

import javax.media.opengl.GL2ES2;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class PositionColorProgram2f extends ShaderProgram {

    public static final int POSITION_ATTRIB = 0;
    public static final int COLOR_ATTRIB = 1;
    public static final int MATRIX_UNIFORM = 0;

    public PositionColorProgram2f() {
        addShader(new Shader(GL2ES2.GL_VERTEX_SHADER,
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "attribute vec4 a_Color;" +
                "varying vec4 v_Color;" +
                "void main()" +
                "{" +
                "v_Color = a_Color;" +
                "gl_Position = u_MVPMatrix * a_Position;" +
                "}"));
        addShader(new Shader(GL2ES2.GL_FRAGMENT_SHADER, "precision mediump float;" +
                "varying vec4 v_Color;" +
                "void main()" +
                "{" +
                "gl_FragColor = v_Color;"+
                "}"));
        addAttribute("a_Position");
        addAttribute("a_Color");
        addUniform("u_MVPMatrix");
    }

}
