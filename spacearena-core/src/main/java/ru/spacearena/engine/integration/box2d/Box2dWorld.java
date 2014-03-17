package ru.spacearena.engine.integration.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends EngineContainer<Box2dObject> {

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

    public int getVelocityIters() {
        return velocityIters;
    }

    public void setVelocityIters(int velocityIters) {
        this.velocityIters = velocityIters;
    }

    public int getPositionIters() {
        return positionIters;
    }

    public void setPositionIters(int positionIters) {
        this.positionIters = positionIters;
    }

    @Override
    protected void onAttachChild(Box2dObject entity) {
        entity.onCreate(this);
    }

    @Override
    public boolean onUpdate(float seconds) {
        if (!super.onUpdate(seconds)) {
            return false;
        }
        world.step(seconds * timeScale, velocityIters, positionIters);
        return true;
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
