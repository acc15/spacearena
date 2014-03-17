package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.integration.box2d.Box2dObject;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends Box2dObject {

    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 2000f;
    public static final float ANGULAR_VELOCITY = 720f;

    private static final PolygonShape FIXTURE_SHAPE = new PolygonShape();
    static {
        FIXTURE_SHAPE.set(new Vec2[] {new Vec2(-2,-2), new Vec2(-2, 2), new Vec2(4, 0.3f), new Vec2(4, -0.3f)}, 4);
    }

    private static final FixtureDef FIXTURE_DEF = new FixtureDef();
    static {
        FIXTURE_DEF.restitution = 0.1f;
        FIXTURE_DEF.density = 87;
        FIXTURE_DEF.shape = FIXTURE_SHAPE;
    }

    public void onPreCreate(BodyDef bodyDef) {
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.linearDamping = 0.1f;
        bodyDef.angularDamping = 0.1f;
    }

    public void onPostCreate(Body body) {
        final Sprite sprite = new Sprite(getEngine().loadImage("ship.png"));
        sprite.setPivot(sprite.getWidth() / 3, sprite.getHeight() / 2);
        sprite.setScale(6 / sprite.getWidth());
        add(sprite);
        body.createFixture(FIXTURE_DEF);
    }

}
