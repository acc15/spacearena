package ru.spacearena.game;

import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionContainer;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends AbstractCollisionObject {

    private Bounds bounds;

    public Bullet(Bounds bounds, float x, float y, float angle) {
        this.bounds = bounds;
        setPosition(x, y);
        setAngle(angle);
        getPhysics().setSpeed(1500f);
        getPhysics().setVelocityByAngle(angle);
        add(new Rectangle(Color.RED, -5, -20, 5, 20));
    }

    @Override
    public Bounds getOriginalBounds() {
        return this.<Rectangle>get(1).getAABB();
    }

    public boolean onCollision(CollisionContainer.CollisionEntity entity, float timeOfImpact, float penetrationX, float penetrationY) {
        return false;
    }

    public boolean canCollide(CollisionContainer.CollisionEntity entity) {
        return entity instanceof Ship;
    }

    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }
}
