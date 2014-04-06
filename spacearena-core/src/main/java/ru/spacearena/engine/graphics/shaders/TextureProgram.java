package ru.spacearena.engine.graphics.shaders;

import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.vbo.VertexBufferLayout;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-06-04
 */
public class TextureProgram extends Program {

    public static final Program.Definition DEFINITION = new Program.Definition() {
        public Program createProgram() {
            return new TextureProgram();
        }
    };

    public static final int POSITION_ATTR = 0;
    public static final int TEXCOORD_ATTR = 1;
    public static final int MATRIX_UNIFORM = 0;
    public static final int TEXTURE_UNIFORM = 1;

    public static final VertexBufferLayout LAYOUT_PT2 = new VertexBufferLayout.Builder().floats(2).floats(2).build();

    private TextureProgram() {
        shader(OpenGL.VERTEX_SHADER,
                "uniform mat4 u_MVPMatrix;" +
                "attribute vec4 a_Position;" +
                "attribute vec2 a_TexCoord;" +
                "varying vec2 v_TexCoord;" +
                "void main()" +
                "{" +
                "v_TexCoord = a_TexCoord;" +
                "gl_Position = u_MVPMatrix * a_Position;" +
                "}").
        shader(OpenGL.FRAGMENT_SHADER,
                "precision mediump float;" +
                "varying vec2 v_TexCoord;" +
                "uniform sampler2D u_Texture;" +
                "void main()" +
                "{" +
                "gl_FragColor = texture2D(u_Texture, v_TexCoord);" +
                "}").
        attribute("a_Position");
        attribute("a_TexCoord");
        uniform("u_MVPMatrix");
        uniform("u_Texture");
    }

}
