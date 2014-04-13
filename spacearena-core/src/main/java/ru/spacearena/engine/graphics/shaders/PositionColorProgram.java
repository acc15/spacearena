package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-04
 */
public class PositionColorProgram extends Program {

    public static final Definition DEFINITION = new Definition() {
        public Program createProgram() {
            return new PositionColorProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int COLOR_ATTR = 1;
    public static final int MATRIX_UNIFORM = 0;

    public static final VertexBufferLayout LAYOUT_P2C3 = new VertexBufferLayout.Builder().
            floats(2).floats(3).build();

    public static final VertexBufferLayout LAYOUT_P2C4 = new VertexBufferLayout.Builder().
            floats(2).floats(4).build();

    private PositionColorProgram() {
        shader("pc.vert");
        shader("pc.frag");
        attribute("a_Position");
        attribute("a_Color");
        uniform("u_MVPMatrix");
    }

}
