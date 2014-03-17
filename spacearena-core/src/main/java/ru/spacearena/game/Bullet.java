package ru.spacearena.game;

import ru.spacearena.engine.collisions.CollisionObject;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.graphics.Color;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends CollisionObject {

    public static final float SPEED = 1500f;

    public Bullet(float x, float y, float angle) {
        //super(new Rect2FPP(-20, -5, 20, 5));

        setPosition(x, y);
        setAngle(angle);
        setVelocityByAngle(angle, SPEED);
        add(new Rectangle(-20, -5, 20, 5, Color.RED));
    }

    public boolean onCollision(CollisionObject object, float time, boolean reference, Contact contact) {
        return false;
    }

    public boolean canCollide(CollisionObject object) {
        return true;
    }

/*
    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }*/
}
