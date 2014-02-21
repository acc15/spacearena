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
        setSpeed(1500f);
        setVelocityByAngle(angle);
        add(new Rectangle(Color.RED, SHAPE[0], SHAPE[1], SHAPE[4], SHAPE[5]));
    }

    public boolean onCollision(CollisionContainer.CollisionEntity entity, boolean b, float penetrationX, float penetrationY) {
        return false;
    }

    public boolean canCollide(CollisionContainer.CollisionEntity entity) {
        return entity instanceof Ship;
    }

    private static final float[] SHAPE = new float[] {-5,-20, -5,20, 5,20, 5,-20};

    @Override
    public float[] getConvexShape(int n) {
        return SHAPE;
    }

    @Override
    public int getConvexShapeCount() {
        return 1;
    }

    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }
}
