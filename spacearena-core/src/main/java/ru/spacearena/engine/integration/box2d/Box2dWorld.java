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

/**
 * @author Vyacheslav Mayorov
 * @since 2014-14-03
 */
public class Box2dWorld extends EngineContainer<Box2dObject> {

    // default update rate = 60Hz = 60FPS = 1/60seconds
    public static final float DEFAULT_TIME_STEP = 1/60f;

    public static final int MAX_SUB_STEPS = 10;

    private final World world;
    private int velocityIters = 7;
    private int positionIters = 3;
    private float timeStep = DEFAULT_TIME_STEP;

    private float accumulator = 0f;

    public World getWorld() {
        return world;
    }

    public Box2dWorld() {
        this(0f, 0f);
    }

    public Box2dWorld(float gravityX, float gravityY) {
        world = new World(new Vec2(gravityX, gravityY));
        world.setAutoClearForces(false);
        world.setContactFilter(new ContactFilter() {
            @Override
            public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
                final Box2dBody o1 = (Box2dBody)fixtureA.getBody().getUserData();
                final Box2dBody o2 = (Box2dBody)fixtureB.getBody().getUserData();
                return o1.canCollide(o2) && o2.canCollide(o1);
            }
        });
        world.setContactListener(new ContactListener() {
            public void beginContact(Contact contact) {
            }

            public void endContact(Contact contact) {
            }

            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            public void postSolve(Contact contact, ContactImpulse impulse) {
                final Box2dBody o1 = (Box2dBody)contact.getFixtureA().getBody().getUserData();
                final Box2dBody o2 = (Box2dBody)contact.getFixtureB().getBody().getUserData();
                o1.onCollision(o2, true, contact, impulse);
                o2.onCollision(o1, false, contact, impulse);
            }
        });
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

    public float getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    public void setFPS(float fps) {
        this.timeStep = 1/fps;
    }

    public float getFPS() {
        return 1/timeStep;
    }

    @Override
    protected void onAttachChild(Box2dObject entity) {
        entity.onCreate(this);
    }

    @Override
    public void onUpdate(float seconds) {
        if (timeStep <= 0) {
            doSingleStep(seconds);
        } else {
            doSubSteps(seconds);
        }
        super.onUpdate(seconds);
    }

    private void doSingleStep(float dt) {
        onStep(dt);
        world.clearForces();
        accumulator = 0f;
        onSmooth(1f);
    }

    private void doSubSteps(float dt) {
        accumulator += dt;
        final float ratio = accumulator/timeStep;
        int discreteSteps = (int)(ratio);
        if (discreteSteps <= 0) {
            onSmooth(ratio);
            return;
        }
        if (discreteSteps > MAX_SUB_STEPS) {
            discreteSteps = MAX_SUB_STEPS;
        }
        for (int i = 0; i < discreteSteps; i++) {
            onStep(timeStep);
        }
        world.clearForces();

        accumulator -= (float) discreteSteps * timeStep;
        onSmooth(accumulator/timeStep);
    }

    public void onSmooth(float ratio) {
        final float prevRatio = 1f - ratio;
        for (Box2dObject b2o: getChildren()) {
            b2o.onSmooth(ratio, prevRatio);
        }
    }

    public void onStep(float dt) {
        for (final Box2dObject b2o: getChildren()) {
            b2o.onBeforeStep(dt);
        }
        world.step(dt, velocityIters, positionIters);
        for (final Box2dObject b2o: getChildren()) {
            b2o.onAfterStep(dt);
        }
    }

}
