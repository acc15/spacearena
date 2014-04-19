package ru.spacearena.engine.graphics.shaders;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-19-04
 */
public class PositionDepthProgram extends ShaderProgram {

    public static final Definition DEFINITION = new Definition() {
        public ShaderProgram createProgram() {
            return new PositionDepthProgram();
        }
    };

    private PositionDepthProgram() {
        shader("pd.vert");
        shader("p.frag");
        attribute("a_Position");
        uniform("u_MVPMatrix");
        uniform("u_Color");
        uniform("u_Depth");
    }
}
