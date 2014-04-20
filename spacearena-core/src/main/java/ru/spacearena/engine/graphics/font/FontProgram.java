package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-07-04
 */
public class FontProgram extends ShaderProgram {

    public static final ShaderProgram.Definition DEFINITION = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            return new FontProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int TEXCOORD_ATTR = 1;
    public static final int MATRIX_UNIFORM = 0;
    public static final int TEXTURE_UNIFORM = 1;
    public static final int COLOR_UNIFORM = 2;

    private FontProgram() {
        shader(OpenGL.GL_VERTEX_SHADER,
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "attribute vec2 a_TexCoord;" +
                "varying vec2 v_TexCoord;" +
                "void main()" +
                "{" +
                "v_TexCoord = a_TexCoord;" +
                "gl_Position = u_MVPMatrix * a_Position;" +
                "}");
        shader(OpenGL.GL_FRAGMENT_SHADER,
                "precision mediump float;" +
                "varying vec2 v_TexCoord;" +
                "uniform sampler2D u_Texture;" +
                "uniform vec4 u_Color;" +
                "uniform float u_Threshold;" +
                "uniform float u_Weight;" +
                "void main()" +
                "{" +
                "float t_Alpha = texture2D(u_Texture, v_TexCoord).x;" +
                "gl_FragColor = u_Color;" +
                "gl_FragColor.w *= (t_Alpha > 0.5 ? 1 : t_Alpha);" +
                "}");
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
        uniform("u_Color");
    }


}
