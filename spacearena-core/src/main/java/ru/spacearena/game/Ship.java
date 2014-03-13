package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionEntity;
import ru.spacearena.engine.collisions.Contact;
import ru.spacearena.engine.common.Sprite;
import ru.vmsoftware.math.geometry.shapes.AABB2F;
import ru.vmsoftware.math.geometry.shapes.Rect2FPR;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends AbstractCollisionObject {

    public static final float MAX_SPEED = 500f;
    public static final float ACCELERATION = 2000f;
    public static final float ANGULAR_VELOCITY = 720f;

    private final Rect2FPR bounds = new Rect2FPR();

    public Ship() {
        add(new Sprite());
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {90,10,90,85};
        getWorldSpace().mapPoints(positions);
        return positions;
    }

    @Override
    public void onInit(Engine engine) {
        getSprite().setImage(engine.loadImage("ship.png"));
        setPivot(getSprite().getWidth()*2/3, getSprite().getHeight() / 2);
        super.onInit(engine);
    }

    private Sprite getSprite() {
        return getChild(0);
    }

    public boolean onCollision(CollisionEntity entity, boolean reference, Contact contact) {

        return true;
    }

    public boolean canCollide(CollisionEntity entity) {
        return true;
    }

    private static final float[][] SHIP_SHAPES = new float[][] {
        {0,0, 0,94, 164,94, 164,0}
    };

    @Override
    public float[] getConvexShape(int n) {
        return SHIP_SHAPES[n];
    }

    @Override
    public int getConvexShapeCount() {
        return SHIP_SHAPES.length;
    }

    public AABB2F getAABB() {
        bounds.moveTo(getX(), getY());
        return bounds;
    }

    /*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
