package ru.spacearena.game.ship;

import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.geometry.shapes.Rect2ID;
import ru.spacearena.engine.graphics.*;
import ru.spacearena.engine.graphics.fbo.FrameBufferObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-27-03
 */
public class Explosion extends EngineObject {

    public static final float DURATION = 5f;


    public static final Texture.Definition TEXTURE = new Texture.Definition().empty(256, 256,
            OpenGL.GL_RGBA, OpenGL.GL_UNSIGNED_INT);
    public static final FrameBufferObject.Definition FRAMEBUF = new FrameBufferObject.Definition().attach(TEXTURE);


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
        try {
            context.drawTo(FRAMEBUF);
            context.getViewport(viewport);
            context.setViewport(0,0,256,256);
            context.setMatrix(identity);
            context.color(Color.BLACK).clear();
            context.color(Color.RED).fillRect(-0.5f, 0.5f, 0.5f, -0.5f);
        } finally {
            context.popMatrix();
            context.setViewport(viewport);
            context.drawTo(null);
        }
        context.drawImage(-10, -10, 10, 10, TEXTURE);
    }
}
