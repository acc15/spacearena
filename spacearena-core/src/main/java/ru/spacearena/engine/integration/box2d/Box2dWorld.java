package ru.spacearena.engine.integration.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.graphics.Matrix;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends Transform {

    private final World world;
    private int velocityIters = 7;
    private int positionIters = 3;
    private float step = 1 / 60f;

    public World getWorld() {
        return world;
    }

    public Box2dWorld() {
        this(0f, 0f);
    }

    public Box2dWorld(float gravityX, float gravityY) {
        world = new World(new Vec2(gravityX, gravityY));
    }

    @Override
    public boolean onUpdate(float seconds) {
        world.step(step, velocityIters, positionIters);
        return true;
    }

    @Override
    public Matrix getViewMatrix() {
        return getLocalSpace();
    }

}
