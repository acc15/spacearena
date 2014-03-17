package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import ru.spacearena.engine.Engine;
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

    public Ship() {
        add(new Sprite());
    }

    private static final PolygonShape FIXTURE_SHAPE = new PolygonShape();
    static {
        FIXTURE_SHAPE.setAsBox(3, 2, new Vec2(1, 0), 0);
    }

    private static final BodyDef BODY_DEF = new BodyDef();
    static {
        BODY_DEF.type = BodyType.DYNAMIC;
    }

    private static final FixtureDef FIXTURE_DEF = new FixtureDef();
    static {
        FIXTURE_DEF.restitution = 0.1f;
        FIXTURE_DEF.density = 87;
        FIXTURE_DEF.shape = FIXTURE_SHAPE;
    }

    @Override
    public void onCreate(World world) {
        final Body body = world.createBody(BODY_DEF);
        body.createFixture(FIXTURE_DEF);
        addBody(body);
    }

    @Override
    public void onInit(Engine engine) {
        getSprite().setImage(engine.loadImage("ship.png"));
        //setPivot(getSprite().getWidth()/3, getSprite().getHeight()/2);
        super.onInit(engine);
    }

    private Sprite getSprite() {
        return getChild(0);
    }

    /*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
