package ru.spacearena.game;

import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionEntity;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.graphics.Color;
import ru.vmsoftware.math.geometry.shapes.AABB2F;
import ru.vmsoftware.math.geometry.shapes.Rect2FPP;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends AbstractCollisionObject {

    public static final float SPEED = 1500f;

    private final Rect2FPP boundingBox = new Rect2FPP();

    public Bullet(float x, float y, float angle) {
        setPosition(x, y);
        setRotation(angle);
        setVelocityByAngle(angle, SPEED);
        add(new Rectangle(Color.RED, SHAPE[0], SHAPE[1], SHAPE[4], SHAPE[5]));
    }

    public boolean onCollision(CollisionEntity entity, boolean reference, Contact contact) {
        return false;
    }

    public boolean canCollide(CollisionEntity entity) {
        return entity instanceof Ship;
    }

    private static final float[] SHAPE = new float[] {-20,-5, -20,5, 20,5, 20,-5};

    public AABB2F getAABB() {
        return boundingBox;
    }

    @Override
    public float[] getConvexShape(int n) {
        return SHAPE;
    }

    @Override
    public int getConvexShapeCount() {
        return 1;
    }
/*
    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }*/
}
