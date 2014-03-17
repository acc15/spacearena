package ru.spacearena.engine.integration.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends Transform<Box2dObject> {

    private final World world;
    private int velocityIters = 7;
    private int positionIters = 3;
    private float timeScale = 1f;

    public World getWorld() {
        return world;
    }

    public Box2dWorld() {
        this(0f, 0f);
    }

    public Box2dWorld(float gravityX, float gravityY) {
        world = new World(new Vec2(gravityX, gravityY));
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    @Override
    protected void onAddChild(Box2dObject entity) {
        entity.onCreate(this);
    }

    @Override
    public boolean onUpdate(float seconds) {
        world.step(seconds * timeScale, velocityIters, positionIters);
        return true;
    }

    @Override
    public Matrix getViewMatrix() {
        return getLocalSpace();
    }

    @Override
    public void onDraw(DrawContext context) {
        final float lw = context.getLineWidth();
        try {
            context.setLineWidth(0);
            super.onDraw(context);
        } finally {
            context.setLineWidth(lw);
        }
    }
}