package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class TextureProgram extends ShaderProgram {

    public static final ShaderProgram.Definition DEFINITION = new ShaderProgram.Definition() {
        public ShaderProgram createProgram() {
            return new TextureProgram();
        }
    };

    public static final VertexBufferLayout LAYOUT_PT2 = new VertexBufferLayout.Builder().floats(2).floats(2).build();

    private TextureProgram() {
        shader("tex.vert");
        shader("tex.frag");
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
    }

}
