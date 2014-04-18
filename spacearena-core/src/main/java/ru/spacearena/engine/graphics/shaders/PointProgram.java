package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-18-04
 */
public class PointProgram extends Program {

    public static final Program.Definition DEFINITION = new Program.Definition() {
        public Program createProgram() {
            return new PointProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int COLOR_ATTR = 1;
    public static final int SIZE_ATTR = 2;
    public static final int MATRIX_UNIFORM = 0;

//    public static final VertexBufferLayout LAYOUT_P2C4S1 = VertexBufferLayout.create().
//            floats(2).floats(4).floats(1).build();
//
    public static final VertexBufferLayout LAYOUT_P2C3S1 = VertexBufferLayout.create().
            floats(2).floats(3).floats(1).build();

    public static final VertexBufferLayout LAYOUT_P2C3 = VertexBufferLayout.create().
            floats(2).floats(3).build();

    private PointProgram() {
        shader("pt.vert");
        shader("pt.frag");
        attribute("a_Position");
        attribute("a_Color");
        attribute("a_PointSize");
        //attribute("a_PointSize");
        uniform("u_MVPMatrix");
    }
}
