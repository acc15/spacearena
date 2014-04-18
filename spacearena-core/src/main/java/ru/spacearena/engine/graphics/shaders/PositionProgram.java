package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
* @author Vyacheslav Mayorov
* @since 2014-29-03
*/
public class PositionProgram extends ShaderProgram {

    public static final Definition DEFINITION = new Definition() {
        public ShaderProgram createProgram() {
            return new PositionProgram();
        }
    };

    public static final VertexBufferLayout LAYOUT_P2 = new VertexBufferLayout.Builder().floats(2).build();

    private PositionProgram() {
        shader("p.vert");
        shader("p.frag");
        attribute("a_Position");
        uniform("u_MVPMatrix");
        uniform("u_Color");
    }
}
