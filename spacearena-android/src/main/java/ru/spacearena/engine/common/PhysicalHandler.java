package ru.spacearena.engine.common;

import ru.spacearena.engine.Point2F;
import ru.spacearena.engine.handlers.UpdateHandler;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class PhysicalHandler implements UpdateHandler {

    private TransformHandler transformHandler;
    private Point2F velocity = Point2F.ZERO;
    private Point2F acceleration = Point2F.ZERO;
    private float angularVelocity = 0f;

    // TODO add min/max speed

    public PhysicalHandler(TransformHandler transformHandler) {
        this.transformHandler = transformHandler;
    }

    public Point2F getVelocity() {
        return velocity;
    }

    public void setVelocity(Point2F velocity) {
        this.velocity = velocity;
    }

    public Point2F getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Point2F acceleration) {
        this.acceleration = acceleration;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean onUpdate(float seconds) {
        transformHandler.setRotation((transformHandler.getRotation() + angularVelocity * seconds) % 360);
        if (!acceleration.isZero()) {
            velocity = velocity.add(acceleration.mul(seconds));
        }
        transformHandler.setTranslate(transformHandler.getTranslate().add(velocity.mul(seconds)));
        return true;
    }
}
