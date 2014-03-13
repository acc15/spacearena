package ru.spacearena.game;

import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionEntity;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Color;
import ru.vmsoftware.math.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends AbstractCollisionObject {

    private Bounds bounds;

    public static final float SPEED = 1500f;

    public Bullet(Bounds bounds, float x, float y, float angle) {
        this.bounds = bounds;
        setPosition(x, y);
        setRotation(angle);
        setVelocity(FloatMathUtils.cos(angle) * angle, FloatMathUtils.sin(angle) * angle);
        //setVelocityByAngle(angle);
        add(new Rectangle(Color.RED, SHAPE[0], SHAPE[1], SHAPE[4], SHAPE[5]));
    }

    public boolean onCollision(CollisionEntity entity, boolean reference, Contact contact) {
        return false;
    }

    public boolean canCollide(CollisionEntity entity) {
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
