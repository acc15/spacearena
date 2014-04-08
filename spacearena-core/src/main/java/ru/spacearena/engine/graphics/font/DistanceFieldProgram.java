package ru.spacearena.engine.graphics.font;

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
        shader("df.vert");
        shader("df.frag");
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
        uniform("u_Color");
    }
}
