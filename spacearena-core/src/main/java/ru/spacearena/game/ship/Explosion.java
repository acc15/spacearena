package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2ID;
import ru.spacearena.engine.graphics.DrawContext2f;
import ru.spacearena.engine.graphics.Matrix;
import ru.spacearena.engine.graphics.OpenGL;
import ru.spacearena.engine.graphics.Texture;
import ru.spacearena.engine.graphics.fbo.FrameBufferObject;
import ru.spacearena.engine.graphics.shaders.DefaultShaders;
import ru.spacearena.engine.graphics.shaders.ShaderProgram;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-27-03
 */
public class Explosion extends EngineObject {

    public static final float DURATION = 5f;


    public static final Texture.Definition TEXTURE = new Texture.Definition().empty(256, 256,
            OpenGL.GL_RGBA, OpenGL.GL_UNSIGNED_INT);
    public static final FrameBufferObject.Definition FRAMEBUF = new FrameBufferObject.Definition().attach(TEXTURE);

    public static final ShaderProgram.Definition BLUR_PROGRAM = new ShaderProgram.Definition().
        shader(DefaultShaders.class, "tex.vert").
        shader(Explosion.class, "blur.frag").
        attribute("a_Position").
        attribute("a_TexCoord").
        uniform("u_MVPMatrix").
        uniform("u_Texture");


    private final float x, y;
    private float duration = 0;

    public Explosion(float x, float y, float angle) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void onUpdate(float seconds) {
        duration += seconds * 5f;
        if (duration > DURATION) {
            kill();
        }
    }

    private final Matrix identity = new Matrix();
    private final Rect2ID viewport = new Rect2ID();

    @Override
    public void onDraw(DrawContext2f context) {
//        try {
//            context.drawTo(FRAMEBUF);
//            context.getViewport(viewport);
//            context.setViewport(0, 0, 256, 256);
//            context.setMatrix(identity);
//            context.color(Color.BLACK).clear();
//            context.color(Color.RED).drawRect(-0.5f, 0.5f, 0.5f, -0.5f);
//            context.color(Color.GREEN).drawRect(-0.5f, -0.5f, 0, 0);
//            context.color(Color.YELLOW).drawRect(-0.25f, 0, 0, 0.25f);
//        } finally {
//            context.popMatrix();
//            context.setViewport(viewport);
//            context.drawTo(null);
//        }
//        final Texture t = context.get(TEXTURE);
//        final VertexBuffer vb = context.getSharedBuffer();
//        vb.reset(DefaultShaders.LAYOUT_P2T2);
//        vb.put(x-10, y-10).put(t.getLeft(), t.getTop()).
//           put(x-10, y+10).put(t.getLeft(), t.getBottom()).
//           put(x+10, y+10).put(t.getRight(), t.getBottom()).
//           put(x+10, y-10).put(t.getRight(), t.getTop());
//        context.use(BLUR_PROGRAM).
//                attrs(vb).
//                uniform(context.getActiveMatrix()).
//                uniform(TEXTURE, 0).
//                draw(OpenGL.GL_TRIANGLE_FAN);

        //context.drawImage();
    }
}
