package ru.spacearena.game;

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
import ru.spacearena.engine.graphics.Path;
import ru.spacearena.engine.integration.box2d.Box2dBody;
import ru.spacearena.engine.timing.Timer;
import ru.spacearena.engine.util.FloatMathUtils;
import ru.spacearena.engine.util.TempUtils;

import java.util.Iterator;
import java.util.LinkedList;

import static ru.spacearena.engine.geometry.primitives.Point2F.p;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends Box2dBody {

    private static final float MAX_SPEED = 30f;
    private static final float ACCELERATION = 30f;
    private static final float ANGULAR_VELOCITY = FloatMathUtils.TWO_PI * 2;

    private static final float STEAM_TIME = 0.5f;

    private static final Point2F[] LOCAL_GUN_POS = new Point2F[]{p(1f, 1.5f), p(1f, -1.5f)};
    private static final Point2F LOCAL_ENGINE_POS = new Point2F(-1.7f, 0f);
    private static final Vec2[] LOCAL_SHAPE = new Vec2[]{new Vec2(-2, -2), new Vec2(-2, 2), new Vec2(4, 0.3f), new Vec2(4, -0.3f)};

    private final LinkedList<FlameParticle> engineParticles = new LinkedList<FlameParticle>();

    public Point2F[] getGuns() {
        return LOCAL_GUN_POS;
    }

    private class EngineFlame extends EngineObject {

        @Override
        public void onDraw(DrawContext context) {
            final Iterator<FlameParticle> iter = engineParticles.descendingIterator();
            int i = 0;

            final float fSize = (float) engineParticles.size();
            float prevX = 0f, prevY = 0f;
            while (iter.hasNext()) {
                final FlameParticle p = iter.next();

                final float size = 1f - (float)i / fSize;
                final float r = size * 0.3f;
                if (i > 0) {
                    final float prevSize = 1f - (float)(i - 1) / fSize;
                    final Path path = context.preparePath();
                    //path.moveTo();

                    context.fillColor(Color.argb(1.f, prevSize, prevSize, 1f));
                    context.fillPath();
                }

                prevX = p.x;
                prevY = p.y;
                ++i;
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

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.2f;
        bodyDef.angularDamping = 0.2f;
    }

    private void addFlameParticle(boolean engineDisabled) {
        final Timer timer = getEngine().getTimer();
        final long t = timer.getTimestamp();

        FlameParticle particle;
        while ((particle = engineParticles.peek()) != null && timer.toSeconds(t - particle.timestamp) > STEAM_TIME) {
            engineParticles.remove();
        }

        if (engineDisabled) {
            return;
        }

        final Point2F pt = mapPoint(TempUtils.tempPoint(LOCAL_ENGINE_POS));

        final FlameParticle p = engineParticles.peekLast();
        if (p != null && pt.equals(p.x, p.y)) {
            p.timestamp = t;
            p.active = !engineDisabled;
            return;
        }
        engineParticles.add(new FlameParticle(t, pt.x, pt.y, true));

    }

    public void flyTo(float dx, float dy, float seconds) {

        final boolean engineDisabled = FloatMathUtils.isZero(dx, dy);
        addFlameParticle(engineDisabled);
        if (engineDisabled) {
            return;
        }

        final float vl = Ship.MAX_SPEED/FloatMathUtils.length(dx, dy);
        final float vx = dx * vl, vy = dy * vl;
        accelerateTo(vx, vy, Ship.ACCELERATION * seconds);
        rotateTo(FloatMathUtils.atan2(vy, vx), Ship.ANGULAR_VELOCITY * seconds);
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
