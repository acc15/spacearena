package ru.spacearena.engine.integration.box2d;

import org.jbox2d.callbacks.ContactFilter;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.graphics.DrawContext;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends EngineContainer<Box2dObject> {

    private final World world;
    private int velocityIters = 7;
    private int positionIters = 3;
    private float timeScale = 1f;

    public World getWorld() {
        return world;
    }

    public Box2dWorld() {
        this(0f, 0f);
    }

    public Box2dWorld(float gravityX, float gravityY) {
        world = new World(new Vec2(gravityX, gravityY));
        world.setContactFilter(new ContactFilter() {
            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                final Box2dObject o1 = (Box2dObject)fixtureA.getBody().getUserData();
                final Box2dObject o2 = (Box2dObject)fixtureB.getBody().getUserData();
                return o1.canCollide(o2) && o2.canCollide(o1);
            }
        });
        world.setContactListener(new ContactListener() {
            public void beginContact(Contact contact) {
                final Box2dObject o1 = (Box2dObject)contact.getFixtureA().getBody().getUserData();
                final Box2dObject o2 = (Box2dObject)contact.getFixtureB().getBody().getUserData();
                o1.onCollision(o2);
                o2.onCollision(o1);
            }

            public void endContact(Contact contact) {
            }

            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    public float getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public int getVelocityIters() {
        return velocityIters;
    }

    public void setVelocityIters(int velocityIters) {
        this.velocityIters = velocityIters;
    }

    public int getPositionIters() {
        return positionIters;
    }

    public void setPositionIters(int positionIters) {
        this.positionIters = positionIters;
    }

    @Override
    protected void onAttachChild(Box2dObject entity) {
        entity.onCreate(this);
    }

    @Override
    public boolean onUpdate(float seconds) {
        world.step(seconds * timeScale, velocityIters, positionIters);
        return super.onUpdate(seconds);
    }

    @Override
    public void onDraw(DrawContext context) {
        final float lw = context.getLineWidth();
        try {
            context.setLineWidth(0);
            super.onDraw(context);
        } finally {
            context.setLineWidth(lw);
        }
    }
}
