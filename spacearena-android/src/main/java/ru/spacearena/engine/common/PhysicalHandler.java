package ru.spacearena.engine.common;

import ru.spacearena.engine.handlers.UpdateHandler;
import ru.spacearena.util.FloatMathUtils;

/**
 * @author Vyacheslav Mayorov
 * @since 2014-12-02
 */
public class PhysicalHandler implements UpdateHandler {

    private TransformHandler transformHandler;
    private float velocityX = 0f, velocityY = 0f;
    private float accelerationX = 0f, accelerationY = 0f;
    private float angularVelocity = 0f;

    // TODO add min/max speed

    public PhysicalHandler(TransformHandler transformHandler) {
        this.transformHandler = transformHandler;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(float accelerationX) {
        this.accelerationX = accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }

    public void setAcceleration(float accelerationX, float accelerationY) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public boolean onUpdate(float seconds) {
        velocityX += accelerationX * seconds;
        velocityY += accelerationY * seconds;
        if (!FloatMathUtils.isZero(velocityX, velocityY)) {
            transformHandler.x += velocityX * seconds;
            transformHandler.y += velocityY * seconds;
            transformHandler.isDirty = true;
        }
        if (!FloatMathUtils.isZero(angularVelocity)) {
            transformHandler.rotation = FloatMathUtils.normalizeDegrees(
                    transformHandler.rotation + angularVelocity * seconds);
            transformHandler.isDirty = true;
        }
        return true;
    }
}
