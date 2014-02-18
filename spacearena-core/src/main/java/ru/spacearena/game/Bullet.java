package ru.spacearena.game;

import ru.spacearena.engine.common.PhysicsHandler;
import ru.spacearena.engine.common.Transform;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends Transform {

    private Bounds bounds;

    public Bullet(Bounds bounds, float x, float y, float angle) {
        this.bounds = bounds;
        setPosition(x, y);
        setAngle(angle);

        final PhysicsHandler physicsHandler = new PhysicsHandler(this);
        physicsHandler.setSpeed(1500f);
        physicsHandler.setVelocityByAngle(angle);
        add(physicsHandler);
        add(new Rectangle(Color.RED, -5, -20, 5, 20));
    }

    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }

    public PhysicsHandler getPhysics() {
        return get(0);
    }

}
