package ru.spacearena.engine.integration.box2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import ru.spacearena.engine.common.GenericContainer;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends GenericContainer {

    private final World world = new World(new Vec2(0, 0));
    private int velocityIters = 7;
    private int positionIters = 3;

    public World getWorld() {
        return world;
    }

    @Override
    public boolean onUpdate(float seconds) {
        world.step(seconds, velocityIters, positionIters);
        return true;
    }


}
