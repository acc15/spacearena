package ru.spacearena.engine.graphics.font;

import ru.spacearena.engine.graphics.shaders.ShaderProgram;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-08-04
 */
public class DistanceFieldProgram extends ShaderProgram {

    public static final ShaderProgram.Definition DEFINITION = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            return new DistanceFieldProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int TEXCOORD_ATTR = 1;
    public static final int MATRIX_UNIFORM = 0;
    public static final int TEXTURE_UNIFORM = 1;
    public static final int COLOR_UNIFORM = 2;
    public static final int SMOOTH_UNIFORM = 3;

    private DistanceFieldProgram() {
        shader("df.vert");
        shader("df.frag");
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
        uniform("u_Color");
        uniform("u_Smooth");
    }
}
