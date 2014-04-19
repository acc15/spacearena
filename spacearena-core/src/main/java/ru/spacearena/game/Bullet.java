package ru.spacearena.game;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.contacts.Contact;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.game.ship.Ship;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Bullet extends GameBody {

    public static final float SPEED = 70f;

    private Ship owner;

    public Bullet(Ship owner, float x, float y, float angle) {
        this.owner = owner;
        setInitialPosition(x, y);
        setInitialVelocity(FloatMathUtils.cos(angle) * SPEED, FloatMathUtils.sin(angle) * SPEED);
        setInitialAngle(angle);
    }

    @Override
    public ObjectType getType() {
        return ObjectType.BULLET;
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
        shape.setAsBox(2f, 0.2f);
        body.createFixture(shape, 5f);
    }

    @Override
    public boolean canCollide(Box2dBody object) {
        if (GameBody.getObjectType(object) == ObjectType.BULLET) {
            return ((Bullet)object).owner != owner;
        }
        return object != owner;
    }

    @Override
    public void onCollision(Box2dBody object, boolean reference, Contact contact, ContactImpulse impulse) {
        markDead();
    }

    @Override
    protected void onDrawTransformed(DrawContext context) {
        drawBodyShapes(context, Color.RED, true);
    }

}
