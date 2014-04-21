package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-21-04
 */
public class DefaultShaders {

    public static final VertexBufferLayout LAYOUT_P2C3 = new VertexBufferLayout.Builder().
            floats(2).floats(3).build();

    public static final VertexBufferLayout LAYOUT_P2C4 = new VertexBufferLayout.Builder().
            floats(2).floats(4).build();

    public static final VertexBufferLayout LAYOUT_P2 = new VertexBufferLayout.Builder().floats(2).build();

    public static final VertexBufferLayout LAYOUT_P2T2 = new VertexBufferLayout.Builder().floats(2).floats(2).build();

    public static final ShaderProgram.Definition POSITION_PROGRAM = new ShaderProgram.Definition().
            shader(DefaultShaders.class, "p.vert").
            shader(DefaultShaders.class, "p.frag").
            attribute("a_Position").
            uniform("u_MVPMatrix").
            uniform("u_Color");

    public static final ShaderProgram.Definition POSITION_COLOR_PROGRAM = new ShaderProgram.Definition().
            shader(DefaultShaders.class, "pc.vert").
            shader(DefaultShaders.class, "pc.frag").
            attribute("a_Position").
            attribute("a_Color").
            uniform("u_MVPMatrix");

    public static final ShaderProgram.Definition POSITION_TEXTURE_PROGRAM = new ShaderProgram.Definition().
            shader(DefaultShaders.class, "tex.vert").
            shader(DefaultShaders.class, "tex.frag").
            attribute("a_Position").
            attribute("a_TexCoord").
            uniform("u_MVPMatrix").
            uniform("u_Texture");

    public static final ShaderProgram.Definition DISTANCE_FIELD_PROGRAM = new ShaderProgram.Definition().
            shader(DefaultShaders.class, "df.vert").
            shader(DefaultShaders.class, "df.frag").
            attribute("a_Position").
            attribute("a_TexCoord").
            uniform("u_MVPMatrix").
            uniform("u_Texture").
            uniform("u_Color").
            uniform("u_Smooth");

}
