package ru.spacearena.game;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.spacearena.engine.EngineContainer;
import ru.spacearena.engine.EngineObject;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;

import java.util.LinkedList;

import static ru.spacearena.engine.geometry.primitives.Point2F.p;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends GameBody {

    private static final float MAX_SPEED = 30f;
    private static final float ACCELERATION = 30f;
    private static final float ANGULAR_VELOCITY = FloatMathUtils.TWO_PI * 2;

    private static final float STEAM_TIME = 0.5f;

    private static final Point2F[] LOCAL_GUN_POS = new Point2F[]{p(1f, 1.5f), p(1f, -1.5f)};
    private static final Point2F LOCAL_ENGINE_POS = new Point2F(-1.3f, 0f);
    private static final Vec2[] LOCAL_SHAPE = new Vec2[]{new Vec2(-2, -2), new Vec2(-2, 2), new Vec2(4, 0.3f), new Vec2(4, -0.3f)};

    private final LinkedList<FlameParticle> engineParticles = new LinkedList<FlameParticle>();

    public Point2F[] getGuns() {
        return LOCAL_GUN_POS;
    }

    private class EngineFlame extends EngineObject {

        @Override
        public void onDraw(DrawContext context) {
            final float fSize = (float) engineParticles.size();
            float prevX = 0f, prevY = 0f;

            int i = 0;
            boolean hasPrev = false;

            final float lw = context.getLineWidth();
            try {
                for (FlameParticle p: engineParticles) {
                    if (hasPrev) {
                        final float prevSize = (float) i / fSize;
                        context.strokeColor(Color.argb(1.f, prevSize, prevSize, 1f));
                        context.setLineWidth(prevSize * 0.5f);
                        context.drawLine(prevX, prevY, p.x, p.y);
                    }
                    prevX = p.x;
                    prevY = p.y;
                    hasPrev = p.active;
                    ++i;
                }
            } finally {
                context.setLineWidth(lw);
            }
        }
    }

    private static class FlameParticle {
        float x, y;
        long timestamp;
        boolean active;

        public FlameParticle(long timestamp, float x, float y, boolean active) {
            this.timestamp = timestamp;
            this.x = x;
            this.y = y;
            this.active = active;
        }
    }

    public Ship(EngineContainer<? super EngineObject> fxContainer) {
        fxContainer.add(new EngineFlame());
    }

    @Override
    public ObjectType getType() {
        return ObjectType.SHIP;
    }

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.2f;
        bodyDef.angularDamping = 0.2f;
    }

    private void addFlameParticle(boolean active) {
        final Timer timer = getEngine().getTimer();
        final long t = timer.getTimestamp();

        FlameParticle particle;
        while ((particle = engineParticles.peek()) != null && timer.toSeconds(t - particle.timestamp) > STEAM_TIME) {
            engineParticles.remove();
        }

        final Point2F pt = mapPoint(TempUtils.tempPoint(LOCAL_ENGINE_POS));
        final FlameParticle last = engineParticles.peekLast();

        if (last == null) {
            if (active) {
                engineParticles.add(new FlameParticle(t, pt.x, pt.y, true));
            }
            return;
        }
        if (pt.equals(last.x, last.y)) {
            if (last.active) {
                last.timestamp = t;
            }
            last.active = active;
            return;
        }
        if (active || last.active) {
            engineParticles.add(new FlameParticle(t, pt.x, pt.y, active));
        }
    }

    public void flyTo(float dx, float dy, float seconds) {

        final boolean active = !FloatMathUtils.isZero(dx, dy);
        addFlameParticle(active);
        if (!active) {
            return;
        }

        final float vl = Ship.MAX_SPEED/FloatMathUtils.length(dx, dy);
        final float vx = dx * vl, vy = dy * vl;
        accelerateTo(vx, vy, Ship.ACCELERATION * seconds);
        rotateTo(FloatMathUtils.atan2(vy, vx), Ship.ANGULAR_VELOCITY * seconds);
    }

    @Override
    public void onCollision(Box2dBody object, boolean isReference, ContactImpulse impulse) {

    }

    public void onPostCreate(Body body) {
        final Sprite sprite = new Sprite(getEngine().getImage("ship.png"));
        sprite.setPivot(sprite.getWidth() / 3, sprite.getHeight() / 2);
        sprite.setScale(6 / sprite.getWidth());
        add(sprite);

        final PolygonShape shape = new PolygonShape();
        shape.set(LOCAL_SHAPE, LOCAL_SHAPE.length);

        final FixtureDef fd = new FixtureDef();
        fd.restitution = 0.1f;
        fd.density = 10f;
        fd.shape = shape;
        body.createFixture(fd);
    }

}
