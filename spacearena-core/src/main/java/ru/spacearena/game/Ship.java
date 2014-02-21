package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionContainer;
import ru.spacearena.engine.common.BoundChecker;
import ru.spacearena.engine.common.Sprite;
import ru.spacearena.engine.geom.Bounds;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends AbstractCollisionObject implements BoundChecker.Bounded, Bounds {

    private float boundDistance = 120f;

    public Ship() {
        setSpeed(5000f);
        setAcceleration(500f);
        setAngularSpeed(720f);
        add(new Sprite());
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {10,70,85,70};
        mapPoints(positions);
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

    @Override
    public Bounds getOriginalBounds() {
        return getSprite();
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
        if (!FloatMathUtils.isZero(dx)) {
            setVelocityX(0);
        }
        if (!FloatMathUtils.isZero(dy)) {
            setVelocityY(0);
        }
    }

    public boolean onCollision(CollisionContainer.CollisionEntity entity, boolean first, float penetrationX, float penetrationY) {

        if (!first) {
            return true;
        }

        final Ship ship1 = this;
        final Ship ship2 = (Ship)entity;

        ship1.translate(ship1.getFrameVelocityX()-penetrationX, ship1.getFrameVelocityY()-penetrationY);
        //ship2.translate(ship2.getFrameVelocityX()+penetrationX, ship2.getFrameVelocityY()+penetrationY);

        final float vx1 = ship1.getCurrentVelocityX();
        final float vx2 = ship2.getCurrentVelocityX();
        ship1.setCurrentVelocityX(-vx1 * 0.2f + vx2 * 0.8f);
        ship2.setCurrentVelocityX(-vx2 * 0.2f + vx1 * 0.8f);

        final float vy1 = ship1.getCurrentVelocityY();
        final float vy2 = ship2.getCurrentVelocityY();
        ship1.setCurrentVelocityY(-vy1 * 0.2f + vy2 * 0.8f);
        ship2.setCurrentVelocityY(-vy2 * 0.2f + vy1 * 0.8f);

        return true;
    }

    public boolean canCollide(CollisionContainer.CollisionEntity entity) {
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
