package ru.spacearena.game;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import ru.spacearena.engine.Engine;
import ru.spacearena.engine.graphics.DrawContext;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.integration.box2d.Box2dObject;
import ru.spacearena.engine.integration.box2d.Box2dWorld;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends Box2dObject {

    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 2000f;
    public static final float ANGULAR_VELOCITY = 720f;

    private Image image;

    private static final PolygonShape FIXTURE_SHAPE = new PolygonShape();
    static {
        FIXTURE_SHAPE.setAsBox(3, 2, new Vec2(1, 0), 0);
    }

    private static final BodyDef BODY_DEF = new BodyDef();
    static {
        BODY_DEF.type = BodyType.DYNAMIC;
        BODY_DEF.linearDamping = 0.1f;
        BODY_DEF.angularDamping = 0.1f;
    }

    private static final FixtureDef FIXTURE_DEF = new FixtureDef();
    static {
        FIXTURE_DEF.restitution = 0.1f;
        FIXTURE_DEF.density = 87;
        FIXTURE_DEF.shape = FIXTURE_SHAPE;
    }

    @Override
    public void onCreate(Box2dWorld world) {
        super.onCreate(world);
        final Body body = world.getWorld().createBody(BODY_DEF);
        body.createFixture(FIXTURE_DEF);
        setBody(body);
    }

    @Override
    public void onAttach(Engine engine) {
        super.onAttach(engine);
        image = getEngine().loadImage("ship.png");
        setPivot(image.getWidth() / 3, image.getHeight() / 2);
    }

    @Override
    protected void onDrawGraphic(DrawContext context) {
        context.drawImage(image, 0, 0);
    }

    /*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
