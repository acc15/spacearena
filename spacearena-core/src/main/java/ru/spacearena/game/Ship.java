package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.collisions.AbstractCollisionObject;
import ru.spacearena.engine.collisions.CollisionContainer;
import ru.spacearena.engine.common.BoundChecker;
import ru.spacearena.engine.common.PhysicsHandler;
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
        getPhysics().setSpeed(5000f);
        getPhysics().setAcceleration(500f);
        getPhysics().setAngularSpeed(720f);
        add(new Sprite());
    }

    @Override
    public Bounds getOriginalBounds() {
        return getSprite();
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

    public PhysicsHandler getPhysics() {
        return get(0);
    }

    private Sprite getSprite() {
        return get(1);
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
            getPhysics().setVelocityX(0);
        }
        if (!FloatMathUtils.isZero(dy)) {
            getPhysics().setVelocityY(0);
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

        final float vx1 = ship1.getPhysics().getCurrentVelocityX();
        final float vx2 = ship2.getPhysics().getCurrentVelocityX();
        ship1.getPhysics().setCurrentVelocityX(-vx1 * 0.2f + vx2 * 0.8f);
        ship2.getPhysics().setCurrentVelocityX(-vx2 * 0.2f + vx1 * 0.8f);

        final float vy1 = ship1.getPhysics().getCurrentVelocityY();
        final float vy2 = ship2.getPhysics().getCurrentVelocityY();
        ship1.getPhysics().setCurrentVelocityY(-vy1 * 0.2f + vy2 * 0.8f);
        ship2.getPhysics().setCurrentVelocityY(-vy2 * 0.2f + vy1 * 0.8f);

        return true;
    }

    public boolean canCollide(CollisionContainer.CollisionEntity entity) {
        return true;
    }
/*
    @Override
    public void onDraw(DrawContext context) {
        super.onDraw(context);
        context.setColor(Color.GREEN);
        context.drawRect(getMinX(), getMinY(), getMaxX(), getMaxY());
    }*/
}
