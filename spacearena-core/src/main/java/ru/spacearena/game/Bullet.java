package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.integration.box2d.Box2dObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends Box2dObject {

    public static final float SPEED = 50f;

    private Ship owner;

    public Bullet(Ship owner, float x, float y, float rotateX, float rotateY, float angle) {
        this.owner = owner;
        setInitialPosition(x, y);
        setInitialVelocity(rotateX * SPEED, rotateY * SPEED);
        setInitialAngle(angle);
    }

    @Override
    protected void onPreCreate(BodyDef bodyDef) {
        bodyDef.bullet = true;
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0f;
        bodyDef.fixedRotation = true;
    }

    @Override
    protected void onPostCreate(Body body) {
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(2f, 0.1f);
        body.createFixture(shape, 2000f);
    }

    @Override
    public boolean canCollide(Box2dObject object) {
        return object != owner;
    }

    @Override
    public void onCollision(Box2dObject object) {
        markDead();
    }

    @Override
    protected void onDrawTransformed(DrawContext context) {
        context.setColor(Color.RED);
        drawBodyShapes(context, true, getBody());
    }

    /*
    @Override
    public boolean onUpdate(float seconds) {
        return super.onUpdate(seconds) &&
               FloatMathUtils.inRange(getX(), bounds.getMinX(), bounds.getMaxX()) &&
               FloatMathUtils.inRange(getY(), bounds.getMinY(), bounds.getMaxY());
    }*/
}
