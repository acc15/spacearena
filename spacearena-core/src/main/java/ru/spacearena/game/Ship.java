package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.integration.box2d.Box2dObject;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends Box2dObject {

    public static final float MAX_SPEED = 20f;
    public static final float ACCELERATION = 30f;
    public static final float ANGULAR_VELOCITY = FloatMathUtils.TWO_PI;

    public Vec2[] getGuns() {
        return GUN_POS;
    }

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.1f;
        bodyDef.angularDamping = 0.1f;
    }

    private static final Vec2[] GUN_POS = new Vec2[]{new Vec2(1f, 1.5f), new Vec2(1f, -1.5f)};
    private static final Vec2[] SHAPE = new Vec2[]{new Vec2(-2, -2), new Vec2(-2, 2), new Vec2(4, 0.3f), new Vec2(4, -0.3f)};

    public void onPostCreate(Body body) {
        final Sprite sprite = new Sprite(getEngine().loadImage("ship.png"));
        sprite.setPivot(sprite.getWidth() / 3, sprite.getHeight() / 2);
        sprite.setScale(6 / sprite.getWidth());
        add(sprite);

        final PolygonShape shape = new PolygonShape();
        shape.set(SHAPE, SHAPE.length);

        final FixtureDef fd = new FixtureDef();
        fd.restitution = 0.1f;
        fd.density = 87;
        fd.shape = shape;
        body.createFixture(fd);
    }

}
