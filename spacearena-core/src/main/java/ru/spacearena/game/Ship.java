package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionEntity;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.common.BoundChecker;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Image;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends AbstractCollisionObject implements BoundChecker.Bounded, Bounds {

    private float boundDistance = 120f;

    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 2000f;
    public static final float ANGULAR_VELOCITY = 720f;


    public Ship() {
        add(new Sprite());
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {10,70,85,70};
        getWorldSpace().mapPoints(positions);
        return positions;
    }

    @Override
    public void onInit(Engine engine) {
        final Image image = engine.loadImage("ship.png");
        getSprite().setImage(image);

        final float px = image.getWidth() / 2, py = image.getHeight() * 2 / 3;
        setPivot(px, py);
        super.onInit(engine);
    }

    private Sprite getSprite() {
        return get(0);
    }

    public Bounds getBounds() {
        return this;
    }

    public float getMinX() {
        return getX() - boundDistance;
    }

    public float getMaxX() {
        return getX() + boundDistance;
    }

    public float getMinY() {
        return getY() - boundDistance;
    }

    public float getMaxY() {
        return getY() + boundDistance;
    }

    public void onOutOfBounds(float dx, float dy) {
        translate(dx, dy);
        /*
        if (!FloatMathUtils.isZero(dx)) {
            setVelocityX(0);
        }
        if (!FloatMathUtils.isZero(dy)) {
            setVelocityY(0);
        }*/
    }

    public boolean onCollision(CollisionEntity entity, boolean reference, Contact contact) {

        return true;
    }

    public boolean canCollide(CollisionEntity entity) {
        return true;
    }

    private static final float[][] SHIP_SHAPES = new float[][] {
        {40,0, 25,90, 70,90, 53,0},
        {47,70, 0, 110, 0, 164, 47, 135},
        {47,70, 47, 135, 94, 164, 94, 110}
    };

    @Override
    public float[] getConvexShape(int n) {
        return SHIP_SHAPES[n];
    }

    @Override
    public int getConvexShapeCount() {
        return SHIP_SHAPES.length;
    }

    /*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
