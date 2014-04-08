package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.shaders.Program;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class DistanceFieldProgram extends Program {

    public static final Program.Definition DEFINITION = new Program.Definition() {
        public Program createProgram() {
            return new DistanceFieldProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int TEXCOORD_ATTR = 1;
    public static final int MATRIX_UNIFORM = 0;
    public static final int TEXTURE_UNIFORM = 1;
    public static final int COLOR_UNIFORM = 2;

    private DistanceFieldProgram() {
        shader(OpenGL.VERTEX_SHADER,
                "uniform mat4 u_MVPMatrix;" +
                        "attribute vec4 a_Position;" +
                        "attribute vec2 a_TexCoord;" +
                        "varying vec2 v_TexCoord;" +
                        "void main()" +
                        "{" +
                        "v_TexCoord = a_TexCoord;" +
                        "gl_Position = u_MVPMatrix * a_Position;" +
                        "}");
        shader(OpenGL.FRAGMENT_SHADER,
                "precision mediump float;" +
                "varying vec2 v_TexCoord;" +
                "uniform sampler2D u_Texture;" +
                "uniform vec4 u_Color;" +
                "const float smoothing = 1.0/32.0;" +
                "void main()" +
                "{" +
                "float t_Distance = texture2D(u_Texture, v_TexCoord).x;" +
                "float t_Alpha = smoothstep(0.5 - smoothing, 0.5 + smoothing, t_Distance);" +
                "gl_FragColor = u_Color;" +
                "gl_FragColor.w *= t_Alpha;" +
                "}");
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
        uniform("u_Color");
    }
}
