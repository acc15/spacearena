package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.geometry.primitives.Point2F;
import ru.spacearena.engine.graphics.Color;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.integration.box2d.Box2dBody;
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

    public Point2F[] getGuns() {
        return LOCAL_GUN_POS;
    }

    private static class SteamParticle {
        float time, x, y;
        boolean active;

        public SteamParticle(float time, float x, float y, boolean active) {
            this.time = time;
            this.x = x;
            this.y = y;
            this.active = active;
        }
    }

    private float timeSum = 0f;

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.2f;
        bodyDef.angularDamping = 0.2f;
    }

    private final LinkedList<SteamParticle> engineParticles = new LinkedList<SteamParticle>();

    public void flyTo(float dx, float dy, float seconds) {

        final boolean engineDisabled = FloatMathUtils.isZero(dx, dy);


        final Point2F pt = mapPoint(TempUtils.tempPoint(LOCAL_ENGINE_POS));
        engineParticles.add(new SteamParticle(seconds, pt.x, pt.y, !engineDisabled));
        timeSum += seconds;

        SteamParticle particle;
        while (timeSum > STEAM_TIME && (particle = engineParticles.poll()) != null) {
            timeSum -= particle.time;
        }
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

    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);

        final Iterator<SteamParticle> iter = engineParticles.descendingIterator();

        int i = 0;
        while (iter.hasNext()) {
            final SteamParticle p = iter.next();
            if (p.active) {
                final float size = (float)(engineParticles.size()-i)/engineParticles.size();
                context.setColor(Color.argb(1.f, size, size, 1f));
                final float r = size * 0.3f;
                context.fillCircle(p.x, p.y, r);
            }
            ++i;
        }
    }


}
