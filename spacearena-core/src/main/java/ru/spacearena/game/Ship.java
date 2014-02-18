package ru.spacearena.game;

import ru.spacearena.engine.Engine;
import ru.spacearena.engine.common.*;
import ru.spacearena.engine.graphics.Image;
import ru.spacearena.engine.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-16-02
 */
public class Ship extends Transform implements BoundChecker.Bounded {

    private float boundDistance = 120f;

    public Ship() {
        final PhysicsHandler physics = new PhysicsHandler(this);
        physics.setSpeed(1000f);
        physics.setAcceleration(1000f);
        physics.setAngularSpeed(720f);
        add(physics);
        add(new Sprite());
    }

    public float[] getGunPositions() {
        final float[] positions = new float[] {35, 100, 110, 100};
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

    public void offset(float dx, float dy) {
        setPosition(getX() + dx, getY() + dy);
        if (!FloatMathUtils.isZero(dx)) {
            getPhysics().setVelocityX(0);
        }
        if (!FloatMathUtils.isZero(dy)) {
            getPhysics().setVelocityY(0);
        }
    }
}
